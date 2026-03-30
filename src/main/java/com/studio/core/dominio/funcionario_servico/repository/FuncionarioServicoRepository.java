package com.studio.core.dominio.funcionario_servico.repository;

import com.studio.core.dominio.funcionario_servico.entity.FuncionarioServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioServicoRepository extends JpaRepository<FuncionarioServico, Long> {
    List<FuncionarioServico> findByFuncionario_Id(Long funcId);
    List<FuncionarioServico> findByServico_Id(Long servicoId);
    Optional<FuncionarioServico> findByFuncionario_IdAndServico_Id(Long funcId, Long servicoId);
    boolean existsByFuncionario_IdAndServico_Id(Long funcId, Long servicoId);
    boolean existsByServico_Id(Long servicoId);
    boolean existsByFuncionario_Id(Long funcId);
}
