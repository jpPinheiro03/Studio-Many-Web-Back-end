package com.studio.core.dominio.funcionario_servico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class FuncionarioServicoRequestDTO {

    @NotNull(message = "Funcionário ID é obrigatório")
    private Long funcionarioId;

    @NotNull(message = "Serviço ID é obrigatório")
    private Long servicoId;

    @Positive(message = "Percentual deve ser positivo")
    private BigDecimal percentualComissao;

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public Long getServicoId() {
        return servicoId;
    }
}
