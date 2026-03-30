package com.studio.core.dominio.relatorio.controller;

import com.studio.core.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
@Tag(name = "Relatórios", description = "Endpoints para relatórios")
public class RelatorioController {

    @Autowired
    private RelatorioService service;

    @GetMapping("/agendamentos")
    @Operation(summary = "Relatório de agendamentos")
    public ResponseEntity<Map<String, Object>> relatorioAgendamentos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.relatorioAgendamentos(inicio, fim));
    }

    @GetMapping("/servicos")
    @Operation(summary = "Relatório de serviços")
    public ResponseEntity<Map<String, Object>> relatorioServicos() {
        return ResponseEntity.ok(service.relatorioServicos());
    }

    @GetMapping("/receita")
    @Operation(summary = "Relatório de receita")
    public ResponseEntity<Map<String, BigDecimal>> relatorioReceita(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.relatorioReceita(inicio, fim));
    }
}
