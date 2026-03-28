package com.studio.core.dominio.horario_trabalho.dto;

import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import java.time.LocalTime;

public class HorarioTrabalhoResponseDTO {

    private Long id;
    private FuncionarioResponseDTO func;
    private Integer diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public FuncionarioResponseDTO getFunc() { return func; }
    public void setFunc(FuncionarioResponseDTO func) { this.func = func; }
    public Integer getDiaSemana() { return diaSemana; }
    public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }

    public static HorarioTrabalhoResponseDTO fromEntity(com.studio.core.dominio.horario_trabalho.entity.HorarioTrabalho entity) {
        HorarioTrabalhoResponseDTO dto = new HorarioTrabalhoResponseDTO();
        dto.setId(entity.getId());
        if (entity.getFunc() != null) {
            dto.setFunc(FuncionarioResponseDTO.fromEntity(entity.getFunc()));
        }
        dto.setDiaSemana(entity.getDiaSemana());
        dto.setHoraInicio(entity.getHoraInicio());
        dto.setHoraFim(entity.getHoraFim());
        return dto;
    }
}
