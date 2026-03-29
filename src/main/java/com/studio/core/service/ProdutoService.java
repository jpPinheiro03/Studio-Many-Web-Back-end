package com.studio.core.service;

import com.studio.core.dominio.pedido.repository.PedidoItemRepository;
import com.studio.core.dominio.produto.entity.Produto;
import com.studio.core.dominio.produto.repository.ProdutoRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;
    
    @Autowired
    private PedidoItemRepository pedidoItemRepository;
    
    public List<Produto> findAll() {
        return repository.findAll();
    }
    
    public Produto findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }
    
    public List<Produto> findAtivos() {
        return repository.findByAtivo(true);
    }
    
    public List<Produto> findEstoqueBaixo(int limite) {
        return repository.findByEstoqueLessThan(limite);
    }
    
    public Produto create(Produto produto) {
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new BadRequestException("Nome do produto é obrigatório");
        }
        if (produto.getPreco() != null && produto.getPreco().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Preço deve ser maior que zero");
        }
        if (produto.getEstoque() != null && produto.getEstoque() < 0) {
            throw new BadRequestException("Estoque não pode ser negativo");
        }
        return repository.save(produto);
    }
    
    public Produto update(Long id, Produto produto) {
        Produto existing = findById(id);
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new BadRequestException("Nome do produto é obrigatório");
        }
        if (produto.getPreco() != null && produto.getPreco().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Preço deve ser maior que zero");
        }
        if (produto.getEstoque() != null && produto.getEstoque() < 0) {
            throw new BadRequestException("Estoque não pode ser negativo");
        }
        existing.setNome(produto.getNome());
        existing.setDescricao(produto.getDescricao());
        existing.setPreco(produto.getPreco());
        existing.setEstoque(produto.getEstoque());
        existing.setAtivo(produto.getAtivo());
        return repository.save(existing);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }
        if (pedidoItemRepository.existsByProduto_Id(id)) {
            throw new BadRequestException("Não é possível excluir produto com pedidos vinculados");
        }
        repository.deleteById(id);
    }
}
