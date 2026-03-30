package com.studio.core.dominio.disponibilidade.controller;

import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.service.DisponibilidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidade")
@Tag(name = "Disponibilidade", description = "Endpoints para verificar disponibilidade")
public class DisponibilidadeController {

    @Autowired
    private DisponibilidadeService service;

    @GetMapping("/slots")
    @Operation(summary = "Verificar slots disponíveis")
    public ResponseEntity<List<LocalTime>> slots(
            @RequestParam Long servicoId,
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.calcularSlots(servicoId, funcionarioId, data));
    }

    @GetMapping("/validar")
    @Operation(summary = "Validar horário")
    public ResponseEntity<Boolean> validar(
            @RequestParam Long servicoId,
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        return ResponseEntity.ok(service.validarHorario(servicoId, funcionarioId, dataHora));
    }

    @GetMapping("/horarios-vagos")
    @Operation(summary = "Listar horários vagos")
    public ResponseEntity<List<LocalTime>> horariosVagos(
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.horariosVagos(funcionarioId, data));
    }

    @GetMapping("/profissionais")
    @Operation(summary = "Listar profissionais disponíveis")
    public ResponseEntity<List<Funcionario>> profissionais(
            @RequestParam Long servicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.profissionaisDisponiveis(servicoId, data));
    }
}
