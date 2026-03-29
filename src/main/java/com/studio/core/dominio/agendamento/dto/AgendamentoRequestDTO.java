package com.studio.core.dominio.agendamento.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data // Lombok: gera getters, setters, toString, equals, hashCode
public class AgendamentoRequestDTO {

    @NotNull(message = "Cliente ID é obrigatório") // Não pode ser nulo
    private Long clienteId;

    @NotNull(message = "Serviço ID é obrigatório") // Não pode ser nulo
    private Long servicoId;

    @NotNull(message = "Funcionário ID é obrigatório") // Não pode ser nulo
    private Long funcionarioId;

    @NotNull(message = "Data/hora de início é obrigatória") // Não pode ser nulo
    private LocalDateTime dataHoraInicio;

    @NotNull(message = "Data/hora de fim é obrigatória") // Não pode ser nulo
    private LocalDateTime dataHoraFim;

    private BigDecimal valorTotal;
    private BigDecimal valorSinal;
    private Integer quantidadeParcelas = 1;
    private String comprovanteSinal;
    private String observacoes;
}
