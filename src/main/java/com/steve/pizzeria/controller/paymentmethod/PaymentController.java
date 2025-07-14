package com.steve.pizzeria.controller.paymentmethod;

import com.steve.pizzeria.dto.PaymentDto;
import com.steve.pizzeria.services.PaymentService;
import com.steve.pizzeria.services.UserService;
import com.steve.pizzeria.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

@Controller
@RequestMapping("/payment_process")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    // Este método es el que renderiza la vista payment_method/index.html
    @GetMapping("/select")
    public String selectPaymentMethod(@RequestParam("amount") double amount, Model model, Principal principal) {
        Long userId = null;
        if (principal != null) {
            userId = userService.getUserDetailsByUsername(principal.getName()).getId();
        }

        if (userId == null) {
            logger.warn("Usuario no autenticado o ID de usuario no disponible para selección de pago. Redirigiendo a login.");
            return "redirect:/login";
        }

        String status = "PENDING";

        model.addAttribute("amount", amount); // Ya usa 'amount'
        model.addAttribute("userId", userId);
        model.addAttribute("status", status);

        logger.info("Redirigiendo a la selección de método de pago. Amount: {}, UserID: {}, Status: {}", amount, userId, status);

        return "payment_method/index";
    }

    // Este método procesa el formulario de pago de payment_method/index.html
    @PostMapping("/process")
    public String processPayment(
            @RequestParam("amount") double amount, // Ya usa 'amount'
            @RequestParam("userId") Long userId,
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("status") String status,
            Model model
    ) {
        if (userId == null) {
            logger.error("Intento de procesar pago sin ID de usuario en el formulario.");
            model.addAttribute("paymentMessage", "Error: ID de usuario no proporcionado.");
            return "payment_method/index";
        }

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setUserId(userId);
        paymentDto.setAmount(amount); // Correcto, usa 'amount'
        paymentDto.setPaymentMethod(paymentMethod);
        paymentDto.setStatus(status);

        logger.info("Intentando procesar pago para UserID: {}, Monto: {}, Método: {}, Estado: {}",
                userId, amount, paymentMethod, status); // Correcto, usa 'amount'

        try {
            PaymentDto savedPayment = paymentService.savePayment(paymentDto);

            if (savedPayment != null && savedPayment.getId() != null) {
                logger.info("Pago registrado exitosamente con ID: {}", savedPayment.getId());
                model.addAttribute("paymentMessage", "¡Pago realizado con éxito!");
                model.addAttribute("paymentStatus", "success");

                // Opcional: Limpiar el carrito después de un pago exitoso
                // cartService.clearUserCart(userId);
                // logger.info("Carrito del usuario {} vaciado después del pago.", userId);

                return "redirect:/payment_success?paymentId=" + savedPayment.getId();
            } else {
                logger.error("Fallo al registrar el pago: La API devolvió un objeto nulo o sin ID.");
                model.addAttribute("paymentMessage", "Error al procesar el pago. Intente de nuevo.");
                model.addAttribute("paymentStatus", "error");
                return "payment_method/index";
            }
        } catch (Exception e) {
            logger.error("Excepción al procesar el pago para UserID {}: {}", userId, e.getMessage());
            model.addAttribute("paymentMessage", "Ocurrió un error inesperado al procesar su pago: " + e.getMessage());
            model.addAttribute("paymentStatus", "error");
            return "payment_method/index";
        }
    }
}