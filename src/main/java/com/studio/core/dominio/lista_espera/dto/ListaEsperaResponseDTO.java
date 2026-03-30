package com.studio.core.dominio.lista_espera.dto;

import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ListaEsperaResponseDTO {

    private Long id;
    private ClienteResponseDTO cliente;
    private ServicoResponseDTO servico;
    private FuncionarioResponseDTO funcionario;
    private LocalDate dataDesejada;
    private LocalTime horarioDesejado;
    private String status;
    private String observacoes;
    private LocalDateTime dataCadastro;
}
