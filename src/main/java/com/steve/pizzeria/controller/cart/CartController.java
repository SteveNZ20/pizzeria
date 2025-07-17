package com.steve.pizzeria.controller.cart;

import com.google.common.collect.Ordering;
import com.steve.pizzeria.dto.CartDto;
import com.steve.pizzeria.dto.UserDto;
import com.steve.pizzeria.services.CartService;
import com.steve.pizzeria.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        Long userId = null;
        UserDto user = null;

        if (principal != null) {
            user = userService.getUserDetailsByUsername(principal.getName());
            if (user != null) {
                userId = user.getId();
                logger.info("Usuario autenticado en el carrito: Username={}, ID={}", principal.getName(), userId);
            } else {
                logger.warn("No se encontraron detalles de usuario para el username: {}", principal.getName());
            }
        }

        if (userId == null) {
            logger.warn("Usuario no autenticado o ID de usuario no disponible. Redirigiendo a login.");
            return "redirect:/login";
        }

        List<CartDto> orders = new ArrayList<>(cartService.getAllOrdersByUserId(userId));

        orders.removeIf(order -> StringUtils.isBlank(order.getProductName()));

        orders = Ordering
                .from(Comparator.comparing(CartDto::getQuantity))
                .reverse()
                .immutableSortedCopy(orders);

        double total = orders.stream().mapToDouble(order -> order.getPrice() * order.getQuantity()).sum();
        logger.info("Se cargaron {} productos para el usuario ID {}. Total: {}", orders.size(), userId, total);

        model.addAttribute("orders", orders);
        model.addAttribute("total", total);
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);

        return "orders/orderIndex";
    }

    @PostMapping("/orders/add")
    public String createOrder(
            @RequestParam Long product_id, // CAMBIO: Tipo a Long para coincidir con CartDto y OrderDetailsModel
            @RequestParam String product_name,
            @RequestParam double price,
            @RequestParam String image,
            Model model,
            Principal principal
    ) {
        Long userIdLong = null;
        if (principal != null) {
            UserDto loggedInUser = userService.getUserDetailsByUsername(principal.getName());
            if (loggedInUser != null) {
                userIdLong = loggedInUser.getId();
            }
        }

        if (userIdLong == null) {
            logger.warn("Usuario no autenticado o ID de usuario no disponible para agregar producto. Redirigiendo a login.");
            return "redirect:/login";
        }

        CartDto newCartItem = new CartDto();
        newCartItem.setProductId(product_id); // Ahora product_id es Long
        newCartItem.setProductName(product_name);
        newCartItem.setPrice(price);
        newCartItem.setQuantity(1);
        newCartItem.setImage(image);
        newCartItem.setUserId(userIdLong); // CAMBIO: Asignación directa, userIdLong ya es Long

        try {
            CartDto savedCartItem = cartService.saveOrder(newCartItem);

            if (savedCartItem != null && savedCartItem.getId() != null) {
                model.addAttribute("message", "Producto agregado correctamente al carrito");
                logger.info("Producto ID {} agregado correctamente al carrito para el usuario ID {}", product_id, userIdLong);
            } else {
                model.addAttribute("message", "Error al agregar el producto al carrito: Respuesta inválida del servicio");
                logger.error("Error al agregar el producto ID {} al carrito para el usuario ID {}: Respuesta nula o sin ID", product_id, userIdLong);
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error de comunicación al agregar el producto al carrito: " + e.getMessage());
            logger.error("Error al llamar a CartService.saveOrder para usuario ID {}: {}", userIdLong, e.getMessage());
        }

        return "redirect:/orders";
    }

    @DeleteMapping("/orders/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
        try {
            logger.info("Solicitud de eliminación de producto en carrito con ID: {}", id);
            cartService.deleteOrder(id);
            logger.info("Producto en carrito con ID {} eliminado correctamente.", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al eliminar el producto en carrito con ID " + id, e);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/orders/update/{id}")
    @ResponseBody
    public ResponseEntity<Void> updateOrderQuantity(@PathVariable("id") Long id,
                                                    @RequestBody UpdateQuantityRequest request) {
        try {
            cartService.updateOrderQuantity(id, request.quantity());
            logger.info("Cantidad actualizada para el producto en carrito ID {}: nueva cantidad {}", id, request.quantity());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al actualizar la cantidad del producto en carrito ID " + id, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/payment_method/select")
    public String selectPaymentMethod(@RequestParam("amount") double amount, Model model, Principal principal) {
        Long userId = null;
        if (principal != null) {
            UserDto loggedInUser = userService.getUserDetailsByUsername(principal.getName());
            if (loggedInUser != null) {
                userId = loggedInUser.getId();
            }
        }

        if (userId == null) {
            logger.warn("Usuario no autenticado o ID de usuario no disponible para selección de pago. Redirigiendo a login.");
            return "redirect:/login";
        }

        String status = "PENDING";

        model.addAttribute("amount", amount);
        model.addAttribute("userId", userId);
        model.addAttribute("status", status);

        logger.info("Redirigiendo a la selección de método de pago. Amount: {}, UserID: {}, Status: {}", amount, userId, status);

        return "payment_method/index";
    }

    public record UpdateQuantityRequest(int quantity) {}
}