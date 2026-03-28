package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.parcela.dto.ParcelaResponseDTO;
import com.studio.core.dominio.parcela.entity.Parcela;
import com.studio.core.dominio.parcela.repository.ParcelaRepository;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    public List<ParcelaResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(ParcelaResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public ParcelaResponseDTO findById(Long id) {
        return ParcelaResponseDTO.fromEntity(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Parcela não encontrada")));
    }
    
    public List<ParcelaResponseDTO> findByAgendamentoId(Long agendamentoId) {
        return repository.findByAgendamentoId(agendamentoId).stream()
            .map(ParcelaResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<ParcelaResponseDTO> findPendentes() {
        return repository.findByStatus(Parcela.StatusParcela.PENDENTE).stream()
            .map(ParcelaResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<ParcelaResponseDTO> findVencidas() {
        return repository.findByDataVencimentoBeforeAndStatus(LocalDate.now(), Parcela.StatusParcela.PENDENTE).stream()
            .map(ParcelaResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<ParcelaResponseDTO> findAVencer(int dias) {
        LocalDate limite = LocalDate.now().plusDays(dias);
        return repository.findByDataVencimentoBetweenAndStatus(LocalDate.now(), limite, Parcela.StatusParcela.PENDENTE).stream()
            .map(ParcelaResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public ParcelaResponseDTO quitar(Long id) {
        Parcela parcela = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Parcela não encontrada"));
        parcela.setStatus(Parcela.StatusParcela.QUITADA);
        parcela.setDataPagamento(LocalDate.now());
        return ParcelaResponseDTO.fromEntity(repository.save(parcela));
    }
    
    public List<ParcelaResponseDTO> quitarTodas(Long agendamentoId) {
        List<Parcela> parcelas = repository.findByAgendamentoId(agendamentoId);
        for (Parcela p : parcelas) {
            if (p.getStatus() == Parcela.StatusParcela.PENDENTE) {
                p.setStatus(Parcela.StatusParcela.QUITADA);
                p.setDataPagamento(LocalDate.now());
            }
        }
        return repository.saveAll(parcelas).stream()
            .map(ParcelaResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<ParcelaResponseDTO> gerarParcelas(Long agendamentoId, int quantidade, LocalDate dataPrimeira) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
            .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));
        
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
        
        return repository.findByAgendamentoId(agendamentoId).stream()
            .map(ParcelaResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
