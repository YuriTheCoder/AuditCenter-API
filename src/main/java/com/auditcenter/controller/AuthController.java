package com.auditcenter.controller;

import com.auditcenter.dto.AuthResponse;
import com.auditcenter.dto.LoginRequest;
import com.auditcenter.dto.RegisterRequest;
import com.auditcenter.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável pelos endpoints de autenticação.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para registrar um novo usuário.
     * @param request DTO com os dados do usuário a ser registrado.
     * @return ResponseEntity com o token JWT.
     */
    @Operation(
            summary = "Registra um novo usuário",
            description = "Cria um novo usuário no sistema e retorna um token JWT para autenticação imediata.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou e-mail já existente")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint para login de um usuário existente.
     * @param request DTO com as credenciais de login.
     * @return ResponseEntity com o token JWT.
     */
    @Operation(
            summary = "Autentica um usuário",
            description = "Autentica um usuário com e-mail e senha e retorna um token JWT em caso de sucesso.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
} 