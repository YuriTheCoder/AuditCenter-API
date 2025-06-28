package com.auditcenter.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro customizado que intercepta todas as requisições para processar o token JWT.
 * Este filtro é executado uma vez por requisição (`OncePerRequestFilter`).
 *
 * Sua lógica é:
 * 1. Extrair o token JWT do cabeçalho "Authorization".
 * 2. Validar o token.
 * 3. Se válido, carregar os detalhes do usuário e configurar o contexto de segurança do Spring.
 *
 * Isso garante que, para cada requisição com um token válido, o usuário seja
 * autenticado e suas permissões sejam carregadas para que o Spring Security
 * possa usá-las para autorizar o acesso aos endpoints.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Se o cabeçalho de autorização não existir ou não começar com "Bearer ",
        // passamos a requisição para o próximo filtro na cadeia e retornamos.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai o token do cabeçalho (removendo o prefixo "Bearer ").
        jwt = authHeader.substring(7);
        userEmail = jwtTokenProvider.extractUsername(jwt);

        // Se o e-mail foi extraído e o usuário ainda não está autenticado no contexto de segurança.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do usuário do banco de dados.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Se o token for válido...
            if (jwtTokenProvider.validateToken(jwt, userDetails)) {
                // ...cria um objeto de autenticação...
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // não precisamos das credenciais (senha) aqui
                        userDetails.getAuthorities()
                );
                // ...adiciona detalhes da requisição web ao token...
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // ...e atualiza o SecurityContextHolder. O Spring saberá que o usuário está autenticado.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continua a execução da cadeia de filtros.
        filterChain.doFilter(request, response);
    }
} 