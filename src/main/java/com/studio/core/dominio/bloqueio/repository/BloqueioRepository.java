package com.studio.core.dominio.bloqueio.repository;

import com.studio.core.dominio.bloqueio.entity.Bloqueio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloqueioRepository extends JpaRepository<Bloqueio, Long> {
    List<Bloqueio> findByFuncionario_Id(Long funcId);
    boolean existsByFuncionario_Id(Long funcId);
    
    @Query("SELECT b FROM Bloqueio b WHERE b.funcionario.id = :funcionarioId AND " +
           "((b.dataInicio <= :dataFim AND b.dataFim >= :dataInicio))")
    List<Bloqueio> findByFuncionario_IdAndDataBetween(@Param("funcionarioId") Long funcId, 
                                                      @Param("dataInicio") LocalDateTime dataInicio, 
                                                      @Param("dataFim") LocalDateTime dataFim);
}
