package com.studio.core.service;

import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteRequestDTO;
import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteResponseDTO;
import com.studio.core.dominio.cliente_pacote.entity.ClientePacote;
import com.studio.core.dominio.cliente_pacote.mapper.ClientePacoteMapper;
import com.studio.core.dominio.cliente_pacote.repository.ClientePacoteRepository;
import org.springframework.context.annotation.Lazy;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.pacote.repository.PacoteRepository;
import com.studio.core.event.cliente_pacote.SessaoUsadaEvent;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    @Lazy
    private ClientePacoteMapper clientePacoteMapper;

    public List<ClientePacoteResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(clientePacoteMapper::toResponse)
            .collect(Collectors.toList());
    }

    public ClientePacoteResponseDTO findById(Long id) {
        return clientePacoteMapper.toResponse(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pacote do cliente não encontrado")));
    }

    public List<ClientePacoteResponseDTO> findByClienteId(Long clienteId) {
        return repository.findByCliente_Id(clienteId).stream()
            .map(clientePacoteMapper::toResponse)
            .collect(Collectors.toList());
    }

    public ClientePacoteResponseDTO create(ClientePacoteRequestDTO dto) {
        var cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        var pacote = pacoteRepository.findById(dto.getPacoteId())
            .orElseThrow(() -> new ResourceNotFoundException("Pacote não encontrado"));

        if (repository.existsByCliente_IdAndPacote_Id(dto.getClienteId(), dto.getPacoteId())) {
            throw new BadRequestException("Cliente já possui este pacote");
        }

        ClientePacote clientePacote = clientePacoteMapper.toEntity(dto);
        clientePacote.setCliente(cliente);
        clientePacote.setPacote(pacote);

        if (clientePacote.getDataValidade() == null && pacote.getValidadeDias() != null && pacote.getValidadeDias() > 0) {
            clientePacote.setDataValidade(LocalDate.now().plusDays(pacote.getValidadeDias()));
        }

        return clientePacoteMapper.toResponse(repository.save(clientePacote));
    }

    public ClientePacoteResponseDTO usarSessao(Long id) {
        ClientePacote cp = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pacote do cliente não encontrado"));

        cp.setSessoesUsadas(cp.getSessoesUsadas() + 1);
        ClientePacote saved = repository.save(cp);

        eventPublisher.publishEvent(new SessaoUsadaEvent(this, saved.getId(),
            saved.getCliente().getId(), saved.getCliente().getNome(),
            saved.getPacote().getId(), saved.getPacote().getNome(),
            saved.getSessoesUsadas(), saved.getPacote().getQuantidadeSessoes()));

        return clientePacoteMapper.toResponse(saved);
    }

    public ClientePacoteResponseDTO updateStatus(Long id, ClientePacote.StatusClientePacote status) {
        ClientePacote cp = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pacote do cliente não encontrado"));
        cp.setStatus(status);
        return clientePacoteMapper.toResponse(repository.save(cp));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pacote do cliente não encontrado");
        }
        repository.deleteById(id);
    }
}
