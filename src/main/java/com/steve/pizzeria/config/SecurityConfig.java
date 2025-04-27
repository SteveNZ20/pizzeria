package com.steve.pizzeria.config;

import com.steve.pizzeria.dto.UserDto;
import com.steve.pizzeria.services.UserService;
import com.steve.pizzeria.security.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

@Configuration
public class SecurityConfig {

    private final UserService userService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public SecurityConfig(UserService userService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.userService = userService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserDto userDto = userService.getUserDetailsByUsername(username);
            if (userDto == null) {
                throw new UsernameNotFoundException("User not found");
            }

            // Aquí se usa la contraseña hasheada del DTO
            String hashedPassword = userDto.getPassword();

            if (!userDto.isStatus()) {  // Suponiendo que getStatus() devuelve true o false
                throw new DisabledException("Cuenta inactiva. Contacte al administrador.");
            }

            // Se asignan los roles basados en el usertype
            return User.builder()
                    .username(userDto.getUsername())
                    .password(hashedPassword)
                    .roles(userDto.getUsertype())  // Asignamos el rol correspondiente
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/logout", "/register", "/", "css/*").permitAll()  // Permitir acceso al login y logout
                        .anyRequest().authenticated()  // Resto de las rutas necesitan autenticación
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Especifica la URL de tu página personalizada de login
                        .permitAll()  // Permitir que cualquiera acceda a la página de login
                        .successHandler(customAuthenticationSuccessHandler)  // Usamos el handler para redirigir al login exitoso
                        .failureHandler((request, response, exception) -> {
                            // Si la causa raíz es un mensaje de cuenta inactiva
                            if (exception instanceof InternalAuthenticationServiceException &&
                                    exception.getMessage() != null &&
                                    exception.getMessage().contains("Cuenta inactiva")) {
                                response.sendRedirect("/login?inactive=true");
                            } else {
                                // Otros tipos de error
                                response.sendRedirect("/login?error=true");
                            }
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Se mantiene
                        .logoutSuccessUrl("/") // Redirigir a la página principal después de hacer logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
}
