package com.studio.core.service;

import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.financeiro.entity.Movimento;
import com.studio.core.dominio.financeiro.repository.MovimentoRepository;
import com.studio.core.dominio.parcela.entity.Parcela;
import com.studio.core.dominio.parcela.repository.ParcelaRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParcelaServiceTest {

    @Mock private ParcelaRepository repository;
    @Mock private MovimentoRepository movimentoRepository;

    @InjectMocks
    private ParcelaService service;

    private Parcela parcela;

    @BeforeEach
    void setUp() {
        parcela = new Parcela();
        parcela.setId(1L);
        parcela.setValor(new BigDecimal("50.00"));
        parcela.setStatus(Parcela.StatusParcela.PENDENTE);
        parcela.setNumero(1);
        parcela.setDataVencimento(LocalDate.now().plusDays(30));
    }

    @Test
    void quitar_DeveMarcarComoQuitada() {
        when(repository.findById(1L)).thenReturn(Optional.of(parcela));
        when(repository.save(any())).thenReturn(parcela);

        var result = service.findById(1L);
        assertNotNull(result);

        service.quitar(1L);

        verify(repository).save(any());
        verify(movimentoRepository).save(any());
    }

    @Test
    void quitar_DeveLancarErro_QuandoJaQuitada() {
        parcela.setStatus(Parcela.StatusParcela.QUITADA);
        when(repository.findById(1L)).thenReturn(Optional.of(parcela));

        assertThrows(BadRequestException.class, () -> service.quitar(1L));
    }

    @Test
    void quitar_DeveLancarErro_QuandoNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.quitar(99L));
    }
}
