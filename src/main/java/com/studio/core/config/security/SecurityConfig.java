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
                        .requestMatchers("/api/auth/cadastro").hasRole("ADMIN")
                        .requestMatchers("/api/auth/me", "/api/auth/usuarios/**").authenticated()
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
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
