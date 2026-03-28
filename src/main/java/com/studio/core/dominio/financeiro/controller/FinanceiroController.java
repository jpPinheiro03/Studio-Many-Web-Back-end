package com.studio.core.dominio.financeiro.controller;

import com.studio.core.dominio.financeiro.entity.Movimento;
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
    
    @GetMapping
    public ResponseEntity<List<Movimento>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/data/{data}")
    public ResponseEntity<List<Movimento>> porData(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.findByData(data));
    }
    
    @GetMapping("/periodo")
    public ResponseEntity<List<Movimento>> periodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.findByPeriodo(inicio, fim));
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
    public ResponseEntity<Movimento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    @Operation(summary = "Criar movimento")
    public ResponseEntity<Movimento> criar(@RequestBody Movimento movimento) {
        Movimento created = service.create(movimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/contas-receber")
    public ResponseEntity<List<Movimento>> contasReceber() {
        return ResponseEntity.ok(service.findContasReceber());
    }
    
    @GetMapping("/contas-pagar")
    public ResponseEntity<List<Movimento>> contasPagar() {
        return ResponseEntity.ok(service.findContasPagar());
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
        List<Movimento> movimentos = service.findByData(data);
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
