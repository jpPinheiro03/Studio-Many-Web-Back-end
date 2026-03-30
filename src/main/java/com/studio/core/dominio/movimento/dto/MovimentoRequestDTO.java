package com.studio.core.dominio.movimento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data // Lombok: gera getters, setters, toString, equals, hashCode
public class MovimentoRequestDTO {

    @NotBlank(message = "Tipo é obrigatório") // Não pode ser vazio nem nulo
    private String tipo;

    private Long agendamentoId;

    private Long usuarioId;

    @NotNull(message = "Valor é obrigatório") // Não pode ser nulo
    @Positive(message = "Valor deve ser positivo") // Deve ser maior que zero
    private BigDecimal valor;

    private LocalDate dataMovimento;

    private String descricao;
}
