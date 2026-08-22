package com.helpdesk.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad.
 * <p>
 * Rutas publicas: registro, login, ping, consola H2 (solo desarrollo).
 * Cualquier otra ruta exige autenticacion.
 * <p>
 * Aun falta: el filtro que realmente lee y valida el JWT (Modulo 7).
 * Por ahora, "requiere autenticacion" existe como regla, pero todavia
 * no hay ninguna forma de que una peticion la cumpla -> toda ruta
 * protegida va a responder 401 por ahora, y eso es lo esperado.
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

            // Lista de reglas por ruta, evaluadas en orden de arriba a abajo.
            // Las especificas van primero; la regla general (anyRequest) al final.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/api/ping", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
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

    // Bean reutilizable en toda la app: cifra passwords al registrar,
    // y compara passwords al hacer login (Modulo 6).
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}