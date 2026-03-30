package com.studio.core.dominio.lista_espera.repository;

import com.studio.core.dominio.lista_espera.entity.ListaEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Long> {
    List<ListaEspera> findByStatus(ListaEspera.StatusListaEspera status);
    List<ListaEspera> findByStatusOrderByDataCadastroAsc(ListaEspera.StatusListaEspera status);
    boolean existsByCliente_IdAndServico_IdAndStatus(Long clienteId, Long servicoId, ListaEspera.StatusListaEspera status);
    boolean existsByCliente_Id(Long clienteId);
    boolean existsByServico_Id(Long servicoId);
}
