package com.studio.core.dominio.funcionario_servico.repository;

import com.studio.core.dominio.funcionario_servico.entity.FuncionarioServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioServicoRepository extends JpaRepository<FuncionarioServico, Long> {
    List<FuncionarioServico> findByFunc_Id(Long funcId);
    List<FuncionarioServico> findByServico_Id(Long servicoId);
    Optional<FuncionarioServico> findByFunc_IdAndServico_Id(Long funcId, Long servicoId);
    boolean existsByFunc_IdAndServico_Id(Long funcId, Long servicoId);
}
