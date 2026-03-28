package com.studio.core.dominio.auditoria.controller;

import com.studio.core.dominio.auditoria.entity.Auditoria;
import com.studio.core.dominio.auditoria.repository.AuditoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@Tag(name = "Auditoria", description = "Endpoints para auditoria")
public class AuditoriaController {
    
    @Autowired
    private AuditoriaRepository repository;
    
    @GetMapping("/{entidade}/{id}")
    @Operation(summary = "Auditoria por entidade e ID")
    public ResponseEntity<List<Auditoria>> porEntidadeEId(
            @PathVariable String entidade,
            @PathVariable Long id) {
        return ResponseEntity.ok(repository.findByEntidadeAndEntidadeIdOrderByDataAcaoDesc(entidade, id));
    }
    
    @GetMapping("/entidade/{entidade}")
    @Operation(summary = "Auditoria por tipo de entidade")
    public ResponseEntity<List<Auditoria>> porEntidade(@PathVariable String entidade) {
        return ResponseEntity.ok(repository.findByEntidadeOrderByDataAcaoDesc(entidade));
    }
    
    @GetMapping("/periodo")
    @Operation(summary = "Auditoria por período")
    public ResponseEntity<List<Auditoria>> periodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(repository.findByDataAcaoBetweenOrderByDataAcaoDesc(inicio, fim));
    }
}
