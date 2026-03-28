package com.studio.core.dominio.servico.controller;

import com.studio.core.dominio.servico.dto.ServicoRequestDTO;
import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import com.studio.core.dominio.servico.entity.Servico;
import com.studio.core.dominio.servico.mapper.ServicoMapper;
import com.studio.core.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
@Tag(name = "Serviços", description = "Endpoints para gestão de serviços")
public class ServicoController {
    
    @Autowired
    private ServicoService service;
    
    @GetMapping
    @Operation(summary = "Listar serviços")
    public ResponseEntity<List<ServicoResponseDTO>> listar() {
        List<ServicoResponseDTO> dtos = service.findAll().stream()
            .map(ServicoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        Servico entity = service.findById(id);
        return ResponseEntity.ok(ServicoMapper.toResponse(entity));
    }
    
    @PostMapping
    @Operation(summary = "Criar serviço")
    public ResponseEntity<ServicoResponseDTO> criar(@Valid @RequestBody ServicoRequestDTO dto) {
        Servico entity = ServicoMapper.toEntity(dto);
        
        Servico created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ServicoMapper.toResponse(created));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ServicoRequestDTO dto) {
        Servico entity = ServicoMapper.toEntity(dto);
        
        Servico updated = service.update(id, entity);
        return ResponseEntity.ok(ServicoMapper.toResponse(updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
