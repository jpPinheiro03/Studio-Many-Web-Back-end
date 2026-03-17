package school.spetch.backend_Studio_many.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import school.spetch.backend_Studio_many.dto.UsuarioRequestDto;
import school.spetch.backend_Studio_many.dto.UsuarioResponseDto;
import school.spetch.backend_Studio_many.exception.UsuarioExistenteException;
import school.spetch.backend_Studio_many.model.Usuario;
import school.spetch.backend_Studio_many.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrar(Usuario usuario){
        if(usuarioExistePorEmail(usuario.getEmail())){
            throw new UsuarioExistenteException("O usuário já existe");
        }

        usuarioRepository.save(usuario);
        return usuario;
    }

    private Boolean usuarioExistePorEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }
}
