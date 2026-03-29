package com.studio.core.dominio.cliente_pacote.repository;

import com.studio.core.dominio.cliente_pacote.entity.ClientePacote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClientePacoteRepository extends JpaRepository<ClientePacote, Long> {
    List<ClientePacote> findByCliente_Id(Long clienteId);
    List<ClientePacote> findByCliente_IdAndStatus(Long clienteId, ClientePacote.StatusClientePacote status);
    boolean existsByCliente_Id(Long clienteId);
    boolean existsByPacote_Id(Long pacoteId);
    boolean existsByCliente_IdAndPacote_Id(Long clienteId, Long pacoteId);
    List<ClientePacote> findByStatus(ClientePacote.StatusClientePacote status);
}
