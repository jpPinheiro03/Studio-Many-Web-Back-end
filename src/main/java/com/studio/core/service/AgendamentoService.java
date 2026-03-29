package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import com.studio.core.event.agendamento.*;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service // Esta classe contém lógica de negócio
@Transactional // Métodos rodam dentro de transação (commit/rollback)
public class AgendamentoService {

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private AgendamentoRepository repository;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private ClienteRepository clienteRepository;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private ServicoRepository servicoRepository;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private FuncionarioRepository funcionarioRepository;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true) // Transação somente leitura (mais rápido)
    public List<Agendamento> findAll() {
        return repository.findAllWithRelations();
    }

    @Transactional(readOnly = true) // Transação somente leitura (mais rápido)
    public Agendamento findById(Long id) {
        return repository.findByIdWithRelations(id)
            .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado com ID: " + id));
    }

    public Agendamento create(Agendamento agendamento) {
        return create(null, null, null, agendamento);
    }

    public Agendamento create(Long clienteId, Long servicoId, Long funcId, Agendamento agendamento) {
        if (clienteId == null) {
            clienteId = agendamento.getClienteId();
        }
        if (servicoId == null) {
            servicoId = agendamento.getServicoId();
        }
        if (funcId == null) {
            funcId = agendamento.getFuncionarioId();
        }

        if (clienteId == null && agendamento.getCliente() != null) {
            clienteId = agendamento.getCliente().getId();
        }
        if (servicoId == null && agendamento.getServico() != null) {
            servicoId = agendamento.getServico().getId();
        }
        if (funcId == null && agendamento.getFuncionario() != null) {
            funcId = agendamento.getFuncionario().getId();
        }

        agendamento.setCliente(clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado")));
        agendamento.setServico(servicoRepository.findById(servicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado")));
        agendamento.setFuncionario(funcionarioRepository.findById(funcId)
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado")));

        if (agendamento.getStatus() == null) {
            agendamento.setStatus(StatusAgendamento.PENDENTE);
        }

        Agendamento saved = repository.save(agendamento);

        eventPublisher.publishEvent(new AgendamentoCriadoEvent(this, saved.getId(),
            saved.getCliente().getId(), saved.getCliente().getNome(), saved.getCliente().getEmail(),
            saved.getFuncionario().getId(), saved.getFuncionario().getNome(),
            saved.getServico().getId(), saved.getServico().getNome(),
            saved.getDataHoraInicio(), saved.getDataHoraFim(),
            saved.getValorTotal(), saved.getValorSinal()));

        return saved;
    }

    public Agendamento updateStatus(Long id, StatusAgendamento status) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(status);
        return repository.save(agendamento);
    }

    public Agendamento update(Long id, Agendamento agendamento) {
        Agendamento existing = findById(id);

        existing.setDataHoraInicio(agendamento.getDataHoraInicio());
        existing.setDataHoraFim(agendamento.getDataHoraFim());
        existing.setValorTotal(agendamento.getValorTotal());
        existing.setValorSinal(agendamento.getValorSinal());
        existing.setQuantidadeParcelas(agendamento.getQuantidadeParcelas());
        existing.setObservacoes(agendamento.getObservacoes());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Agendamento> findByData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(LocalTime.MAX);
        return repository.findByDataInicio(inicio, fim);
    }

    public List<Agendamento> findByPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.findByPeriodo(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));
    }

    public List<Agendamento> findByClienteId(Long clienteId) {
        return repository.findByCliente_Id(clienteId);
    }

    public List<Agendamento> findByFuncionarioId(Long funcionarioId) {
        return repository.findByFuncionario_Id(funcionarioId);
    }

    public List<Agendamento> findByStatus(StatusAgendamento status) {
        return repository.findByStatus(status);
    }

    public Agendamento naoCompareceu(Long id) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(StatusAgendamento.NAO_COMPARECEU);
        return repository.save(agendamento);
    }

    @Transactional(readOnly = true) // Transação somente leitura (mais rápido)
    public Page<Agendamento> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllPaginated(pageable);
    }

    public List<Agendamento> findByDataAndFuncionarioId(LocalDate data, Long funcionarioId) {
        return findByData(data).stream()
            .filter(a -> a.getFuncionario().getId().equals(funcionarioId))
            .toList();
    }

    public List<Agendamento> findProximos(int dias) {
        LocalDate hoje = LocalDate.now();
        return findByPeriodo(hoje, hoje.plusDays(dias));
    }

    public Agendamento cancelar(Long id, String motivo, String canceladoPor) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamento.setMotivoCancelamento(motivo);
        Agendamento saved = repository.save(agendamento);

        eventPublisher.publishEvent(new AgendamentoCanceladoEvent(this, id,
            saved.getCliente().getId(), saved.getCliente().getNome(), saved.getCliente().getEmail(),
            saved.getFuncionario().getId(), saved.getFuncionario().getNome(), motivo));

        return saved;
    }

    public Agendamento enviarComprovanteSinal(Long id, String comprovanteSinal) {
        Agendamento agendamento = findById(id);
        agendamento.setComprovanteSinal(comprovanteSinal);

        boolean autoConfirmar = agendamento.getServico().getConfirmacaoAutomatica() != null
            && agendamento.getServico().getConfirmacaoAutomatica();

        if (autoConfirmar) {
            agendamento.setStatus(StatusAgendamento.CONFIRMADO);
            agendamento.setDataConfirmacaoSinal(LocalDateTime.now());
        } else {
            agendamento.setStatus(StatusAgendamento.AGUARDANDO_CONFIRMACAO);
        }

        Agendamento saved = repository.save(agendamento);

        eventPublisher.publishEvent(new ComprovanteEnviadoEvent(this, id,
            saved.getCliente().getId(), saved.getCliente().getNome(), comprovanteSinal));

        if (autoConfirmar) {
            eventPublisher.publishEvent(new AgendamentoStatusAlteradoEvent(this, id,
                StatusAgendamento.PENDENTE, StatusAgendamento.CONFIRMADO,
                saved.getCliente().getId(), saved.getCliente().getNome(), saved.getCliente().getEmail(),
                saved.getFuncionario().getId(), saved.getFuncionario().getNome()));
        }

        return saved;
    }

    public Agendamento confirmarSinal(Long id) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        agendamento.setDataConfirmacaoSinal(LocalDateTime.now());
        Agendamento saved = repository.save(agendamento);

        eventPublisher.publishEvent(new AgendamentoStatusAlteradoEvent(this, id,
            StatusAgendamento.AGUARDANDO_CONFIRMACAO, StatusAgendamento.CONFIRMADO,
            saved.getCliente().getId(), saved.getCliente().getNome(), saved.getCliente().getEmail(),
            saved.getFuncionario().getId(), saved.getFuncionario().getNome()));

        return saved;
    }

    public Agendamento registrarChegada(Long id) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(StatusAgendamento.EM_ATENDIMENTO);
        agendamento.setDataChegada(LocalDateTime.now());
        Agendamento saved = repository.save(agendamento);

        eventPublisher.publishEvent(new ClienteChegouEvent(this, id,
            saved.getCliente().getId(), saved.getCliente().getNome(),
            saved.getFuncionario().getId(), saved.getFuncionario().getNome(),
            saved.getDataChegada()));

        return saved;
    }

    public Agendamento finalizarAtendimento(Long id) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        agendamento.setDataFinalizacao(LocalDateTime.now());
        Agendamento saved = repository.save(agendamento);

        eventPublisher.publishEvent(new AgendamentoFinalizadoEvent(this, id,
            saved.getCliente().getId(), saved.getCliente().getNome(),
            saved.getFuncionario().getId(), saved.getFuncionario().getNome(),
            saved.getServico().getId(), saved.getServico().getNome(),
            saved.getValorTotal(), saved.getDataFinalizacao()));

        return saved;
    }
}
