package com.steve.pizzeria.controller.user;

import com.steve.pizzeria.dto.UserDto;
import com.steve.pizzeria.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para las operaciones de los usuarios del sistema.
 * Gestiona las vistas del dashboard, edición y actualización de datos del usuario.
 *
 * Rutas manejadas:
 * - GET /user/{username}
 * - GET /user/{username}/edit
 * - POST /user/{username}
 *
 * Utiliza {@link UserService} para recuperar y actualizar la información del usuario.
 *
 * @author Franco
 * @version 1.0
 */
@Controller
public class UserController {

    private final UserService userService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param userService Servicio para operaciones con usuarios
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Muestra el dashboard del usuario.
     *
     * @param username Nombre de usuario
     * @param model Modelo de datos para la vista
     * @return Vista del dashboard del usuario o mensaje de error si no se encuentra
     */
    @GetMapping("/user/{username}")
    public String userDashboard(@PathVariable String username, Model model) {
        UserDto userDto = userService.getUserDetailsByUsername(username);
        if (userDto != null) {
            model.addAttribute("user", userDto);
            return "user/index";
        } else {
            return "Usuario no encontrado"; // Puedes personalizar esta vista de error
        }
    }

    /**
     * Muestra el formulario de edición del usuario.
     *
     * @param username Nombre de usuario
     * @param model Modelo de datos para la vista
     * @return Vista de edición del usuario o mensaje de error si no se encuentra
     */
    @GetMapping("/user/{username}/edit")
    public String editUser(@PathVariable String username, Model model) {
        UserDto userDto = userService.getUserDetailsByUsername(username);
        if (userDto != null) {
            model.addAttribute("user", userDto);
            return "user/edit";
        } else {
            return "Usuario no encontrado"; // Vista de error opcional
        }
    }

    /**
     * Procesa la actualización del usuario.
     *
     * @param username Nombre de usuario a actualizar
     * @param userDto Objeto con los datos actualizados
     * @return Redirección al dashboard del usuario actualizado
     */
    @PostMapping("/user/{username}")
    public String updateUser(@PathVariable String username, @ModelAttribute UserDto userDto) {
        userDto.setUsername(username); // Asegura coherencia si el formulario no lo trae
        userService.updateUser(userDto);
        return "redirect:/user/" + username;
    }
}