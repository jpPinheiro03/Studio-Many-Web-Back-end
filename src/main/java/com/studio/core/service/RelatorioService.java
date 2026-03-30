package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.financeiro.repository.MovimentoRepository;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class RelatorioService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private MovimentoRepository movimentoRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> relatorioAgendamentos(LocalDate inicio, LocalDate fim) {
        List<Agendamento> agendamentos = agendamentoRepository.findByPeriodo(
            inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));

        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("total", agendamentos.size());
        relatorio.put("confirmados", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.CONFIRMADO).count());
        relatorio.put("pendentes", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.PENDENTE).count());
        relatorio.put("cancelados", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.CANCELADO).count());
        relatorio.put("concluidos", agendamentos.stream().filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO).count());
        return relatorio;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> relatorioServicos() {
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("totalServicos", servicoRepository.count());
        relatorio.put("servicos", servicoRepository.findAll());
        return relatorio;
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> relatorioReceita(LocalDate inicio, LocalDate fim) {
        BigDecimal receitas = movimentoRepository.totalReceitas(inicio, fim);
        BigDecimal despesas = movimentoRepository.totalDespesas(inicio, fim);
        BigDecimal saldo = movimentoRepository.calcularSaldo(inicio, fim);

        Map<String, BigDecimal> relatorio = new LinkedHashMap<>();
        relatorio.put("receitas", receitas);
        relatorio.put("despesas", despesas);
        relatorio.put("saldo", saldo);
        return relatorio;
    }
}
