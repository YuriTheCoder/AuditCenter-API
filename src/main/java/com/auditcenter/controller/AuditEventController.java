package com.auditcenter.controller;

import com.auditcenter.dto.AuditEventDto;
import com.auditcenter.dto.WebhookEventRequest;
import com.auditcenter.service.AuditEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * Controller para manipulação de eventos de auditoria.
 * Todos os endpoints aqui são protegidos e requerem autenticação via JWT.
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Tag(name = "Eventos de Auditoria", description = "Endpoints para criar e consultar eventos de auditoria")
@SecurityRequirement(name = "bearerAuth") // Aplica a exigência de token JWT a todos os endpoints neste controller
public class AuditEventController {

    private final AuditEventService auditEventService;

    /**
     * Endpoint de webhook para receber eventos de sistemas externos.
     * @param request O payload do evento.
     * @return O evento que foi salvo.
     */
    @PostMapping("/webhook")
    @Operation(summary = "Recebe um evento de auditoria via webhook", description = "Endpoint para sistemas externos postarem eventos. Requer autenticação.")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')") // Apenas usuários autenticados podem enviar eventos
    public ResponseEntity<AuditEventDto> receiveWebhookEvent(@Valid @RequestBody WebhookEventRequest request) {
        AuditEventDto savedEvent = auditEventService.saveEvent(request);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    /**
     * Lista os eventos de auditoria.
     * A lógica de quem vê o quê é tratada no AuditEventService.
     * @return Uma lista de eventos de auditoria.
     */
    @GetMapping
    @Operation(summary = "Lista eventos de auditoria", description = "Retorna uma lista de eventos. ADMINs veem tudo, ANALYSTs veem apenas os seus.")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<List<AuditEventDto>> listEvents() {
        return ResponseEntity.ok(auditEventService.listEvents());
    }

    /**
     * Abre um fluxo de Server-Sent Events (SSE) para receber eventos em tempo real.
     * @return um SseEmitter que representa a conexão com o cliente.
     */
    @GetMapping("/stream")
    @Operation(summary = "Recebe eventos de auditoria em tempo real", description = "Estabelece uma conexão SSE para streaming de eventos.")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public SseEmitter streamEvents() {
        // Cria um emitter com um timeout longo (ex: 1 hora) para manter a conexão aberta.
        SseEmitter emitter = new SseEmitter(3600_000L);
        auditEventService.addEmitter(emitter);
        return emitter;
    }
} 