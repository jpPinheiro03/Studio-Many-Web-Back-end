package com.studio.core.dominio.agendamento.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendamentoRecorrenteRequestDTO {

    private Long clienteId;
    private Long servicoId;
    private Long funcionarioId;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private BigDecimal valorTotal;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String frequencia;
    private Integer diaSemana;
}
