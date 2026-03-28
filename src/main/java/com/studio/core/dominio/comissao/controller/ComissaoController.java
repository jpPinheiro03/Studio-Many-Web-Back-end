package com.studio.core.dominio.comissao.controller;

import com.studio.core.dominio.comissao.dto.ComissaoResponseDTO;
import com.studio.core.service.ComissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/comissoes")
@Tag(name = "Comissões", description = "Endpoints para gestão de comissões")
public class ComissaoController {
    
    @Autowired
    private ComissaoService service;
    
    @GetMapping
    public ResponseEntity<List<ComissaoResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/profissional/{id}")
    public ResponseEntity<List<ComissaoResponseDTO>> porProfissional(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByFuncionarioId(id));
    }
    
    @GetMapping("/profissional/{id}/pendentes")
    public ResponseEntity<List<ComissaoResponseDTO>> pendentesPorProfissional(@PathVariable Long id) {
        return ResponseEntity.ok(service.findPendentesPorFuncionario(id));
    }
    
    @GetMapping("/profissional/{id}/total-pendente")
    public ResponseEntity<BigDecimal> totalPendente(@PathVariable Long id) {
        return ResponseEntity.ok(service.totalPendentePorFuncionario(id));
    }
    
    @GetMapping("/pendentes")
    public ResponseEntity<List<ComissaoResponseDTO>> pendentes() {
        return ResponseEntity.ok(service.findPendentes());
    }
    
    @GetMapping("/periodo")
    public ResponseEntity<List<ComissaoResponseDTO>> periodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.findByPeriodo(inicio, fim));
    }
    
    @PostMapping("/gerar/{agendamentoId}")
    @Operation(summary = "Gerar comissão")
    public ResponseEntity<ComissaoResponseDTO> gerar(
            @PathVariable Long agendamentoId,
            @RequestParam(defaultValue = "40") BigDecimal percentual) {
        return ResponseEntity.ok(service.gerar(agendamentoId, percentual));
    }
    
    @PutMapping("/{id}/pagar")
    public ResponseEntity<ComissaoResponseDTO> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(service.pagar(id));
    }
    
    @PutMapping("/profissional/{id}/pagar-todas")
    public ResponseEntity<List<ComissaoResponseDTO>> pagarTodas(@PathVariable Long id) {
        return ResponseEntity.ok(service.pagarTodasPorFuncionario(id));
    }
}
