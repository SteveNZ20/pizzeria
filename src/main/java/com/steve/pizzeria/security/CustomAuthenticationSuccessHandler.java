package com.steve.pizzeria.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities();
        Boolean status = true; // Recuerda reemplazar esto con la lógica real para obtener el estado del usuario

        if (status) {
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                response.sendRedirect("/admin");
            } else {
                // Redirige a una URL específica del usuario, pasando el nombre de usuario como parte de la ruta
                response.sendRedirect("/user/" + username);
            }
        } else {
            response.sendRedirect("/login?error=Usuario+inactivo.+Contacte+al+administrador.");
        }
    }
}