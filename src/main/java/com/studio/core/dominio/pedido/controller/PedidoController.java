package com.studio.core.dominio.pedido.controller;

import com.studio.core.dominio.pedido.dto.PedidoRequestDTO;
import com.studio.core.dominio.pedido.dto.PedidoResponseDTO;
import com.studio.core.dominio.pedido.entity.Pedido;
import com.studio.core.dominio.pedido.mapper.PedidoMapper;
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
    
    @Autowired
    private PedidoMapper pedidoMapper;
    
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        List<Pedido> pedidos;
        if (page != null) {
            pedidos = service.findPaginated(page, size);
        } else {
            pedidos = service.findAll();
        }
        List<PedidoResponseDTO> dtos = pedidos.stream()
            .map(pedidoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<PedidoResponseDTO>> porCliente(@PathVariable Long id) {
        List<PedidoResponseDTO> dtos = service.findByClienteId(id).stream()
            .map(pedidoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoMapper.toResponse(service.findById(id)));
    }
    
    @PostMapping
    @Operation(summary = "Criar pedido")
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO dto) {
        Pedido created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoMapper.toResponse(created));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar status do pedido")
    public ResponseEntity<PedidoResponseDTO> atualizar(@PathVariable Long id, @RequestBody Map<String, String> params) {
        Pedido.StatusPedido status;
        try {
            status = Pedido.StatusPedido.valueOf(params.get("status"));
        } catch (IllegalArgumentException e) {
            throw new com.studio.core.exception.BadRequestException("Status inválido: " + params.get("status"));
        }
        return ResponseEntity.ok(pedidoMapper.toResponse(service.updateStatus(id, status)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
