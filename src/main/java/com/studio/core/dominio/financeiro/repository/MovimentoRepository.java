package com.studio.core.dominio.financeiro.repository;

import com.studio.core.dominio.financeiro.entity.Movimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimentoRepository extends JpaRepository<Movimento, Long> {
    List<Movimento> findByDataMovimento(LocalDate data);
    List<Movimento> findByDataMovimentoBetween(LocalDate inicio, LocalDate fim);
    
    @Query("SELECT COALESCE(SUM(CASE WHEN m.tipo = 'RECEITA' THEN m.valor ELSE 0 END), 0) - " +
           "COALESCE(SUM(CASE WHEN m.tipo = 'DESPESA' THEN m.valor ELSE 0 END), 0) " +
           "FROM Movimento m WHERE m.dataMovimento BETWEEN :inicio AND :fim")
    java.math.BigDecimal calcularSaldo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
    
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimento m WHERE m.tipo = 'RECEITA' AND m.dataMovimento BETWEEN :inicio AND :fim")
    java.math.BigDecimal totalReceitas(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
    
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimento m WHERE m.tipo = 'DESPESA' AND m.dataMovimento BETWEEN :inicio AND :fim")
    java.math.BigDecimal totalDespesas(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
    
    List<Movimento> findByTipoAndDataMovimentoBetween(Movimento.TipoMovimento tipo, LocalDate inicio, LocalDate fim);
    List<Movimento> findByTipo(Movimento.TipoMovimento tipo);
}
