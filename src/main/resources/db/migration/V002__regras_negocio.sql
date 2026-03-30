-- ============================================================
-- V001: Regras de Negocio no Banco de Dados
-- Constraints, Triggers e Procedures
-- ============================================================

-- ============================================================
-- 1. CHECK CONSTRAINTS (validacoes de campo)
-- ============================================================

-- Servicos: preco e duracao devem ser positivos
ALTER TABLE servicos ADD CONSTRAINT chk_servico_preco CHECK (preco > 0);
ALTER TABLE servicos ADD CONSTRAINT chk_servico_duracao CHECK (duracao_minutos > 0);

-- Produtos: preco > 0, estoque >= 0
ALTER TABLE produtos ADD CONSTRAINT chk_produto_preco CHECK (preco > 0);
ALTER TABLE produtos ADD CONSTRAINT chk_produto_estoque CHECK (estoque >= 0);

-- Pacotes: sessoes, preco e validade positivos
ALTER TABLE pacotes ADD CONSTRAINT chk_pacote_sessoes CHECK (quantidade_sessoes > 0);
ALTER TABLE pacotes ADD CONSTRAINT chk_pacote_preco CHECK (preco > 0);
ALTER TABLE pacotes ADD CONSTRAINT chk_pacote_validade CHECK (validade_dias > 0);

-- Agendamentos: datas coerentes, valor positivo
ALTER TABLE agendamentos ADD CONSTRAINT chk_agendamento_datas CHECK (data_hora_fim > data_hora_inicio);
ALTER TABLE agendamentos ADD CONSTRAINT chk_agendamento_valor CHECK (valor_total > 0);

-- Parcelas: valor positivo
ALTER TABLE parcelas ADD CONSTRAINT chk_parcela_valor CHECK (valor > 0);

-- Comissoes: valor e percentual positivos
ALTER TABLE comissoes ADD CONSTRAINT chk_comissao_valor CHECK (valor > 0);
ALTER TABLE comissoes ADD CONSTRAINT chk_comissao_percentual CHECK (percentual >= 0 AND percentual <= 100);

-- Movimentos: valor positivo
ALTER TABLE movimentos ADD CONSTRAINT chk_movimento_valor CHECK (valor > 0);
ALTER TABLE movimentos ADD CONSTRAINT chk_movimento_tipo CHECK (tipo IN ('RECEITA', 'DESPESA'));

-- Horario de trabalho: hora_inicio < hora_fim
ALTER TABLE horario_trabalho ADD CONSTRAINT chk_horario_ordem CHECK (hora_fim > hora_inicio);
ALTER TABLE horario_trabalho ADD CONSTRAINT chk_horario_dia CHECK (dia_semana >= 1 AND dia_semana <= 7);

-- Bloqueios: data_inicio < data_fim
ALTER TABLE bloqueios ADD CONSTRAINT chk_bloqueio_datas CHECK (data_fim > data_inicio);

-- Pedido Itens: quantidade > 0
ALTER TABLE pedido_itens ADD CONSTRAINT chk_item_quantidade CHECK (quantidade > 0);

-- Agendamentos: status valido
ALTER TABLE agendamentos ADD CONSTRAINT chk_agendamento_status CHECK (
    status IN ('PENDENTE', 'AGUARDANDO_CONFIRMACAO', 'CONFIRMADO', 'EM_ATENDIMENTO',
               'CONCLUIDO', 'CANCELADO', 'NAO_COMPARECEU', 'REAGENDADO')
);

-- Parcelas: status valido
ALTER TABLE parcelas ADD CONSTRAINT chk_parcela_status CHECK (
    status IN ('PENDENTE', 'QUITADA', 'VENCIDA', 'CANCELADA')
);

-- Comissoes: status valido
ALTER TABLE comissoes ADD CONSTRAINT chk_comissao_status CHECK (
    status IN ('PENDENTE', 'PAGA')
);

-- Pedidos: status valido
ALTER TABLE pedidos ADD CONSTRAINT chk_pedido_status CHECK (
    status IN ('PENDENTE', 'PAGO', 'CANCELADO')
);

-- Cliente Pacotes: status valido
ALTER TABLE cliente_pacotes ADD CONSTRAINT chk_cliente_pacote_status CHECK (
    status IN ('ATIVO', 'USADO', 'EXPIRADO', 'CANCELADO')
);

-- Lista Espera: status valido
ALTER TABLE lista_espera ADD CONSTRAINT chk_lista_espera_status CHECK (
    status IN ('AGUARDANDO', 'ATENDIDO', 'CANCELADO')
);

