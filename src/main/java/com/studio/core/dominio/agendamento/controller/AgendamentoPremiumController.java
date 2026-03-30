package com.studio.core.dominio.agendamento.controller;

import com.studio.core.dominio.agendamento.dto.AgendamentoRecorrenteRequestDTO;
import com.studio.core.dominio.agendamento.dto.AgendamentoResponseDTO;
import com.studio.core.dominio.agendamento.dto.CancelamentoPremiumRequestDTO;
import com.studio.core.dominio.agendamento.dto.HistoricoNoShowDTO;
import com.studio.core.dominio.agendamento.dto.ReagendamentoRequestDTO;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.mapper.AgendamentoMapper;
import com.studio.core.service.AgendamentoPremiumService;
import com.studio.core.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos-premium")
@Tag(name = "Agendamento Premium", description = "Endpoints premium para agendamentos")
public class AgendamentoPremiumController {

    @Autowired
    private AgendamentoPremiumService service;

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private AgendamentoMapper agendamentoMapper;

    @PutMapping("/{id}/reagendar")
    @Operation(summary = "Reagendar agendamento")
    public ResponseEntity<AgendamentoResponseDTO> reagendar(
            @PathVariable Long id, @RequestBody ReagendamentoRequestDTO dto) {
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.reagendar(id, dto)));
    }

    @GetMapping("/{id}/taxa-cancelamento")
    @Operation(summary = "Calcular taxa de cancelamento")
    public ResponseEntity<Map<String, Object>> calcularTaxa(@PathVariable Long id) {
        BigDecimal taxa = service.calcularTaxaCancelamento(id);
        Agendamento a = agendamentoService.findById(id);
        long horas = java.time.Duration.between(java.time.LocalDateTime.now(), a.getDataHoraInicio()).toHours();
        return ResponseEntity.ok(Map.of(
            "agendamentoId", id,
            "taxa", taxa,
            "horasAntes", horas,
            "politica", Map.of(
                "> 48h", "sem taxa",
                "24-48h", "20%",
                "12-24h", "50%",
                "< 12h", "100%"
            )
        ));
    }

    @PutMapping("/{id}/cancelar-premium")
    @Operation(summary = "Cancelar com política de taxa")
    public ResponseEntity<AgendamentoResponseDTO> cancelarPremium(
            @PathVariable Long id, @RequestBody CancelamentoPremiumRequestDTO dto) {
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.cancelarComTaxa(id, dto)));
    }

    @GetMapping("/no-shows/{clienteId}")
    @Operation(summary = "Histórico de no-shows do cliente")
    public ResponseEntity<HistoricoNoShowDTO> historicoNoShows(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.historicoNoShows(clienteId));
    }

    @GetMapping("/no-shows/{clienteId}/bloqueado")
    @Operation(summary = "Verifica se cliente está bloqueado por no-shows")
    public ResponseEntity<Map<String, Boolean>> clienteBloqueado(@PathVariable Long clienteId) {
        return ResponseEntity.ok(Map.of("bloqueado", service.clienteBloqueadoPorNoShows(clienteId)));
    }

    @GetMapping("/sugestoes")
    @Operation(summary = "Sugerir horários alternativos próximos")
    public ResponseEntity<List<LocalTime>> sugestoes(
            @RequestParam Long servicoId,
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataDesejada,
            @RequestParam(defaultValue = "7") int diasRange) {
        return ResponseEntity.ok(service.sugerirHorariosProximos(servicoId, funcionarioId, dataDesejada, diasRange));
    }

    @PostMapping("/recorrente")
    @Operation(summary = "Criar agendamentos recorrentes")
    public ResponseEntity<List<AgendamentoResponseDTO>> criarRecorrente(@RequestBody AgendamentoRecorrenteRequestDTO dto) {
        List<AgendamentoResponseDTO> dtos = service.criarRecorrente(dto).stream()
            .map(agendamentoMapper::toResponse).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }
}
