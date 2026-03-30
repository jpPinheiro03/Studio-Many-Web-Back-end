package com.studio.core.event.listener;

import com.studio.core.event.agendamento.*;
import com.studio.core.event.cliente_pacote.SessaoUsadaEvent;
import com.studio.core.event.comissao.ComissaoPagaEvent;
import com.studio.core.event.lista_espera.ListaEsperaEvent;
import com.studio.core.event.parcela.ParcelaQuitadaEvent;
import com.studio.core.event.pedido.PedidoStatusAlteradoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);

    @EventListener
    @Async
    public void onAgendamentoCriado(AgendamentoCriadoEvent event) {
        log.info("[EVENTO] Agendamento #{} criado - Cliente: {} ({}), Serviço: {}, Data: {}",
            event.getEntidadeId(), event.getClienteNome(), event.getClienteEmail(),
            event.getServicoNome(), event.getDataHoraInicio());

        // TODO: Enviar notificação de confirmação de agendamento
        // notificationService.enviarConfirmacaoAgendamento(event);
    }

    @EventListener
    @Async
    public void onComprovanteEnviado(ComprovanteEnviadoEvent event) {
        log.info("[EVENTO] Comprovante enviado - Agendamento #{}, Cliente: {}",
            event.getEntidadeId(), event.getClienteNome());

        // TODO: Notificar funcionário que comprovante foi enviado
        // notificationService.notificarFuncionarioComprovante(event);
    }

    @EventListener
    @Async
    public void onStatusAlterado(AgendamentoStatusAlteradoEvent event) {
        log.info("[EVENTO] Status alterado - Agendamento #{}: {} → {}, Cliente: {}",
            event.getEntidadeId(), event.getStatusAnterior(), event.getStatusNovo(),
            event.getClienteNome());

        // TODO: Notificar cliente sobre mudança de status
        // notificationService.notificarMudancaStatus(event);
    }

    @EventListener
    @Async
    public void onClienteChegou(ClienteChegouEvent event) {
        log.info("[EVENTO] Cliente chegou - Agendamento #{}, Cliente: {}, Funcionário: {}",
            event.getEntidadeId(), event.getClienteNome(), event.getFuncionarioNome());

        // TODO: Notificar funcionário que cliente chegou
        // notificationService.notificarFuncionarioChegada(event);
    }

    @EventListener
    @Async
    public void onAgendamentoFinalizado(AgendamentoFinalizadoEvent event) {
        log.info("[EVENTO] Agendamento finalizado - #{}, Cliente: {}, Valor: R$ {}",
            event.getEntidadeId(), event.getClienteNome(), event.getValorTotal());

        // TODO: Enviar pesquisa de satisfação
        // notificationService.enviarPesquisSatisfacao(event);
    }

    @EventListener
    @Async
    public void onAgendamentoCancelado(AgendamentoCanceladoEvent event) {
        log.info("[EVENTO] Agendamento cancelado - #{}, Cliente: {}, Motivo: {}",
            event.getEntidadeId(), event.getClienteNome(), event.getMotivo());

        // TODO: Notificar cliente sobre cancelamento
        // notificationService.notificarCancelamento(event);
    }

    @EventListener
    @Async
    public void onParcelaQuitada(ParcelaQuitadaEvent event) {
        log.info("[EVENTO] Parcela quitada - #{}, Cliente: {}, Parcela: {}/{}, Valor: R$ {}",
            event.getEntidadeId(), event.getClienteNome(),
            event.getNumero(), event.getTotalParcelas(), event.getValor());

        // TODO: Enviar comprovante de pagamento
        // notificationService.enviarComprovantePagamento(event);
    }

    @EventListener
    @Async
    public void onComissaoPaga(ComissaoPagaEvent event) {
        log.info("[EVENTO] Comissão paga - #{}, Funcionário: {}, Valor: R$ {}",
            event.getEntidadeId(), event.getFuncionarioNome(), event.getValor());

        // TODO: Notificar funcionário sobre pagamento de comissão
        // notificationService.notificarFuncionarioComissao(event);
    }

    @EventListener
    @Async
    public void onPedidoStatusAlterado(PedidoStatusAlteradoEvent event) {
        log.info("[EVENTO] Pedido #{} status: {} → {}, Cliente: {}, Valor: R$ {}",
            event.getEntidadeId(), event.getStatusAnterior(), event.getStatusNovo(),
            event.getClienteNome(), event.getValorTotal());

        // TODO: Notificar cliente sobre status do pedido
        // notificationService.notificarPedidoStatus(event);
    }

    @EventListener
    @Async
    public void onSessaoUsada(SessaoUsadaEvent event) {
        log.info("[EVENTO] Sessão usada - Cliente: {}, Pacote: {}, Sessões: {}/{}",
            event.getClienteNome(), event.getPacoteNome(),
            event.getSessoesUsadas(), event.getTotalSessoes());

        // TODO: Notificar cliente sobre uso de sessão
        // notificationService.notificarSessaoUsada(event);
    }

    @EventListener
    @Async
    public void onListaEspera(ListaEsperaEvent event) {
        log.info("[EVENTO] Lista de espera #{}: {} → {}, Cliente: {}, Serviço: {}",
            event.getEntidadeId(), event.getStatusAnterior(), event.getStatusNovo(),
            event.getClienteNome(), event.getServicoNome());

        // TODO: Notificar sobre mudanças na lista de espera
        // notificationService.notificarListaEspera(event);
    }

    @EventListener
    @Async
    public void onAgendamentoReagendado(AgendamentoReagendadoEvent event) {
        log.info("[EVENTO] Agendamento #{} reagendado - Cliente: {}, De: {} Para: {}, Motivo: {}",
            event.getEntidadeId(), event.getClienteNome(),
            event.getDataOriginal(), event.getDataNova(), event.getMotivo());
        // TODO: Notificar cliente sobre reagendamento
    }

    @EventListener
    @Async
    public void onAgendamentoCanceladoComTaxa(AgendamentoCanceladoComTaxaEvent event) {
        log.info("[EVENTO] Agendamento #{} cancelado com taxa - Cliente: {}, Taxa: R$ {} ({}%), {}h antes",
            event.getEntidadeId(), event.getClienteNome(),
            event.getTaxa(), event.getPercentual(), event.getHorasAntes());
        // TODO: Notificar cliente sobre cancelamento com taxa
    }
}
