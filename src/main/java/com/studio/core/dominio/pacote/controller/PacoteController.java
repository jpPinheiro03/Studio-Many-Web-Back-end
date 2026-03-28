package com.studio.core.dominio.pacote.controller;

import com.studio.core.dominio.pacote.dto.PacoteRequestDTO;
import com.studio.core.dominio.pacote.dto.PacoteResponseDTO;
import com.studio.core.dominio.pacote.entity.Pacote;
import com.studio.core.service.PacoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pacotes")
@Tag(name = "Pacotes", description = "Endpoints para gestão de pacotes")
public class PacoteController {
    
    @Autowired
    private PacoteService service;
    
    @GetMapping
    public ResponseEntity<List<PacoteResponseDTO>> listar() {
        List<PacoteResponseDTO> dtos = service.findAll().stream()
            .map(PacoteResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PacoteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PacoteResponseDTO.fromEntity(service.findById(id)));
    }
    
    @PostMapping
    @Operation(summary = "Criar pacote")
    public ResponseEntity<PacoteResponseDTO> criar(@Valid @RequestBody PacoteRequestDTO dto) {
        Pacote entity = new Pacote();
        entity.setNome(dto.getNome());
        entity.setQuantidadeSessoes(dto.getQuantidadeSessoes());
        entity.setPreco(dto.getPreco());
        entity.setValidadeDias(dto.getValidadeDias());
        entity.setAtivo(dto.getAtivo());
        
        Pacote created = service.create(dto.getServicoId(), entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(PacoteResponseDTO.fromEntity(created));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PacoteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PacoteRequestDTO dto) {
        Pacote entity = service.findById(id);
        entity.setNome(dto.getNome());
        entity.setQuantidadeSessoes(dto.getQuantidadeSessoes());
        entity.setPreco(dto.getPreco());
        entity.setValidadeDias(dto.getValidadeDias());
        entity.setAtivo(dto.getAtivo());
        
        Pacote updated = service.update(id, entity);
        return ResponseEntity.ok(PacoteResponseDTO.fromEntity(updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
