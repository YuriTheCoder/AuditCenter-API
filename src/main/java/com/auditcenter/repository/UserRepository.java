package com.auditcenter.repository;

import com.auditcenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade User.
 *
 * Esta interface estende JpaRepository, que já fornece uma implementação padrão para
 * as operações básicas de CRUD (Create, Read, Update, Delete) sobre a entidade User.
 * O Spring Data JPA cria uma implementação proxy em tempo de execução.
 *
 * - JpaRepository<User, Long>: O primeiro parâmetro genérico é a entidade que este
 *   repositório gerencia (User). O segundo é o tipo da chave primária da entidade (Long).
 *
 * A anotação @Repository é opcional aqui, pois o Spring Data JPA já reconhece
 * interfaces que estendem JpaRepository como beans de repositório. No entanto,
 * é uma boa prática para clareza e consistência.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo seu endereço de e-mail.
     *
     * O Spring Data JPA implementa este método automaticamente com base no seu nome.
     * Ele entende "findByEmail" e gera a consulta SQL correspondente (SELECT * FROM users WHERE email = ?).
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return um Optional contendo o usuário, se encontrado, ou um Optional vazio caso contrário.
     *         Usar Optional é uma boa prática para evitar NullPointerExceptions.
     */
    Optional<User> findByEmail(String email);
} 