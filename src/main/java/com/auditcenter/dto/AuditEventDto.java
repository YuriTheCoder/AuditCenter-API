package com.auditcenter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para representar um evento de auditoria.
 * Usado para transferir dados de eventos de auditoria para os clientes da API (respostas).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representação de um evento de auditoria.")
public class AuditEventDto {

    @Schema(description = "ID único do evento.", example = "1")
    private Long id;

    @Schema(description = "Nome do sistema que originou o evento.", example = "Financeiro")
    private String systemName;

    @Schema(description = "E-mail do usuário associado ao evento.", example = "user@example.com")
    private String userEmail;

    @Schema(description = "Ação realizada.", example = "PAGAMENTO_APROVADO")
    private String action;

    @Schema(description = "Data e hora do registro do evento.")
    private LocalDateTime timestamp;

    @Schema(description = "Metadados adicionais em formato JSON.", example = "{\"transactionId\": 12345, \"value\": 99.90}")
    private String metadata;
} 