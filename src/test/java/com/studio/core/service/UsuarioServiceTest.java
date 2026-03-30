package com.studio.core.service;

import com.studio.core.config.security.JwtUtil;
import com.studio.core.dominio.usuario.dto.LoginRequestDTO;
import com.studio.core.dominio.usuario.dto.UsuarioRequestDTO;
import com.studio.core.dominio.usuario.entity.Usuario;
import com.studio.core.dominio.usuario.repository.UsuarioRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioRepository repository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("admin@teste.com");
        usuario.setSenha("$2a$10$hashed");
        usuario.setRole(Usuario.Role.ADMIN);
        usuario.setAtivo(true);
    }

    @Test
    void create_DeveCriarUsuario() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setEmail("novo@teste.com");
        dto.setSenha("senha123");

        when(repository.existsByEmail("novo@teste.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("$2a$10$hashed");
        when(repository.save(any())).thenReturn(usuario);

        var result = service.create(dto);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void create_DeveLancarErro_EmailDuplicado() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setEmail("admin@teste.com");
        dto.setSenha("senha123");

        when(repository.existsByEmail("admin@teste.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.create(dto));
    }

    @Test
    void login_DeveRetornarToken() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("admin@teste.com");
        dto.setSenha("senha123");

        when(repository.findByEmail("admin@teste.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", "$2a$10$hashed")).thenReturn(true);
        when(jwtUtil.generateToken("admin@teste.com", "ADMIN")).thenReturn("jwt-token");

        var result = service.login(dto);

        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
    }

    @Test
    void login_DeveLancarErro_SenhaIncorreta() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("admin@teste.com");
        dto.setSenha("errada");

        when(repository.findByEmail("admin@teste.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("errada", "$2a$10$hashed")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> service.login(dto));
    }

    @Test
    void login_DeveLancarErro_UsuarioInativo() {
        usuario.setAtivo(false);
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("admin@teste.com");
        dto.setSenha("senha123");

        when(repository.findByEmail("admin@teste.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", "$2a$10$hashed")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.login(dto));
    }

    @Test
    void changePassword_DeveAlterarSenha() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaAtual", "$2a$10$hashed")).thenReturn(true);
        when(passwordEncoder.encode("novaSenha")).thenReturn("$2a$10$newHashed");

        service.changePassword(1L, "senhaAtual", "novaSenha");

        verify(repository).save(any());
    }

    @Test
    void changePassword_DeveLancarErro_SenhaAtualIncorreta() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("errada", "$2a$10$hashed")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> service.changePassword(1L, "errada", "novaSenha"));
    }
}
