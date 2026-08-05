package com.helpdesk.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad TEMPORAL.
 * <p>
 * Objetivo actual: desbloquear la consola de H2 durante el desarrollo,
 * mientras aun no existe autenticacion JWT.
 * <p>
 * En el Modulo 6 este filtro se reemplaza por la version definitiva:
 * rutas publicas (/api/auth/**), rutas protegidas por autenticacion,
 * y rutas protegidas por rol (RBAC).
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF protege sesiones basadas en cookies. Nuestra API sera
            // stateless (JWT en el header Authorization), asi que no aplica.
            // Esta linea SI se queda para siempre, no es temporal.
            .csrf(csrf -> csrf.disable())

            // TEMPORAL: mientras no exista JWT, dejamos todo abierto.
            // En el Modulo 6 esto se reemplaza por reglas por ruta y por rol.
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // La consola H2 se renderiza dentro de un <frame>. Por defecto
            // Spring Security prohibe que cualquier respuesta se muestre en
            // un frame (protege contra clickjacking). sameOrigin() permite
            // el framing solo si viene del mismo origen (nuestra propia app),
            // que es exactamente lo que necesita /h2-console.
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}