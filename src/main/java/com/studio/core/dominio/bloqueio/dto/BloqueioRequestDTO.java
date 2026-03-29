package com.studio.core.dominio.bloqueio.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BloqueioRequestDTO {

    @NotNull(message = "Funcionário ID é obrigatório")
    private Long funcionarioId;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDateTime dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDateTime dataFim;

    private String motivo;
}