-- Usuarios: role valido
ALTER TABLE usuarios ADD CONSTRAINT chk_usuario_role CHECK (
    role IN ('ADMIN', 'FUNCIONARIO')
);

-- Funcionario Servicos: percentual entre 0 e 100
ALTER TABLE funcionario_servicos ADD CONSTRAINT chk_fs_percentual CHECK (
    percentual_comissao >= 0 AND percentual_comissao <= 100
);

-- ============================================================
-- 2. TRIGGER: Auditoria automatica em agendamentos
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_agendamento_after_update
AFTER UPDATE ON agendamentos
FOR EACH ROW
BEGIN
    IF OLD.status != NEW.status THEN
        INSERT INTO auditoria (entidade, entidade_id, operacao, dados_anteriores, dados_novos, created_at)
        VALUES (
            'Agendamento',
            NEW.id,
            CONCAT('STATUS_', NEW.status),
            CONCAT('status=', OLD.status),
            CONCAT('status=', NEW.status),
            NOW()
        );
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 3. TRIGGER: Auditoria automatica em pedidos
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_pedido_after_update
AFTER UPDATE ON pedidos
FOR EACH ROW
BEGIN
    IF OLD.status != NEW.status THEN
        INSERT INTO auditoria (entidade, entidade_id, operacao, dados_anteriores, dados_novos, created_at)
        VALUES (
            'Pedido',
            NEW.id,
            CONCAT('STATUS_', NEW.status),
            CONCAT('status=', OLD.status),
            CONCAT('status=', NEW.status),
            NOW()
        );
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 4. TRIGGER: Debitar estoque ao pagar pedido
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_pedido_debitar_estoque
AFTER UPDATE ON pedidos
FOR EACH ROW
BEGIN
    IF OLD.status = 'PENDENTE' AND NEW.status = 'PAGO' THEN
        UPDATE produtos p
        JOIN pedido_itens pi ON pi.produto_id = p.id
        SET p.estoque = p.estoque - pi.quantidade
        WHERE pi.pedido_id = NEW.id;
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 5. TRIGGER: Devolver estoque ao cancelar pedido pago
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_pedido_devolver_estoque
AFTER UPDATE ON pedidos
FOR EACH ROW
BEGIN
    IF OLD.status = 'PAGO' AND NEW.status = 'CANCELADO' THEN
        UPDATE produtos p
        JOIN pedido_itens pi ON pi.produto_id = p.id
        SET p.estoque = p.estoque + pi.quantidade
        WHERE pi.pedido_id = NEW.id;
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 6. TRIGGER: Gerar movimento financeiro ao finalizar agendamento
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_agendamento_gerar_receita
AFTER UPDATE ON agendamentos
FOR EACH ROW
BEGIN
    IF OLD.status != 'CONCLUIDO' AND NEW.status = 'CONCLUIDO' AND NEW.valor_total > 0 THEN
        INSERT INTO movimentos (tipo, agendamento_id, valor, data_movimento, descricao, created_at)
        SELECT 'RECEITA', NEW.id, NEW.valor_total, CURDATE(),
               CONCAT('Receita do agendamento #', NEW.id), NOW()
        FROM DUAL
        WHERE NOT EXISTS (
            SELECT 1 FROM movimentos
            WHERE agendamento_id = NEW.id AND tipo = 'RECEITA'
        );
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 7. TRIGGER: Cancelar parcelas pendentes ao cancelar agendamento
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_agendamento_cancelar_parcelas
AFTER UPDATE ON agendamentos
FOR EACH ROW
BEGIN
    IF OLD.status != 'CANCELADO' AND NEW.status = 'CANCELADO' THEN
        UPDATE parcelas
        SET status = 'CANCELADA'
        WHERE agendamento_id = NEW.id AND status = 'PENDENTE';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 8. TRIGGER: Estorno financeiro ao cancelar agendamento com receita
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_agendamento_estorno
AFTER UPDATE ON agendamentos
FOR EACH ROW
BEGIN
    IF OLD.status != 'CANCELADO' AND NEW.status = 'CANCELADO' THEN
        INSERT INTO movimentos (tipo, agendamento_id, valor, data_movimento, descricao, created_at)
        SELECT 'DESPESA', NEW.id, m.valor, CURDATE(),
               CONCAT('Estorno por cancelamento do agendamento #', NEW.id), NOW()
        FROM movimentos m
        WHERE m.agendamento_id = NEW.id AND m.tipo = 'RECEITA';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 9. TRIGGER: Gerar comissao ao finalizar agendamento
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_agendamento_gerar_comissao
AFTER UPDATE ON agendamentos
FOR EACH ROW
BEGIN
    IF OLD.status != 'CONCLUIDO' AND NEW.status = 'CONCLUIDO' AND NEW.valor_total > 0 THEN
        INSERT INTO comissoes (funcionario_id, agendamento_id, valor, percentual, data_comissao, status)
        SELECT NEW.funcionario_id, NEW.id,
               NEW.valor_total * 30 / 100,
               30, CURDATE(), 'PENDENTE'
        FROM DUAL
        WHERE NOT EXISTS (
            SELECT 1 FROM comissoes
            WHERE agendamento_id = NEW.id
        );
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 10. TRIGGER: Expirar pacote quando sessoes esgotadas
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_cliente_pacote_expirar
AFTER UPDATE ON cliente_pacotes
FOR EACH ROW
BEGIN
    DECLARE total_sessoes INT;
    SELECT quantidade_sessoes INTO total_sessoes
    FROM pacotes WHERE id = NEW.pacote_id;

    IF NEW.sessoes_usadas >= total_sessoes AND NEW.status = 'ATIVO' THEN
        UPDATE cliente_pacotes SET status = 'USADO' WHERE id = NEW.id;
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 11. PROCEDURE: Gerar parcelas automaticamente
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_gerar_parcelas(
    IN p_agendamento_id BIGINT,
    IN p_quantidade INT,
    IN p_data_primeira DATE
)
BEGIN
    DECLARE v_valor_total DECIMAL(10,2);
    DECLARE v_valor_parcela DECIMAL(10,2);
    DECLARE v_i INT DEFAULT 1;

    SELECT valor_total INTO v_valor_total
    FROM agendamentos WHERE id = p_agendamento_id;

    SET v_valor_parcela = ROUND(v_valor_total / p_quantidade, 2);

    WHILE v_i <= p_quantidade DO
        INSERT INTO parcelas (agendamento_id, numero, valor, data_vencimento, status)
        VALUES (p_agendamento_id, v_i, v_valor_parcela,
                DATE_ADD(p_data_primeira, INTERVAL (v_i - 1) MONTH),
                'PENDENTE');
        SET v_i = v_i + 1;
    END WHILE;
