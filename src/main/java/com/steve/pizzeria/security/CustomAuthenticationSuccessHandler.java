package com.steve.pizzeria.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Obtén el nombre de usuario del principal (que es el usuario logueado)
        String username = authentication.getName();

        // Aquí se debe obtener el usertype (por ejemplo, usando el servicio de usuario)
        String usertype = authentication.getAuthorities().toString();
        Boolean status = true; // Aquí deberías obtener el estado del usuario desde la base de datos

        // Verifica el estado del usuario
        if (status) {
            // Redirige según el usertype (admin o user)
            if (usertype.contains("admin")) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/user");
            }
        } else {
            // Redirige al login con un mensaje de error
            response.sendRedirect("/login?error=Usuario+inactivo.+Contacte+al+administrador.");
        }
    }
}