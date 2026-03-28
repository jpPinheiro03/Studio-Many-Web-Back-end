package com.studio.core.dominio.usuario.dto;

import com.studio.core.dominio.usuario.entity.Usuario;

public class LoginResponseDTO {
    
    private String token;
    private String tipo = "Bearer";
    private UsuarioResponseDTO usuario;

    public LoginResponseDTO(String token, Usuario usuario) {
        this.token = token;
        this.usuario = UsuarioResponseDTO.fromEntity(usuario);
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public UsuarioResponseDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioResponseDTO usuario) { this.usuario = usuario; }
}
