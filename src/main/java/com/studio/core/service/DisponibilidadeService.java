package com.studio.core.service;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.bloqueio.entity.Bloqueio;
import com.studio.core.dominio.bloqueio.repository.BloqueioRepository;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.horario_trabalho.entity.HorarioTrabalho;
import com.studio.core.dominio.horario_trabalho.repository.HorarioTrabalhoRepository;
import com.studio.core.dominio.servico.entity.Servico;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class DisponibilidadeService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private HorarioTrabalhoRepository horarioTrabalhoRepository;

    @Autowired
    private BloqueioRepository bloqueioRepository;

    @Transactional(readOnly = true)
    public List<LocalTime> calcularSlots(Long servicoId, Long funcionarioId, LocalDate data) {
        Servico servico = servicoRepository.findById(servicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        List<HorarioTrabalho> horarios = horarioTrabalhoRepository.findByFunc_IdAndDiaSemana(funcionarioId, data.getDayOfWeek().getValue());
        if (horarios.isEmpty()) {
            return Collections.emptyList();
        }

        List<Agendamento> agendamentos = agendamentoRepository.findByFuncionario_Id(funcionarioId);
        List<Bloqueio> bloqueios = bloqueioRepository.findByFunc_IdAndDataBetween(
            funcionarioId, data.atStartOfDay(), data.plusDays(1).atStartOfDay());

        List<LocalTime> disponiveis = new ArrayList<>();

        for (HorarioTrabalho ht : horarios) {
            LocalTime hora = ht.getHoraInicio();
            while (hora.plusMinutes(servico.getDuracaoMinutos()).compareTo(ht.getHoraFim()) <= 0) {
                LocalDateTime slotInicio = LocalDateTime.of(data, hora);
                LocalDateTime slotFim = slotInicio.plusMinutes(servico.getDuracaoMinutos());

                final LocalTime h = hora;
                boolean ocupado = agendamentos.stream()
                    .filter(a -> a.getDataHoraInicio().toLocalDate().equals(data))
                    .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO
                              && a.getStatus() != StatusAgendamento.NAO_COMPARECEU)
                    .anyMatch(a -> {
                        LocalTime aInicio = a.getDataHoraInicio().toLocalTime();
                        LocalTime aFim = a.getDataHoraFim().toLocalTime();
                        return h.isBefore(aFim) && h.plusMinutes(servico.getDuracaoMinutos()).isAfter(aInicio);
                    });

                boolean bloqueado = bloqueios.stream()
                    .anyMatch(b -> slotInicio.isBefore(b.getDataFim()) && slotFim.isAfter(b.getDataInicio()));

                if (!ocupado && !bloqueado) {
                    disponiveis.add(hora);
                }
                hora = hora.plusMinutes(30);
            }
        }

        return disponiveis;
    }

    @Transactional(readOnly = true)
    public boolean validarHorario(Long servicoId, Long funcionarioId, LocalDateTime dataHora) {
        Servico servico = servicoRepository.findById(servicoId)
            .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado"));

        LocalDateTime dataFim = dataHora.plusMinutes(servico.getDuracaoMinutos());

        List<Agendamento> agendamentos = agendamentoRepository.findByFuncionario_Id(funcionarioId);
        boolean ocupado = agendamentos.stream()
            .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO
                      && a.getStatus() != StatusAgendamento.NAO_COMPARECEU)
            .anyMatch(a -> dataHora.isBefore(a.getDataHoraFim()) && dataFim.isAfter(a.getDataHoraInicio()));

        List<HorarioTrabalho> horarios = horarioTrabalhoRepository.findByFunc_IdAndDiaSemana(
            funcionarioId, dataHora.getDayOfWeek().getValue());
        boolean noHorario = horarios.stream()
            .anyMatch(ht -> !dataHora.toLocalTime().isBefore(ht.getHoraInicio())
                        && !dataFim.toLocalTime().isAfter(ht.getHoraFim()));

        List<Bloqueio> bloqueios = bloqueioRepository.findByFunc_IdAndDataBetween(funcionarioId, dataHora, dataFim);
        boolean bloqueado = !bloqueios.isEmpty();

        return !ocupado && noHorario && !bloqueado;
    }

    @Transactional(readOnly = true)
    public List<LocalTime> horariosVagos(Long funcionarioId, LocalDate data) {
        List<HorarioTrabalho> horarios = horarioTrabalhoRepository.findByFunc_IdAndDiaSemana(funcionarioId, data.getDayOfWeek().getValue());
        if (horarios.isEmpty()) {
            return Collections.emptyList();
        }

        List<Agendamento> agendamentos = agendamentoRepository.findByFuncionario_Id(funcionarioId);
        List<LocalTime> vagos = new ArrayList<>();

        for (HorarioTrabalho ht : horarios) {
            LocalTime hora = ht.getHoraInicio();
            while (hora.compareTo(ht.getHoraFim()) < 0) {
                final LocalTime h = hora;
                boolean ocupado = agendamentos.stream()
                    .filter(a -> a.getDataHoraInicio().toLocalDate().equals(data))
                    .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO
                              && a.getStatus() != StatusAgendamento.NAO_COMPARECEU)
                    .anyMatch(a -> a.getDataHoraInicio().toLocalTime().equals(h));

                if (!ocupado) {
                    vagos.add(hora);
                }
                hora = hora.plusMinutes(30);
            }
        }

        return vagos;
    }

    @Transactional(readOnly = true)
    public List<Funcionario> profissionaisDisponiveis(Long servicoId, LocalDate data) {
        return funcionarioRepository.findAll();
    }
}
