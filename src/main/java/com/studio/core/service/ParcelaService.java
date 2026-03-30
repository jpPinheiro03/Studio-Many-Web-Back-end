package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.parcela.dto.ParcelaResponseDTO;
import com.studio.core.dominio.parcela.entity.Parcela;
import com.studio.core.dominio.parcela.mapper.ParcelaMapper;
import com.studio.core.dominio.parcela.repository.ParcelaRepository;
import org.springframework.context.annotation.Lazy;
import com.studio.core.event.parcela.ParcelaQuitadaEvent;
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
public class ParcelaService {

    @Autowired
    private ParcelaRepository repository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    @Lazy
    private ParcelaMapper parcelaMapper;

    public List<ParcelaResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(parcelaMapper::toResponse)
            .collect(Collectors.toList());
    }

    public ParcelaResponseDTO findById(Long id) {
        return parcelaMapper.toResponse(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Parcela não encontrada")));
    }

    public List<ParcelaResponseDTO> findByAgendamentoId(Long agendamentoId) {
        return repository.findByAgendamento_Id(agendamentoId).stream()
            .map(parcelaMapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<ParcelaResponseDTO> findPendentes() {
        return repository.findByStatus(Parcela.StatusParcela.PENDENTE).stream()
            .map(parcelaMapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<ParcelaResponseDTO> findVencidas() {
        return repository.findByDataVencimentoBeforeAndStatus(LocalDate.now(), Parcela.StatusParcela.PENDENTE).stream()
            .map(parcelaMapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<ParcelaResponseDTO> findAVencer(int dias) {
        LocalDate limite = LocalDate.now().plusDays(dias);
        return repository.findByDataVencimentoBetweenAndStatus(LocalDate.now(), limite, Parcela.StatusParcela.PENDENTE).stream()
            .map(parcelaMapper::toResponse)
            .collect(Collectors.toList());
    }

    public ParcelaResponseDTO quitar(Long id) {
        Parcela parcela = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Parcela não encontrada"));
        parcela.setStatus(Parcela.StatusParcela.QUITADA);
        parcela.setDataPagamento(LocalDate.now());
        Parcela saved = repository.save(parcela);

        eventPublisher.publishEvent(new ParcelaQuitadaEvent(this, saved.getId(),
            saved.getAgendamento().getId(),
            saved.getAgendamento().getCliente().getId(),
            saved.getAgendamento().getCliente().getNome(),
            saved.getNumero(), null,
            saved.getValor(), saved.getDataPagamento()));

        return parcelaMapper.toResponse(saved);
    }

    public List<ParcelaResponseDTO> quitarTodas(Long agendamentoId) {
        List<Parcela> parcelas = repository.findByAgendamento_Id(agendamentoId);
        for (Parcela p : parcelas) {
            if (p.getStatus() == Parcela.StatusParcela.PENDENTE) {
                p.setStatus(Parcela.StatusParcela.QUITADA);
                p.setDataPagamento(LocalDate.now());
                repository.save(p);
            }
        }
        return repository.findByAgendamento_Id(agendamentoId).stream()
            .map(parcelaMapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<ParcelaResponseDTO> gerarParcelas(Long agendamentoId, int quantidade, LocalDate dataPrimeira) {
        if (quantidade <= 0) {
            throw new BadRequestException("Quantidade de parcelas deve ser maior que zero");
        }

        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
            .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        if (agendamento.getValorTotal() == null || agendamento.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Agendamento não possui valor total válido para gerar parcelas");
        }

        List<Parcela> existentes = repository.findByAgendamento_Id(agendamentoId);
        if (!existentes.isEmpty()) {
            throw new BadRequestException("Já existem parcelas geradas para este agendamento");
        }

        BigDecimal valorParcela = agendamento.getValorTotal().divide(BigDecimal.valueOf(quantidade), 2, java.math.RoundingMode.HALF_UP);

        for (int i = 1; i <= quantidade; i++) {
            Parcela parcela = new Parcela();
            parcela.setAgendamento(agendamento);
            parcela.setNumero(i);
            parcela.setValor(valorParcela);
            parcela.setDataVencimento(dataPrimeira.plusMonths(i - 1));
            parcela.setStatus(Parcela.StatusParcela.PENDENTE);
            repository.save(parcela);
        }

        return repository.findByAgendamento_Id(agendamentoId).stream()
            .map(parcelaMapper::toResponse)
            .collect(Collectors.toList());
    }
}
