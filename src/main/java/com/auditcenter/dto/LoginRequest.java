package com.auditcenter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para a requisição de login.
 * Contém as credenciais (email e senha) necessárias para autenticar um usuário.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para autenticar um usuário e obter um token JWT.")
public class LoginRequest {

    @Schema(description = "E-mail do usuário.", example = "admin@auditcenter.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "O formato do e-mail é inválido.")
    private String email;

    @Schema(description = "Senha do usuário.", example = "strongpassword", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "A senha não pode estar em branco.")
    private String password;
} 