package com.auditcenter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal que inicia a aplicação Spring Boot "AuditCenter".
 *
 * A anotação @SpringBootApplication é uma meta-anotação que engloba:
 * - @Configuration: Define a classe como uma fonte de beans para o contexto da aplicação.
 * - @EnableAutoConfiguration: Habilita a configuração automática do Spring Boot, que tenta
 *   configurar a aplicação com base nas dependências do classpath.
 * - @ComponentScan: Scaneia o pacote atual e seus sub-pacotes em busca de componentes
 *   Spring (Controllers, Services, Repositories, etc.).
 *
 * As anotações @OpenAPIDefinition e @SecurityScheme configuram globalmente a
 * documentação OpenAPI (Swagger) para a API.
 * - @OpenAPIDefinition: Fornece metadados gerais sobre a API.
 * - @SecurityScheme: Define o esquema de segurança "Bearer Authentication" para JWT,
 *   permitindo que o token seja enviado no cabeçalho Authorization das requisições
 *   feitas a partir da UI do Swagger.
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "AuditCenter API", version = "1.0", description = "API para registrar e consultar eventos de auditoria."))
@SecurityScheme(
    name = "bearerAuth",
    description = "Autenticação via Token JWT",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class AuditCenterApplication {

    /**
     * O método main é o ponto de entrada da aplicação.
     * Ele delega a inicialização para a classe SpringApplication do Spring Boot.
     * @param args Argumentos de linha de comando (não utilizados neste projeto).
     */
    public static void main(String[] args) {
        SpringApplication.run(AuditCenterApplication.class, args);
    }

} 