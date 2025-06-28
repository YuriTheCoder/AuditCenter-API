package com.auditcenter.service;

import com.auditcenter.dto.AuditEventDto;
import com.auditcenter.entity.AuditEvent;
import com.auditcenter.entity.Role;
import com.auditcenter.entity.User;
import com.auditcenter.mapper.AuditEventMapper;
import com.auditcenter.repository.AuditEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o AuditEventService.
 *
 * @ExtendWith(MockitoExtension.class): Habilita a integração do Mockito com o JUnit 5.
 */
@ExtendWith(MockitoExtension.class)
class AuditEventServiceTest {

    // @Mock: Cria um mock (objeto simulado) para a dependência.
    @Mock
    private AuditEventRepository auditEventRepository;

    @Mock
    private AuditEventMapper auditEventMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    // @InjectMocks: Cria uma instância da classe a ser testada e injeta os mocks criados com @Mock nela.
    @InjectMocks
    private AuditEventService auditEventService;

    private User adminUser;
    private User analystUser;

    /**
     * O método @BeforeEach é executado antes de cada teste.
     * Usamos para configurar o estado inicial necessário para os testes.
     */
    @BeforeEach
    void setUp() {
        // Configura o mock do SecurityContextHolder para simular um usuário autenticado.
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        adminUser = User.builder().id(1L).name("Admin").email("admin@test.com").role(Role.ADMIN).build();
        analystUser = User.builder().id(2L).name("Analyst").email("analyst@test.com").role(Role.ANALYST).build();
    }

    @Test
    void testListEvents_AsAdmin_ShouldReturnAllEvents() {
        // Arrange (Organizar)
        // 1. Simula o usuário autenticado como ADMIN
        when(authentication.getPrincipal()).thenReturn(adminUser);
        // 2. Simula as permissões do usuário para o teste - REMOVIDO POR SER DESNECESSÁRIO
        // doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))).when(authentication).getAuthorities();

        // 3. Define o comportamento esperado dos mocks
        List<AuditEvent> mockEvents = List.of(new AuditEvent(), new AuditEvent());
        when(auditEventRepository.findAll()).thenReturn(mockEvents);

        List<AuditEventDto> mockDtos = List.of(new AuditEventDto(), new AuditEventDto());
        when(auditEventMapper.toDtoList(mockEvents)).thenReturn(mockDtos);

        // Act (Agir)
        // 4. Executa o método a ser testado
        List<AuditEventDto> result = auditEventService.listEvents();

        // Assert (Afirmar)
        // 5. Verifica se o resultado é o esperado
        assertEquals(2, result.size(), "O admin deveria ver 2 eventos.");
        verify(auditEventRepository, times(1)).findAll();
        verify(auditEventRepository, never()).findByUserEmail(anyString());
    }

    @Test
    void testListEvents_AsAnalyst_ShouldReturnOnlyOwnEvents() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(analystUser);
        // doReturn(List.of(new SimpleGrantedAuthority("ROLE_ANALYST"))).when(authentication).getAuthorities();

        List<AuditEvent> mockEvents = List.of(new AuditEvent());
        when(auditEventRepository.findByUserEmail(analystUser.getEmail())).thenReturn(mockEvents);

        List<AuditEventDto> mockDtos = List.of(new AuditEventDto());
        when(auditEventMapper.toDtoList(mockEvents)).thenReturn(mockDtos);

        // Act
        List<AuditEventDto> result = auditEventService.listEvents();

        // Assert
        assertEquals(1, result.size(), "O analista deveria ver 1 evento.");
        verify(auditEventRepository, times(1)).findByUserEmail(analystUser.getEmail());
        verify(auditEventRepository, never()).findAll();
    }
} 