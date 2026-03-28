package com.studio.core.dominio.auditoria.repository;

import com.studio.core.dominio.auditoria.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByEntidadeAndEntidadeIdOrderByDataAcaoDesc(String entidade, Long entidadeId);
    List<Auditoria> findByEntidadeOrderByDataAcaoDesc(String entidade);
    List<Auditoria> findByDataAcaoBetweenOrderByDataAcaoDesc(LocalDateTime inicio, LocalDateTime fim);
}
