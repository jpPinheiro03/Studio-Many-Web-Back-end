package com.studio.core.dominio.funcionario_servico.mapper;

import com.studio.core.dominio.funcionario_servico.dto.FuncionarioServicoRequestDTO;
import com.studio.core.dominio.funcionario_servico.dto.FuncionarioServicoResponseDTO;
import com.studio.core.dominio.funcionario_servico.entity.FuncionarioServico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FuncionarioServicoMapper {

    FuncionarioServico toEntity(FuncionarioServicoRequestDTO dto);

    FuncionarioServicoResponseDTO toResponse(FuncionarioServico entity);
}
