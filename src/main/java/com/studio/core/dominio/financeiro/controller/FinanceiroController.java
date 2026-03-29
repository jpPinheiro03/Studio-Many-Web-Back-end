package com.studio.core.dominio.financeiro.controller;

import com.studio.core.dominio.financeiro.mapper.MovimentoMapper;
import com.studio.core.dominio.movimento.dto.MovimentoRequestDTO;
import com.studio.core.dominio.movimento.dto.MovimentoResponseDTO;
import com.studio.core.service.FinanceiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/financeiro")
@Tag(name = "Financeiro", description = "Endpoints para gestão financeira")
public class FinanceiroController {

    @Autowired
    private FinanceiroService service;

    @Autowired
    private MovimentoMapper movimentoMapper;

    @GetMapping
    public ResponseEntity<List<MovimentoResponseDTO>> listar() {
        List<MovimentoResponseDTO> dtos = service.findAll().stream()
            .map(movimentoMapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/data/{data}")
    public ResponseEntity<List<MovimentoResponseDTO>> porData(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<MovimentoResponseDTO> dtos = service.findByData(data).stream()
            .map(movimentoMapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<MovimentoResponseDTO>> periodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<MovimentoResponseDTO> dtos = service.findByPeriodo(inicio, fim).stream()
            .map(movimentoMapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/resumo-mensal")
    public ResponseEntity<Map<String, BigDecimal>> resumoMensal(
            @RequestParam Integer ano, @RequestParam Integer mes) {
        LocalDate inicio = LocalDate.of(ano, mes, 1);
        LocalDate fim = inicio.withDayOfMonth(inicio.lengthOfMonth());
        return ResponseEntity.ok(Map.of(
            "receitas", service.totalReceitas(inicio, fim),
            "despesas", service.totalDespesas(inicio, fim),
            "saldo", service.calcularSaldo(inicio, fim)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(movimentoMapper.toResponse(service.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar movimento")
    public ResponseEntity<MovimentoResponseDTO> criar(@RequestBody MovimentoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(movimentoMapper.toResponse(service.create(movimentoMapper.toEntity(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contas-receber")
    public ResponseEntity<List<MovimentoResponseDTO>> contasReceber() {
        List<MovimentoResponseDTO> dtos = service.findContasReceber().stream()
            .map(movimentoMapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/contas-pagar")
    public ResponseEntity<List<MovimentoResponseDTO>> contasPagar() {
        List<MovimentoResponseDTO> dtos = service.findContasPagar().stream()
            .map(movimentoMapper::toResponse).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/dia/{data}/resumo")
    public ResponseEntity<Map<String, BigDecimal>> resumoDia(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        BigDecimal receitas = service.totalReceitas(data, data);
        BigDecimal despesas = service.totalDespesas(data, data);
        return ResponseEntity.ok(Map.of(
            "receitas", receitas,
            "despesas", despesas,
            "saldo", receitas.subtract(despesas)
        ));
    }

    @GetMapping("/dia/{data}/detalhado")
    public ResponseEntity<Map<String, Object>> detalhadoDia(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<MovimentoResponseDTO> movimentos = service.findByData(data).stream()
            .map(movimentoMapper::toResponse).toList();
        BigDecimal receitas = service.totalReceitas(data, data);
        BigDecimal despesas = service.totalDespesas(data, data);

        Map<String, Object> detalhado = Map.of(
            "movimentos", movimentos,
            "resumo", Map.of(
                "receitas", receitas,
                "despesas", despesas,
                "saldo", receitas.subtract(despesas)
            )
        );
        return ResponseEntity.ok(detalhado);
    }
}
