package com.studio.core.service;

import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository repository;
    
    public List<Cliente> findAll() {
        return repository.findAll();
    }
    
    public Cliente findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
    }
    
    public Cliente create(Cliente cliente) {
        if (cliente.getDataCadastro() == null) {
            cliente.setDataCadastro(LocalDateTime.now());
        }
        if (cliente.getEstagioFunil() == null) {
            cliente.setEstagioFunil("NOVO");
        }
        return repository.save(cliente);
    }
    
    public Cliente update(Long id, Cliente cliente) {
        Cliente existing = findById(id);
        existing.setNome(cliente.getNome());
        existing.setEmail(cliente.getEmail());
        existing.setTelefone(cliente.getTelefone());
        existing.setCpf(cliente.getCpf());
        existing.setEndereco(cliente.getEndereco());
        existing.setObservacoes(cliente.getObservacoes());
        if (cliente.getEstagioFunil() != null) {
            existing.setEstagioFunil(cliente.getEstagioFunil());
        }
        return repository.save(existing);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
    
    public List<Cliente> findByTelefone(String telefone) {
        return repository.findByTelefone(telefone);
    }
    
    public List<Cliente> findByNomeContaining(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }
    
    public List<Cliente> findInativos(int dias) {
        return repository.findByAtivoFalse();
    }
}
