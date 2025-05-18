package com.steve.pizzeria.controller.login;

import com.steve.pizzeria.dto.UserDto;
import com.steve.pizzeria.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Controlador encargado del manejo de autenticación y registro de usuarios.
 * Gestiona las vistas de login y registro, así como la lógica de creación de nuevos usuarios.
 *
 * Rutas manejadas:
 * - /login
 * - /register (GET y POST)
 *
 * Utiliza los servicios de {@link UserService} y {@link PasswordEncoder}.
 *
 * @author Franco
 * @version 1.0
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Muestra el formulario de inicio de sesión.
     *
     * @param model Modelo de datos para la vista
     * @return Vista HTML del login: "login"
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "login";
    }

    /**
     * Muestra el formulario de registro.
     *
     * @param model Modelo de datos para la vista
     * @return Vista HTML del registro: "register"
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    /**
     * Procesa el formulario de registro.
     * Valida si el nombre de usuario ya existe, asigna valores predeterminados,
     * hashea la contraseña y guarda el usuario.
     *
     * @param formUser Objeto con los datos del formulario
     * @param model Modelo de datos para la vista
     * @return Redirección al login si el registro es exitoso,
     *         o regresa al formulario si hay error
     */
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") UserDto formUser, Model model) {
        for (UserDto user : userService.getAllUsers()) {
            if (user.getUsername().equals(formUser.getUsername())) {
                model.addAttribute("error", "El nombre de usuario ya existe");
                return "users/register";
            }
        }

        // Asignar valores por defecto
        Date now = new Date();
        formUser.setUsertype("user");
        formUser.setStatus(true);
        formUser.setFechaCreacion(now);
        formUser.setFechaActualizacion(now);

        // Encriptar contraseña
        String hashedPassword = passwordEncoder.encode(formUser.getPassword());
        formUser.setPassword(hashedPassword);

        // Guardar usuario
        userService.saveUser(formUser);

        return "redirect:/login";
    }
}