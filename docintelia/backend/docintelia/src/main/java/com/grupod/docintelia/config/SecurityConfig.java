package com.grupod.docintelia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtFilter jwtFilter, AuthenticationProvider authenticationProvider) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Permitir preflight OPTIONS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Endpoints públicos
                        .requestMatchers("/api/auth/authenticate").permitAll()

                        // Endpoints de documento - ESPECÍFICOS PRIMERO
                        .requestMatchers("/api/documento/previsualizar/**").permitAll()
                        .requestMatchers("/api/documento/descargar/**").permitAll()
                        .requestMatchers("/api/documento/buscar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/documento/{id}").permitAll() // ← AGREGADO para tu endpoint específico

                        // Endpoints que requieren autenticación
                        .requestMatchers("/api/documento/gestion-documentos").hasAnyAuthority("ADMINISTRADOR","BIBLIOTECARIO")
                        .requestMatchers("/api/documento/upload").hasAnyAuthority("ADMINISTRADOR","BIBLIOTECARIO")
                        .requestMatchers("/api/documento/eliminar/**").hasAnyAuthority("ADMINISTRADOR","BIBLIOTECARIO")
                        .requestMatchers("/api/documento/actualizar-metadatos/**").hasAnyAuthority("ADMINISTRADOR","BIBLIOTECARIO")
                        .requestMatchers("/api/documento/indexar-carpeta").hasAnyAuthority("ADMINISTRADOR","BIBLIOTECARIO")

                        // Regla general para documento (para cualquier otro endpoint no especificado)
                        .requestMatchers("/api/documento/**").hasAnyAuthority("ADMINISTRADOR","BIBLIOTECARIO")

                        // Otros endpoints
                        .requestMatchers("/api/auth/register").hasAuthority("ADMINISTRADOR")
                        .requestMatchers("/api/rol/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers("/api/biblioteca/**").hasAnyAuthority("ADMINISTRADOR","BIBLIOTECARIO")
                        .requestMatchers("/api/usuario/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers("/api/cuenta/**").hasAnyAuthority("ADMINISTRADOR","BIBLIOTECARIO")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000")); // Agregué ambos puertos comunes
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        configuration.setExposedHeaders(List.of("Content-Disposition")); // Importante para descargas
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}