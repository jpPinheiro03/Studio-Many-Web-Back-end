package com.studio.core.dominio.cliente.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String endereco;
    private String observacoes;
    private String estagioFunil;
    private LocalDateTime dataCadastro;
    private Boolean ativo;
}
