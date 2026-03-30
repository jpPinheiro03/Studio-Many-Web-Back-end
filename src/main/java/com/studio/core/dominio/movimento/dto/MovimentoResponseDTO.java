package com.studio.core.dominio.movimento.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MovimentoResponseDTO {

    private Long id;
    private String tipo;
    private Long agendamentoId;
    private Long usuarioId;
    private BigDecimal valor;
    private LocalDate dataMovimento;
    private String descricao;
    private LocalDateTime dataCadastro;
}
