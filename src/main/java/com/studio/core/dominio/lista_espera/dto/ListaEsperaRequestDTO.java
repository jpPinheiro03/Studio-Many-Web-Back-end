package com.studio.core.dominio.lista_espera.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ListaEsperaRequestDTO {

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;

    @NotNull(message = "Serviço ID é obrigatório")
    private Long servicoId;

    private Long funcionarioId;
    private LocalDate dataDesejada;
    private LocalTime horarioDesejado;
    private String observacoes;
}
