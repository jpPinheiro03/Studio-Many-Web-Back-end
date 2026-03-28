package com.studio.core.dominio.horario_trabalho.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class HorarioTrabalhoRequestDTO {

    @NotNull(message = "Funcionário ID é obrigatório")
    private Long funcId;

    @NotNull(message = "Dia da semana é obrigatório")
    private Integer diaSemana;

    @NotNull(message = "Hora de início é obrigatória")
    private LocalTime horaInicio;

    @NotNull(message = "Hora de fim é obrigatória")
    private LocalTime horaFim;

    public Long getFuncionarioId() { return funcId; }
    public void setFuncionarioId(Long funcId) { this.funcId = funcId; }
    public Integer getDiaSemana() { return diaSemana; }
    public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }
}
