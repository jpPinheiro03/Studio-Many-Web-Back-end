package com.studio.core.dominio.funcionario_servico.dto;

import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class FuncionarioServicoResponseDTO {

    private Long id;
    private FuncionarioResponseDTO funcionario;
    private ServicoResponseDTO servico;
    private BigDecimal percentualComissao;
}
