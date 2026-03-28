package com.studio.core.dominio.produto.controller;

import com.studio.core.dominio.produto.dto.ProdutoRequestDTO;
import com.studio.core.dominio.produto.dto.ProdutoResponseDTO;
import com.studio.core.dominio.produto.entity.Produto;
import com.studio.core.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Endpoints para gestao de produtos")
public class ProdutoController {
    
    @Autowired
    private ProdutoService service;
    
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        List<ProdutoResponseDTO> dtos = service.findAll().stream()
            .map(ProdutoResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoResponseDTO>> ativos() {
        List<ProdutoResponseDTO> dtos = service.findAtivos().stream()
            .map(ProdutoResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ProdutoResponseDTO>> estoqueBaixo(@RequestParam(defaultValue = "10") int limite) {
        List<ProdutoResponseDTO> dtos = service.findEstoqueBaixo(limite).stream()
            .map(ProdutoResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ProdutoResponseDTO.fromEntity(service.findById(id)));
    }
    
    @PostMapping
    @Operation(summary = "Criar produto")
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        Produto entity = new Produto();
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getPreco());
        entity.setEstoque(dto.getEstoque());
        entity.setAtivo(dto.getAtivo());
        
        Produto created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponseDTO.fromEntity(created));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO dto) {
        Produto entity = service.findById(id);
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getPreco());
        entity.setEstoque(dto.getEstoque());
        entity.setAtivo(dto.getAtivo());
        
        Produto updated = service.update(id, entity);
        return ResponseEntity.ok(ProdutoResponseDTO.fromEntity(updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
