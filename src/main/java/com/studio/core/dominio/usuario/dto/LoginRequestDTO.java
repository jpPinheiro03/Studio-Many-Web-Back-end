package com.studio.core.dominio.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Lombok: gera getters, setters, toString, equals, hashCode
public class LoginRequestDTO {

    @NotBlank(message = "Email é obrigatório") // Não pode ser vazio nem nulo
    @Email(message = "Email inválido") // Deve ser formato de email válido
    private String email;

    @NotBlank(message = "Senha é obrigatória") // Não pode ser vazio nem nulo
    private String senha;
}
