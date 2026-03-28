package com.studio.core.service;

import com.studio.core.dominio.bloqueio.dto.BloqueioRequestDTO;
import com.studio.core.dominio.bloqueio.dto.BloqueioResponseDTO;
import com.studio.core.dominio.bloqueio.entity.Bloqueio;
import com.studio.core.dominio.bloqueio.repository.BloqueioRepository;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
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
    private FuncionarioRepository FuncionarioRepository;
    
    public List<BloqueioResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(BloqueioResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<BloqueioResponseDTO> findByFuncionarioId(Long FuncionarioId) {
        return repository.findByFunc_Id(FuncionarioId).stream()
            .map(BloqueioResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public BloqueioResponseDTO create(BloqueioRequestDTO dto) {
        Funcionario func = FuncionarioRepository.findById(dto.getFuncionarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        
        Bloqueio bloqueio = new Bloqueio();
        bloqueio.setFunc(func);
        bloqueio.setDataInicio(dto.getDataInicio());
        bloqueio.setDataFim(dto.getDataFim());
        bloqueio.setMotivo(dto.getMotivo());
        
        return BloqueioResponseDTO.fromEntity(repository.save(bloqueio));
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Bloqueio não encontrado");
        }
        repository.deleteById(id);
    }
}
