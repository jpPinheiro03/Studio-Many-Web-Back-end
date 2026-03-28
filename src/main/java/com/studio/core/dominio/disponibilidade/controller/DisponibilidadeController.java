package com.studio.core.dominio.disponibilidade.controller;

import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.servico.entity.Servico;
import com.studio.core.dominio.servico.repository.ServicoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/disponibilidade")
@Tag(name = "Disponibilidade", description = "Endpoints para verificar disponibilidade")
public class DisponibilidadeController {
    
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    
    @Autowired
    private FuncionarioRepository FuncionarioRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    @GetMapping("/slots")
    @Operation(summary = "Verificar slots disponíveis")
    public ResponseEntity<List<LocalTime>> slots(
            @RequestParam Long servicoId,
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        Servico servico = servicoRepository.findById(servicoId).orElse(null);
        if (servico == null) return ResponseEntity.notFound().build();
        
        List<Agendamento> agendamentos = agendamentoRepository.findByFuncionario_Id(funcionarioId);
        List<LocalTime> disponiveis = new ArrayList<>();
        
        LocalTime hora = LocalTime.of(8, 0);
        LocalTime fim = LocalTime.of(20, 0);
        
        while (hora.plusMinutes(servico.getDuracaoMinutos()).compareTo(fim) <= 0) {
            final LocalTime h = hora;
            boolean ocupado = agendamentos.stream()
                .filter(a -> a.getDataHoraInicio().toLocalDate().equals(data))
                .anyMatch(a -> a.getDataHoraInicio().toLocalTime().equals(h));
            
            if (!ocupado) {
                disponiveis.add(hora);
            }
            hora = hora.plusMinutes(30);
        }
        
        return ResponseEntity.ok(disponiveis);
    }
    
    @GetMapping("/validar")
    @Operation(summary = "Validar horário")
    public ResponseEntity<Boolean> validar(
            @RequestParam Long servicoId,
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        
        List<Agendamento> agendamentos = agendamentoRepository.findByFuncionario_Id(funcionarioId);
        boolean ocupado = agendamentos.stream()
            .anyMatch(a -> a.getDataHoraInicio().equals(dataHora));
        
        return ResponseEntity.ok(!ocupado);
    }
    
    @GetMapping("/horarios-vagos")
    @Operation(summary = "Listar horários vagos")
    public ResponseEntity<List<LocalTime>> horariosVagos(
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        List<Agendamento> agendamentos = agendamentoRepository.findByFuncionario_Id(funcionarioId);
        List<LocalTime> vagos = new ArrayList<>();
        
        LocalTime hora = LocalTime.of(8, 0);
        LocalTime fim = LocalTime.of(20, 0);
        
        while (hora.compareTo(fim) < 0) {
            final LocalTime h = hora;
            boolean ocupado = agendamentos.stream()
                .filter(a -> a.getDataHoraInicio().toLocalDate().equals(data))
                .anyMatch(a -> a.getDataHoraInicio().toLocalTime().equals(h));
            
            if (!ocupado) {
                vagos.add(hora);
            }
            hora = hora.plusMinutes(30);
        }
        
        return ResponseEntity.ok(vagos);
    }
    
    @GetMapping("/profissionais")
    @Operation(summary = "Listar profissionais disponíveis")
    public ResponseEntity<List<Funcionario>> profissionais(
            @RequestParam Long servicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        return ResponseEntity.ok(FuncionarioRepository.findAll());
    }
}
