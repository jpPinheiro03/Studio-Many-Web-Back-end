package com.studio.core.dominio.relatorio.controller;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.financeiro.repository.MovimentoRepository;
import com.studio.core.dominio.servico.entity.Servico;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/relatorios")
@Tag(name = "Relatórios", description = "Endpoints para relatórios")
public class RelatorioController {
    
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private MovimentoRepository movimentoRepository;
    
    @GetMapping("/agendamentos")
    @Operation(summary = "Relatório de agendamentos")
    public ResponseEntity<Map<String, Object>> relatorioAgendamentos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        List<Agendamento> agendamentos = agendamentoRepository.findByPeriodo(inicio.atStartOfDay(), fim.atTime(java.time.LocalTime.MAX));
        
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("total", agendamentos.size());
        relatorio.put("confirmados", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.CONFIRMADO).count());
        relatorio.put("pendentes", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.PENDENTE).count());
        relatorio.put("cancelados", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.CANCELADO).count());
        relatorio.put("concluidos", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO || a.getStatus() == StatusAgendamento.REALIZADO).count());
        relatorio.put("realizados", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.REALIZADO).count());
        
        return ResponseEntity.ok(relatorio);
    }
    
    @GetMapping("/servicos")
    @Operation(summary = "Relatório de serviços")
    public ResponseEntity<Map<String, Object>> relatorioServicos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        List<Servico> servicos = servicoRepository.findAll();
        
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("totalServicos", servicos.size());
        relatorio.put("servicos", servicos);
        
        return ResponseEntity.ok(relatorio);
    }
    
    @GetMapping("/receita")
    @Operation(summary = "Relatório de receita")
    public ResponseEntity<Map<String, BigDecimal>> relatorioReceita(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        BigDecimal receitas = movimentoRepository.totalReceitas(inicio, fim);
        BigDecimal despesas = movimentoRepository.totalDespesas(inicio, fim);
        BigDecimal saldo = movimentoRepository.calcularSaldo(inicio, fim);
        
        Map<String, BigDecimal> relatorio = new HashMap<>();
        relatorio.put("receitas", receitas);
        relatorio.put("despesas", despesas);
        relatorio.put("saldo", saldo);
        
        return ResponseEntity.ok(relatorio);
    }
}
