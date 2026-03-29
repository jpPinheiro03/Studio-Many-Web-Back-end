package com.studio.core.dominio.usuario.dto;

import com.studio.core.dominio.usuario.entity.Usuario;
import lombok.Data;

@Data
public class LoginResponseDTO {
    
    private String token;
    private String tipo = "Bearer";
    private UsuarioResponseDTO usuario;

    public LoginResponseDTO(String token, Usuario usuario) {
        this.token = token;
        this.usuario = new UsuarioResponseDTO();
        this.usuario.setId(usuario.getId());
        this.usuario.setEmail(usuario.getEmail());
        this.usuario.setRole(usuario.getRole());
        this.usuario.setDataCadastro(usuario.getDataCadastro());
        this.usuario.setAtivo(usuario.getAtivo());
        if (usuario.getFuncionario() != null) {
            this.usuario.setFuncionarioId(usuario.getFuncionario().getId());
            this.usuario.setNomeFuncionario(usuario.getFuncionario().getNome());
        }
    }
}
