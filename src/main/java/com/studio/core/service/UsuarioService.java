package com.studio.core.service;

import com.studio.core.config.security.JwtUtil;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.usuario.dto.LoginRequestDTO;
import com.studio.core.dominio.usuario.dto.LoginResponseDTO;
import com.studio.core.dominio.usuario.dto.UsuarioRequestDTO;
import com.studio.core.dominio.usuario.dto.UsuarioResponseDTO;
import com.studio.core.dominio.usuario.entity.Usuario;
import com.studio.core.dominio.usuario.repository.UsuarioRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository repository;
    
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public List<UsuarioResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }
    
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        return UsuarioResponseDTO.fromEntity(usuario);
    }
    
    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email já cadastrado");
        }
        
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setRole(dto.getRole());
        
        if (dto.getFuncionarioId() != null) {
            Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + dto.getFuncionarioId()));
            usuario.setFuncionario(funcionario);
        }
        
        return UsuarioResponseDTO.fromEntity(repository.save(usuario));
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
        return UsuarioResponseDTO.fromEntity(usuario);
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
