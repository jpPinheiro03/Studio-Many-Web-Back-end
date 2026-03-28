package com.studio.core.dominio.funcionario_servico.controller;

import com.studio.core.dominio.funcionario_servico.dto.FuncionarioServicoRequestDTO;
import com.studio.core.dominio.funcionario_servico.dto.FuncionarioServicoResponseDTO;
import com.studio.core.service.FuncionarioServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/funcionario-servicos")
@Tag(name = "Serviços do Funcionário", description = "Endpoints para associar serviços a funcionários")
public class FuncionarioServicoController {
    
    @Autowired
    private FuncionarioServicoService service;
    
    @GetMapping
    public ResponseEntity<List<FuncionarioServicoResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/funcionario/{id}")
    public ResponseEntity<List<FuncionarioServicoResponseDTO>> porFuncionario(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByFuncionarioId(id));
    }
    
    @PostMapping
    @Operation(summary = "Associar serviço ao funcionário")
    public ResponseEntity<FuncionarioServicoResponseDTO> criar(@Valid @RequestBody FuncionarioServicoRequestDTO dto) {
        FuncionarioServicoResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
