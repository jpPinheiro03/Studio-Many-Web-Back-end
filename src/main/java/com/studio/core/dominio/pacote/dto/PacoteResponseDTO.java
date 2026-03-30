package com.studio.core.dominio.pacote.dto;

import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PacoteResponseDTO {

    private Long id;
    private String nome;
    private ServicoResponseDTO servico;
    private Integer quantidadeSessoes;
    private BigDecimal preco;
    private Integer validadeDias;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
}
