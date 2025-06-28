package com.auditcenter.mapper;

import com.auditcenter.dto.AuditEventDto;
import com.auditcenter.dto.WebhookEventRequest;
import com.auditcenter.entity.AuditEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper para conversão entre entidades AuditEvent e seus DTOs.
 *
 * A anotação @Mapper(componentModel = "spring") diz ao MapStruct para gerar uma
 * implementação desta interface que é um bean Spring e pode ser injetado em outros
 * componentes, como serviços.
 *
 * MapStruct é uma ferramenta de "convenção sobre configuração". Se os nomes dos
 * campos nas classes de origem e destino forem os mesmos, o mapeamento é automático.
 */
@Mapper(componentModel = "spring")
public interface AuditEventMapper {

    /**
     * Instância do mapper que pode ser usada se a injeção de dependência não estiver disponível.
     */
    AuditEventMapper INSTANCE = Mappers.getMapper(AuditEventMapper.class);

    /**
     * Converte uma entidade AuditEvent para um AuditEventDto.
     *
     * @param auditEvent A entidade a ser convertida.
     * @return O DTO correspondente.
     */
    AuditEventDto toDto(AuditEvent auditEvent);

    /**
     * Converte uma lista de entidades AuditEvent para uma lista de AuditEventDto.
     * O MapStruct gera automaticamente a implementação para este método de lista
     * se um método de mapeamento de item único (como o acima) existir.
     *
     * @param auditEvents A lista de entidades a ser convertida.
     * @return A lista de DTOs correspondente.
     */
    List<AuditEventDto> toDtoList(List<AuditEvent> auditEvents);

    /**
     * Converte um WebhookEventRequest (DTO de entrada) para uma entidade AuditEvent.
     *
     * @Mapping(target = "metadata", expression = "java(request.getMetadataAsJsonString())")
     * Esta anotação é necessária porque o tipo do campo 'metadata' é diferente entre
     * o DTO (Map<String, Object>) e a entidade (String).
     * Indicamos ao MapStruct para usar o resultado do método 'getMetadataAsJsonString()'
     * do DTO de origem para preencher o campo 'metadata' da entidade de destino.
     *
     * @Mapping(target = "id", ignore = true)
     * Ignoramos o campo 'id' porque ele deve ser gerado pelo banco de dados na inserção,
     * não vindo da requisição.
     *
     * @Mapping(target = "timestamp", ignore = true)
     * Ignoramos o campo 'timestamp' porque ele será preenchido automaticamente pela
     * anotação @CreationTimestamp na entidade.
     *
     * @param request O DTO de entrada a ser convertido.
     * @return A entidade AuditEvent correspondente.
     */
    @Mapping(target = "metadata", expression = "java(request.getMetadataAsJsonString())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    AuditEvent toEntity(WebhookEventRequest request);
} 