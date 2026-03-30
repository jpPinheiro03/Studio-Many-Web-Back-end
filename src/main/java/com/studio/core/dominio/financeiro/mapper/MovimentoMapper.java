package com.studio.core.dominio.financeiro.mapper;

import com.studio.core.dominio.financeiro.entity.Movimento;
import com.studio.core.dominio.movimento.dto.MovimentoRequestDTO;
import com.studio.core.dominio.movimento.dto.MovimentoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimentoMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "agendamento", ignore = true) // Setado pelo service
    @Mapping(target = "usuario", ignore = true) // Setado pelo service
    @Mapping(target = "dataCadastro", ignore = true) // Setado pelo banco
    Movimento toEntity(MovimentoRequestDTO dto);

    @Mapping(source = "agendamento.id", target = "agendamentoId")
    @Mapping(source = "usuario.id", target = "usuarioId")
    MovimentoResponseDTO toResponse(Movimento entity);
}
