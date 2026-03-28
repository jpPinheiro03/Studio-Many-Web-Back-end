package com.studio.core.dominio.horario_trabalho.repository;

import com.studio.core.dominio.horario_trabalho.entity.HorarioTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HorarioTrabalhoRepository extends JpaRepository<HorarioTrabalho, Long> {
    List<HorarioTrabalho> findByFunc_Id(Long funcId);
    List<HorarioTrabalho> findByFunc_IdAndDiaSemana(Long funcId, Integer diaSemana);
}
