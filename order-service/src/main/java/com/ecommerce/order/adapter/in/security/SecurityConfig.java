package com.ecommerce.order.adapter.in.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desactivamos CSRF porque usamos JWT (no sesiones basadas en cookies)
                .csrf(csrf -> csrf.disable())

                // 2. Configuramos políticas de acceso por endpoint y rol
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("ADMIN", "SALES", "LOGISTICS", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("ADMIN", "SALES")
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/*/shipping-address").hasAnyRole("ADMIN", "SALES")
                        .requestMatchers(HttpMethod.POST, "/api/orders/*/ship").hasAnyRole("ADMIN", "LOGISTICS")
                        .requestMatchers(HttpMethod.POST, "/api/orders/*/complete").hasAnyRole("ADMIN", "LOGISTICS")
                        .requestMatchers(HttpMethod.POST, "/api/orders/*/cancel").hasAnyRole("ADMIN", "SALES")
                        .requestMatchers(HttpMethod.POST, "/api/orders/*/return").hasAnyRole("ADMIN", "SALES", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/orders/*/refund").hasAnyRole("ADMIN", "SALES")
                        .anyRequest().authenticated())

                // 3. Fuerza respuesta 401 para peticiones no autenticadas
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Missing or invalid token\"}");
                        }))

                // 4. Stateless: No guardamos estado en el servidor
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5. Registramos filtro JWT antes del filtro estándar de Spring
                .addFilterBefore(jwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService) {
        return new JwtAuthenticationFilter(jwtService);
    }
}