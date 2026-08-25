package com.helpdesk.api.config;

import com.helpdesk.api.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/registro", "/api/auth/login", "/api/auth/refresh",
                                  "/api/ping", "/h2-console/**").permitAll()

                // Especificas primero (orden importa)
                .requestMatchers(HttpMethod.GET, "/api/tickets/vencidos").hasAnyRole("SOPORTE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/tickets/mios").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/api/tickets/*/estado").hasAnyRole("SOPORTE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/tickets/*").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/tickets").hasAnyRole("SOPORTE", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/tickets").authenticated()

                .requestMatchers(HttpMethod.POST, "/api/admin/soporte").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
