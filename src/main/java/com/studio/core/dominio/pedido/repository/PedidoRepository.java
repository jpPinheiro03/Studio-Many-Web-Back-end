package com.studio.core.dominio.pedido.repository;

import com.studio.core.dominio.pedido.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCliente_Id(Long clienteId);
}
