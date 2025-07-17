package com.steve.pizzeria.controller.admin;

import com.steve.pizzeria.dto.OrderDto;
import com.steve.pizzeria.dto.ProductDto;
import com.steve.pizzeria.dto.UserDto;
import com.steve.pizzeria.services.OrderService;
import com.steve.pizzeria.services.ProductService;
import com.steve.pizzeria.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador encargado de manejar las vistas del panel de administración.
 * Permite visualizar estadísticas generales, gestionar usuarios y productos.
 *
 * Rutas manejadas:
 * - /admin
 * - /admin/users
 * - /admin/enableUser
 * - /admin/disableUser
 * - /admin/administrators  // Nueva ruta agregada
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

    @Autowired
    private OrderService orderService;

    /**
     * Muestra el panel de administración con estadísticas de productos, usuarios y pedidos.
     * Incluye un filtro opcional por rango de fecha ("desde" y "hasta").
     *
     * @param model Modelo de datos para la vista
     * @param startDateString Fecha de inicio del filtro (opcional)
     * @param endDateString Fecha de fin del filtro (opcional)
     * @return Vista HTML correspondiente: "admin/index"
     */
    @GetMapping("/admin")
    public String adminDashboard(Model model,
                                 @RequestParam(value = "startDate", required = false) String startDateString,
                                 @RequestParam(value = "endDate", required = false) String endDateString) {

        List<UserDto> users = userService.getAllUsers();
        List<ProductDto> products = productService.getAllProducts();
        List<OrderDto> allOrders = orderService.getAllOrders();

        LocalDate startDate, endDate;

        // Si los parámetros de fecha no están presentes, usamos la fecha actual por defecto
        if (startDateString == null || endDateString == null || startDateString.isEmpty() || endDateString.isEmpty()) {
            startDate = LocalDate.now();
            endDate = LocalDate.now();
        } else {
            startDate = LocalDate.parse(startDateString);
            endDate = LocalDate.parse(endDateString);
        }

        // Aplicar filtro de fecha a los pedidos
        List<OrderDto> filteredOrders = allOrders.stream()
                .filter(order -> {
                    LocalDate orderDate = order.getCreatedAt().toLocalDate();
                    // El filtro es inclusivo: la fecha debe ser igual o posterior a startDate y
                    // igual o anterior a endDate.
                    return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                })
                .collect(Collectors.toList());

        // Calcular estadísticas sobre los pedidos filtrados
        long numberOfPendingOrders = filteredOrders.stream()
                .filter(order -> "pending".equalsIgnoreCase(order.getStatus()))
                .count();

        long numberOfCompletedOrders = filteredOrders.stream()
                .filter(order -> "success".equalsIgnoreCase(order.getStatus()))
                .count();

        long numberOfOrders = filteredOrders.size();

        // Calcular estadísticas sobre productos y usuarios (sin filtro de fecha)
        long numberOfProducts = products.stream()
                .filter(ProductDto::isStatus)
                .count();

        long numberOfUsers = users.stream()
                .filter(user -> "user".equalsIgnoreCase(user.getUsertype()) && user.isStatus())
                .count();

        long numberOfAdmins = users.stream()
                .filter(user -> "admin".equalsIgnoreCase(user.getUsertype()))
                .count();

        // Añadir los datos al modelo
        model.addAttribute("numberOfPendingOrders", numberOfPendingOrders);
        model.addAttribute("numberOfCompletedOrders", numberOfCompletedOrders);
        model.addAttribute("numberOfProducts", numberOfProducts);
        model.addAttribute("numberOfUsers", numberOfUsers);
        model.addAttribute("numberOfAdmins", numberOfAdmins);
        model.addAttribute("numberOfOrders", numberOfOrders);

        // Pasar las fechas al modelo para que la vista las mantenga en los inputs
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

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
     * Muestra la lista de usuarios con rol "admin".
     *
     * @param model Modelo de datos para la vista
     * @return Vista HTML correspondiente: "admin/admins"
     */
    @GetMapping("/admin/administrators")
    public String adminAdministrators(Model model) {
        List<UserDto> allUsers = userService.getAllUsers();
        List<UserDto> filteredAdmins = allUsers.stream()
                .filter(user -> "admin".equalsIgnoreCase(user.getUsertype()))
                .toList();

        model.addAttribute("admins", filteredAdmins);
        return "admin/admins";
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