END$$

DELIMITER ;

-- ============================================================
-- 12. PROCEDURE: Quitar todas parcelas de um agendamento
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_quitar_todas_parcelas(
    IN p_agendamento_id BIGINT
)
BEGIN
    UPDATE parcelas
    SET status = 'QUITADA', data_pagamento = CURDATE()
    WHERE agendamento_id = p_agendamento_id AND status = 'PENDENTE';
END$$

DELIMITER ;

-- ============================================================
-- 13. PROCEDURE: Pagar todas comissoes de um funcionario
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_pagar_comissoes_funcionario(
    IN p_funcionario_id BIGINT
)
BEGIN
    UPDATE comissoes
    SET status = 'PAGA', data_pagamento = CURDATE()
    WHERE funcionario_id = p_funcionario_id AND status = 'PENDENTE';
END$$

DELIMITER ;

-- ============================================================
-- 14. VIEW: Agendamentos do dia com dados completos
-- ============================================================

CREATE OR REPLACE VIEW vw_agendamentos_hoje AS
SELECT
    a.id,
    c.nome AS cliente_nome,
    c.telefone AS cliente_telefone,
    s.nome AS servico_nome,
    f.nome AS funcionario_nome,
    a.data_hora_inicio,
    a.data_hora_fim,
    a.status,
    a.valor_total
FROM agendamentos a
JOIN clientes c ON a.cliente_id = c.id
JOIN servicos s ON a.servico_id = s.id
JOIN funcionarios f ON a.funcionario_id = f.id
WHERE DATE(a.data_hora_inicio) = CURDATE()
ORDER BY a.data_hora_inicio;

-- ============================================================
-- 15. VIEW: Resumo financeiro mensal
-- ============================================================

CREATE OR REPLACE VIEW vw_resumo_financeiro AS
SELECT
    YEAR(data_movimento) AS ano,
    MONTH(data_movimento) AS mes,
    SUM(CASE WHEN tipo = 'RECEITA' THEN valor ELSE 0 END) AS receitas,
    SUM(CASE WHEN tipo = 'DESPESA' THEN valor ELSE 0 END) AS despesas,
    SUM(CASE WHEN tipo = 'RECEITA' THEN valor ELSE -valor END) AS saldo
FROM movimentos
GROUP BY YEAR(data_movimento), MONTH(data_movimento)
ORDER BY ano DESC, mes DESC;
