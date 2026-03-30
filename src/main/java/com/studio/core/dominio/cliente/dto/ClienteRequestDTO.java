package com.studio.core.dominio.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Lombok: gera getters, setters, toString, equals, hashCode
public class ClienteRequestDTO {

    @NotBlank(message = "Nome é obrigatório") // Não pode ser vazio nem nulo
    private String nome;

    @Email(message = "Email inválido") // Deve ser formato de email válido
    private String email;

    @NotBlank(message = "Telefone é obrigatório") // Não pode ser vazio nem nulo
    private String telefone;

    private String cpf;
    private String endereco;
    private String observacoes;
    private Boolean ativo = true;
}
