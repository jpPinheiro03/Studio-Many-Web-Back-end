package com.studio.core.service;

import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.pedido.entity.Pedido;
import com.studio.core.dominio.pedido.entity.PedidoItem;
import com.studio.core.dominio.pedido.repository.PedidoRepository;
import com.studio.core.dominio.produto.repository.ProdutoRepository;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository repository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    public List<Pedido> findAll() {
        return repository.findAll();
    }
    
    public Pedido findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }
    
    public List<Pedido> findByClienteId(Long clienteId) {
        return repository.findByCliente_Id(clienteId);
    }
    
    public Pedido create(Map<String, Object> params) {
        Pedido pedido = new Pedido();
        
        Long clienteId = Long.parseLong(params.get("clienteId").toString());
        pedido.setCliente(clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado")));
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itens = (List<Map<String, Object>>) params.get("itens");
        
        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> itemParams : itens) {
            Long produtoId = Long.parseLong(itemParams.get("produtoId").toString());
            Integer quantidade = (Integer) itemParams.get("quantidade");
            
            var produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
            
            pedido.addItem(produto, quantidade);
            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(quantidade)));
        }
        
        pedido.setValorTotal(total);
        return repository.save(pedido);
    }
    
    public Pedido updateStatus(Long id, Pedido.StatusPedido status) {
        Pedido pedido = findById(id);
        pedido.setStatus(status);
        return repository.save(pedido);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido não encontrado");
        }
        repository.deleteById(id);
    }
}
