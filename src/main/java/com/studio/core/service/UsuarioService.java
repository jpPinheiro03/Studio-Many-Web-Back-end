package com.studio.core.service;

import com.studio.core.config.security.JwtUtil;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.usuario.dto.LoginRequestDTO;
import com.studio.core.dominio.usuario.dto.LoginResponseDTO;
import com.studio.core.dominio.usuario.dto.UsuarioRequestDTO;
import com.studio.core.dominio.usuario.dto.UsuarioResponseDTO;
import com.studio.core.dominio.usuario.entity.Usuario;
import com.studio.core.dominio.usuario.mapper.UsuarioMapper;
import com.studio.core.dominio.usuario.repository.UsuarioRepository;
import com.studio.core.exception.ConflictRequestException;
import org.springframework.context.annotation.Lazy;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Esta classe contém lógica de negócio
@Transactional // Métodos rodam dentro de transação (commit/rollback)
public class UsuarioService {

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private UsuarioRepository repository;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private FuncionarioRepository funcionarioRepository;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private PasswordEncoder passwordEncoder;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private JwtUtil jwtUtil;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    @Lazy // Inicialização tardia (evita循环 dependency)
    private UsuarioMapper usuarioMapper;
    
    public List<UsuarioResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }
    
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        return usuarioMapper.toResponse(usuario);
    }
    
    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new ConflictRequestException("Email já cadastrado");
        }
        
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setRole(Usuario.Role.FUNCIONARIO);
        
        if (dto.getFuncionarioId() != null) {
            Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + dto.getFuncionarioId()));
            usuario.setFuncionario(funcionario);
        }
        
        return usuarioMapper.toResponse(repository.save(usuario));
    }
    
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {
        Usuario existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        
        if (dto.getEmail() != null && !dto.getEmail().equals(existing.getEmail())) {
            if (repository.existsByEmail(dto.getEmail())) {
                throw new BadRequestException("Email já cadastrado");
            }
            existing.setEmail(dto.getEmail());
        }
        
        if (dto.getFuncionarioId() != null) {
            Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
            existing.setFuncionario(funcionario);
        }
        
        return usuarioMapper.toResponse(repository.save(existing));
    }
    
    public void changePassword(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new BadRequestException("Senha atual incorreta");
        }
        
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        repository.save(usuario);
    }
    
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("Email ou senha inválidos"));
        
        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new BadRequestException("Email ou senha inválidos");
        }
        
        if (!usuario.getAtivo()) {
            throw new BadRequestException("Usuário inativo");
        }
        
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRole().name());
        return new LoginResponseDTO(token, usuario);
    }
    
    public UsuarioResponseDTO getCurrentUser(String email) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return usuarioMapper.toResponse(usuario);
    }
    
    public void delete(Long id) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        
        if (usuario.getRole() == Usuario.Role.ADMIN) {
            long totalAdmins = repository.countByRole(Usuario.Role.ADMIN);
            if (totalAdmins <= 1) {
                throw new BadRequestException("Não é possível excluir o último administrador do sistema");
            }
        }
        
        repository.deleteById(id);
    }
}
