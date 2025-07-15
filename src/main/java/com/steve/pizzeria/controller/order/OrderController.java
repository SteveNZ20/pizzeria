package com.steve.pizzeria.controller.order;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    @GetMapping("/orders_details")
    public String viewOrderDetails(@RequestParam("userId") Long userId, Model model) {

        // La lógica para buscar los detalles de la orden...

        return "orders/orders_details";
    }
}