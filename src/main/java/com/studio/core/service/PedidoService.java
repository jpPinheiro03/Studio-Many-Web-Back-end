package com.studio.core.service;

import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.pedido.dto.PedidoRequestDTO;
import com.studio.core.dominio.pedido.entity.Pedido;
import com.studio.core.dominio.pedido.repository.PedidoRepository;
import com.studio.core.dominio.produto.entity.Produto;
import com.studio.core.dominio.produto.repository.ProdutoRepository;
import com.studio.core.event.pedido.PedidoStatusAlteradoEvent;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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

    public List<Pedido> findPaginated(int page, int size) {
        List<Pedido> all = repository.findAll();
        int start = page * size;
        int end = Math.min(start + size, all.size());
        if (start >= all.size()) return List.of();
        return all.subList(start, end);
    }

    public Pedido create(PedidoRequestDTO dto) {
        Pedido pedido = new Pedido();

        var cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        pedido.setCliente(cliente);

        List<PedidoRequestDTO.PedidoItemRequestDTO> itens = dto.getItens();

        if (itens == null || itens.isEmpty()) {
            throw new BadRequestException("Pedido deve conter pelo menos um item");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (PedidoRequestDTO.PedidoItemRequestDTO itemDto : itens) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            pedido.addItem(produto, itemDto.getQuantidade());
            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade())));
        }

        pedido.setValorTotal(total);
        Pedido saved = repository.save(pedido);

        Long clienteId = saved.getCliente().getId();
        String clienteNome = saved.getCliente().getNome();
        
        eventPublisher.publishEvent(new PedidoStatusAlteradoEvent(this, saved.getId(),
            null, Pedido.StatusPedido.PENDENTE,
            clienteId, clienteNome, saved.getValorTotal()));

        return saved;
    }

    public Pedido updateStatus(Long id, Pedido.StatusPedido status) {
        Pedido pedido = findById(id);
        Pedido.StatusPedido statusAnterior = pedido.getStatus();
        pedido.setStatus(status);
        Pedido saved = repository.save(pedido);
        
        eventPublisher.publishEvent(new PedidoStatusAlteradoEvent(this, saved.getId(),
            statusAnterior, status,
            saved.getCliente().getId(), saved.getCliente().getNome(), saved.getValorTotal()));
        
        return saved;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
