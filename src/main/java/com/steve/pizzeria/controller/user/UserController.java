package com.steve.pizzeria.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user")
    public String userIndex() {
        return "user/index"; // Asegúrate de tener este archivo HTML en tus vistas
    }
}