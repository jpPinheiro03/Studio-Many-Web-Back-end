package com.studio.core.dominio.bloqueio.controller;

import com.studio.core.dominio.bloqueio.dto.BloqueioRequestDTO;
import com.studio.core.dominio.bloqueio.dto.BloqueioResponseDTO;
import com.studio.core.service.BloqueioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bloqueios")
@Tag(name = "Bloqueios", description = "Endpoints para gestão de bloqueios de funcionários")
public class BloqueioController {
    
    @Autowired
    private BloqueioService service;
    
    @GetMapping
    public ResponseEntity<List<BloqueioResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/funcionario/{id}")
    public ResponseEntity<List<BloqueioResponseDTO>> porFuncionario(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByFuncionarioId(id));
    }
    
    @PostMapping
    @Operation(summary = "Criar bloqueio")
    public ResponseEntity<BloqueioResponseDTO> criar(@Valid @RequestBody BloqueioRequestDTO dto) {
        BloqueioResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
