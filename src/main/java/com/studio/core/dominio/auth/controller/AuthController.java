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
import java.util.Map;

@RestController // Esta classe aceita requisições HTTP e retorna JSON
@RequestMapping("/api/auth") // URL base para todos os endpoints desta classe
@Tag(name = "Autenticação", description = "Endpoints para autenticação e gerenciamento de usuários") // Swagger: agrupa endpoints por categoria
public class AuthController {

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private UsuarioService service;

    @PostMapping("/cadastro") // Endpoint que aceita requisições POST (criar) no caminho /cadastro
    @Operation(summary = "Cadastrar usuário (apenas ADMIN)") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO dto) { // Valida os campos e recebe os dados do corpo da requisição HTTP (JSON)
        UsuarioResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PostMapping("/login") // Endpoint que aceita requisições POST (criar) no caminho /login
    @Operation(summary = "Login") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) { // Valida os campos e recebe os dados do corpo da requisição HTTP (JSON)
        LoginResponseDTO loginResponse = service.login(dto);
        return ResponseEntity.ok(loginResponse);
    }
    
    @GetMapping("/me") // Endpoint que aceita requisições GET no caminho /me
    @Operation(summary = "Usuário atual") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<UsuarioResponseDTO> getCurrentUser(Authentication authentication) { // Recebe o objeto de autenticação do Spring Security
        return ResponseEntity.ok(service.getCurrentUser(authentication.getName()));
    }
    
    @GetMapping("/usuarios") // Endpoint que aceita requisições GET no caminho /usuarios
    @Operation(summary = "Listar usuários") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/usuarios/{id}") // Endpoint que aceita requisições GET com parâmetro na URL
    @Operation(summary = "Buscar usuário por ID") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) { // Recebe o valor da URL (ex: /usuarios/{id})
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PutMapping("/usuarios/{id}") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Atualizar usuário") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) { // Recebe o valor da URL e os dados do corpo da requisição HTTP (JSON)
        return ResponseEntity.ok(service.update(id, dto));
    }
    
    @PutMapping("/usuarios/{id}/trocar-senha") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Trocar senha do usuário") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<Void> trocarSenha(@PathVariable Long id, @RequestBody Map<String, String> body) { // Recebe o valor da URL e os dados do corpo da requisição HTTP (JSON)
        String senhaAtual = body.get("senhaAtual");
        String novaSenha = body.get("novaSenha");
        if (novaSenha == null || novaSenha.length() < 6) {
            throw new com.studio.core.exception.BadRequestException("Nova senha deve ter no mínimo 6 caracteres");
        }
        service.changePassword(id, senhaAtual, novaSenha);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/usuarios/{id}") // Endpoint que aceita requisições DELETE (excluir) com parâmetro na URL
    @Operation(summary = "Excluir usuário") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<Void> excluir(@PathVariable Long id) { // Recebe o valor da URL (ex: /usuarios/{id})
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
