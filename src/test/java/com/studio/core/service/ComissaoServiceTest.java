package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.comissao.entity.Comissao;
import com.studio.core.dominio.comissao.repository.ComissaoRepository;
import com.studio.core.dominio.financeiro.repository.MovimentoRepository;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComissaoServiceTest {

    @Mock private ComissaoRepository repository;
    @Mock private AgendamentoRepository agendamentoRepository;
    @Mock private MovimentoRepository movimentoRepository;

    @InjectMocks
    private ComissaoService service;

    private Agendamento agendamento;
    private Funcionario funcionario;

    @BeforeEach
    void setUp() {
        funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Funcionário Teste");

        agendamento = new Agendamento();
        agendamento.setId(1L);
        agendamento.setFuncionario(funcionario);
        agendamento.setValorTotal(new BigDecimal("100.00"));
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
    }

    @Test
    void gerar_DeveCriarComissao() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(repository.findByAgendamento_Id(1L)).thenReturn(Collections.emptyList());
        when(repository.save(any())).thenReturn(new Comissao());

        service.gerar(1L, new BigDecimal("30"));

        verify(repository).save(any());
    }

    @Test
    void gerar_DeveLancarErro_QuandoAgendamentoNaoConcluido() {
        agendamento.setStatus(StatusAgendamento.PENDENTE);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        assertThrows(BadRequestException.class, () -> service.gerar(1L, new BigDecimal("30")));
    }

    @Test
    void gerar_DeveLancarErro_QuandoPercentualInvalido() {
        assertThrows(BadRequestException.class, () -> service.gerar(1L, new BigDecimal("150")));
        assertThrows(BadRequestException.class, () -> service.gerar(1L, new BigDecimal("-1")));
    }

    @Test
    void gerar_DeveLancarErro_QuandoJaExiste() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(repository.findByAgendamento_Id(1L)).thenReturn(Collections.singletonList(new Comissao()));

        assertThrows(BadRequestException.class, () -> service.gerar(1L, new BigDecimal("30")));
    }

    @Test
    void pagar_DeveCriarMovimento() {
        Comissao comissao = new Comissao();
        comissao.setId(1L);
        comissao.setValor(new BigDecimal("30"));
        comissao.setFunc(funcionario);
        comissao.setAgendamento(agendamento);
        comissao.setStatus(Comissao.StatusComissao.PENDENTE);

        when(repository.findById(1L)).thenReturn(Optional.of(comissao));
        when(repository.save(any())).thenReturn(comissao);

        service.pagar(1L);

        verify(repository).save(any());
        verify(movimentoRepository).save(any());
    }

    @Test
    void pagar_DeveLancarErro_QuandoJaPaga() {
        Comissao comissao = new Comissao();
        comissao.setStatus(Comissao.StatusComissao.PAGA);

        when(repository.findById(1L)).thenReturn(Optional.of(comissao));

        assertThrows(BadRequestException.class, () -> service.pagar(1L));
    }
}
