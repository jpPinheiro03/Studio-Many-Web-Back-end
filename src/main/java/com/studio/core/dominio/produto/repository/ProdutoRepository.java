package com.studio.core.dominio.produto.repository;

import com.studio.core.dominio.produto.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByAtivo(Boolean ativo);
    List<Produto> findByEstoqueLessThan(Integer limite);
}
