package com.studio.core.dominio.pedido.controller;

import com.studio.core.dominio.pedido.dto.PedidoRequestDTO;
import com.studio.core.dominio.pedido.dto.PedidoResponseDTO;
import com.studio.core.dominio.pedido.entity.Pedido;
import com.studio.core.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para gestao de pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService service;
    
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        List<PedidoResponseDTO> dtos = service.findAll().stream()
            .map(PedidoResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<PedidoResponseDTO>> porCliente(@PathVariable Long id) {
        List<PedidoResponseDTO> dtos = service.findByClienteId(id).stream()
            .map(PedidoResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PedidoResponseDTO.fromEntity(service.findById(id)));
    }
    
    @PostMapping
    @Operation(summary = "Criar pedido")
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO dto) {
        Map<String, Object> params = Map.of(
            "clienteId", dto.getClienteId(),
            "itens", dto.getItens()
        );
        Pedido created = service.create(params);
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoResponseDTO.fromEntity(created));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar status do pedido")
    public ResponseEntity<PedidoResponseDTO> atualizar(@PathVariable Long id, @RequestBody Map<String, String> params) {
        Pedido.StatusPedido status = Pedido.StatusPedido.valueOf(params.get("status"));
        return ResponseEntity.ok(PedidoResponseDTO.fromEntity(service.updateStatus(id, status)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
