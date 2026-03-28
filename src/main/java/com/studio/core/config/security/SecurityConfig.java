package com.studio.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> 
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/clientes/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST, "/api/clientes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/clientes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/**").hasRole("ADMIN")
                        .requestMatchers("/api/funcionarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/servicos/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers("/api/agendamentos/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers("/api/disponibilidade/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers("/api/relatorios/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers("/api/lista-espera/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers("/api/financeiro/**").hasRole("ADMIN")
                        .requestMatchers("/api/parcelas/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers("/api/produtos/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers("/api/pedidos/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers("/api/pacotes/**").hasRole("ADMIN")
                        .requestMatchers("/api/cliente-pacotes/**").hasAnyRole("ADMIN", "FUNCIONARIO")
                        .requestMatchers("/api/funcionario-servicos/**").hasRole("ADMIN")
                        .requestMatchers("/api/horario-trabalho/**").hasRole("ADMIN")
                        .requestMatchers("/api/bloqueios/**").hasRole("ADMIN")
                        .requestMatchers("/api/comissoes/**").hasRole("ADMIN")
                        .requestMatchers("/api/auditoria/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
