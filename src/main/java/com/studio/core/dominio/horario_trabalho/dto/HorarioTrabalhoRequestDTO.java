package com.studio.core.dominio.horario_trabalho.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalTime;

@Data
public class HorarioTrabalhoRequestDTO {

    @NotNull(message = "Funcionário ID é obrigatório")
    private Long funcionarioId;

    @NotNull(message = "Dia da semana é obrigatório")
    @Schema(example = "1")
    private Integer diaSemana;

    @NotNull(message = "Hora de início é obrigatória")
    @Schema(example = "08:00")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    @Schema(example = "12:00")
    private LocalTime horaFim;

    public Long getFuncionarioId() {
        return funcionarioId;
    }
}
