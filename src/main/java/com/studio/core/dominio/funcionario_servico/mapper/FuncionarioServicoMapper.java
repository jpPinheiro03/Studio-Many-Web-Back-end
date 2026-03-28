package com.studio.core.dominio.funcionario_servico.mapper;

import com.studio.core.dominio.funcionario_servico.dto.FuncionarioServicoRequestDTO;
import com.studio.core.dominio.funcionario_servico.dto.FuncionarioServicoResponseDTO;
import com.studio.core.dominio.funcionario_servico.entity.FuncionarioServico;

public class FuncionarioServicoMapper {

    public static FuncionarioServico toEntity(FuncionarioServicoRequestDTO dto) {
        FuncionarioServico entity = new FuncionarioServico();
        entity.setPercentualComissao(dto.getPercentualComissao());
        return entity;
    }

    public static FuncionarioServicoResponseDTO toResponse(FuncionarioServico entity) {
        FuncionarioServicoResponseDTO dto = new FuncionarioServicoResponseDTO();
        dto.setId(entity.getId());
        if (entity.getFunc() != null) {
            dto.setFunc(new com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO());
            dto.getFunc().setId(entity.getFunc().getId());
            dto.getFunc().setNome(entity.getFunc().getNome());
        }
        if (entity.getServico() != null) {
            dto.setServico(new com.studio.core.dominio.servico.dto.ServicoResponseDTO());
            dto.getServico().setId(entity.getServico().getId());
            dto.getServico().setNome(entity.getServico().getNome());
        }
        dto.setPercentualComissao(entity.getPercentualComissao());
        return dto;
    }
}
