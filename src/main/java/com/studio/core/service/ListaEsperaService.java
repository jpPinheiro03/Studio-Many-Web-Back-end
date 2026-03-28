package com.studio.core.service;

import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.lista_espera.entity.ListaEspera;
import com.studio.core.dominio.lista_espera.repository.ListaEsperaRepository;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
            funcId = listaEspera.getFuncionarioId();
        }
        
        if (clienteId == null && listaEspera.getCliente() != null) {
            clienteId = listaEspera.getCliente().getId();
        }
        if (servicoId == null && listaEspera.getServico() != null) {
            servicoId = listaEspera.getServico().getId();
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
        return repository.save(listaEspera);
    }
    
    public ListaEspera marcarAtendido(Long id) {
        ListaEspera lista = findById(id);
        lista.setStatus(ListaEspera.StatusListaEspera.ATENDIDO);
        return repository.save(lista);
    }
    
    public ListaEspera cancelar(Long id) {
        ListaEspera lista = findById(id);
        lista.setStatus(ListaEspera.StatusListaEspera.CANCELADO);
        return repository.save(lista);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Lista de espera não encontrada");
        }
        repository.deleteById(id);
    }
}
