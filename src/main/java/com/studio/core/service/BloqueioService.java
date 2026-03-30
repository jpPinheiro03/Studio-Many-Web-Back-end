package com.studio.core.service;

import com.studio.core.dominio.bloqueio.dto.BloqueioRequestDTO;
import com.studio.core.dominio.bloqueio.dto.BloqueioResponseDTO;
import com.studio.core.dominio.bloqueio.entity.Bloqueio;
import com.studio.core.dominio.bloqueio.mapper.BloqueioMapper;
import com.studio.core.dominio.bloqueio.repository.BloqueioRepository;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BloqueioService {

    @Autowired
    private BloqueioRepository repository;
    
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private BloqueioMapper bloqueioMapper;
    
    public List<BloqueioResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(bloqueioMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    public List<BloqueioResponseDTO> findByFuncionarioId(Long funcionarioId) {
        return repository.findByFuncionario_Id(funcionarioId).stream()
            .map(bloqueioMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    public BloqueioResponseDTO create(BloqueioRequestDTO dto) {
        Funcionario func = funcionarioRepository.findById(dto.getFuncionarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        
        if (dto.getDataInicio() == null || dto.getDataFim() == null) {
            throw new BadRequestException("Data de início e fim são obrigatórias");
        }
        if (dto.getDataInicio().isAfter(dto.getDataFim()) || dto.getDataInicio().isEqual(dto.getDataFim())) {
            throw new BadRequestException("Data de início deve ser anterior à data de fim");
        }
        
        Bloqueio bloqueio = bloqueioMapper.toEntity(dto);
        bloqueio.setFuncionario(func);
        
        return bloqueioMapper.toResponse(repository.save(bloqueio));
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Bloqueio não encontrado");
        }
        repository.deleteById(id);
    }
}
