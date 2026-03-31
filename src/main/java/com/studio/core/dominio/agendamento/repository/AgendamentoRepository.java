package com.studio.core.dominio.agendamento.repository;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("SELECT a FROM Agendamento a JOIN FETCH a.cliente JOIN FETCH a.servico JOIN FETCH a.funcionario")
    List<Agendamento> findAllWithRelations();

    @Query(value = "SELECT a FROM Agendamento a JOIN FETCH a.cliente JOIN FETCH a.servico JOIN FETCH a.funcionario",
            countQuery = "SELECT COUNT(a) FROM Agendamento a")
    Page<Agendamento> findAllPaginated(Pageable pageable);

    @Query("SELECT a FROM Agendamento a JOIN FETCH a.cliente JOIN FETCH a.servico JOIN FETCH a.funcionario WHERE a.id = :id")
    Optional<Agendamento> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT a FROM Agendamento a JOIN FETCH a.cliente JOIN FETCH a.servico JOIN FETCH a.funcionario WHERE a.funcionario.id = :funcId")
    List<Agendamento> findByFuncionario_Id(@Param("funcId") Long funcionarioId);

    @Query("SELECT a FROM Agendamento a JOIN FETCH a.cliente JOIN FETCH a.servico JOIN FETCH a.funcionario WHERE a.cliente.id = :clienteId")
    List<Agendamento> findByCliente_Id(@Param("clienteId") Long clienteId);

    List<Agendamento> findByStatus(StatusAgendamento status);

    @Query("SELECT a FROM Agendamento a JOIN FETCH a.cliente JOIN FETCH a.servico JOIN FETCH a.funcionario " +
            "WHERE a.dataHoraInicio >= :inicio AND a.dataHoraInicio < :fim")
    List<Agendamento> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT a FROM Agendamento a JOIN FETCH a.cliente JOIN FETCH a.servico JOIN FETCH a.funcionario " +
            "WHERE a.dataHoraInicio >= :inicio AND a.dataHoraInicio < :fim AND a.status NOT IN ('CANCELADO', 'NAO_COMPARECEU')")
    List<Agendamento> findByDataInicio(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT a FROM Agendamento a WHERE a.funcionario.id = :funcId AND a.status NOT IN ('CANCELADO', 'NAO_COMPARECEU') " +
            "AND a.dataHoraInicio < :dataFim AND a.dataHoraFim > :dataHoraInicio")
    List<Agendamento> findConflitoHorario(@Param("funcId") Long funcId,
                                          @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
                                          @Param("dataFim") LocalDateTime dataFim);

    boolean existsByCliente_Id(Long clienteId);

    boolean existsByFuncionario_Id(Long funcionarioId);

    boolean existsByServico_Id(Long servicoId);
}
