package school.spetch.backend_Studio_many.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.spetch.backend_Studio_many.exception.EntidadeExistenteException;
import school.spetch.backend_Studio_many.entity.Usuario;
import school.spetch.backend_Studio_many.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrar(Usuario usuario){
        if(usuarioExistePorEmail(usuario.getEmail())){
            throw new EntidadeExistenteException("Já existe um usuário com este email");
        }

        return usuarioRepository.save(usuario);
    }

    private Boolean usuarioExistePorEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }
}
