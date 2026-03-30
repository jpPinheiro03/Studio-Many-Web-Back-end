package com.studio.core.service;

import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoRequestDTO;
import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoResponseDTO;
import com.studio.core.dominio.horario_trabalho.entity.HorarioTrabalho;
import com.studio.core.dominio.horario_trabalho.mapper.HorarioTrabalhoMapper;
import com.studio.core.dominio.horario_trabalho.repository.HorarioTrabalhoRepository;
import com.studio.core.exception.BadRequestException;
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
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private HorarioTrabalhoMapper horarioTrabalhoMapper;
    
    public List<HorarioTrabalhoResponseDTO> findAll() {
        return repository.findAll().stream()
            .map(horarioTrabalhoMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    public List<HorarioTrabalhoResponseDTO> findByFuncionarioId(Long funcionarioId) {
        return repository.findByFuncionario_Id(funcionarioId).stream()
            .map(horarioTrabalhoMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    public HorarioTrabalhoResponseDTO create(HorarioTrabalhoRequestDTO dto) {
        Funcionario func = funcionarioRepository.findById(dto.getFuncionarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        
        if (dto.getHoraInicio() == null || dto.getHoraFim() == null) {
            throw new BadRequestException("Horário de início e fim são obrigatórios");
        }
        if (dto.getHoraInicio().isAfter(dto.getHoraFim()) || dto.getHoraInicio().equals(dto.getHoraFim())) {
            throw new BadRequestException("Horário de início deve ser anterior ao horário de fim");
        }
        if (dto.getDiaSemana() == null || dto.getDiaSemana() < 1 || dto.getDiaSemana() > 7) {
            throw new BadRequestException("Dia da semana deve estar entre 1 (segunda) e 7 (domingo)");
        }
        
        HorarioTrabalho ht = horarioTrabalhoMapper.toEntity(dto);
        ht.setFuncionario(func);
        
        return horarioTrabalhoMapper.toResponse(repository.save(ht));
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Horário não encontrado");
        }
        repository.deleteById(id);
    }
}
