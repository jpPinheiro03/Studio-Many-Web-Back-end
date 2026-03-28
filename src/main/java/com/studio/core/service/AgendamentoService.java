package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    
    public List<Agendamento> findAll() {
        return repository.findAll();
    }
    
    public Agendamento findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado com ID: " + id));
    }
    
    public Agendamento create(Agendamento agendamento) {
        return create(null, null, null, agendamento);
    }
    
    public Agendamento create(Long clienteId, Long servicoId, Long funcId, Agendamento agendamento) {
        if (clienteId == null) {
            clienteId = agendamento.getClienteId();
        }
        if (servicoId == null) {
            servicoId = agendamento.getServicoId();
        }
        if (funcId == null) {
            funcId = agendamento.getFuncionarioId();
        }
        
        if (clienteId == null && agendamento.getCliente() != null) {
            clienteId = agendamento.getCliente().getId();
        }
        if (servicoId == null && agendamento.getServico() != null) {
            servicoId = agendamento.getServico().getId();
        }
        if (funcId == null && agendamento.getFuncionario() != null) {
            funcId = agendamento.getFuncionario().getId();
        }
        
        agendamento.setCliente(clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado")));
        agendamento.setServico(servicoRepository.findById(servicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado")));
        agendamento.setFuncionario(funcionarioRepository.findById(funcId)
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado")));
        
        if (agendamento.getStatus() == null) {
            agendamento.setStatus(StatusAgendamento.PENDENTE);
        }
        return repository.save(agendamento);
    }
    
    public Agendamento updateStatus(Long id, StatusAgendamento status) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(status);
        return repository.save(agendamento);
    }
    
    public Agendamento update(Long id, Agendamento agendamento) {
        Agendamento existing = findById(id);
        existing.setDataHoraInicio(agendamento.getDataHoraInicio());
        existing.setDataHoraFim(agendamento.getDataHoraFim());
        existing.setValorTotal(agendamento.getValorTotal());
        existing.setValorSinal(agendamento.getValorSinal());
        existing.setQuantidadeParcelas(agendamento.getQuantidadeParcelas());
        existing.setObservacoes(agendamento.getObservacoes());
        return repository.save(existing);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Agendamento não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
    
    public List<Agendamento> findByData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(LocalTime.MAX);
        return repository.findByDataInicio(inicio, fim);
    }
    
    public List<Agendamento> findByPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.findByPeriodo(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));
    }
    
    public List<Agendamento> findByClienteId(Long clienteId) {
        return repository.findByCliente_Id(clienteId);
    }
    
    public List<Agendamento> findByFuncionarioId(Long funcionarioId) {
        return repository.findByFuncionario_Id(funcionarioId);
    }
    
    public List<Agendamento> findByStatus(StatusAgendamento status) {
        return repository.findByStatus(status);
    }
    
    public List<Agendamento> findPaginated(int page, int size) {
        List<Agendamento> all = repository.findAll();
        int start = page * size;
        int end = Math.min(start + size, all.size());
        if (start >= all.size()) return List.of();
        return all.subList(start, end);
    }
    
    public List<Agendamento> findByDataAndFuncionarioId(LocalDate data, Long FuncionarioId) {
        return findByData(data).stream()
            .filter(a -> a.getFuncionario().getId().equals(FuncionarioId))
            .toList();
    }
    
    public List<Agendamento> findProximos(int dias) {
        LocalDate hoje = LocalDate.now();
        return findByPeriodo(hoje, hoje.plusDays(dias));
    }
    
    public Agendamento cancelar(Long id, String motivo, String canceladoPor) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamento.setMotivoCancelamento(motivo);
        return repository.save(agendamento);
    }
}
