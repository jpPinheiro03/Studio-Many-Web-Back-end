package com.studio.core.service;

import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;
    
    public List<Funcionario> findAll() {
        return repository.findAll();
    }
    
    public Funcionario findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));
    }
    
    public Funcionario create(Funcionario funcionario) {
        if (funcionario.getDataCadastro() == null) {
            funcionario.setDataCadastro(LocalDateTime.now());
        }
        return repository.save(funcionario);
    }
    
    public Funcionario update(Long id, Funcionario funcionario) {
        Funcionario existing = findById(id);
        existing.setNome(funcionario.getNome());
        existing.setEmail(funcionario.getEmail());
        existing.setTelefone(funcionario.getTelefone());
        existing.setCpf(funcionario.getCpf());
        existing.setEspecialidade(funcionario.getEspecialidade());
        if (funcionario.getAtivo() != null) {
            existing.setAtivo(funcionario.getAtivo());
        }
        return repository.save(existing);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Funcionário não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
