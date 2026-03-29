package com.studio.core.service;

import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.bloqueio.repository.BloqueioRepository;
import com.studio.core.dominio.comissao.repository.ComissaoRepository;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.funcionario_servico.repository.FuncionarioServicoRepository;
import com.studio.core.dominio.horario_trabalho.repository.HorarioTrabalhoRepository;
import com.studio.core.dominio.usuario.repository.UsuarioRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;
    
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    
    @Autowired
    private ComissaoRepository comissaoRepository;
    
    @Autowired
    private HorarioTrabalhoRepository horarioTrabalhoRepository;
    
    @Autowired
    private BloqueioRepository bloqueioRepository;
    
    @Autowired
    private FuncionarioServicoRepository funcionarioServicoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Funcionario> findAll() {
        return repository.findAll();
    }
    
    public Funcionario findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));
    }
    
    public Funcionario create(Funcionario funcionario) {
        if (funcionario.getEmail() != null && !funcionario.getEmail().isBlank()) {
            if (repository.existsByEmail(funcionario.getEmail())) {
                throw new BadRequestException("Email já cadastrado para outro funcionário");
            }
        }
        if (funcionario.getDataCadastro() == null) {
            funcionario.setDataCadastro(LocalDateTime.now());
        }
        return repository.save(funcionario);
    }
    
    public Funcionario update(Long id, Funcionario funcionario) {
        Funcionario existing = findById(id);
        if (funcionario.getEmail() != null && !funcionario.getEmail().equals(existing.getEmail())) {
            if (repository.existsByEmail(funcionario.getEmail())) {
                throw new BadRequestException("Email já cadastrado para outro funcionário");
            }
        }
        existing.setNome(funcionario.getNome());
        existing.setEmail(funcionario.getEmail());
        existing.setTelefone(funcionario.getTelefone());
        existing.setCpf(funcionario.getCpf());
        existing.setEspecialidade(funcionario.getEspecialidade());
        if (funcionario.getAtivo() != null) {
            existing.setAtivo(funcionario.getAtivo());
        }
        return repository.save(existing);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Funcionário não encontrado com ID: " + id);
        }
        if (agendamentoRepository.existsByFuncionario_Id(id)) {
            throw new BadRequestException("Não é possível excluir funcionário com agendamentos vinculados");
        }
        if (comissaoRepository.existsByFunc_Id(id)) {
            throw new BadRequestException("Não é possível excluir funcionário com comissões vinculadas");
        }
        if (horarioTrabalhoRepository.existsByFunc_Id(id)) {
            throw new BadRequestException("Não é possível excluir funcionário com horário de trabalho cadastrado");
        }
        if (bloqueioRepository.existsByFunc_Id(id)) {
            throw new BadRequestException("Não é possível excluir funcionário com bloqueios cadastrados");
        }
        if (funcionarioServicoRepository.existsByFunc_Id(id)) {
            throw new BadRequestException("Não é possível excluir funcionário com serviços vinculados");
        }
        repository.deleteById(id);
    }
}
