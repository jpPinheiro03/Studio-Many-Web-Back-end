package com.studio.core.dominio.pacote.repository;

import com.studio.core.dominio.pacote.entity.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacoteRepository extends JpaRepository<Pacote, Long> {
    boolean existsByServico_Id(Long servicoId);
}
