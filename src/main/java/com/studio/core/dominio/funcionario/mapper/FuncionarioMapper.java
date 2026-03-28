package com.studio.core.dominio.funcionario.mapper;

import com.studio.core.dominio.funcionario.dto.FuncionarioRequestDTO;
import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import com.studio.core.dominio.funcionario.entity.Funcionario;

public class FuncionarioMapper {

    public static Funcionario toEntity(FuncionarioRequestDTO dto) {
        Funcionario entity = new Funcionario();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setCpf(dto.getCpf());
        entity.setEspecialidade(dto.getEspecialidade());
        entity.setAtivo(dto.getAtivo());
        return entity;
    }

    public static FuncionarioResponseDTO toResponse(Funcionario entity) {
        FuncionarioResponseDTO dto = new FuncionarioResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setCpf(entity.getCpf());
        dto.setEspecialidade(entity.getEspecialidade());
        dto.setAtivo(entity.getAtivo());
        dto.setDataCadastro(entity.getDataCadastro());
        return dto;
    }
}
