package com.studio.core.dominio.horario_trabalho.repository;

import com.studio.core.dominio.horario_trabalho.entity.HorarioTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HorarioTrabalhoRepository extends JpaRepository<HorarioTrabalho, Long> {
    List<HorarioTrabalho> findByFuncionario_Id(Long funcId);
    List<HorarioTrabalho> findByFuncionario_IdAndDiaSemana(Long funcId, Integer diaSemana);
    boolean existsByFuncionario_Id(Long funcId);
}
