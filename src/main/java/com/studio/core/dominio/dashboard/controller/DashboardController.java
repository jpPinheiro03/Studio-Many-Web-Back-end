package com.studio.core.dominio.dashboard.controller;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Endpoints para dashboard e métricas")
public class DashboardController {

    @Autowired
    private DashboardService service;

    @GetMapping("/resumo-dia")
    @Operation(summary = "Resumo do dia")
    public ResponseEntity<Map<String, Object>> resumoDia() {
        return ResponseEntity.ok(service.resumoDia());
    }

    @GetMapping("/metricas")
    @Operation(summary = "Métricas gerais")
    public ResponseEntity<Map<String, Object>> metricas(@RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(service.metricas(dias));
    }

    @GetMapping("/agenda-semana")
    @Operation(summary = "Agenda da semana")
    public ResponseEntity<Map<String, List<Agendamento>>> agendaSemana() {
        return ResponseEntity.ok(service.agendaSemana());
    }
}
