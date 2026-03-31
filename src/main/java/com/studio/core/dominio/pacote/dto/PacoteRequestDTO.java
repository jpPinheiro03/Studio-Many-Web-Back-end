package com.studio.core.dominio.pacote.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PacoteRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(example = "Limpeza de pele 5x")
    private String nome;

    @NotNull(message = "Serviço ID é obrigatório")
    private Long servicoId;

    @NotNull(message = "Quantidade de sessões é obrigatória")
    @Positive(message = "Quantidade de sessões deve ser positiva")
    @Schema(example = "5")
    private Integer quantidadeSessoes;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    @Schema(example = "110")
    private BigDecimal preco;

    @NotNull
    @Positive(message = "Validade deve ser positiva")
    @Schema(example = "90")
    private Integer validadeDias;

    private Boolean ativo = true;
}
