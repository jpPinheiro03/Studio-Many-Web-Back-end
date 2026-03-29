package com.studio.core.service;

import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.lista_espera.entity.ListaEspera;
import com.studio.core.dominio.lista_espera.repository.ListaEsperaRepository;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import com.studio.core.event.lista_espera.ListaEsperaEvent;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ListaEsperaService {

    @Autowired
    private ListaEsperaRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<ListaEspera> findAll() {
        return repository.findAll();
    }

    public ListaEspera findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lista de espera não encontrada"));
    }

    public List<ListaEspera> findByStatus(ListaEspera.StatusListaEspera status) {
        return repository.findByStatus(status);
    }

    public List<ListaEspera> findAguardando() {
        return repository.findByStatusOrderByDataCadastroAsc(ListaEspera.StatusListaEspera.AGUARDANDO);
    }

    public ListaEspera create(ListaEspera listaEspera) {
        return create(null, null, null, listaEspera);
    }

    public ListaEspera create(Long clienteId, Long servicoId, Long funcId, ListaEspera listaEspera) {
        if (clienteId == null) {
            clienteId = listaEspera.getClienteId();
        }
        if (servicoId == null) {
            servicoId = listaEspera.getServicoId();
        }
        if (funcId == null) {
            funcId = listaEspera.getFuncId();
        }

        if (clienteId == null && listaEspera.getCliente() != null) {
            clienteId = listaEspera.getCliente().getId();
        }
        if (servicoId == null && listaEspera.getServico() != null) {
            servicoId = listaEspera.getServico().getId();
        }

        if (clienteId == null) {
            throw new BadRequestException("Cliente é obrigatório");
        }
        if (servicoId == null) {
            throw new BadRequestException("Serviço é obrigatório");
        }

        if (repository.existsByCliente_IdAndServico_IdAndStatus(clienteId, servicoId, ListaEspera.StatusListaEspera.AGUARDANDO)) {
            throw new BadRequestException("Cliente já está na lista de espera para este serviço");
        }

        listaEspera.setCliente(clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado")));
        listaEspera.setServico(servicoRepository.findById(servicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado")));

        if (funcId != null) {
            listaEspera.setFunc(funcionarioRepository.findById(funcId)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado")));
        }

        listaEspera.setStatus(ListaEspera.StatusListaEspera.AGUARDANDO);
        ListaEspera saved = repository.save(listaEspera);

        eventPublisher.publishEvent(new ListaEsperaEvent(this, saved.getId(),
            null, ListaEspera.StatusListaEspera.AGUARDANDO,
            saved.getCliente().getId(), saved.getCliente().getNome(),
            saved.getServico().getId(), saved.getServico().getNome()));

        return saved;
    }

    public ListaEspera marcarAtendido(Long id) {
        ListaEspera lista = findById(id);
        lista.setStatus(ListaEspera.StatusListaEspera.ATENDIDO);
        ListaEspera saved = repository.save(lista);

        eventPublisher.publishEvent(new ListaEsperaEvent(this, saved.getId(),
            ListaEspera.StatusListaEspera.AGUARDANDO, ListaEspera.StatusListaEspera.ATENDIDO,
            saved.getCliente().getId(), saved.getCliente().getNome(),
            saved.getServico().getId(), saved.getServico().getNome()));

        return saved;
    }

    public ListaEspera cancelar(Long id) {
        ListaEspera lista = findById(id);
        ListaEspera.StatusListaEspera statusAnterior = lista.getStatus();
        lista.setStatus(ListaEspera.StatusListaEspera.CANCELADO);
        ListaEspera saved = repository.save(lista);

        eventPublisher.publishEvent(new ListaEsperaEvent(this, saved.getId(),
            statusAnterior, ListaEspera.StatusListaEspera.CANCELADO,
            saved.getCliente().getId(), saved.getCliente().getNome(),
            saved.getServico().getId(), saved.getServico().getNome()));

        return saved;
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Lista de espera não encontrada");
        }
        repository.deleteById(id);
    }
}
