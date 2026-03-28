package com.studio.core.dominio.horario_trabalho.controller;

import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoRequestDTO;
import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoResponseDTO;
import com.studio.core.service.HorarioTrabalhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/horario-trabalho")
@Tag(name = "Horário de Trabalho", description = "Endpoints para gestão de horários de trabalho")
public class HorarioTrabalhoController {
    
    @Autowired
    private HorarioTrabalhoService service;
    
    @GetMapping
    public ResponseEntity<List<HorarioTrabalhoResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/funcionario/{id}")
    public ResponseEntity<List<HorarioTrabalhoResponseDTO>> porFuncionario(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByFuncionarioId(id));
    }
    
    @PostMapping
    @Operation(summary = "Criar horário de trabalho")
    public ResponseEntity<HorarioTrabalhoResponseDTO> criar(@Valid @RequestBody HorarioTrabalhoRequestDTO dto) {
        HorarioTrabalhoResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
