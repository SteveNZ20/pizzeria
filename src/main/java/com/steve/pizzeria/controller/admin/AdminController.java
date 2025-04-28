package com.steve.pizzeria.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.steve.pizzeria.services.UserService;
import com.steve.pizzeria.dto.UserDto;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService; // <-- Te faltaba esto

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        List<UserDto> users = userService.getAllUsers();

        // Contar solo los que tienen userType = "user"
        long numberOfUsers = users.stream()
                .filter(user -> "user".equalsIgnoreCase(user.getUsertype()))
                .count();

        // Contar solo los que tienen userType = "user"
        long numberOfAdmins = users.stream()
                .filter(user -> "admin".equalsIgnoreCase(user.getUsertype()))
                .count();

        // Agregar otros datos al modelo
        model.addAttribute("totalPendings", 0);
        model.addAttribute("totalCompletes", 0);
        model.addAttribute("numberOfOrders", 0);
        model.addAttribute("numberOfProducts", 0);
        model.addAttribute("numberOfUsers", numberOfUsers);
        model.addAttribute("numberOfAdmins", numberOfAdmins);

        return "admin/index"; // Renderiza admin/index.html
    }
}