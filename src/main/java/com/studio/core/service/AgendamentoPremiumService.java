package com.studio.core.service;

import com.studio.core.dominio.agendamento.dto.AgendamentoRecorrenteRequestDTO;
import com.studio.core.dominio.agendamento.dto.CancelamentoPremiumRequestDTO;
import com.studio.core.dominio.agendamento.dto.HistoricoNoShowDTO;
import com.studio.core.dominio.agendamento.dto.ReagendamentoRequestDTO;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import com.studio.core.event.agendamento.AgendamentoCanceladoComTaxaEvent;
import com.studio.core.event.agendamento.AgendamentoReagendadoEvent;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AgendamentoPremiumService {

    @Autowired
    private AgendamentoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private DisponibilidadeService disponibilidadeService;

    public Agendamento reagendar(Long agendamentoId, ReagendamentoRequestDTO dto) {
        Agendamento original = agendamentoService.findById(agendamentoId);

        if (original.getStatus() != StatusAgendamento.PENDENTE
            && original.getStatus() != StatusAgendamento.AGUARDANDO_CONFIRMACAO
            && original.getStatus() != StatusAgendamento.CONFIRMADO) {
            throw new BadRequestException("Só é possível reagendar agendamentos PENDENTES, AGUARDANDO_CONFIRMACAO ou CONFIRMADOS");
        }

        if (original.getDataHoraInicio().isBefore(LocalDateTime.now().plusHours(24))) {
            throw new BadRequestException("Reagendamento deve ser feito com pelo menos 24 horas de antecedência");
        }

        int reagendamentos = original.getQuantidadeReagendamentos() != null ? original.getQuantidadeReagendamentos() : 0;
        if (reagendamentos >= 3) {
            throw new BadRequestException("Limite de 3 reagendamentos por agendamento atingido");
        }

        if (dto.getNovaDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Nova data deve ser futura");
        }

        if (dto.getNovaDataHoraInicio().isAfter(dto.getNovaDataHoraFim())) {
            throw new BadRequestException("Data de início deve ser anterior à data de fim");
        }

        List<Agendamento> conflitos = repository.findConflitoHorario(
            original.getFuncionario().getId(), dto.getNovaDataHoraInicio(), dto.getNovaDataHoraFim());
        if (!conflitos.isEmpty()) {
            throw new BadRequestException("Funcionário já possui agendamento no novo horário");
        }

        Agendamento novo = new Agendamento();
        novo.setCliente(original.getCliente());
        novo.setServico(original.getServico());
        novo.setFuncionario(original.getFuncionario());
        novo.setDataHoraInicio(dto.getNovaDataHoraInicio());
        novo.setDataHoraFim(dto.getNovaDataHoraFim());
        novo.setValorTotal(original.getValorTotal());
        novo.setValorSinal(original.getValorSinal());
        novo.setQuantidadeParcelas(original.getQuantidadeParcelas());
        novo.setComprovanteSinal(original.getComprovanteSinal());
        novo.setStatus(StatusAgendamento.PENDENTE);
        novo.setObservacoes("Reagendado do agendamento #" + agendamentoId + ". Motivo: " + dto.getMotivo());
        novo.setAgendamentoOriginalId(agendamentoId);
        novo.setQuantidadeReagendamentos(reagendamentos + 1);

        Agendamento novoSalvo = repository.save(novo);

        original.setStatus(StatusAgendamento.REAGENDADO);
        original.setMotivoCancelamento("Reagendado para " + dto.getNovaDataHoraInicio() + ". Motivo: " + dto.getMotivo());
        repository.save(original);

        auditoriaService.registrar("Agendamento", agendamentoId, "REAGENDAR",
            "status=" + original.getStatus(), "novoId=" + novoSalvo.getId(), null);

        eventPublisher.publishEvent(new AgendamentoReagendadoEvent(this,
            agendamentoId, original.getCliente().getId(), original.getCliente().getNome(),
            original.getCliente().getEmail(), original.getDataHoraInicio(),
            dto.getNovaDataHoraInicio(), dto.getMotivo(), reagendamentos + 1));

        return novoSalvo;
    }

    public BigDecimal calcularTaxaCancelamento(Long agendamentoId) {
        Agendamento agendamento = agendamentoService.findById(agendamentoId);
        long horasAte = java.time.Duration.between(LocalDateTime.now(), agendamento.getDataHoraInicio()).toHours();

        if (agendamento.getValorTotal() == null) return BigDecimal.ZERO;

        if (horasAte > 48) return BigDecimal.ZERO;
        if (horasAte > 24) return agendamento.getValorTotal().multiply(new BigDecimal("0.20"));
        if (horasAte > 12) return agendamento.getValorTotal().multiply(new BigDecimal("0.50"));
        return agendamento.getValorTotal();
    }

    public Agendamento cancelarComTaxa(Long agendamentoId, CancelamentoPremiumRequestDTO dto) {
        Agendamento agendamento = agendamentoService.findById(agendamentoId);

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new BadRequestException("Não é possível cancelar agendamento concluído");
        }
        if (agendamento.getStatus() == StatusAgendamento.CANCELADO || agendamento.getStatus() == StatusAgendamento.REAGENDADO) {
            throw new BadRequestException("Agendamento já está cancelado/reagendado");
        }

        BigDecimal taxa = calcularTaxaCancelamento(agendamentoId);
        long horasAte = java.time.Duration.between(LocalDateTime.now(), agendamento.getDataHoraInicio()).toHours();

        String dadosAnteriores = "status=" + agendamento.getStatus();

        agendamento.setTaxaCancelamento(taxa);
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamento.setMotivoCancelamento(dto.getMotivo());
        Agendamento salvo = repository.save(agendamento);

        auditoriaService.registrar("Agendamento", agendamentoId, "CANCELAR_COM_TAXA",
            dadosAnteriores, "status=CANCELADO, taxa=" + taxa, null);

        int percentual = 0;
        if (horasAte <= 12) percentual = 100;
        else if (horasAte <= 24) percentual = 50;
        else if (horasAte <= 48) percentual = 20;

        eventPublisher.publishEvent(new AgendamentoCanceladoComTaxaEvent(this,
            agendamentoId, salvo.getCliente().getId(), salvo.getCliente().getNome(),
            taxa, percentual, horasAte));

        return salvo;
    }

    @Transactional(readOnly = true)
    public HistoricoNoShowDTO historicoNoShows(Long clienteId) {
        List<Agendamento> todos = repository.findByCliente_Id(clienteId);

        long totalNoShows = todos.stream()
            .filter(a -> a.getStatus() == StatusAgendamento.NAO_COMPARECEU)
            .count();

        LocalDateTime ultimoNoShow = todos.stream()
            .filter(a -> a.getStatus() == StatusAgendamento.NAO_COMPARECEU)
            .map(Agendamento::getDataHoraInicio)
            .max(LocalDateTime::compareTo)
            .orElse(null);

        HistoricoNoShowDTO dto = new HistoricoNoShowDTO();
        dto.setClienteId(clienteId);
        dto.setClienteNome(todos.isEmpty() ? null : todos.get(0).getCliente().getNome());
        dto.setTotalNoShows((int) totalNoShows);
        dto.setTotalAgendamentos(todos.size());
        dto.setPercentualNoShow(todos.isEmpty() ? 0 : (int) ((totalNoShows * 100) / todos.size()));
        dto.setUltimoNoShow(ultimoNoShow);
        return dto;
    }

    @Transactional(readOnly = true)
    public boolean clienteBloqueadoPorNoShows(Long clienteId) {
        HistoricoNoShowDTO historico = historicoNoShows(clienteId);
        return historico.getPercentualNoShow() > 30 && historico.getTotalNoShows() >= 3;
    }

    @Transactional(readOnly = true)
    public List<LocalTime> sugerirHorariosProximos(Long servicoId, Long funcionarioId,
                                                     LocalDateTime dataDesejada, int diasRange) {
        List<LocalTime> sugestoes = new ArrayList<>();

        for (int i = 0; i <= diasRange; i++) {
            LocalDate data = dataDesejada.toLocalDate().plusDays(i);
            List<LocalTime> slots = disponibilidadeService.calcularSlots(servicoId, funcionarioId, data);

            LocalTime horaDesejada = dataDesejada.toLocalTime();
            for (LocalTime slot : slots) {
                long diffMinutos = Math.abs(java.time.Duration.between(horaDesejada, slot).toMinutes());
                if (diffMinutos <= 120) {
                    sugestoes.add(slot);
                }
            }
        }

        return sugestoes.stream().distinct().sorted().limit(10).toList();
    }

    public List<Agendamento> criarRecorrente(AgendamentoRecorrenteRequestDTO dto) {
        if (dto.getHoraInicio().isAfter(dto.getHoraFim())) {
            throw new BadRequestException("Hora de início deve ser anterior à hora de fim");
        }

        List<Agendamento> criados = new ArrayList<>();
        LocalDate data = dto.getDataInicio();

        while (!data.isAfter(dto.getDataFim())) {
            if (dto.getDiaSemana() != null && data.getDayOfWeek().getValue() != dto.getDiaSemana()) {
                data = data.plusDays(1);
                continue;
            }

            LocalDateTime inicio = LocalDateTime.of(data, dto.getHoraInicio());
            LocalDateTime fim = LocalDateTime.of(data, dto.getHoraFim());

            List<Agendamento> conflitos = repository.findConflitoHorario(
                dto.getFuncionarioId(), inicio, fim);

            if (conflitos.isEmpty()) {
                Agendamento agendamento = new Agendamento();
                agendamento.setCliente(clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado")));
                agendamento.setServico(servicoRepository.findById(dto.getServicoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado")));
                agendamento.setFuncionario(funcionarioRepository.findById(dto.getFuncionarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado")));
                agendamento.setDataHoraInicio(inicio);
                agendamento.setDataHoraFim(fim);
                agendamento.setValorTotal(dto.getValorTotal());
                agendamento.setStatus(StatusAgendamento.PENDENTE);
                agendamento.setRecorrente(true);
                agendamento.setFrequenciaRecorrencia(dto.getFrequencia());
                agendamento.setObservacoes("Agendamento recorrente " + dto.getFrequencia());

                criados.add(repository.save(agendamento));
            }

            switch (dto.getFrequencia()) {
                case "SEMANAL" -> data = data.plusWeeks(1);
                case "QUINZENAL" -> data = data.plusWeeks(2);
                case "MENSAL" -> data = data.plusMonths(1);
                default -> data = data.plusDays(1);
            }
        }

        return criados;
    }
}
