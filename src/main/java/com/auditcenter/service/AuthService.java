package com.auditcenter.service;

import com.auditcenter.dto.AuthResponse;
import com.auditcenter.dto.LoginRequest;
import com.auditcenter.dto.RegisterRequest;
import com.auditcenter.entity.User;
import com.auditcenter.repository.UserRepository;
import com.auditcenter.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de negócio de autenticação.
 * Lida com o registro e login de usuários.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * Registra um novo usuário no sistema.
     *
     * @param request DTO com os dados de registro.
     * @return um AuthResponse contendo o token JWT para o usuário recém-criado.
     */
    public AuthResponse register(RegisterRequest request) {
        // Verifica se o usuário já existe (pode-se criar uma exceção customizada para isso)
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Usuário com este e-mail já existe.");
        }

        // Cria a nova entidade User
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Criptografa a senha
                .role(request.getRole())
                .build();

        // Salva o usuário no banco de dados
        userRepository.save(user);

        // Gera o token JWT para o novo usuário
        var jwtToken = jwtTokenProvider.generateToken(user);

        // Retorna a resposta com o token
        return AuthResponse.builder().accessToken(jwtToken).build();
    }

    /**
     * Autentica um usuário existente.
     *
     * @param request DTO com as credenciais de login.
     * @return um AuthResponse contendo o token JWT.
     */
    public AuthResponse login(LoginRequest request) {
        // O AuthenticationManager cuida da validação da senha.
        // Se as credenciais estiverem erradas, ele lançará uma exceção (ex: BadCredentialsException),
        // que será tratada pelo nosso handler de exceções global.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Se a autenticação for bem-sucedida, busca o usuário para gerar o token.
        // O método findByEmail não deve falhar aqui, pois a autenticação já passou.
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Erro inesperado ao buscar usuário após autenticação."));

        // Gera o token
        var jwtToken = jwtTokenProvider.generateToken(user);

        // Retorna a resposta com o token
        return AuthResponse.builder().accessToken(jwtToken).build();
    }
} 