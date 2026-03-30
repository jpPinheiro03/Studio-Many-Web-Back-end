package com.studio.core.dominio.horario_trabalho.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalTime;

@Data
public class HorarioTrabalhoRequestDTO {

    @NotNull(message = "Funcionário ID é obrigatório")
    private Long funcionarioId;

    @NotNull(message = "Dia da semana é obrigatório")
    private Integer diaSemana;

    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    public Long getFuncionarioId() {
        return funcionarioId;
    }
}
