package com.studio.core.dominio.funcionario_servico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class FuncionarioServicoRequestDTO {

    @NotNull(message = "Funcionário ID é obrigatório")
    private Long funcId;

    @NotNull(message = "Serviço ID é obrigatório")
    private Long servicoId;

    @Positive(message = "Percentual deve ser positivo")
    private BigDecimal percentualComissao;

    public Long getFuncionarioId() { return funcId; }
    public void setFuncionarioId(Long funcId) { this.funcId = funcId; }
    public Long getServicoId() { return servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public BigDecimal getPercentualComissao() { return percentualComissao; }
    public void setPercentualComissao(BigDecimal percentualComissao) { this.percentualComissao = percentualComissao; }
}
