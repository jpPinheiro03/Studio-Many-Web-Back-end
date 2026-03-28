package com.studio.core.dominio.cliente.repository;

import com.studio.core.dominio.cliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByTelefone(String telefone);
    Optional<Cliente> findByEmail(String email);
    List<Cliente> findByAtivoTrue();
    List<Cliente> findByAtivoFalse();
    
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.dataCadastro >= :inicio AND c.dataCadastro < :fim")
    Long countByDataCadastroBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
