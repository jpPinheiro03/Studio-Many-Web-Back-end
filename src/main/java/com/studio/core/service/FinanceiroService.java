package com.studio.core.service;

import com.studio.core.dominio.financeiro.entity.Movimento;
import com.studio.core.dominio.financeiro.repository.MovimentoRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FinanceiroService {

    @Autowired
    private MovimentoRepository repository;
    
    public List<Movimento> findAll() {
        return repository.findAll();
    }
    
    public Movimento findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movimento não encontrado"));
    }
    
    public List<Movimento> findByData(LocalDate data) {
        return repository.findByDataMovimento(data);
    }
    
    public List<Movimento> findByPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.findByDataMovimentoBetween(inicio, fim);
    }
    
    public BigDecimal calcularSaldo(LocalDate inicio, LocalDate fim) {
        return repository.calcularSaldo(inicio, fim);
    }
    
    public BigDecimal totalReceitas(LocalDate inicio, LocalDate fim) {
        return repository.totalReceitas(inicio, fim);
    }
    
    public BigDecimal totalDespesas(LocalDate inicio, LocalDate fim) {
        return repository.totalDespesas(inicio, fim);
    }
    
    public Movimento create(Movimento movimento) {
        if (movimento.getTipo() == null) {
            throw new BadRequestException("Tipo do movimento é obrigatório (RECEITA ou DESPESA)");
        }
        if (movimento.getValor() == null || movimento.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Valor do movimento deve ser maior que zero");
        }
        if (movimento.getDataMovimento() == null) {
            movimento.setDataMovimento(LocalDate.now());
        }
        return repository.save(movimento);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Movimento não encontrado");
        }
        repository.deleteById(id);
    }
    
    public List<Movimento> findContasReceber() {
        return repository.findByTipo(Movimento.TipoMovimento.RECEITA);
    }
    
    public List<Movimento> findContasPagar() {
        return repository.findByTipo(Movimento.TipoMovimento.DESPESA);
    }
}
