package com.studio.core.dominio.lista_espera.mapper;

import com.studio.core.dominio.cliente.mapper.ClienteMapper;
import com.studio.core.dominio.funcionario.mapper.FuncionarioMapper;
import com.studio.core.dominio.lista_espera.dto.ListaEsperaRequestDTO;
import com.studio.core.dominio.lista_espera.dto.ListaEsperaResponseDTO;
import com.studio.core.dominio.lista_espera.entity.ListaEspera;
import com.studio.core.dominio.servico.mapper.ServicoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, ServicoMapper.class, FuncionarioMapper.class})
public interface ListaEsperaMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "cliente", ignore = true) // Setado pelo service
    @Mapping(target = "servico", ignore = true) // Setado pelo service
    @Mapping(target = "funcionario", ignore = true) // Setado pelo service
    @Mapping(target = "status", ignore = true) // Setado pelo service
    @Mapping(target = "dataCadastro", ignore = true) // Setado pelo banco
    ListaEspera toEntity(ListaEsperaRequestDTO dto);

    ListaEsperaResponseDTO toResponse(ListaEspera entity);
}
