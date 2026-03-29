package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.bloqueio.repository.BloqueioRepository;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.comissao.repository.ComissaoRepository;
import com.studio.core.dominio.financeiro.repository.MovimentoRepository;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.parcela.repository.ParcelaRepository;
import com.studio.core.dominio.servico.entity.Servico;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock private AgendamentoRepository agendamentoRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ServicoRepository servicoRepository;
    @Mock private FuncionarioRepository funcionarioRepository;
    @Mock private BloqueioRepository bloqueioRepository;
    @Mock private MovimentoRepository movimentoRepository;
    @Mock private ComissaoRepository comissaoRepository;
    @Mock private ParcelaRepository parcelaRepository;
    @Mock private AuditoriaService auditoriaService;

    @InjectMocks
    private AgendamentoService service;

    private Agendamento agendamento;
    private Cliente cliente;
    private Servico servico;
    private Funcionario funcionario;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        cliente.setAtivo(true);

        servico = new Servico();
        servico.setId(1L);
        servico.setNome("Serviço Teste");
        servico.setPreco(new BigDecimal("100"));
        servico.setDuracaoMinutos(60);
        servico.setAtivo(true);

        funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Funcionário Teste");
        funcionario.setAtivo(true);

        agendamento = new Agendamento();
        agendamento.setId(1L);
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setFuncionario(funcionario);
        agendamento.setDataHoraInicio(LocalDateTime.now().plusDays(1));
        agendamento.setDataHoraFim(LocalDateTime.now().plusDays(1).plusHours(1));
        agendamento.setValorTotal(new BigDecimal("100"));
        agendamento.setStatus(StatusAgendamento.PENDENTE);
    }

    @Test
    void findById_DeveRetornarAgendamento_QuandoExiste() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        Agendamento result = service.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_DeveLancarExcecao_QuandoNaoExiste() {
        when(agendamentoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void create_DeveCriarComStatusPendente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(agendamentoRepository.findConflitoHorario(any(), any(), any())).thenReturn(Collections.emptyList());
        when(bloqueioRepository.findByFunc_IdAndDataBetween(any(), any(), any())).thenReturn(Collections.emptyList());
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        Agendamento result = service.create(1L, 1L, 1L, agendamento);

        assertEquals(StatusAgendamento.PENDENTE, result.getStatus());
        verify(agendamentoRepository).save(any());
    }

    @Test
    void create_DeveLancarErro_QuandoFuncionarioInativo() {
        funcionario.setAtivo(false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(agendamentoRepository.findConflitoHorario(any(), any(), any())).thenReturn(Collections.emptyList());
        when(bloqueioRepository.findByFunc_IdAndDataBetween(any(), any(), any())).thenReturn(Collections.emptyList());

        assertThrows(BadRequestException.class, () -> service.create(1L, 1L, 1L, agendamento));
    }

    @Test
    void create_DeveLancarErro_QuandoConflitoDeHorario() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(agendamentoRepository.findConflitoHorario(any(), any(), any())).thenReturn(Collections.singletonList(agendamento));

        assertThrows(BadRequestException.class, () -> service.create(1L, 1L, 1L, agendamento));
    }

    @Test
    void confirmarSinal_DeveTransicionarParaConfirmado() {
        agendamento.setStatus(StatusAgendamento.AGUARDANDO_CONFIRMACAO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        Agendamento result = service.confirmarSinal(1L);

        assertEquals(StatusAgendamento.CONFIRMADO, result.getStatus());
        assertNotNull(result.getDataConfirmacaoSinal());
    }

    @Test
    void confirmarSinal_DeveLancarErro_QuandoStatusIncorreto() {
        agendamento.setStatus(StatusAgendamento.PENDENTE);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        assertThrows(BadRequestException.class, () -> service.confirmarSinal(1L));
    }

    @Test
    void finalizarAtendimento_DeveTransicionarParaConcluido() {
        agendamento.setStatus(StatusAgendamento.EM_ATENDIMENTO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);
        when(movimentoRepository.findByAgendamento_Id(1L)).thenReturn(Collections.emptyList());
        when(comissaoRepository.findByAgendamento_Id(1L)).thenReturn(Collections.emptyList());

        Agendamento result = service.finalizarAtendimento(1L);

        assertEquals(StatusAgendamento.CONCLUIDO, result.getStatus());
        assertNotNull(result.getDataFinalizacao());
        verify(movimentoRepository).save(any());
        verify(comissaoRepository).save(any());
    }

    @Test
    void cancelar_DeveLancarErro_QuandoConcluido() {
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        assertThrows(BadRequestException.class, () -> service.cancelar(1L, "motivo", null));
    }
}
