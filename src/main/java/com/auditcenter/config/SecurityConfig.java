package com.auditcenter.config;

import com.auditcenter.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração principal para o Spring Security.
 * Responsável por configurar as regras de segurança HTTP, provedor de autenticação,
 * gerenciamento de sessão e o filtro JWT.
 *
 * Anotações:
 * - @Configuration: Indica que esta classe contém beans de configuração do Spring.
 * - @EnableWebSecurity: Habilita a integração do Spring Security com o Spring MVC.
 * - @EnableMethodSecurity: Habilita a segurança em nível de método (ex: @PreAuthorize).
 * - @RequiredArgsConstructor: Lombok, para injeção de dependências.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private static final String[] WHITE_LIST_URL = {
            "/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    /**
     * O Bean SecurityFilterChain define a cadeia de filtros de segurança e as regras
     * de proteção para as requisições HTTP. Esta é a abordagem moderna e recomendada
     * para configurar o Spring Security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita o CSRF (Cross-Site Request Forgery)
                // Não é necessário para APIs REST stateless que usam tokens.
                .csrf(csrf -> csrf.disable())

                // 2. Define as regras de autorização para cada endpoint
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público aos endpoints de autenticação e documentação do Swagger
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        // Todas as outras requisições devem ser autenticadas
                        .anyRequest().authenticated()
                )

                // 3. Configura o gerenciamento de sessão para ser stateless
                // A cada requisição o usuário deve se re-autenticar via token, sem estado de sessão no servidor.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Define o provedor de autenticação customizado
                .authenticationProvider(authenticationProvider)

                // 5. Adiciona nosso filtro JWT antes do filtro padrão de username/password
                // Isso garante que nossa lógica de validação de token seja executada primeiro.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
} 