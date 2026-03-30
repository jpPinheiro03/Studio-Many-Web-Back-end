package com.studio.core.dominio.comissao.mapper;

import com.studio.core.dominio.agendamento.mapper.AgendamentoMapper;
import com.studio.core.dominio.comissao.dto.ComissaoResponseDTO;
import com.studio.core.dominio.comissao.entity.Comissao;
import com.studio.core.dominio.funcionario.mapper.FuncionarioMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FuncionarioMapper.class, AgendamentoMapper.class})
public interface ComissaoMapper {

    ComissaoResponseDTO toResponse(Comissao entity);
}
