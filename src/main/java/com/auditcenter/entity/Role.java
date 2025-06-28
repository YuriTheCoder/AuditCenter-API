package com.auditcenter.entity;

/**
 * Enum que representa as permissões (papéis) de um usuário no sistema.
 * As roles são usadas pelo Spring Security para controlar o acesso aos endpoints.
 *
 * - ADMIN: Tem acesso total ao sistema, incluindo a visualização de todos os eventos de auditoria.
 * - ANALYST: Tem acesso limitado, podendo ver apenas os eventos de auditoria
 *   relacionados ao seu próprio sistema/e-mail.
 */
public enum Role {
    ADMIN,
    ANALYST
} 