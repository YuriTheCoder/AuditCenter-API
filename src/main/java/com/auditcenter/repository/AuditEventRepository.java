package com.auditcenter.repository;

import com.auditcenter.entity.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade AuditEvent.
 *
 * Estende JpaRepository para fornecer operações de banco de dados para a entidade AuditEvent.
 *
 * @see com.auditcenter.entity.AuditEvent
 */
@Repository
public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {

    /**
     * Busca todos os eventos de auditoria associados a um e-mail de usuário específico.
     *
     * Este método será usado para a funcionalidade onde um usuário com a role 'ANALYST'
     * só pode ver os eventos que ele mesmo gerou (ou que estão associados ao seu e-mail).
     * O Spring Data JPA criará a consulta necessária a partir do nome do método.
     *
     * @param userEmail O e-mail do usuário para o qual os eventos serão buscados.
     * @return Uma lista de AuditEvent's pertencentes ao usuário especificado.
     */
    List<AuditEvent> findByUserEmail(String userEmail);
} 