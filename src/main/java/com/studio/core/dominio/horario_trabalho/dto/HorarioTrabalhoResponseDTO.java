package com.studio.core.dominio.horario_trabalho.dto;

import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import lombok.Data;
import java.time.LocalTime;

@Data
public class HorarioTrabalhoResponseDTO {

    private Long id;
    private FuncionarioResponseDTO func;
    private Integer diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
}
