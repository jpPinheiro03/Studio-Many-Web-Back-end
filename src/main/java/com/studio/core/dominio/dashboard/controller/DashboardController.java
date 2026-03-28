package com.studio.core.dominio.dashboard.controller;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Endpoints para dashboard e métricas")
public class DashboardController {
    
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private FuncionarioRepository FuncionarioRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    @GetMapping("/resumo-dia")
    @Operation(summary = "Resumo do dia")
    public ResponseEntity<Map<String, Object>> resumoDia() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio = hoje.atStartOfDay();
        LocalDateTime fim = hoje.atTime(LocalTime.MAX);
        
        List<Agendamento> agendamentos = agendamentoRepository.findByPeriodo(inicio, fim);
        
        BigDecimal receita = agendamentos.stream()
            .filter(a -> a.getStatus() == com.studio.core.dominio.agendamento.entity.StatusAgendamento.CONCLUIDO 
                      || a.getStatus() == com.studio.core.dominio.agendamento.entity.StatusAgendamento.REALIZADO)
            .map(Agendamento::getValorTotal)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, Object> resumo = new HashMap<>();
        resumo.put("totalAgendamentos", agendamentos.size());
        resumo.put("confirmados", agendamentos.stream().filter(a -> a.getStatus() == com.studio.core.dominio.agendamento.entity.StatusAgendamento.CONFIRMADO).count());
        resumo.put("pendentes", agendamentos.stream().filter(a -> a.getStatus() == com.studio.core.dominio.agendamento.entity.StatusAgendamento.PENDENTE).count());
        resumo.put("receitaDia", receita);
        
        return ResponseEntity.ok(resumo);
    }
    
    @GetMapping("/metricas")
    @Operation(summary = "Métricas gerais")
    public ResponseEntity<Map<String, Object>> metricas(@RequestParam(defaultValue = "30") int dias) {
        Map<String, Object> metricas = new HashMap<>();
        metricas.put("totalClientes", clienteRepository.count());
        metricas.put("totalFuncionarios", FuncionarioRepository.count());
        metricas.put("totalServicos", servicoRepository.count());
        metricas.put("totalAgendamentos", agendamentoRepository.count());
        
        return ResponseEntity.ok(metricas);
    }
    
    @GetMapping("/agenda-semana")
    @Operation(summary = "Agenda da semana")
    public ResponseEntity<Map<String, List<Agendamento>>> agendaSemana() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio = hoje.atStartOfDay();
        LocalDateTime fim = hoje.plusDays(7).atTime(LocalTime.MAX);
        
        List<Agendamento> agendamentos = agendamentoRepository.findByPeriodo(inicio, fim);
        
        Map<String, List<Agendamento>> agenda = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDate dia = hoje.plusDays(i);
            final LocalDate d = dia;
            agenda.put(dia.toString(), agendamentos.stream()
                .filter(a -> a.getDataHoraInicio().toLocalDate().equals(d))
                .toList());
        }
        
        return ResponseEntity.ok(agenda);
    }
}
