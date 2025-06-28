package com.auditcenter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para a resposta de autenticação.
 * Retorna o token de acesso JWT para o cliente.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta que contém o token de acesso JWT.")
public class AuthResponse {

    @Schema(description = "Token de acesso JWT.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhdWRpdGNlbnRlci5jb20iLCJpYXQiOjE3MDQ4MjU2MDAsImV4cCI6MTcwNDkxMjAwMH0.abc...")
    private String accessToken;
} 