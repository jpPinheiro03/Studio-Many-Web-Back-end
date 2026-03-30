package com.studio.core.service;

import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.funcionario_servico.repository.FuncionarioServicoRepository;
import com.studio.core.dominio.lista_espera.repository.ListaEsperaRepository;
import com.studio.core.dominio.pacote.repository.PacoteRepository;
import com.studio.core.dominio.servico.entity.Servico;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ServicoService {

    @Autowired
    private ServicoRepository repository;
    
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    
    @Autowired
    private PacoteRepository pacoteRepository;
    
    @Autowired
    private FuncionarioServicoRepository funcionarioServicoRepository;
    
    @Autowired
    private ListaEsperaRepository listaEsperaRepository;
    
    public List<Servico> findAll() {
        return repository.findAll();
    }
    
    public Servico findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + id));
    }
    
    public Servico create(Servico servico) {
        if (servico.getPreco() != null && servico.getPreco().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Preço do serviço deve ser maior que zero");
        }
        if (servico.getDuracaoMinutos() != null && servico.getDuracaoMinutos() <= 0) {
            throw new BadRequestException("Duração do serviço deve ser maior que zero");
        }
        return repository.save(servico);
    }
    
    public Servico update(Long id, Servico servico) {
        Servico existing = findById(id);
        if (servico.getPreco() != null && servico.getPreco().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Preço do serviço deve ser maior que zero");
        }
        if (servico.getDuracaoMinutos() != null && servico.getDuracaoMinutos() <= 0) {
            throw new BadRequestException("Duração do serviço deve ser maior que zero");
        }
        existing.setNome(servico.getNome());
        existing.setDescricao(servico.getDescricao());
        existing.setPreco(servico.getPreco());
        existing.setDuracaoMinutos(servico.getDuracaoMinutos());
        if (servico.getAtivo() != null) {
            existing.setAtivo(servico.getAtivo());
        }
        if (servico.getConfirmacaoAutomatica() != null) {
            existing.setConfirmacaoAutomatica(servico.getConfirmacaoAutomatica());
        }
        return repository.save(existing);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Serviço não encontrado com ID: " + id);
        }
        if (agendamentoRepository.existsByServico_Id(id)) {
            throw new BadRequestException("Não é possível excluir serviço com agendamentos vinculados");
        }
        if (pacoteRepository.existsByServico_Id(id)) {
            throw new BadRequestException("Não é possível excluir serviço com pacotes vinculados");
        }
        if (funcionarioServicoRepository.existsByServico_Id(id)) {
            throw new BadRequestException("Não é possível excluir serviço com funcionários vinculados");
        }
        if (listaEsperaRepository.existsByServico_Id(id)) {
            throw new BadRequestException("Não é possível excluir serviço com registros na lista de espera");
        }
        repository.deleteById(id);
    }
}
