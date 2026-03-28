package com.studio.core.dominio.funcionario.repository;

import com.studio.core.dominio.funcionario.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByAtivoTrue();
    Optional<Funcionario> findByEmail(String email);
}
