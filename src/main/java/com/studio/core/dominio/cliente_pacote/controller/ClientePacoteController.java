package com.studio.core.dominio.cliente_pacote.controller;

import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteRequestDTO;
import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteResponseDTO;
import com.studio.core.dominio.cliente_pacote.entity.ClientePacote;
import com.studio.core.service.ClientePacoteService;
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
@RequestMapping("/api/cliente-pacotes")
@Tag(name = "Pacotes do Cliente", description = "Endpoints para gestão de pacotes do cliente")
public class ClientePacoteController {
    
    @Autowired
    private ClientePacoteService service;
    
    @GetMapping
    public ResponseEntity<List<ClientePacoteResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<ClientePacoteResponseDTO>> porCliente(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByClienteId(id));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClientePacoteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    @Operation(summary = "Criar pacote para cliente")
    public ResponseEntity<ClientePacoteResponseDTO> criar(@Valid @RequestBody ClientePacoteRequestDTO dto) {
        ClientePacoteResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PostMapping("/{id}/usar-sessao")
    @Operation(summary = "Usar sessão do pacote")
    public ResponseEntity<ClientePacoteResponseDTO> usarSessao(@PathVariable Long id) {
        return ResponseEntity.ok(service.usarSessao(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClientePacoteResponseDTO> atualizar(@PathVariable Long id, @RequestBody Map<String, String> params) {
        ClientePacote.StatusClientePacote status = ClientePacote.StatusClientePacote.valueOf(params.get("status"));
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
