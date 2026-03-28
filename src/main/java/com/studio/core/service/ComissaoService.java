package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.comissao.dto.ComissaoResponseDTO;
import com.studio.core.dominio.comissao.entity.Comissao;
import com.studio.core.dominio.comissao.repository.ComissaoRepository;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
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
public class ComissaoService {

    @Autowired
    private ComissaoRepository repository;
    
    @Autowired
    private FuncionarioRepository FuncionarioRepository;
    
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    
    public List<ComissaoResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(ComissaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public ComissaoResponseDTO findById(Long id) {
        return ComissaoResponseDTO.fromEntity(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comissão não encontrada")));
    }
    
    public List<ComissaoResponseDTO> findByFuncionarioId(Long FuncionarioId) {
        return repository.findByFunc_Id(FuncionarioId).stream()
            .map(ComissaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<ComissaoResponseDTO> findPendentes() {
        return repository.findByStatus(Comissao.StatusComissao.PENDENTE).stream()
            .map(ComissaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<ComissaoResponseDTO> findPendentesPorFuncionario(Long FuncionarioId) {
        return repository.findByFunc_IdAndStatus(FuncionarioId, Comissao.StatusComissao.PENDENTE).stream()
            .map(ComissaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public BigDecimal totalPendentePorFuncionario(Long FuncionarioId) {
        return repository.totalPendentePorFuncionario(FuncionarioId);
    }
    
    public List<ComissaoResponseDTO> findByPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.findByDataComissaoBetween(inicio, fim).stream()
            .map(ComissaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public ComissaoResponseDTO gerar(Long agendamentoId, BigDecimal percentual) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
            .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));
        
        Comissao comissao = new Comissao();
        comissao.setFunc(agendamento.getFuncionario());
        comissao.setAgendamento(agendamento);
        
        BigDecimal valorComissao = agendamento.getValorTotal().multiply(percentual).divide(BigDecimal.valueOf(100));
        comissao.setValor(valorComissao);
        comissao.setPercentual(percentual);
        comissao.setDataComissao(LocalDate.now());
        comissao.setStatus(Comissao.StatusComissao.PENDENTE);
        
        return ComissaoResponseDTO.fromEntity(repository.save(comissao));
    }
    
    public ComissaoResponseDTO pagar(Long id) {
        Comissao comissao = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comissão não encontrada"));
        comissao.setStatus(Comissao.StatusComissao.PAGA);
        comissao.setDataPagamento(LocalDate.now());
        return ComissaoResponseDTO.fromEntity(repository.save(comissao));
    }
    
    public List<ComissaoResponseDTO> pagarTodasPorFuncionario(Long FuncionarioId) {
        List<Comissao> comissoes = repository.findByFunc_IdAndStatus(FuncionarioId, Comissao.StatusComissao.PENDENTE);
        for (Comissao c : comissoes) {
            c.setStatus(Comissao.StatusComissao.PAGA);
            c.setDataPagamento(LocalDate.now());
        }
        return repository.saveAll(comissoes).stream()
            .map(ComissaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
