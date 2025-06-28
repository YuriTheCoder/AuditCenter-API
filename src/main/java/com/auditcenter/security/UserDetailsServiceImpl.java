package com.auditcenter.security;

import com.auditcenter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço que implementa a interface UserDetailsService do Spring Security.
 *
 * A principal responsabilidade desta classe é carregar os detalhes de um usuário
 * (como senha e permissões) a partir de uma fonte de dados (nosso banco de dados)
 * com base no nome de usuário (que no nosso caso é o e-mail).
 *
 * O Spring Security usará este serviço durante o processo de autenticação para
 * obter as informações do usuário e verificar se a senha fornecida corresponde
 * à senha armazenada.
 *
 * @Service: Marca esta classe como um serviço Spring, tornando-a um bean gerenciado.
 * @RequiredArgsConstructor: Anotação do Lombok que gera um construtor com todos os
 * campos `final`, facilitando a injeção de dependência (melhor prática).
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Carrega um usuário pelo seu e-mail.
     *
     * @param username O e-mail do usuário a ser carregado.
     * @return um objeto UserDetails (nossa entidade User implementa esta interface).
     * @throws UsernameNotFoundException se o usuário não for encontrado no banco de dados.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username));
    }
} 