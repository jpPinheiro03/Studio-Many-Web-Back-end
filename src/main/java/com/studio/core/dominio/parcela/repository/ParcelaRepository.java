package com.studio.core.dominio.parcela.repository;

import com.studio.core.dominio.parcela.entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
    List<Parcela> findByAgendamento_Id(Long agendamentoId);
    List<Parcela> findByAgendamentoId(Long agendamentoId);
    List<Parcela> findByStatus(Parcela.StatusParcela status);
    List<Parcela> findByDataVencimentoBeforeAndStatus(LocalDate data, Parcela.StatusParcela status);
    List<Parcela> findByDataVencimentoBetweenAndStatus(LocalDate inicio, LocalDate fim, Parcela.StatusParcela status);
}
