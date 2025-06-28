package com.auditcenter.service;

import com.auditcenter.dto.AuditEventDto;
import com.auditcenter.dto.WebhookEventRequest;
import com.auditcenter.entity.AuditEvent;
import com.auditcenter.entity.User;
import com.auditcenter.mapper.AuditEventMapper;
import com.auditcenter.repository.AuditEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Serviço para a lógica de negócio relacionada a eventos de auditoria.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditEventService {

    private final AuditEventRepository auditEventRepository;
    private final AuditEventMapper auditEventMapper;

    // Lista thread-safe para armazenar os emissores SSE (conexões de clientes).
    // CopyOnWriteArrayList é uma boa escolha para cenários com muitas leituras (envio de eventos)
    // e poucas escritas (novas conexões/desconexões).
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /**
     * Adiciona um novo SseEmitter à lista de conexões ativas.
     * @param emitter O SseEmitter que representa a conexão do cliente.
     */
    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(err -> emitters.remove(emitter));
        log.info("Novo cliente SSE conectado. Total de conexões: {}", emitters.size());
    }

    /**
     * Salva um novo evento de auditoria vindo do webhook e o transmite via SSE.
     *
     * @param request O DTO com os dados do evento.
     * @return O DTO do evento salvo.
     */
    public AuditEventDto saveEvent(WebhookEventRequest request) {
        // Mapeia o DTO de requisição para a entidade
        AuditEvent auditEvent = auditEventMapper.toEntity(request);
        // Salva a entidade no banco de dados
        AuditEvent savedEvent = auditEventRepository.save(auditEvent);
        log.info("Novo evento de auditoria salvo: {}", savedEvent);
        // Mapeia a entidade salva para o DTO de resposta
        AuditEventDto savedEventDto = auditEventMapper.toDto(savedEvent);

        // Envia o evento para todos os clientes SSE conectados
        sendEventToSseClients(savedEventDto);

        return savedEventDto;
    }

    /**
     * Lista os eventos de auditoria com base na permissão do usuário autenticado.
     * - ADMIN: vê todos os eventos.
     * - ANALYST: vê apenas os eventos associados ao seu e-mail.
     *
     * @return Uma lista de DTOs de eventos de auditoria.
     */
    public List<AuditEventDto> listEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<AuditEvent> events;
        if (currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            log.info("Usuário ADMIN {} listando todos os eventos.", currentUser.getEmail());
            events = auditEventRepository.findAll();
        } else { // ANALYST
            log.info("Usuário ANALYST {} listando seus próprios eventos.", currentUser.getEmail());
            events = auditEventRepository.findByUserEmail(currentUser.getEmail());
        }

        return auditEventMapper.toDtoList(events);
    }

    /**
     * Envia um evento para todos os clientes SSE conectados.
     * @param eventDto O evento a ser enviado.
     */
    private void sendEventToSseClients(AuditEventDto eventDto) {
        log.info("Enviando evento SSE para {} clientes.", emitters.size());
        for (SseEmitter emitter : emitters) {
            try {
                // Envia o evento no formato SSE
                emitter.send(SseEmitter.event().name("audit-event").data(eventDto));
            } catch (IOException e) {
                // Se a conexão estiver fechada/quebrada, o emitter será removido
                // pela chamada onCompletion/onTimeout/onError.
                log.warn("Erro ao enviar evento SSE para um cliente. Removendo-o da lista.", e);
                emitters.remove(emitter);
            }
        }
    }
} 