package com.studio.core.service;

import com.studio.core.dominio.auditoria.entity.Auditoria;
import com.studio.core.dominio.auditoria.repository.AuditoriaRepository;
import com.studio.core.dominio.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void registrar(String entidade, Long entidadeId, String acao, String dadosAnteriores, String dadosNovos, Long usuarioId) {
        Auditoria auditoria = new Auditoria();
        auditoria.setEntidade(entidade);
        auditoria.setEntidadeId(entidadeId);
        auditoria.setAcao(acao);
        auditoria.setDadosAnteriores(dadosAnteriores);
        auditoria.setDadosNovos(dadosNovos);

        if (usuarioId != null) {
            usuarioRepository.findById(usuarioId).ifPresent(auditoria::setUsuario);
        } else {
            try {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                    usuarioRepository.findByEmail(auth.getName()).ifPresent(auditoria::setUsuario);
                }
            } catch (Exception ignored) {}
        }

        repository.save(auditoria);
    }

    @Transactional(readOnly = true)
    public List<Auditoria> findByEntidadeAndId(String entidade, Long entidadeId) {
        return repository.findByEntidadeAndEntidadeIdOrderByDataAcaoDesc(entidade, entidadeId);
    }

    @Transactional(readOnly = true)
    public List<Auditoria> findByEntidade(String entidade) {
        return repository.findByEntidadeOrderByDataAcaoDesc(entidade);
    }

    @Transactional(readOnly = true)
    public List<Auditoria> findByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findByDataAcaoBetweenOrderByDataAcaoDesc(inicio, fim);
    }
}
