package com.auditcenter.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO para receber um evento de auditoria através do endpoint de webhook.
 * Representa o payload JSON que sistemas externos enviarão.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para receber eventos de sistemas externos via webhook.")
public class WebhookEventRequest {

    @Schema(description = "Nome do sistema que envia o evento.", example = "Sistema de Vendas", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O nome do sistema não pode ser nulo.")
    private String systemName;

    @Schema(description = "E-mail do usuário que realizou a ação no sistema de origem.", example = "vendedor@vendas.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O e-mail do usuário não pode ser nulo.")
    private String userEmail;

    @Schema(description = "Ação específica que ocorreu.", example = "VENDA_REALIZADA", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "A ação não pode ser nula.")
    private String action;

    /**
     * Campo flexível para metadados adicionais.
     * O Jackson (biblioteca de serialização/desserialização JSON usada pelo Spring)
     * irá automaticamente converter o objeto JSON 'metadata' da requisição em um Map.
     */
    @Schema(description = "Objeto JSON com metadados adicionais sobre o evento.", example = "{\"productId\": 789, \"amount\": 2, \"totalPrice\": 150.00}")
    @NotNull(message = "Os metadados não podem ser nulos.")
    private Map<String, Object> metadata;

    /**
     * Método utilitário para converter o mapa de metadados em uma string JSON.
     * Isso é útil para persistir os metadados no banco de dados, que espera uma string.
     * @return A representação em String JSON dos metadados.
     */
    public String getMetadataAsJsonString() {
        try {
            // ObjectMapper é thread-safe e pode ser reutilizado, mas para simplicidade
            // instanciamos um novo aqui. Em cenários de alta performance, injetar um
            // bean singleton do ObjectMapper seria o ideal.
            return new ObjectMapper().writeValueAsString(this.metadata);
        } catch (Exception e) {
            // Em uma aplicação real, um log de erro seria apropriado aqui.
            return "{}";
        }
    }
} 