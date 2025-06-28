package com.auditcenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Representa um evento de auditoria no sistema.
 * Cada instância desta classe corresponde a um registro na tabela 'audit_events'.
 *
 * Anotações JPA:
 * - @Entity: Marca esta classe como uma entidade JPA.
 * - @Table(name = "audit_events"): Define o nome da tabela no banco de dados.
 *
 * Anotações Lombok:
 * - @Data: Gera getters, setters, etc.
 * - @Builder: Padrão Builder para construção de objetos.
 * - @NoArgsConstructor e @AllArgsConstructor: Construtores.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_events")
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do sistema que originou o evento.
     * Facilita a filtragem de eventos por sistema de origem.
     */
    @Column(nullable = false)
    private String systemName;

    /**
     * E-mail do usuário associado ao evento.
     * Usado para rastrear qual usuário realizou a ação.
     */
    @Column(nullable = false)
    private String userEmail;

    /**
     * Ação que foi realizada (ex: "USER_LOGIN", "DATA_UPDATE").
     */
    @Column(nullable = false)
    private String action;

    /**
     * Carimbo de data e hora de quando o evento foi registrado.
     * - @CreationTimestamp: Anotação do Hibernate que popula automaticamente
     *   este campo com o timestamp atual no momento da criação da entidade.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    /**
     * Metadados adicionais em formato JSON.
     * Armazena um JSON como uma String contendo detalhes extras sobre o evento.
     * - @Lob: Especifica que este campo deve ser persistido como um Large Object (CLOB para String).
     *   Isso é adequado para armazenar textos longos, como um payload JSON.
     * - @Column(columnDefinition = "TEXT"): Em muitos dialetos SQL, como PostgreSQL e H2,
     *   definir a coluna como 'TEXT' é uma forma mais explícita e comum de lidar com JSON como string.
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String metadata;
} 