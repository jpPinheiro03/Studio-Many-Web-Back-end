package com.studio.core.dominio.usuario.dto;

import com.studio.core.dominio.usuario.entity.Usuario;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioResponseDTO {
    
    private Long id;
    private String email;
    private Usuario.Role role;
    private Long funcionarioId;
    private String nomeFuncionario;
    private LocalDateTime dataCadastro;
    private Boolean ativo;
}
