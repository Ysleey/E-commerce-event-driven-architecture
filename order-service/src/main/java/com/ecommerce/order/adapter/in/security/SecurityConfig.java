package com.ecommerce.order.adapter.in.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ecommerce.order.adapter.in.observability.CorrelationIdFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final CorrelationIdFilter correlationIdFilter;

    public SecurityConfig(JwtService jwtService, CorrelationIdFilter correlationIdFilter) {
        this.jwtService = jwtService;
        this.correlationIdFilter = correlationIdFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())

                // 1. Desactivamos CSRF porque usamos JWT (no sesiones basadas en cookies)
                .csrf(csrf -> csrf.disable())

                // 2. Configuramos políticas de acceso por endpoint y rol
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                    .requestMatchers("/actuator/info", "/actuator/info/**").hasAnyRole("ADMIN", "LOGISTICS")
                    .requestMatchers("/actuator/metrics", "/actuator/metrics/**").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
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
                            String correlationId = resolveCorrelationId(request);
                            response.getWriter().write(String.format(
                                    "{\"error\":\"Unauthorized\",\"message\":\"Missing or invalid token\",\"correlationId\":\"%s\"}",
                                    correlationId));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            String correlationId = resolveCorrelationId(request);
                            response.getWriter().write(String.format(
                                    "{\"error\":\"Forbidden\",\"message\":\"Insufficient permissions for this resource\",\"correlationId\":\"%s\"}",
                                    correlationId));
                        }))

                // 4. Stateless: No guardamos estado en el servidor
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5. Registramos filtro JWT antes del filtro estándar de Spring
                .addFilterBefore(correlationIdFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter(jwtService), CorrelationIdFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService) {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Correlation-Id"));
        configuration.setExposedHeaders(List.of("X-Correlation-Id"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private String resolveCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER);
        return correlationId == null || correlationId.isBlank() ? "N/A" : correlationId;
    }
}