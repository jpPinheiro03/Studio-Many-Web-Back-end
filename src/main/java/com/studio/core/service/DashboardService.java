package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class DashboardService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> resumoDia() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio = hoje.atStartOfDay();
        LocalDateTime fim = hoje.atTime(LocalTime.MAX);

        List<Agendamento> agendamentos = agendamentoRepository.findByPeriodo(inicio, fim);

        BigDecimal receita = agendamentos.stream()
            .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO)
            .map(Agendamento::getValorTotal)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> resumo = new HashMap<>();
        resumo.put("totalAgendamentos", agendamentos.size());
        resumo.put("confirmados", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.CONFIRMADO).count());
        resumo.put("pendentes", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.PENDENTE).count());
        resumo.put("receitaDia", receita);
        return resumo;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> metricas(int dias) {
        Map<String, Object> metricas = new HashMap<>();
        metricas.put("totalClientes", clienteRepository.count());
        metricas.put("totalFuncionarios", funcionarioRepository.count());
        metricas.put("totalServicos", servicoRepository.count());
        metricas.put("totalAgendamentos", agendamentoRepository.count());
        return metricas;
    }

    @Transactional(readOnly = true)
    public Map<String, List<Agendamento>> agendaSemana() {
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
        return agenda;
    }
}
