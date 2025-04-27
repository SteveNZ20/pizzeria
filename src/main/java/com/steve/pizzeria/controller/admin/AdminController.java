package com.steve.pizzeria.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String userIndex() {
        return "admin/index"; // Asegúrate de tener este archivo HTML en tus vistas
    }
}