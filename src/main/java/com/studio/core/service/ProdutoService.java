package com.studio.core.service;

import com.studio.core.dominio.produto.entity.Produto;
import com.studio.core.dominio.produto.repository.ProdutoRepository;
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
        return repository.save(produto);
    }
    
    public Produto update(Long id, Produto produto) {
        Produto existing = findById(id);
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
        repository.deleteById(id);
    }
}
