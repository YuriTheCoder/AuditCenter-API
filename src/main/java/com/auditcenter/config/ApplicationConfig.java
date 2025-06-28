package com.auditcenter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe de configuração para os beans da aplicação relacionados à segurança.
 * Separa a criação de beans da configuração da cadeia de filtros (SecurityConfig),
 * mantendo o código mais organizado.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserDetailsService userDetailsService;

    /**
     * Define o provedor de autenticação que o Spring Security usará.
     * Configuramos um DaoAuthenticationProvider, que é o provedor padrão para
     * autenticação baseada em banco de dados.
     *
     * @return uma instância de AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Informa ao provedor qual serviço usar para carregar os detalhes do usuário.
        authProvider.setUserDetailsService(userDetailsService);
        // Informa ao provedor qual o codificador de senhas a ser usado para comparar as senhas.
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Expõe o AuthenticationManager do Spring como um Bean.
     * O AuthenticationManager é o principal responsável por processar uma requisição de autenticação.
     *
     * @param config A configuração de autenticação do Spring.
     * @return uma instância de AuthenticationManager.
     * @throws Exception se houver erro ao obter o AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define o bean para o codificador de senhas.
     * Usamos o BCrypt, que é o padrão recomendado e um algoritmo de hashing forte e seguro.
     *
     * @return uma instância de PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
