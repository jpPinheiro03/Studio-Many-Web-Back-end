package com.studio.core.dominio.funcionario.controller;

import com.studio.core.dominio.funcionario.dto.FuncionarioRequestDTO;
import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.mapper.FuncionarioMapper;
import com.studio.core.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@Tag(name = "Funcionários", description = "Endpoints para gestão de funcionários")
public class FuncionarioController {
    
    @Autowired
    private FuncionarioService service;
    
    @Autowired
    private FuncionarioMapper funcionarioMapper;
    
    @GetMapping
    @Operation(summary = "Listar funcionários")
    public ResponseEntity<List<FuncionarioResponseDTO>> listar() {
        List<FuncionarioResponseDTO> dtos = service.findAll().stream()
            .map(funcionarioMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@PathVariable Long id) {
        Funcionario entity = service.findById(id);
        return ResponseEntity.ok(funcionarioMapper.toResponse(entity));
    }
    
    @PostMapping
    @Operation(summary = "Criar funcionário")
    public ResponseEntity<FuncionarioResponseDTO> criar(@Valid @RequestBody FuncionarioRequestDTO dto) {
        Funcionario entity = funcionarioMapper.toEntity(dto);
        
        Funcionario created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioMapper.toResponse(created));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody FuncionarioRequestDTO dto) {
        Funcionario entity = funcionarioMapper.toEntity(dto);
        
        Funcionario updated = service.update(id, entity);
        return ResponseEntity.ok(funcionarioMapper.toResponse(updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
