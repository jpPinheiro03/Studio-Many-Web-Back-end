package com.studio.core.dominio.parcela.controller;

import com.studio.core.dominio.parcela.dto.ParcelaResponseDTO;
import com.studio.core.service.ParcelaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parcelas")
@Tag(name = "Parcelas", description = "Endpoints para gestão de parcelas")
public class ParcelaController {
    
    @Autowired
    private ParcelaService service;
    
    @GetMapping
    public ResponseEntity<List<ParcelaResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/agendamento/{id}")
    public ResponseEntity<List<ParcelaResponseDTO>> porAgendamento(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByAgendamentoId(id));
    }
    
    @GetMapping("/pendentes")
    public ResponseEntity<List<ParcelaResponseDTO>> pendentes() {
        return ResponseEntity.ok(service.findPendentes());
    }
    
    @GetMapping("/vencidas")
    public ResponseEntity<List<ParcelaResponseDTO>> vencidas() {
        return ResponseEntity.ok(service.findVencidas());
    }
    
    @GetMapping("/vencer-proximo")
    public ResponseEntity<List<ParcelaResponseDTO>> aVencer(@RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(service.findAVencer(dias));
    }
    
    @PostMapping("/gerar/{agendamentoId}")
    @Operation(summary = "Gerar parcelas")
    public ResponseEntity<List<ParcelaResponseDTO>> gerar(
            @PathVariable Long agendamentoId,
            @RequestBody Map<String, Object> params) {
        int quantidade = (int) params.get("quantidade");
        LocalDate dataPrimeira = LocalDate.parse((String) params.get("dataPrimeira"));
        return ResponseEntity.ok(service.gerarParcelas(agendamentoId, quantidade, dataPrimeira));
    }
    
    @PutMapping("/{id}/quitar")
    public ResponseEntity<ParcelaResponseDTO> quitar(@PathVariable Long id) {
        return ResponseEntity.ok(service.quitar(id));
    }
    
    @PutMapping("/agendamento/{id}/quitar-todas")
    public ResponseEntity<List<ParcelaResponseDTO>> quitarTodas(@PathVariable Long id) {
        return ResponseEntity.ok(service.quitarTodas(id));
    }
}
