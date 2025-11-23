package com.fing.app.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ⚠️ ESTE @Bean FALTABA
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/estilos.css", "/script.js", 
                                "/registro.html", "/api/registro", "/addProd", "/lista").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/registro.html")  // Página personalizada de login
                .loginProcessingUrl("/login")  // URL donde Spring procesa el login
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/registro.html?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Para APIs REST

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}