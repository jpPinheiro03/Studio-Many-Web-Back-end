package com.studio.core.dominio.comissao.dto;

import com.studio.core.dominio.agendamento.dto.AgendamentoResponseDTO;
import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ComissaoResponseDTO {

    private Long id;
    private FuncionarioResponseDTO funcionario;
    private AgendamentoResponseDTO agendamento;
    private BigDecimal valor;
    private BigDecimal percentual;
    private LocalDate dataComissao;
    private LocalDate dataPagamento;
    private String status;
}
