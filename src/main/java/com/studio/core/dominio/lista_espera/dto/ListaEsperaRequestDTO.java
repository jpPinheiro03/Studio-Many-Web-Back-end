package com.studio.core.dominio.lista_espera.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @NotNull(message = "Funcionário ID é obrigatório")
    private Long funcionarioId;

    @NotNull(message = "Data desejada deve existir")
    @Schema(example = "2026-03-28")
    private LocalDate dataDesejada;

    @NotNull(message = "Horário desejado deve existir")
    @Schema(example = "08:00:00")
    private LocalTime horarioDesejado;
}
