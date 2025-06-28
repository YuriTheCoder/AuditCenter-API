package com.auditcenter.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Componente responsável por todas as operações relacionadas ao JWT:
 * - Geração de novos tokens.
 * - Validação de tokens existentes.
 * - Extração de informações (claims) de tokens.
 *
 * A anotação @Component faz desta classe um bean Spring, permitindo que seja
 * injetada em outras partes da aplicação (ex: nos serviços de autenticação).
 */
@Component
public class JwtTokenProvider {

    // Injeta os valores de configuração do application.properties.
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    /**
     * Gera um token JWT para um usuário.
     * O "subject" do token será o e-mail do usuário.
     * @param userDetails Detalhes do usuário fornecidos pelo Spring Security.
     * @return Uma string com o token JWT.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // É possível adicionar claims customizadas ao token, como as roles do usuário.
        // claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Valida um token. Verifica se o nome de usuário no token corresponde e se não está expirado.
     * @param token O token JWT.
     * @param userDetails Os detalhes do usuário para comparar.
     * @return true se o token for válido, false caso contrário.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrai o nome de usuário (subject) do token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai um claim específico do token usando uma função resolver.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        // O parser do Jwts lida com a verificação da assinatura.
        // Se for inválida, ele lança uma exceção que será tratada pelo Spring Security.
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Gera a chave de assinatura a partir da string secreta definida nas propriedades.
     * Para o algoritmo HS256, a chave precisa ter um tamanho mínimo seguro.
     */
    private Key getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
} 