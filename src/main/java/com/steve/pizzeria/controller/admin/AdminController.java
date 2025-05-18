package com.steve.pizzeria.controller.admin;

import com.steve.pizzeria.dto.ProductDto;
import com.steve.pizzeria.dto.UserDto;
import com.steve.pizzeria.services.ProductService;
import com.steve.pizzeria.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Controlador encargado de manejar las vistas del panel de administración.
 * Permite visualizar estadísticas generales, gestionar usuarios y productos.
 *
 * Rutas manejadas:
 * - /admin
 * - /admin/users
 * - /admin/enableUser
 * - /admin/disableUser
 *
 * Este controlador utiliza los servicios {@link UserService} y {@link ProductService}.
 *
 * @author Franco
 * @version 1.0
 */
@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * Muestra el panel de administración con estadísticas de productos, usuarios y pedidos.
     *
     * @param model Modelo de datos para la vista
     * @return Vista HTML correspondiente: "admin/index"
     */
    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        List<UserDto> users = userService.getAllUsers();
        List<ProductDto> products = productService.getAllProducts();

        long numberOfProducts = products.stream()
                .filter(ProductDto::isStatus)
                .count();

        long numberOfUsers = users.stream()
                .filter(user -> "user".equalsIgnoreCase(user.getUsertype()) && user.isStatus())
                .count();

        long numberOfAdmins = users.stream()
                .filter(user -> "admin".equalsIgnoreCase(user.getUsertype()))
                .count();

        model.addAttribute("totalPendings", 0);
        model.addAttribute("totalCompletes", 0);
        model.addAttribute("numberOfOrders", 0);
        model.addAttribute("numberOfProducts", numberOfProducts);
        model.addAttribute("numberOfUsers", numberOfUsers);
        model.addAttribute("numberOfAdmins", numberOfAdmins);

        return "admin/index";
    }

    /**
     * Muestra la lista de usuarios con rol "user".
     *
     * @param model Modelo de datos para la vista
     * @return Vista HTML correspondiente: "admin/users"
     */
    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        List<UserDto> allUsers = userService.getAllUsers();
        List<UserDto> filteredUsers = allUsers.stream()
                .filter(user -> "user".equalsIgnoreCase(user.getUsertype()))
                .toList();

        model.addAttribute("users", filteredUsers);
        return "admin/users";
    }

    /**
     * Habilita un usuario (cambia su estado a activo).
     *
     * @param userId ID del usuario a habilitar
     * @param model Modelo de datos para la vista
     * @return Vista HTML actualizada de usuarios: "admin/users"
     */
    @PostMapping("/admin/enableUser")
    public String enableUser(Long userId, Model model) {
        userService.enableUser(userId);
        List<UserDto> users = userService.getUsersByType("user");
        model.addAttribute("users", users);
        return "admin/users";
    }

    /**
     * Deshabilita un usuario (cambia su estado a inactivo).
     *
     * @param userId ID del usuario a deshabilitar
     * @param model Modelo de datos para la vista
     * @return Vista HTML actualizada de usuarios: "admin/users"
     */
    @PostMapping("/admin/disableUser")
    public String disableUser(Long userId, Model model) {
        userService.disableUser(userId);
        List<UserDto> users = userService.getUsersByType("user");
        model.addAttribute("users", users);
        return "admin/users";
    }
}