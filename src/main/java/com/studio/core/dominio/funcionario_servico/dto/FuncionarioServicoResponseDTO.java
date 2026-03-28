package com.studio.core.dominio.funcionario_servico.dto;

import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import java.math.BigDecimal;

public class FuncionarioServicoResponseDTO {

    private Long id;
    private FuncionarioResponseDTO func;
    private ServicoResponseDTO servico;
    private BigDecimal percentualComissao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public FuncionarioResponseDTO getFunc() { return func; }
    public void setFunc(FuncionarioResponseDTO func) { this.func = func; }
    public ServicoResponseDTO getServico() { return servico; }
    public void setServico(ServicoResponseDTO servico) { this.servico = servico; }
    public BigDecimal getPercentualComissao() { return percentualComissao; }
    public void setPercentualComissao(BigDecimal percentualComissao) { this.percentualComissao = percentualComissao; }

    public static FuncionarioServicoResponseDTO fromEntity(com.studio.core.dominio.funcionario_servico.entity.FuncionarioServico entity) {
        FuncionarioServicoResponseDTO dto = new FuncionarioServicoResponseDTO();
        dto.setId(entity.getId());
        if (entity.getFunc() != null) {
            dto.setFunc(FuncionarioResponseDTO.fromEntity(entity.getFunc()));
        }
        if (entity.getServico() != null) {
            dto.setServico(ServicoResponseDTO.fromEntity(entity.getServico()));
        }
        dto.setPercentualComissao(entity.getPercentualComissao());
        return dto;
    }
}
