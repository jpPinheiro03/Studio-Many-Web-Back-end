package com.studio.core.dominio.parcela.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ParcelaRequestDTO {

    @NotNull(message = "A quantidade de parcelas é obrigatória")
    @Positive(message = "A quantidade deve ser positiva")
    @Schema(example = "3")
    private Integer quantidade;

    @NotNull(message = "A data da primeira parcela é obrigatória")
    @Schema(example = "2026-04-10")
    private LocalDate dataPrimeira;
}