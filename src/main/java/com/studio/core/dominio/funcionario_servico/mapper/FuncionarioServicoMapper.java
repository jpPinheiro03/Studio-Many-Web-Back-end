package com.studio.core.dominio.funcionario_servico.mapper;

import com.studio.core.dominio.funcionario.mapper.FuncionarioMapper;
import com.studio.core.dominio.funcionario_servico.dto.FuncionarioServicoRequestDTO;
import com.studio.core.dominio.funcionario_servico.dto.FuncionarioServicoResponseDTO;
import com.studio.core.dominio.funcionario_servico.entity.FuncionarioServico;
import com.studio.core.dominio.servico.mapper.ServicoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FuncionarioMapper.class, ServicoMapper.class})
public interface FuncionarioServicoMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "funcionario", ignore = true) // Setado pelo service
    @Mapping(target = "servico", ignore = true) // Setado pelo service
    @Mapping(target = "funcionarioId", source = "funcionarioId") // DTO.funcionarioId → entity.funcionarioId
    FuncionarioServico toEntity(FuncionarioServicoRequestDTO dto);

    FuncionarioServicoResponseDTO toResponse(FuncionarioServico entity);
}
