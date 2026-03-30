package com.studio.core.event.agendamento;

import com.studio.core.event.BaseEvent;

import java.math.BigDecimal;

public class AgendamentoCanceladoComTaxaEvent extends BaseEvent {

    private final Long clienteId;
    private final String clienteNome;
    private final BigDecimal taxa;
    private final int percentual;
    private final long horasAntes;

    public AgendamentoCanceladoComTaxaEvent(Object source, Long agendamentoId,
                                             Long clienteId, String clienteNome,
                                             BigDecimal taxa, int percentual, long horasAntes) {
        super(source, "Agendamento", agendamentoId);
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.taxa = taxa;
        this.percentual = percentual;
        this.horasAntes = horasAntes;
    }

    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public BigDecimal getTaxa() { return taxa; }
    public int getPercentual() { return percentual; }
    public long getHorasAntes() { return horasAntes; }
}
