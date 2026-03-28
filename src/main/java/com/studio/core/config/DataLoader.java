package com.studio.core.config;

import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.funcionario.repository.FuncionarioRepository;
import com.studio.core.dominio.usuario.entity.Usuario;
import com.studio.core.dominio.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private FuncionarioRepository funcionarioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (funcionarioRepository.count() == 0) {
            Funcionario admin = new Funcionario();
            admin.setNome("Administrador");
            admin.setEmail("admin@studio.com");
            admin.setTelefone("11999999999");
            admin.setCpf("00000000000");
            admin.setEspecialidade("Gerência");
            admin.setAtivo(true);
            admin = funcionarioRepository.save(admin);
            
            Usuario usuario = new Usuario();
            usuario.setEmail("admin@studio.com");
            usuario.setSenha(passwordEncoder.encode("123456"));
            usuario.setRole(Usuario.Role.ADMIN);
            usuario.setFuncionario(admin);
            usuarioRepository.save(usuario);
            
            System.out.println("=== DADOS INICIAIS CRIADOS ===");
            System.out.println("Funcionário: Administrador (ID: " + admin.getId() + ")");
            System.out.println("Usuário: admin@studio.com / 123456");
        }
    }
}
