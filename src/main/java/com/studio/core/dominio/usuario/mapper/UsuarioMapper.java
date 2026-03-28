package com.studio.core.dominio.usuario.mapper;

import com.studio.core.dominio.usuario.dto.UsuarioRequestDTO;
import com.studio.core.dominio.usuario.dto.UsuarioResponseDTO;
import com.studio.core.dominio.usuario.entity.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario entity = new Usuario();
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());
        entity.setRole(dto.getRole());
        return entity;
    }

    public static UsuarioResponseDTO toResponse(Usuario entity) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setAtivo(entity.getAtivo());
        if (entity.getFuncionario() != null) {
            dto.setFuncionarioId(entity.getFuncionario().getId());
            dto.setNomeFuncionario(entity.getFuncionario().getNome());
        }
        return dto;
    }
}
