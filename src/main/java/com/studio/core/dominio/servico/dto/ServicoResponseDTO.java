package com.studio.core.dominio.servico.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServicoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer duracaoMinutos;
    private Boolean ativo;
    private Boolean confirmacaoAutomatica;
}
