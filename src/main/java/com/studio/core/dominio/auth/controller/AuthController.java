package com.studio.core.dominio.auth.controller;

import com.studio.core.dominio.usuario.dto.LoginRequestDTO;
import com.studio.core.dominio.usuario.dto.LoginResponseDTO;
import com.studio.core.dominio.usuario.dto.UsuarioRequestDTO;
import com.studio.core.dominio.usuario.dto.UsuarioResponseDTO;
import com.studio.core.dominio.usuario.entity.Usuario;
import com.studio.core.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e gerenciamento de usuários")
public class AuthController {
    
    @Autowired
    private UsuarioService service;
    
    @PostMapping("/cadastro")
    @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário no sistema")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica o usuário e retorna token JWT")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO loginResponse = service.login(dto);
        return ResponseEntity.ok(loginResponse);
    }
    
    @GetMapping("/me")
    @Operation(summary = "Usuário atual", description = "Retorna os dados do usuário logado")
    public ResponseEntity<UsuarioResponseDTO> getCurrentUser(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(service.getCurrentUser(usuario.getEmail()));
    }
    
    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuários", description = "Retorna todos os usuários")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/usuarios/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @DeleteMapping("/usuarios/{id}")
    @Operation(summary = "Excluir usuário")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
