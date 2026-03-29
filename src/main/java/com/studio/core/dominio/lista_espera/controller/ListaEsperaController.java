package com.studio.core.dominio.lista_espera.controller;

import com.studio.core.dominio.lista_espera.dto.ListaEsperaRequestDTO;
import com.studio.core.dominio.lista_espera.dto.ListaEsperaResponseDTO;
import com.studio.core.dominio.lista_espera.entity.ListaEspera;
import com.studio.core.dominio.lista_espera.mapper.ListaEsperaMapper;
import com.studio.core.service.ListaEsperaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lista-espera")
@Tag(name = "Lista de Espera", description = "Endpoints para gestao de lista de espera")
public class ListaEsperaController {
    
    @Autowired
    private ListaEsperaService service;
    
    @Autowired
    private ListaEsperaMapper listaEsperaMapper;
    
    @GetMapping
    @Operation(summary = "Listar todos")
    public ResponseEntity<List<ListaEsperaResponseDTO>> listar() {
        List<ListaEsperaResponseDTO> dtos = service.findAll().stream()
            .map(listaEsperaMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Listar por status")
    public ResponseEntity<List<ListaEsperaResponseDTO>> porStatus(@PathVariable ListaEspera.StatusListaEspera status) {
        List<ListaEsperaResponseDTO> dtos = service.findByStatus(status).stream()
            .map(listaEsperaMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/aguardando")
    @Operation(summary = "Listar aguardando")
    public ResponseEntity<List<ListaEsperaResponseDTO>> aguardando() {
        List<ListaEsperaResponseDTO> dtos = service.findAguardando().stream()
            .map(listaEsperaMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ListaEsperaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(listaEsperaMapper.toResponse(service.findById(id)));
    }
    
    @PostMapping
    @Operation(summary = "Adicionar a lista de espera")
    public ResponseEntity<ListaEsperaResponseDTO> criar(@Valid @RequestBody ListaEsperaRequestDTO dto) {
        ListaEspera entity = listaEsperaMapper.toEntity(dto);
        
        ListaEspera created = service.create(dto.getClienteId(), dto.getServicoId(), dto.getFuncId(), entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(listaEsperaMapper.toResponse(created));
    }
    
    @PutMapping("/{id}/atendido")
    @Operation(summary = "Marcar como atendido")
    public ResponseEntity<ListaEsperaResponseDTO> marcarAtendido(@PathVariable Long id) {
        return ResponseEntity.ok(listaEsperaMapper.toResponse(service.marcarAtendido(id)));
    }
    
    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar")
    public ResponseEntity<ListaEsperaResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(listaEsperaMapper.toResponse(service.cancelar(id)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
