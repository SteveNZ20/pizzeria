package com.steve.pizzeria.controller.order;

import com.steve.pizzeria.dto.OrderDto;
import com.steve.pizzeria.dto.UserDto;
import com.steve.pizzeria.services.OrderService;
import com.steve.pizzeria.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/orders_details")
    public String viewOrderDetails(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirige si el usuario no está autenticado
        }

        // Obtiene el ID del usuario autenticado de forma segura
        String username = principal.getName();
        UserDto userDto = userService.getUserDetailsByUsername(username);

        if (userDto == null) {
            // Manejar el caso donde el usuario no existe en la base de datos
            return "redirect:/login";
        }

        Long userId = userDto.getId();

        // Llama al servicio para obtener la lista de pedidos del usuario
        List<OrderDto> orders = orderService.getOrdersByUserId(String.valueOf(userId));

        // Agrega la lista de pedidos y el ID del usuario al modelo
        model.addAttribute("orders", orders);
        model.addAttribute("userId", userId);
        model.addAttribute("user", userDto);

        return "orders/orders_details";
    }

    // Aquí puedes agregar otros métodos si los necesitas
    @GetMapping("/create_order")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("order", new OrderDto());
        return "orders/create_order_form";
    }
}