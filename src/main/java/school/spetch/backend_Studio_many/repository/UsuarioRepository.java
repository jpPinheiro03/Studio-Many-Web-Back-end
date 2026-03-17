package school.spetch.backend_Studio_many.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.spetch.backend_Studio_many.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Boolean existsByEmail(String email);
}
