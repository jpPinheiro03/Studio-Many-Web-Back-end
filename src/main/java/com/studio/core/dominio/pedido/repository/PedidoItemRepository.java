package com.studio.core.dominio.pedido.repository;

import com.studio.core.dominio.pedido.entity.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
    boolean existsByProduto_Id(Long produtoId);
}
