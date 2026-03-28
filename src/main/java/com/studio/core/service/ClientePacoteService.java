package com.studio.core.service;

import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteRequestDTO;
import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteResponseDTO;
import com.studio.core.dominio.cliente_pacote.entity.ClientePacote;
import com.studio.core.dominio.cliente_pacote.mapper.ClientePacoteMapper;
import com.studio.core.dominio.cliente_pacote.repository.ClientePacoteRepository;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.pacote.repository.PacoteRepository;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientePacoteService {

    @Autowired
    private ClientePacoteRepository repository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private PacoteRepository pacoteRepository;
    
    public List<ClientePacoteResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(ClientePacoteMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    public ClientePacoteResponseDTO findById(Long id) {
        return ClientePacoteMapper.toResponse(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pacote do cliente não encontrado")));
    }
    
    public List<ClientePacoteResponseDTO> findByClienteId(Long clienteId) {
        return repository.findByCliente_Id(clienteId).stream()
            .map(ClientePacoteMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    public ClientePacoteResponseDTO create(ClientePacoteRequestDTO dto) {
        var cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        
        var pacote = pacoteRepository.findById(dto.getPacoteId())
            .orElseThrow(() -> new ResourceNotFoundException("Pacote não encontrado"));
        
        ClientePacote clientePacote = ClientePacoteMapper.toEntity(dto, cliente, pacote);
        
        return ClientePacoteMapper.toResponse(repository.save(clientePacote));
    }
    
    public ClientePacoteResponseDTO usarSessao(Long id) {
        ClientePacote cp = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pacote do cliente não encontrado"));
        int totalSessoes = cp.getPacote().getQuantidadeSessoes();
        int usadas = cp.getSessoesUsadas();
        
        if (usadas >= totalSessoes) {
            throw new ResourceNotFoundException("Todas as sessões já foram utilizadas");
        }
        
        cp.setSessoesUsadas(usadas + 1);
        
        if (usadas + 1 >= totalSessoes) {
            cp.setStatus(ClientePacote.StatusClientePacote.USADO);
        }
        
        return ClientePacoteMapper.toResponse(repository.save(cp));
    }
    
    public ClientePacoteResponseDTO updateStatus(Long id, ClientePacote.StatusClientePacote status) {
        ClientePacote cp = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pacote do cliente não encontrado"));
        cp.setStatus(status);
        return ClientePacoteMapper.toResponse(repository.save(cp));
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pacote do cliente não encontrado");
        }
        repository.deleteById(id);
    }
}
