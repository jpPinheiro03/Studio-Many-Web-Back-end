package com.studio.core.dominio.servico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data // Lombok: gera getters, setters, toString, equals, hashCode
public class ServicoRequestDTO {

    @NotBlank(message = "Nome é obrigatório") // Não pode ser vazio nem nulo
    private String nome;

    private String descricao; // Campo opcional

    @NotNull(message = "Preço é obrigatório") // Não pode ser nulo
    @Positive(message = "Preço deve ser positivo") // Deve ser maior que zero
    private BigDecimal preco;

    @NotNull(message = "Duração é obrigatória") // Não pode ser nulo
    @Positive(message = "Duração deve ser positiva") // Deve ser maior que zero
    private Integer duracaoMinutos;

    private Boolean ativo = true;

    private Boolean confirmacaoAutomatica = false;
}
