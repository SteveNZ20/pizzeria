package com.steve.pizzeria.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import com.steve.pizzeria.dto.UserDto;
import com.steve.pizzeria.services.UserService;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyecta BCryptPasswordEncoder aquí

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "login"; // Correcta ubicación del login.html
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";  // Esto debe corresponder con el archivo register.html en templates
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") UserDto formUser, Model model) {
        // Verifica si el usuario ya existe (opcional)
        for (UserDto user : userService.getAllUsers()) {
            if (user.getUsername().equals(formUser.getUsername())) {
                model.addAttribute("error", "El nombre de usuario ya existe");
                return "users/register"; // Regresa al formulario de registro con el error
            }
        }

        // Asignar valores predeterminados
        formUser.setUsertype("user");
        formUser.setStatus(true);

        // Hashear la contraseña antes de guardarla
        String hashedPassword = passwordEncoder.encode(formUser.getPassword());
        formUser.setPassword(hashedPassword);

        // Guarda el nuevo usuario
        userService.saveUser(formUser);

        // Redirige a login después de registrar
        return "redirect:/login";
    }

//    @GetMapping("/")
//    public String homePage(Model model) {
//        // Verificar si el usuario está autenticado
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
//            // El usuario está logueado, podemos agregarle el nombre de usuario al modelo
//            UserDto userDto = (UserDto) authentication.getPrincipal();
//            model.addAttribute("user", userDto);
//        }
//
//        return "/"; // O la vista principal donde se muestra el header
//    }
//
//    @GetMapping("/profile")
//    public String showProfile(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
//            UserDto userDto = (UserDto) authentication.getPrincipal();
//            model.addAttribute("user", userDto);
//        }
//        return "profile"; // Vista donde se muestra la información del usuario
//    }
}