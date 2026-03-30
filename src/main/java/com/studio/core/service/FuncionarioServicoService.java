package com.studio.core.service;

import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.funcionario_servico.dto.FuncionarioServicoRequestDTO;
import com.studio.core.dominio.funcionario_servico.dto.FuncionarioServicoResponseDTO;
import com.studio.core.dominio.funcionario_servico.entity.FuncionarioServico;
import com.studio.core.dominio.funcionario_servico.mapper.FuncionarioServicoMapper;
import com.studio.core.dominio.funcionario_servico.repository.FuncionarioServicoRepository;
import com.studio.core.dominio.servico.entity.Servico;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FuncionarioServicoService {

    @Autowired
    private FuncionarioServicoRepository repository;
    
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private FuncionarioServicoMapper funcionarioServicoMapper;
    
    public List<FuncionarioServicoResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(funcionarioServicoMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    public List<FuncionarioServicoResponseDTO> findByFuncionarioId(Long funcionarioId) {
        return repository.findByFuncionario_Id(funcionarioId).stream()
            .map(funcionarioServicoMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    public FuncionarioServicoResponseDTO create(FuncionarioServicoRequestDTO dto) {
        Funcionario func = funcionarioRepository.findById(dto.getFuncionarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        
        Servico servico = servicoRepository.findById(dto.getServicoId())
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));
        
        if (repository.existsByFuncionario_IdAndServico_Id(dto.getFuncionarioId(), dto.getServicoId())) {
            throw new BadRequestException("Associação entre funcionário e serviço já existe");
        }
        
        FuncionarioServico fs = funcionarioServicoMapper.toEntity(dto);
        fs.setFuncionario(func);
        fs.setServico(servico);
        
        return funcionarioServicoMapper.toResponse(repository.save(fs));
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Associação não encontrada");
        }
        repository.deleteById(id);
    }
}
