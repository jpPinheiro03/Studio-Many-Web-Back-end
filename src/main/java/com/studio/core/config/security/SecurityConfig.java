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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration // Classe de configuração do Spring. Diz ao Spring: “essa classe contém definições de objetos que devem ser gerenciados pelo container.”
@EnableWebSecurity // Ativa a segurança do Spring Security
/*
Ela diz ao Spring:
"Ative o mecanismo de segurança HTTP e permita configurar autenticação, autorização e filtros de segurança."
@EnableWebSecurity
o Spring registra automaticamente a infraestrutura de segurança:

Security Filter Chain
Authentication Manager
Authorization Manager
CSRF protection
Session security

Ou seja, toda a pipeline de segurança HTTP.
HTTP Request
↓
Security Filter Chain
↓
Authentication
↓
Authorization
↓
Controller
 */
public class SecurityConfig {

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean // Método que cria um objeto gerenciado pelo Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> 
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/cadastro").permitAll()
                        .requestMatchers("/api/auth/me", "/api/auth/usuarios/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/clientes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/clientes/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/clientes/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/**").permitAll()
                        .requestMatchers("/api/funcionarios/**").permitAll()
                        .requestMatchers("/api/servicos/**").permitAll()
                        .requestMatchers("/api/agendamentos/**").permitAll()
                        .requestMatchers("/api/disponibilidade/**").permitAll()
                        .requestMatchers("/api/dashboard/**").permitAll()
                        .requestMatchers("/api/relatorios/**").permitAll()
                        .requestMatchers("/api/lista-espera/**").permitAll()
                        .requestMatchers("/api/financeiro/**").permitAll()
                        .requestMatchers("/api/parcelas/**").permitAll()
                        .requestMatchers("/api/produtos/**").permitAll()
                        .requestMatchers("/api/pedidos/**").permitAll()
                        .requestMatchers("/api/pacotes/**").permitAll()
                        .requestMatchers("/api/cliente-pacotes/**").permitAll()
                        .requestMatchers("/api/funcionario-servicos/**").permitAll()
                        .requestMatchers("/api/horario-trabalho/**").permitAll()
                        .requestMatchers("/api/bloqueios/**").permitAll()
                        .requestMatchers("/api/comissoes/**").permitAll()
                        .requestMatchers("/api/auditoria/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    
    @Bean // Método que cria um objeto gerenciado pelo Spring
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        String origins = System.getenv().getOrDefault("CORS_ALLOWED_ORIGINS", "http://localhost:3000,http://localhost:5173");
        configuration.setAllowedOrigins(Arrays.asList(origins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean // Método que cria um objeto gerenciado pelo Spring
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
