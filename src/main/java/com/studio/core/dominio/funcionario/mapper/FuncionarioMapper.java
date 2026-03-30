package com.studio.core.dominio.funcionario.mapper;

import com.studio.core.dominio.funcionario.dto.FuncionarioRequestDTO;
import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "dataCadastro", ignore = true) // Setado pelo banco
    Funcionario toEntity(FuncionarioRequestDTO dto);

    FuncionarioResponseDTO toResponse(Funcionario entity);
}
