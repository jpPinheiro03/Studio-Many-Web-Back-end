package com.studio.core.dominio.usuario.mapper;

import com.studio.core.dominio.usuario.dto.UsuarioRequestDTO;
import com.studio.core.dominio.usuario.dto.UsuarioResponseDTO;
import com.studio.core.dominio.usuario.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "senha", ignore = true) // Setado pelo service (criptografada)
    @Mapping(target = "role", ignore = true) // Setado pelo service
    @Mapping(target = "funcionario", ignore = true) // Setado pelo service
    @Mapping(target = "ativo", ignore = true) // Valor padrão true
    @Mapping(target = "dataCadastro", ignore = true) // Setado pelo banco
    Usuario toEntity(UsuarioRequestDTO dto);

    @Mapping(source = "funcionario.id", target = "funcionarioId")
    @Mapping(source = "funcionario.nome", target = "nomeFuncionario")
    UsuarioResponseDTO toResponse(Usuario entity);
}
