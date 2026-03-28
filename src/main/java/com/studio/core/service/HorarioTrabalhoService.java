package com.studio.core.service;

import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoRequestDTO;
import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoResponseDTO;
import com.studio.core.dominio.horario_trabalho.entity.HorarioTrabalho;
import com.studio.core.dominio.horario_trabalho.repository.HorarioTrabalhoRepository;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HorarioTrabalhoService {

    @Autowired
    private HorarioTrabalhoRepository repository;
    
    @Autowired
    private FuncionarioRepository FuncionarioRepository;
    
    public List<HorarioTrabalhoResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(HorarioTrabalhoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public List<HorarioTrabalhoResponseDTO> findByFuncionarioId(Long FuncionarioId) {
        return repository.findByFunc_Id(FuncionarioId).stream()
            .map(HorarioTrabalhoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public HorarioTrabalhoResponseDTO create(HorarioTrabalhoRequestDTO dto) {
        Funcionario func = FuncionarioRepository.findById(dto.getFuncionarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        
        HorarioTrabalho ht = new HorarioTrabalho();
        ht.setFunc(func);
        ht.setDiaSemana(dto.getDiaSemana());
        ht.setHoraInicio(dto.getHoraInicio());
        ht.setHoraFim(dto.getHoraFim());
        
        return HorarioTrabalhoResponseDTO.fromEntity(repository.save(ht));
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Horário não encontrado");
        }
        repository.deleteById(id);
    }
}
