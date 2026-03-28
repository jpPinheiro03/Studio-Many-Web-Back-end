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
    private FuncionarioRepository FuncionarioRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    public List<FuncionarioServicoResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(FuncionarioServicoMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    public List<FuncionarioServicoResponseDTO> findByFuncionarioId(Long FuncionarioId) {
        return repository.findByFunc_Id(FuncionarioId).stream()
            .map(FuncionarioServicoMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    public FuncionarioServicoResponseDTO create(FuncionarioServicoRequestDTO dto) {
        Funcionario func = FuncionarioRepository.findById(dto.getFuncionarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        
        Servico servico = servicoRepository.findById(dto.getServicoId())
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));
        
        FuncionarioServico fs = FuncionarioServicoMapper.toEntity(dto);
        fs.setFunc(func);
        fs.setServico(servico);
        
        return FuncionarioServicoMapper.toResponse(repository.save(fs));
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Associação não encontrada");
        }
        repository.deleteById(id);
    }
}
