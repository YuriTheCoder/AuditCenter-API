package com.auditcenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Representa a entidade 'User' no banco de dados.
 * Esta classe é um pilar para a autenticação e autorização, implementando
 * a interface UserDetails do Spring Security.
 *
 * Anotações JPA:
 * - @Entity: Marca esta classe como uma entidade JPA, ou seja, ela será mapeada para uma tabela no banco de dados.
 * - @Table(name = "users"): Especifica o nome da tabela no banco de dados.
 *
 * Anotações Lombok:
 * - @Data: Gera getters, setters, toString, equals e hashCode.
 * - @Builder: Implementa o padrão de projeto Builder para facilitar a criação de objetos.
 * - @NoArgsConstructor: Gera um construtor sem argumentos.
 * - @AllArgsConstructor: Gera um construtor com todos os argumentos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    /**
     * Identificador único do usuário.
     * - @Id: Define este campo como a chave primária da tabela.
     * - @GeneratedValue(strategy = GenerationType.IDENTITY): Configura a geração do valor da chave primária
     *   para ser autoincrementada pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do usuário.
     * - @Column(nullable = false): Garante que este campo não pode ser nulo no banco de dados.
     */
    @Column(nullable = false)
    private String name;

    /**
     * E-mail do usuário, usado como nome de usuário para login.
     * - @Column(unique = true, nullable = false): Garante que cada e-mail seja único e não nulo.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Senha do usuário, armazenada de forma criptografada (hash).
     * - @Column(nullable = false): Garante que a senha não pode ser nula.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Papel (role) do usuário, que define suas permissões.
     * - @Enumerated(EnumType.STRING): Diz ao JPA para persistir o enum como uma String ("ADMIN", "ANALYST")
     *   em vez de um valor numérico (0, 1), o que torna o banco de dados mais legível.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Métodos da interface UserDetails implementados abaixo.
    // O Spring Security usa esses métodos para interagir com nosso modelo de usuário.

    /**
     * Retorna as permissões concedidas ao usuário.
     * O Spring Security usa esta coleção para verificar as autorizações.
     * Criamos uma lista com uma única autoridade baseada no 'role' do usuário.
     * É convencional prefixar as roles com "ROLE_".
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Retorna a senha usada para autenticar o usuário.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retorna o nome de usuário usado para autenticar o usuário. No nosso caso, é o e-mail.
     */
    @Override
    public String getUsername() {
        return email;
    }

    // Os métodos abaixo controlam o estado da conta. Para este projeto, vamos
    // simplificar e considerar que a conta está sempre ativa e válida.

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
} 