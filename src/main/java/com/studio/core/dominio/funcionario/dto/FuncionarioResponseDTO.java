package com.studio.core.dominio.funcionario.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FuncionarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String especialidade;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
}
