# AuditCenter API

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.1-brightgreen)
![Security](https://img.shields.io/badge/Security-JWT%20&%20BCrypt-critical)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-blueviolet)
![Maven](https://img.shields.io/badge/Build-Maven-orange)

**AuditCenter** é uma API RESTful robusta e segura, construída com Java e Spring Boot, projetada para centralizar e gerenciar eventos de auditoria de múltiplos sistemas. A aplicação conta com autenticação baseada em JWT, controle de acesso por papéis (Roles), e uma funcionalidade inovadora de streaming de eventos em tempo real com Server-Sent Events (SSE).

---

## Funcionalidades Principais

- **Segurança de Ponta**: Autenticação via JWT e senhas criptografadas com BCrypt. Endpoints são protegidos com base em papéis (`ADMIN`, `ANALYST`).
- **API de Auditoria**:
  - `POST /events/webhook`: Endpoint para receber eventos de auditoria de outros sistemas.
  - `GET /events`: Lista os eventos de auditoria. Um `ADMIN` vê todos os eventos, enquanto um `ANALYST` vê apenas os eventos associados ao seu e-mail.
- **Streaming em Tempo Real**:
  - `GET /events/stream`: Estabelece uma conexão Server-Sent Events (SSE) para que clientes possam receber eventos de auditoria assim que eles acontecem.
- **API de Autenticação**:
  - `POST /auth/register`: Registra novos usuários (`ADMIN` ou `ANALYST`).
  - `POST /auth/login`: Autentica usuários e retorna um token JWT.
- **Documentação Interativa**: API totalmente documentada com Swagger (OpenAPI 3), permitindo fácil exploração e teste dos endpoints.
- **Tratamento de Erros**: Respostas de erro padronizadas e claras para uma melhor experiência do desenvolvedor.

---

## Estrutura do Projeto

O projeto segue uma arquitetura em camadas para garantir a separação de responsabilidades e a manutenibilidade:

```
com.auditcenter
├── config         // Configurações do Spring Security e Beans
├── controller     // Controladores REST (porta de entrada da API)
├── dto            // Data Transfer Objects (para requisições e respostas)
├── entity         // Entidades JPA (modelo de dados)
├── exception      // Handler de exceções global
├── mapper         // Mapeadores (MapStruct) para conversão DTO-Entidade
├── repository     // Repositórios Spring Data JPA (acesso ao banco)
├── security       // Lógica de JWT, filtros e UserDetailsService
└── service        // Lógica de negócio da aplicação
```

---

## Como Configurar e Rodar o Projeto

### Pré-requisitos
- **Java 17** ou superior
- **Apache Maven** 3.6 ou superior
- Um terminal (como `cmd`, `PowerShell` ou `Git Bash`)

### Passos para Execução

1.  **Clone o Repositório** (quando estiver no GitHub):
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    cd AuditCenter
    ```

2.  **Inicie a Aplicação via Maven**:
    O comando abaixo irá compilar o projeto e iniciar o servidor web embutido na porta `8080`.
    ```bash
    mvn spring-boot:run
    ```
    O servidor estará pronto quando você vir a mensagem `Tomcat started on port(s): 8080 (http)`. Deixe este terminal aberto.

3.  **Acesse a Documentação da API (Swagger)**:
    Com o servidor rodando, abra seu navegador e acesse:
    [**http://localhost:8080/swagger-ui.html**](http://localhost:8080/swagger-ui.html)

---

## Como Usar a API (via Swagger)

1.  **Crie Usuários**: Use o endpoint `POST /auth/register` para criar um usuário `ADMIN` e um `ANALYST`.
2.  **Faça Login**: Use `POST /auth/login` com as credenciais de um dos usuários para obter um token JWT.
3.  **Autorize-se**: Clique no botão "Authorize" no topo da página do Swagger, cole o token no formato `Bearer <seu-token>` e autorize.
4.  **Teste os Endpoints**: Agora você pode usar os endpoints protegidos na seção "Eventos de Auditoria" para criar e listar eventos. 