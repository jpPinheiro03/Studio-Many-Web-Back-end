package com.studio.core.service;

import com.studio.core.dominio.pacote.entity.Pacote;
import com.studio.core.dominio.pacote.repository.PacoteRepository;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PacoteService {

    @Autowired
    private PacoteRepository repository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    public List<Pacote> findAll() {
        return repository.findAll();
    }
    
    public Pacote findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pacote não encontrado"));
    }
    
    public Pacote create(Pacote pacote) {
        return create(null, pacote);
    }
    
    public Pacote create(Long servicoId, Pacote pacote) {
        if (servicoId == null) {
            servicoId = pacote.getServicoId();
        }
        if (servicoId == null && pacote.getServico() != null) {
            servicoId = pacote.getServico().getId();
        }
        pacote.setServico(servicoRepository.findById(servicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado")));
        return repository.save(pacote);
    }
    
    public Pacote update(Long id, Pacote pacote) {
        Pacote existing = findById(id);
        existing.setNome(pacote.getNome());
        existing.setQuantidadeSessoes(pacote.getQuantidadeSessoes());
        existing.setPreco(pacote.getPreco());
        existing.setValidadeDias(pacote.getValidadeDias());
        existing.setAtivo(pacote.getAtivo());
        return repository.save(existing);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pacote não encontrado");
        }
        repository.deleteById(id);
    }
}
