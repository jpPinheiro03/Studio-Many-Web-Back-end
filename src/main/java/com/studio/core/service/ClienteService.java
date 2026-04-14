package com.studio.core.service;

import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.EntityConflictException;
import com.studio.core.exception.ResourceNotFoundException;
import com.studio.core.validator.CpfValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service // Esta classe contém lógica de negócio
@Transactional // Métodos rodam dentro de transação (commit/rollback)
public class ClienteService {

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private ClienteRepository repository;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private CpfValidator cpfValidator;

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
    }

    public Cliente create(Cliente cliente) {
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
            if (repository.existsByEmail(cliente.getEmail())) {
                throw new EntityConflictException("Email já cadastrado");
            }
        }
        if (cliente.getCpf() != null && !cliente.getCpf().isEmpty()) {
            cpfValidator.validar(cliente.getCpf());
        }
        if (cliente.getDataCadastro() == null) {
            cliente.setDataCadastro(LocalDateTime.now());
        }
        if (cliente.getEstagioFunil() == null) {
            cliente.setEstagioFunil("NOVO");
        }
        return repository.save(cliente);
    }

    public Cliente update(Long id, Cliente cliente) {
        if (cliente.getCpf() != null && !cliente.getCpf().isEmpty()) {
            cpfValidator.validar(cliente.getCpf());
        }
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

    @Transactional(readOnly = true) // Transação somente leitura (mais rápido)
    public List<Cliente> findPaginated(int page, int size) {
        List<Cliente> all = repository.findAll();
        int start = page * size;
        int end = Math.min(start + size, all.size());
        if (start >= all.size()) return List.of();
        return all.subList(start, end);
    }
}
