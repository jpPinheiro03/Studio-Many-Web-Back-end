package com.studio.core.dominio.comissao.mapper;

import com.studio.core.dominio.comissao.dto.ComissaoResponseDTO;
import com.studio.core.dominio.comissao.entity.Comissao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComissaoMapper {

    ComissaoResponseDTO toResponse(Comissao entity);
}
