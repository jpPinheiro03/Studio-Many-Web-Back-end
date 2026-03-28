package com.studio.core.service;

import com.studio.core.dominio.servico.entity.Servico;
import com.studio.core.dominio.servico.repository.ServicoRepository;
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
    
    public List<Servico> findAll() {
        return repository.findAll();
    }
    
    public Servico findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + id));
    }
    
    public Servico create(Servico servico) {
        return repository.save(servico);
    }
    
    public Servico update(Long id, Servico servico) {
        Servico existing = findById(id);
        existing.setNome(servico.getNome());
        existing.setDescricao(servico.getDescricao());
        existing.setPreco(servico.getPreco());
        existing.setDuracaoMinutos(servico.getDuracaoMinutos());
        if (servico.getAtivo() != null) {
            existing.setAtivo(servico.getAtivo());
        }
        return repository.save(existing);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Serviço não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
