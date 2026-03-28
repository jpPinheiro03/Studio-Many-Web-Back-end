package com.studio.core.dominio.comissao.repository;

import com.studio.core.dominio.comissao.entity.Comissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComissaoRepository extends JpaRepository<Comissao, Long> {
    List<Comissao> findByFunc_Id(Long funcId);
    List<Comissao> findByFunc_IdAndStatus(Long funcId, Comissao.StatusComissao status);
    List<Comissao> findByStatus(Comissao.StatusComissao status);
    List<Comissao> findByDataComissaoBetween(LocalDate inicio, LocalDate fim);
    
    @Query("SELECT COALESCE(SUM(c.valor), 0) FROM Comissao c WHERE c.func.id = :funcionarioId AND c.status = 'PENDENTE'")
    BigDecimal totalPendentePorFuncionario(@Param("funcionarioId") Long FuncionarioId);
}
