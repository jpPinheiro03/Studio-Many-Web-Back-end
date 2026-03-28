package com.studio.core.dominio.agendamento.controller;

import com.studio.core.dominio.agendamento.dto.AgendamentoRequestDTO;
import com.studio.core.dominio.agendamento.dto.AgendamentoResponseDTO;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.mapper.AgendamentoMapper;
import com.studio.core.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos", description = "Endpoints para gestão de agendamentos")
public class AgendamentoController {
    
    @Autowired
    private AgendamentoService service;
    
    @GetMapping
    @Operation(summary = "Listar agendamentos")
    public ResponseEntity<List<AgendamentoResponseDTO>> listar() {
        List<AgendamentoResponseDTO> dtos = service.findAll().stream()
            .map(AgendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoMapper.toResponse(service.findById(id)));
    }
    
    @PostMapping
    @Operation(summary = "Criar agendamento")
    public ResponseEntity<AgendamentoResponseDTO> criar(@Valid @RequestBody AgendamentoRequestDTO dto) {
        Agendamento entity = new Agendamento();
        entity.setDataHoraInicio(dto.getDataHoraInicio());
        entity.setDataHoraFim(dto.getDataHoraFim());
        entity.setValorTotal(dto.getValorTotal());
        entity.setValorSinal(dto.getValorSinal());
        entity.setQuantidadeParcelas(dto.getQuantidadeParcelas());
        entity.setObservacoes(dto.getObservacoes());
        
        Agendamento created = service.create(dto.getClienteId(), dto.getServicoId(), dto.getFuncionarioId(), entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(AgendamentoMapper.toResponse(created));
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status")
    public ResponseEntity<AgendamentoResponseDTO> atualizarStatus(
            @PathVariable Long id, 
            @RequestParam StatusAgendamento status) {
        return ResponseEntity.ok(AgendamentoMapper.toResponse(service.updateStatus(id, status)));
    }
    
    @PostMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar agendamento")
    public ResponseEntity<AgendamentoResponseDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoMapper.toResponse(service.updateStatus(id, StatusAgendamento.CONFIRMADO)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizar(@PathVariable Long id, @RequestBody AgendamentoRequestDTO dto) {
        Agendamento entity = service.findById(id);
        entity.setDataHoraInicio(dto.getDataHoraInicio());
        entity.setDataHoraFim(dto.getDataHoraFim());
        entity.setValorTotal(dto.getValorTotal());
        entity.setValorSinal(dto.getValorSinal());
        entity.setQuantidadeParcelas(dto.getQuantidadeParcelas());
        entity.setObservacoes(dto.getObservacoes());
        return ResponseEntity.ok(AgendamentoMapper.toResponse(service.update(id, entity)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/hoje")
    @Operation(summary = "Agendamentos de hoje")
    public ResponseEntity<List<AgendamentoResponseDTO>> hoje() {
        List<AgendamentoResponseDTO> dtos = service.findByData(LocalDate.now()).stream()
            .map(AgendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/periodo")
    public ResponseEntity<List<AgendamentoResponseDTO>> periodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<AgendamentoResponseDTO> dtos = service.findByPeriodo(inicio, fim).stream()
            .map(AgendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/status")
    @Operation(summary = "Agendamentos por status")
    public ResponseEntity<List<AgendamentoResponseDTO>> porStatus(@RequestParam StatusAgendamento status) {
        List<AgendamentoResponseDTO> dtos = service.findByStatus(status).stream()
            .map(AgendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/funcionario/{id}")
    @Operation(summary = "Agendamentos por funcionário")
    public ResponseEntity<List<AgendamentoResponseDTO>> porFuncionario(@PathVariable Long id) {
        List<AgendamentoResponseDTO> dtos = service.findByFuncionarioId(id).stream()
            .map(AgendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/cliente/{id}")
    @Operation(summary = "Agendamentos por cliente")
    public ResponseEntity<List<AgendamentoResponseDTO>> porCliente(@PathVariable Long id) {
        List<AgendamentoResponseDTO> dtos = service.findByClienteId(id).stream()
            .map(AgendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/proximos")
    @Operation(summary = "Próximos agendamentos")
    public ResponseEntity<List<AgendamentoResponseDTO>> proximos(@RequestParam(defaultValue = "7") int dias) {
        LocalDate hoje = LocalDate.now();
        List<AgendamentoResponseDTO> dtos = service.findByPeriodo(hoje, hoje.plusDays(dias)).stream()
            .map(AgendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/hoje/resumo")
    @Operation(summary = "Resumo do dia")
    public ResponseEntity<List<AgendamentoResponseDTO>> resumoDia() {
        List<AgendamentoResponseDTO> dtos = service.findByData(LocalDate.now()).stream()
            .map(AgendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar agendamento")
    public ResponseEntity<AgendamentoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoMapper.toResponse(service.updateStatus(id, StatusAgendamento.CANCELADO)));
    }
    
    @PutMapping("/{id}/nao-compareceu")
    @Operation(summary = "Não compareceu")
    public ResponseEntity<AgendamentoResponseDTO> naoCompareceu(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoMapper.toResponse(service.updateStatus(id, StatusAgendamento.NAO_COMPARECEU)));
    }
    
    @PutMapping("/{id}/encerrar")
    @Operation(summary = "Encerrar agendamento")
    public ResponseEntity<AgendamentoResponseDTO> encerrar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoMapper.toResponse(service.updateStatus(id, StatusAgendamento.CONCLUIDO)));
    }
}
