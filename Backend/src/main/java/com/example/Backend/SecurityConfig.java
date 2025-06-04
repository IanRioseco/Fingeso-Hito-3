package com.example.Backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desactiva protección CSRF (solo si no usas sesiones o formularios)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permitir TODAS las rutas
                );
        return http.build();
    }
}
