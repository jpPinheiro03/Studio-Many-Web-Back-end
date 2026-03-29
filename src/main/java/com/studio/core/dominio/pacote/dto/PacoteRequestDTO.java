package com.studio.core.dominio.pacote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PacoteRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Serviço ID é obrigatório")
    private Long servicoId;

    @NotNull(message = "Quantidade de sessões é obrigatória")
    @Positive(message = "Quantidade de sessões deve ser positiva")
    private Integer quantidadeSessoes;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private BigDecimal preco;

    @Positive(message = "Validade deve ser positiva")
    private Integer validadeDias;

    private Boolean ativo = true;
}
