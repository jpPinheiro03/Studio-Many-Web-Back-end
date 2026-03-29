package com.studio.core.dominio.agendamento.dto;

import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AgendamentoResponseDTO {

    private Long id;
    private ClienteResponseDTO cliente;
    private ServicoResponseDTO servico;
    private FuncionarioResponseDTO funcionario;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String status;
    private BigDecimal valorTotal;
    private BigDecimal valorSinal;
    private Integer quantidadeParcelas;
    private String comprovanteSinal;
    private LocalDateTime dataConfirmacaoSinal;
    private LocalDateTime dataChegada;
    private LocalDateTime dataFinalizacao;
    private String motivoCancelamento;
    private String observacoes;
}
