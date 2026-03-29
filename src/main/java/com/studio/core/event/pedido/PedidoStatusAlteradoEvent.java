package com.studio.core.event.pedido;

import com.studio.core.dominio.pedido.entity.Pedido;
import com.studio.core.event.BaseEvent;
import java.math.BigDecimal;

public class PedidoStatusAlteradoEvent extends BaseEvent {

    private final Pedido.StatusPedido statusAnterior;
    private final Pedido.StatusPedido statusNovo;
    private final Long clienteId;
    private final String clienteNome;
    private final BigDecimal valorTotal;

    public PedidoStatusAlteradoEvent(Object source, Long pedidoId,
                                      Pedido.StatusPedido statusAnterior, Pedido.StatusPedido statusNovo,
                                      Long clienteId, String clienteNome, BigDecimal valorTotal) {
        super(source, "Pedido", pedidoId);
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.valorTotal = valorTotal;
    }

    public Pedido.StatusPedido getStatusAnterior() { return statusAnterior; }
    public Pedido.StatusPedido getStatusNovo() { return statusNovo; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public BigDecimal getValorTotal() { return valorTotal; }
}
