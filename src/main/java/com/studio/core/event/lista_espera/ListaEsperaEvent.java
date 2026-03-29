package com.studio.core.event.lista_espera;

import com.studio.core.dominio.lista_espera.entity.ListaEspera;
import com.studio.core.event.BaseEvent;

public class ListaEsperaEvent extends BaseEvent {

    private final ListaEspera.StatusListaEspera statusAnterior;
    private final ListaEspera.StatusListaEspera statusNovo;
    private final Long clienteId;
    private final String clienteNome;
    private final Long servicoId;
    private final String servicoNome;

    public ListaEsperaEvent(Object source, Long listaEsperaId,
                             ListaEspera.StatusListaEspera statusAnterior, ListaEspera.StatusListaEspera statusNovo,
                             Long clienteId, String clienteNome,
                             Long servicoId, String servicoNome) {
        super(source, "ListaEspera", listaEsperaId);
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.servicoId = servicoId;
        this.servicoNome = servicoNome;
    }

    public ListaEspera.StatusListaEspera getStatusAnterior() { return statusAnterior; }
    public ListaEspera.StatusListaEspera getStatusNovo() { return statusNovo; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public Long getServicoId() { return servicoId; }
    public String getServicoNome() { return servicoNome; }
}
