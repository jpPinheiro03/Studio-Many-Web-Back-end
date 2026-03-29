package com.studio.core.dominio.funcionario.mapper;

import com.studio.core.dominio.funcionario.dto.FuncionarioRequestDTO;
import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    Funcionario toEntity(FuncionarioRequestDTO dto);

    FuncionarioResponseDTO toResponse(Funcionario entity);
}
