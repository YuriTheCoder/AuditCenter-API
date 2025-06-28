package com.auditcenter.dto;

import com.auditcenter.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para o registro de um novo usuário.
 *
 * Esta classe é usada para transportar os dados da requisição HTTP (corpo do JSON)
 * para o controller. O uso de DTOs é uma boa prática para desacoplar a camada de
 * API da camada de persistência (entidades).
 *
 * Anotações de Validação (Jakarta Bean Validation):
 * - @NotBlank: Garante que o campo não é nulo e não contém apenas espaços em branco.
 * - @Email: Valida se o formato da string corresponde a um e-mail válido.
 * - @Size: Valida o tamanho do campo (mínimo e máximo de caracteres).
 * - @NotNull: Garante que o campo não é nulo.
 *
 * Anotação Swagger (@Schema):
 * - Descreve o DTO e seus campos na documentação da API gerada pelo Swagger.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para registrar um novo usuário no sistema.")
public class RegisterRequest {

    @Schema(description = "Nome completo do usuário.", example = "Analista de Sistemas", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O nome não pode estar em branco.")
    private String name;

    @Schema(description = "Endereço de e-mail do usuário. Será usado para login.", example = "analyst@system.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "O formato do e-mail é inválido.")
    private String email;

    @Schema(description = "Senha do usuário. Deve ter entre 6 e 20 caracteres.", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "A senha não pode estar em branco.")
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres.")
    private String password;

    @Schema(description = "Papel do usuário no sistema. Pode ser ADMIN ou ANALYST.", example = "ANALYST", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "O papel não pode ser nulo.")
    private Role role;
} 