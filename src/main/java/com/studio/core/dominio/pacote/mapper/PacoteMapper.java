package com.studio.core.dominio.pacote.mapper;

import com.studio.core.dominio.pacote.dto.PacoteRequestDTO;
import com.studio.core.dominio.pacote.dto.PacoteResponseDTO;
import com.studio.core.dominio.pacote.entity.Pacote;
import com.studio.core.dominio.servico.mapper.ServicoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ServicoMapper.class})
public interface PacoteMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "servico", ignore = true) // Setado pelo service
    @Mapping(target = "dataCadastro", ignore = true) // Setado pelo banco
    Pacote toEntity(PacoteRequestDTO dto);

    PacoteResponseDTO toResponse(Pacote entity);
}
