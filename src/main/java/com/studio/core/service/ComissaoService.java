package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.comissao.dto.ComissaoResponseDTO;
import com.studio.core.dominio.comissao.entity.Comissao;
import com.studio.core.dominio.comissao.mapper.ComissaoMapper;
import com.studio.core.dominio.comissao.repository.ComissaoRepository;
import com.studio.core.event.comissao.ComissaoPagaEvent;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComissaoService {

    @Autowired
    private ComissaoRepository repository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ComissaoMapper comissaoMapper;

    public List<ComissaoResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(comissaoMapper::toResponse)
            .collect(Collectors.toList());
    }

    public ComissaoResponseDTO findById(Long id) {
        return comissaoMapper.toResponse(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comissão não encontrada")));
    }

    public List<ComissaoResponseDTO> findByFuncionarioId(Long funcionarioId) {
        return repository.findByFuncionario_Id(funcionarioId).stream()
            .map(comissaoMapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<ComissaoResponseDTO> findPendentes() {
        return repository.findByStatus(Comissao.StatusComissao.PENDENTE).stream()
            .map(comissaoMapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<ComissaoResponseDTO> findPendentesPorFuncionario(Long funcionarioId) {
        return repository.findByFuncionario_IdAndStatus(funcionarioId, Comissao.StatusComissao.PENDENTE).stream()
            .map(comissaoMapper::toResponse)
            .collect(Collectors.toList());
    }

    public BigDecimal totalPendentePorFuncionario(Long funcionarioId) {
        return repository.totalPendentePorFuncionario(funcionarioId);
    }

    public List<ComissaoResponseDTO> findByPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.findByDataComissaoBetween(inicio, fim).stream()
            .map(comissaoMapper::toResponse)
            .collect(Collectors.toList());
    }

    public ComissaoResponseDTO gerar(Long agendamentoId, BigDecimal percentual) {
        if (percentual == null || percentual.compareTo(BigDecimal.ZERO) < 0 || percentual.compareTo(new BigDecimal("100")) > 0) {
            throw new BadRequestException("Percentual deve estar entre 0 e 100");
        }

        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
            .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        List<Comissao> existentes = repository.findByAgendamento_Id(agendamentoId);
        if (!existentes.isEmpty()) {
            throw new BadRequestException("Já existe comissão gerada para este agendamento");
        }

        Comissao comissao = new Comissao();
        comissao.setFuncionario(agendamento.getFuncionario());
        comissao.setAgendamento(agendamento);

        BigDecimal valorComissao = agendamento.getValorTotal().multiply(percentual).divide(BigDecimal.valueOf(100));
        comissao.setValor(valorComissao);
        comissao.setPercentual(percentual);
        comissao.setDataComissao(LocalDate.now());
        comissao.setStatus(Comissao.StatusComissao.PENDENTE);

        return comissaoMapper.toResponse(repository.save(comissao));
    }

    public ComissaoResponseDTO pagar(Long id) {
        Comissao comissao = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comissão não encontrada"));
        comissao.setStatus(Comissao.StatusComissao.PAGA);
        comissao.setDataPagamento(LocalDate.now());
        Comissao saved = repository.save(comissao);

        eventPublisher.publishEvent(new ComissaoPagaEvent(this, saved.getId(),
            saved.getFuncionario().getId(), saved.getFuncionario().getNome(),
            saved.getAgendamento() != null ? saved.getAgendamento().getId() : null,
            saved.getValor(), saved.getPercentual(), saved.getDataPagamento()));

        return comissaoMapper.toResponse(saved);
    }

    public List<ComissaoResponseDTO> pagarTodasPorFuncionario(Long funcionarioId) {
        List<Comissao> comissoes = repository.findByFuncionario_IdAndStatus(funcionarioId, Comissao.StatusComissao.PENDENTE);
        for (Comissao c : comissoes) {
            c.setStatus(Comissao.StatusComissao.PAGA);
            c.setDataPagamento(LocalDate.now());
            repository.save(c);
        }
        return repository.findByFuncionario_IdAndStatus(funcionarioId, Comissao.StatusComissao.PAGA).stream()
            .map(comissaoMapper::toResponse)
            .collect(Collectors.toList());
    }
}
