package com.studio.core.dominio.cliente.controller;

import com.studio.core.dominio.cliente.dto.ClienteRequestDTO;
import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Endpoints para gestão de clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService service;
    
    @GetMapping
    @Operation(summary = "Listar clientes", description = "Retorna todos os clientes")
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        List<ClienteResponseDTO> dtos = service.findAll().stream()
            .map(ClienteResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        Cliente entity = service.findById(id);
        return ResponseEntity.ok(ClienteResponseDTO.fromEntity(entity));
    }
    
    @PostMapping
    @Operation(summary = "Criar cliente", description = "Cadastra um novo cliente")
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) {
        Cliente entity = new Cliente();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setCpf(dto.getCpf());
        entity.setEndereco(dto.getEndereco());
        entity.setObservacoes(dto.getObservacoes());
        entity.setAtivo(dto.getAtivo());
        
        Cliente created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponseDTO.fromEntity(created));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        Cliente entity = service.findById(id);
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setCpf(dto.getCpf());
        entity.setEndereco(dto.getEndereco());
        entity.setObservacoes(dto.getObservacoes());
        entity.setAtivo(dto.getAtivo());
        
        Cliente updated = service.update(id, entity);
        return ResponseEntity.ok(ClienteResponseDTO.fromEntity(updated));
    }
    
    @GetMapping("/telefone/{telefone}")
    @Operation(summary = "Buscar cliente por telefone")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorTelefone(@PathVariable String telefone) {
        List<ClienteResponseDTO> dtos = service.findByTelefone(telefone).stream()
            .map(ClienteResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/busca")
    @Operation(summary = "Buscar clientes por nome")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@RequestParam String q) {
        List<ClienteResponseDTO> dtos = service.findByNomeContaining(q).stream()
            .map(ClienteResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/inativos")
    @Operation(summary = "Listar clientes inativos")
    public ResponseEntity<List<ClienteResponseDTO>> inativos(@RequestParam(defaultValue = "90") int dias) {
        List<ClienteResponseDTO> dtos = service.findInativos(dias).stream()
            .map(ClienteResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir cliente")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
