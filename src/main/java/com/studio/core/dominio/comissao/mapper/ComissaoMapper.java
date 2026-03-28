package com.studio.core.dominio.comissao.mapper;

import com.studio.core.dominio.comissao.dto.ComissaoResponseDTO;
import com.studio.core.dominio.comissao.entity.Comissao;

public class ComissaoMapper {

    public static ComissaoResponseDTO toResponse(Comissao entity) {
        ComissaoResponseDTO dto = new ComissaoResponseDTO();
        dto.setId(entity.getId());
        if (entity.getFunc() != null) {
            dto.setFunc(new com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO());
            dto.getFunc().setId(entity.getFunc().getId());
            dto.getFunc().setNome(entity.getFunc().getNome());
        }
        dto.setValor(entity.getValor());
        dto.setPercentual(entity.getPercentual());
        dto.setDataComissao(entity.getDataComissao());
        dto.setDataPagamento(entity.getDataPagamento());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        return dto;
    }
}
