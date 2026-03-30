package com.studio.core.dominio.usuario.dto;

import com.studio.core.dominio.usuario.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data // Lombok: gera getters, setters, toString, equals, hashCode
public class UsuarioRequestDTO {

    @NotBlank(message = "Email é obrigatório") // Não pode ser vazio nem nulo
    @Email(message = "Email inválido") // Deve ser formato de email válido
    private String email;

    @NotBlank(message = "Senha é obrigatória") // Não pode ser vazio nem nulo
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres") // Tamanho mínimo da string
    private String senha;

    @NotNull(message = "Role é obrigatória") // Não pode ser nulo
    private Usuario.Role role;
    
    private Long funcionarioId;

    public Long getFuncionarioId() {
        return funcionarioId;
    }
}
