package com.studio.core.dominio.agendamento.repository;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    
    List<Agendamento> findByFuncionario_Id(Long funcionarioId);
    List<Agendamento> findByCliente_Id(Long clienteId);
    List<Agendamento> findByStatus(StatusAgendamento status);
    
    @Query("SELECT a FROM Agendamento a WHERE a.dataHoraInicio >= :inicio AND a.dataHoraInicio < :fim")
    List<Agendamento> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    @Query("SELECT a FROM Agendamento a WHERE a.dataHoraInicio >= :inicio AND a.dataHoraInicio < :fim AND a.status NOT IN ('CANCELADO', 'NAO_COMPARECEU')")
    List<Agendamento> findByDataInicio(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
