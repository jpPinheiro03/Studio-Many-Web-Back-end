package com.studio.core.dominio.servico.repository;

import com.studio.core.dominio.servico.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByAtivoTrue();
}
