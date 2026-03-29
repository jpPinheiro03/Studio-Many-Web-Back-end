-- ============================================================
-- V002: Regras de Negocio Avancadas no Banco
-- Tudo que o banco consegue fazer, fica no banco
-- O service fica apenas para: JWT, Auth, Upload, HTTP, Eventos
-- ============================================================

-- ============================================================
-- 1. TRIGGERS DE VALIDACAO DE TRANSICOES DE STATUS
-- ============================================================

DELIMITER $$

-- Agendamento: validar transicoes de status
CREATE TRIGGER trg_agendamento_validar_status
BEFORE UPDATE ON agendamentos
FOR EACH ROW
BEGIN
    -- Nao pode voltar de CONCLUIDO
    IF OLD.status = 'CONCLUIDO' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel alterar agendamento concluido';
    END IF;

    -- Nao pode voltar de CANCELADO
    IF OLD.status = 'CANCELADO' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel alterar agendamento cancelado';
    END IF;

    -- PENDENTE -> AGUARDANDO_CONFIRMACAO ou CANCELADO
    IF OLD.status = 'PENDENTE' AND NEW.status NOT IN ('AGUARDANDO_CONFIRMACAO', 'CANCELADO') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'De PENDENTE so pode ir para AGUARDANDO_CONFIRMACAO ou CANCELADO';
    END IF;

    -- AGUARDANDO_CONFIRMACAO -> CONFIRMADO ou CANCELADO
    IF OLD.status = 'AGUARDANDO_CONFIRMACAO' AND NEW.status NOT IN ('CONFIRMADO', 'CANCELADO') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'De AGUARDANDO_CONFIRMACAO so pode ir para CONFIRMADO ou CANCELADO';
    END IF;

    -- CONFIRMADO -> EM_ATENDIMENTO, NAO_COMPARECEU ou CANCELADO
    IF OLD.status = 'CONFIRMADO' AND NEW.status NOT IN ('EM_ATENDIMENTO', 'NAO_COMPARECEU', 'CANCELADO') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'De CONFIRMADO so pode ir para EM_ATENDIMENTO, NAO_COMPARECEU ou CANCELADO';
    END IF;

    -- EM_ATENDIMENTO -> CONCLUIDO, NAO_COMPARECEU ou CANCELADO
    IF OLD.status = 'EM_ATENDIMENTO' AND NEW.status NOT IN ('CONCLUIDO', 'NAO_COMPARECEU', 'CANCELADO') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'De EM_ATENDIMENTO so pode ir para CONCLUIDO, NAO_COMPARECEU ou CANCELADO';
    END IF;

    -- Auto-registrar timestamps conforme transicao
    IF OLD.status != NEW.status THEN
        IF NEW.status = 'CONFIRMADO' AND NEW.data_confirmacao_sinal IS NULL THEN
            SET NEW.data_confirmacao_sinal = NOW();
        END IF;
        IF NEW.status = 'EM_ATENDIMENTO' AND NEW.data_chegada IS NULL THEN
            SET NEW.data_chegada = NOW();
        END IF;
        IF NEW.status = 'CONCLUIDO' AND NEW.data_finalizacao IS NULL THEN
            SET NEW.data_finalizacao = NOW();
        END IF;
    END IF;
END$$

-- Parcela: validar transicao de status
CREATE TRIGGER trg_parcela_validar_status
BEFORE UPDATE ON parcelas
FOR EACH ROW
BEGIN
    IF OLD.status = 'QUITADA' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel alterar parcela ja quitada';
    END IF;

    IF OLD.status = 'CANCELADA' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel alterar parcela ja cancelada';
    END IF;

    -- Auto data_pagamento ao quitar
    IF OLD.status = 'PENDENTE' AND NEW.status = 'QUITADA' AND NEW.data_pagamento IS NULL THEN
        SET NEW.data_pagamento = CURDATE();
    END IF;
END$$

-- Comissao: validar transicao de status
CREATE TRIGGER trg_comissao_validar_status
BEFORE UPDATE ON comissoes
FOR EACH ROW
BEGIN
    IF OLD.status = 'PAGA' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel alterar comissao ja paga';
    END IF;

    -- Auto data_pagamento ao pagar
    IF OLD.status = 'PENDENTE' AND NEW.status = 'PAGA' AND NEW.data_pagamento IS NULL THEN
        SET NEW.data_pagamento = CURDATE();
    END IF;
END$$

-- Pedido: validar transicao de status
CREATE TRIGGER trg_pedido_validar_status
BEFORE UPDATE ON pedidos
FOR EACH ROW
BEGIN
    IF OLD.status = 'CANCELADO' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel alterar pedido cancelado';
    END IF;

    IF OLD.status = 'PAGO' AND NEW.status = 'PENDENTE' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel voltar pedido pago para pendente';
    END IF;
END$$

-- Cliente Pacote: validar transicao
CREATE TRIGGER trg_cliente_pacote_validar_status
BEFORE UPDATE ON cliente_pacotes
FOR EACH ROW
BEGIN
    IF OLD.status = 'USADO' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel alterar pacote ja totalmente usado';
    END IF;

    IF OLD.status = 'EXPIRADO' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel alterar pacote expirado';
    END IF;

    -- Validar sessoes usadas nao pode diminuir
    IF NEW.sessoes_usadas < OLD.sessoes_usadas THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sessoes usadas nao podem diminuir';
    END IF;
END$$

-- Lista Espera: validar transicao
CREATE TRIGGER trg_lista_espera_validar_status
BEFORE UPDATE ON lista_espera
FOR EACH ROW
BEGIN
    IF OLD.status = 'ATENDIDO' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel alterar item ja atendido';
    END IF;

    IF OLD.status = 'CANCELADO' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel alterar item ja cancelado';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 2. TRIGGER: Impedir agendamento no passado
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_agendamento_validar_data_insert
BEFORE INSERT ON agendamentos
FOR EACH ROW
BEGIN
    IF NEW.data_hora_inicio < NOW() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel criar agendamento no passado';
    END IF;

    -- Auto calcular data_fim se nao informada
    IF NEW.data_hora_fim IS NULL THEN
        SELECT DATE_ADD(NEW.data_hora_inicio, INTERVAL duracao_minutos MINUTE)
        INTO NEW.data_hora_fim
        FROM servicos WHERE id = NEW.servico_id;
    END IF;

    -- Auto status se nao informado
    IF NEW.status IS NULL THEN
        SET NEW.status = 'PENDENTE';
    END IF;

    SET NEW.data_criacao = NOW();
END$$

DELIMITER ;

-- ============================================================
-- 3. TRIGGER: Impedir conflito de horario no INSERT
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_agendamento_validar_conflito
BEFORE INSERT ON agendamentos
FOR EACH ROW
BEGIN
    DECLARE v_conflito INT DEFAULT 0;

    SELECT COUNT(*) INTO v_conflito
    FROM agendamentos
    WHERE funcionario_id = NEW.funcionario_id
      AND status NOT IN ('CANCELADO', 'NAO_COMPARECEU', 'REAGENDADO')
      AND data_hora_inicio < NEW.data_hora_fim
      AND data_hora_fim > NEW.data_hora_inicio;

    IF v_conflito > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Funcionario ja possui agendamento neste horario';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 4. TRIGGER: Impedir bloqueio no INSERT
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_agendamento_validar_bloqueio
BEFORE INSERT ON agendamentos
FOR EACH ROW
BEGIN
    DECLARE v_bloqueado INT DEFAULT 0;

    SELECT COUNT(*) INTO v_bloqueado
    FROM bloqueios
    WHERE funcionario_id = NEW.funcionario_id
      AND data_inicio < NEW.data_hora_fim
      AND data_fim > NEW.data_hora_inicio;

    IF v_bloqueado > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Horario bloqueado pelo funcionario';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 5. TRIGGER: Impedir entidades inativas no INSERT
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_agendamento_validar_ativos
BEFORE INSERT ON agendamentos
FOR EACH ROW
BEGIN
    DECLARE v_cliente_ativo BOOLEAN;
    DECLARE v_servico_ativo BOOLEAN;
    DECLARE v_func_ativo BOOLEAN;

    SELECT ativo INTO v_cliente_ativo FROM clientes WHERE id = NEW.cliente_id;
    SELECT ativo INTO v_servico_ativo FROM servicos WHERE id = NEW.servico_id;
    SELECT ativo INTO v_func_ativo FROM funcionarios WHERE id = NEW.funcionario_id;

    IF v_cliente_ativo = FALSE THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cliente esta inativo';
    END IF;

    IF v_servico_ativo = FALSE THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Servico esta inativo';
    END IF;

    IF v_func_ativo = FALSE THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Funcionario esta inativo';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 6. TRIGGER: Gerar movimento financeiro ao quitar parcela
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_parcela_gerar_movimento
AFTER UPDATE ON parcelas
FOR EACH ROW
BEGIN
    IF OLD.status = 'PENDENTE' AND NEW.status = 'QUITADA' THEN
        INSERT INTO movimentos (tipo, agendamento_id, valor, data_movimento, descricao, data_cadastro)
        VALUES ('RECEITA', NEW.agendamento_id, NEW.valor, CURDATE(),
                CONCAT('Recebimento parcela ', NEW.numero, ' do agendamento #', NEW.agendamento_id),
                NOW());
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 7. TRIGGER: Gerar movimento financeiro ao pagar comissao
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_comissao_gerar_movimento
AFTER UPDATE ON comissoes
FOR EACH ROW
BEGIN
    DECLARE v_func_nome VARCHAR(255);

    IF OLD.status = 'PENDENTE' AND NEW.status = 'PAGA' THEN
        SELECT nome INTO v_func_nome FROM funcionarios WHERE id = NEW.funcionario_id;

        INSERT INTO movimentos (tipo, agendamento_id, valor, data_movimento, descricao, data_cadastro)
        VALUES ('DESPESA', NEW.agendamento_id, NEW.valor, CURDATE(),
                CONCAT('Pagamento de comissao #', NEW.id, ' para ', v_func_nome),
                NOW());
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 8. TRIGGER: Impedir delete de cliente com dependencias
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_cliente_validar_delete
BEFORE DELETE ON clientes
FOR EACH ROW
BEGIN
    DECLARE v_agendamentos INT;
    DECLARE v_pedidos INT;
    DECLARE v_pacotes INT;

    SELECT COUNT(*) INTO v_agendamentos FROM agendamentos WHERE cliente_id = OLD.id;
    SELECT COUNT(*) INTO v_pedidos FROM pedidos WHERE cliente_id = OLD.id;
    SELECT COUNT(*) INTO v_pacotes FROM cliente_pacotes WHERE cliente_id = OLD.id;

    IF v_agendamentos > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel excluir cliente com agendamentos';
    END IF;

    IF v_pedidos > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel excluir cliente com pedidos';
    END IF;

    IF v_pacotes > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel excluir cliente com pacotes';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 9. TRIGGER: Impedir delete de funcionario com dependencias
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_funcionario_validar_delete
BEFORE DELETE ON funcionarios
FOR EACH ROW
BEGIN
    DECLARE v_agendamentos INT;
    DECLARE v_comissoes INT;

    SELECT COUNT(*) INTO v_agendamentos FROM agendamentos WHERE funcionario_id = OLD.id;
    SELECT COUNT(*) INTO v_comissoes FROM comissoes WHERE funcionario_id = OLD.id;

    IF v_agendamentos > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel excluir funcionario com agendamentos';
    END IF;

    IF v_comissoes > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel excluir funcionario com comissoes';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 10. TRIGGER: Impedir delete de servico com dependencias
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_servico_validar_delete
BEFORE DELETE ON servicos
FOR EACH ROW
BEGIN
    DECLARE v_agendamentos INT;
    DECLARE v_pacotes INT;

    SELECT COUNT(*) INTO v_agendamentos FROM agendamentos WHERE servico_id = OLD.id;
    SELECT COUNT(*) INTO v_pacotes FROM pacotes WHERE servico_id = OLD.id;

    IF v_agendamentos > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel excluir servico com agendamentos';
    END IF;

    IF v_pacotes > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel excluir servico com pacotes';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 11. TRIGGER: Impedir delete de produto com pedidos
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_produto_validar_delete
BEFORE DELETE ON produtos
FOR EACH ROW
BEGIN
    DECLARE v_itens INT;

    SELECT COUNT(*) INTO v_itens FROM pedido_itens WHERE produto_id = OLD.id;

    IF v_itens > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nao e possivel excluir produto com pedidos vinculados';
    END IF;
END$$

DELIMITER ;

-- ============================================================
-- 12. PROCEDURES DE RELATORIO
-- ============================================================

-- Relatorio de agendamentos por periodo
CREATE PROCEDURE sp_relatorio_agendamentos(
    IN p_inicio DATE,
    IN p_fim DATE
)
BEGIN
    SELECT
        COUNT(*) AS total,
        SUM(CASE WHEN status = 'CONFIRMADO' THEN 1 ELSE 0 END) AS confirmados,
        SUM(CASE WHEN status = 'PENDENTE' OR status = 'AGUARDANDO_CONFIRMACAO' THEN 1 ELSE 0 END) AS pendentes,
        SUM(CASE WHEN status = 'CANCELADO' THEN 1 ELSE 0 END) AS cancelados,
        SUM(CASE WHEN status = 'CONCLUIDO' THEN 1 ELSE 0 END) AS concluidos,
        SUM(CASE WHEN status = 'NAO_COMPARECEU' THEN 1 ELSE 0 END) AS nao_compareceu,
        SUM(CASE WHEN status = 'EM_ATENDIMENTO' THEN 1 ELSE 0 END) AS em_atendimento,
        SUM(CASE WHEN status = 'REAGENDADO' THEN 1 ELSE 0 END) AS reagendados,
        SUM(CASE WHEN status = 'CONCLUIDO' THEN valor_total ELSE 0 END) AS receita_total
    FROM agendamentos
    WHERE DATE(data_hora_inicio) BETWEEN p_inicio AND p_fim;
END$$

-- Ranking de servicos mais vendidos
CREATE PROCEDURE sp_ranking_servicos(
    IN p_inicio DATE,
    IN p_fim DATE
)
BEGIN
    SELECT
        s.id,
        s.nome,
        COUNT(a.id) AS total_agendamentos,
        SUM(CASE WHEN a.status = 'CONCLUIDO' THEN a.valor_total ELSE 0 END) AS receita_total,
        SUM(CASE WHEN a.status = 'CANCELADO' THEN 1 ELSE 0 END) AS cancelamentos
    FROM servicos s
    LEFT JOIN agendamentos a ON a.servico_id = s.id
        AND DATE(a.data_hora_inicio) BETWEEN p_inicio AND p_fim
    GROUP BY s.id, s.nome
    ORDER BY total_agendamentos DESC;
END$$

-- Ranking de funcionarios mais produtivos
CREATE PROCEDURE sp_ranking_funcionarios(
    IN p_inicio DATE,
    IN p_fim DATE
)
BEGIN
    SELECT
        f.id,
        f.nome,
        f.especialidade,
        COUNT(a.id) AS total_agendamentos,
        SUM(CASE WHEN a.status = 'CONCLUIDO' THEN 1 ELSE 0 END) AS concluidos,
        SUM(CASE WHEN a.status = 'CONCLUIDO' THEN a.valor_total ELSE 0 END) AS receita_gerada,
        SUM(CASE WHEN a.status = 'NAO_COMPARECEU' THEN 1 ELSE 0 END) AS no_shows
    FROM funcionarios f
    LEFT JOIN agendamentos a ON a.funcionario_id = f.id
        AND DATE(a.data_hora_inicio) BETWEEN p_inicio AND p_fim
    GROUP BY f.id, f.nome, f.especialidade
    ORDER BY concluidos DESC;
END$$

-- Controle de caixa diario
CREATE PROCEDURE sp_caixa_diario(
    IN p_data DATE
)
BEGIN
    SELECT
        p_data AS data,
        SUM(CASE WHEN tipo = 'RECEITA' THEN valor ELSE 0 END) AS entradas,
        SUM(CASE WHEN tipo = 'DESPESA' THEN valor ELSE 0 END) AS saidas,
        SUM(CASE WHEN tipo = 'RECEITA' THEN valor ELSE -valor END) AS saldo,
        COUNT(CASE WHEN tipo = 'RECEITA' THEN 1 END) AS qtd_receitas,
        COUNT(CASE WHEN tipo = 'DESPESA' THEN 1 END) AS qtd_despesas
    FROM movimentos
    WHERE data_movimento = p_data;
END$$

-- Parcelas vencidas
CREATE PROCEDURE sp_parcelas_vencidas()
BEGIN
    SELECT
        p.id, p.numero, p.valor, p.data_vencimento,
        c.nome AS cliente_nome, c.telefone AS cliente_telefone,
        a.id AS agendamento_id,
        DATEDIFF(CURDATE(), p.data_vencimento) AS dias_atraso
    FROM parcelas p
    JOIN agendamentos a ON p.agendamento_id = a.id
    JOIN clientes c ON a.cliente_id = c.id
    WHERE p.status = 'PENDENTE'
      AND p.data_vencimento < CURDATE()
    ORDER BY p.data_vencimento ASC;
END$$

-- Pacotes proximos do vencimento
CREATE PROCEDURE sp_pacotes_vencendo(
    IN p_dias INT
)
BEGIN
    SELECT
        cp.id, cp.sessoes_usadas, cp.data_validade,
        c.nome AS cliente_nome, c.telefone AS cliente_telefone,
        p.nome AS pacote_nome, p.quantidade_sessoes,
        (p.quantidade_sessoes - cp.sessoes_usadas) AS sessoes_restantes,
        DATEDIFF(cp.data_validade, CURDATE()) AS dias_restantes
    FROM cliente_pacotes cp
    JOIN clientes c ON cp.cliente_id = c.id
    JOIN pacotes p ON cp.pacote_id = p.id
    WHERE cp.status = 'ATIVO'
      AND cp.data_validade BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL p_dias DAY)
    ORDER BY cp.data_validade ASC;
END$$

-- Historico completo do cliente
CREATE PROCEDURE sp_historico_cliente(
    IN p_cliente_id BIGINT
)
BEGIN
    SELECT 'AGENDAMENTO' AS tipo, a.id, a.data_hora_inicio AS data, a.status,
           s.nome AS descricao, a.valor_total AS valor
    FROM agendamentos a JOIN servicos s ON a.servico_id = s.id
    WHERE a.cliente_id = p_cliente_id

    UNION ALL

    SELECT 'PEDIDO', pe.id, pe.data_pedido, pe.status,
           CONCAT('Pedido com ', COUNT(pi.id), ' itens'), pe.valor_total
    FROM pedidos pe JOIN pedido_itens pi ON pi.pedido_id = pe.id
    WHERE pe.cliente_id = p_cliente_id
    GROUP BY pe.id

    ORDER BY data DESC;
END$$

DELIMITER ;

-- ============================================================
-- 13. VIEWS DE PENDENCIAS
-- ============================================================

-- Tudo pendente por funcionario
CREATE OR REPLACE VIEW vw_pendencias_funcionarios AS
SELECT
    f.id AS funcionario_id,
    f.nome AS funcionario_nome,
    'AGENDAMENTO' AS tipo,
    a.id AS referencia_id,
    a.data_hora_inicio AS data,
    a.valor_total AS valor,
    a.status
FROM funcionarios f
JOIN agendamentos a ON a.funcionario_id = f.id
WHERE a.status IN ('PENDENTE', 'AGUARDANDO_CONFIRMACAO', 'CONFIRMADO')

UNION ALL

SELECT
    f.id, f.nome,
    'COMISSAO',
    c.id, c.data_comissao, c.valor, c.status
FROM funcionarios f
JOIN comissoes c ON c.funcionario_id = f.id
WHERE c.status = 'PENDENTE'

ORDER BY funcionario_nome, data;

-- Clientes com pacotes ativos
CREATE OR REPLACE VIEW vw_clientes_pacotes_ativos AS
SELECT
    c.id AS cliente_id,
    c.nome AS cliente_nome,
    c.telefone AS cliente_telefone,
    p.nome AS pacote_nome,
    cp.sessoes_usadas,
    p.quantidade_sessoes,
    (p.quantidade_sessoes - cp.sessoes_usadas) AS sessoes_restantes,
    cp.data_validade,
    DATEDIFF(cp.data_validade, CURDATE()) AS dias_restantes
FROM cliente_pacotes cp
JOIN clientes c ON cp.cliente_id = c.id
JOIN pacotes p ON cp.pacote_id = p.id
WHERE cp.status = 'ATIVO'
ORDER BY cp.data_validade ASC;

-- ============================================================
-- 14. INDICES DE PERFORMANCE
-- ============================================================

CREATE INDEX idx_agendamento_data ON agendamentos(data_hora_inicio, data_hora_fim);
CREATE INDEX idx_agendamento_status ON agendamentos(status);
CREATE INDEX idx_agendamento_func_data ON agendamentos(funcionario_id, data_hora_inicio);
CREATE INDEX idx_agendamento_cliente ON agendamentos(cliente_id);
CREATE INDEX idx_parcela_status ON parcelas(status, data_vencimento);
CREATE INDEX idx_comissao_func ON comissoes(funcionario_id, status);
CREATE INDEX idx_movimento_data ON movimentos(data_movimento, tipo);
CREATE INDEX idx_movimento_agendamento ON movimentos(agendamento_id);
CREATE INDEX idx_pedido_cliente ON pedidos(cliente_id);
CREATE INDEX idx_lista_espera_status ON lista_espera(status);
CREATE INDEX idx_cliente_pacote_status ON cliente_pacotes(status, data_validade);
CREATE INDEX idx_horario_func_dia ON horario_trabalho(funcionario_id, dia_semana);
CREATE INDEX idx_bloqueio_func ON bloqueios(funcionario_id, data_inicio, data_fim);

-- ============================================================
-- 15. FULL-TEXT SEARCH
-- ============================================================

ALTER TABLE clientes ADD FULLTEXT INDEX ft_cliente_busca (nome, email, telefone, cpf);
ALTER TABLE funcionarios ADD FULLTEXT INDEX ft_funcionario_busca (nome, email, especialidade);
ALTER TABLE servicos ADD FULLTEXT INDEX ft_servico_busca (nome, descricao);

DELIMITER $$

-- Busca inteligente de clientes
CREATE PROCEDURE sp_buscar_clientes(
    IN p_termo VARCHAR(255)
)
BEGIN
    SELECT id, nome, email, telefone, cpf,
           MATCH(nome, email, telefone, cpf) AGAINST(p_termo IN NATURAL LANGUAGE MODE) AS relevancia
    FROM clientes
    WHERE MATCH(nome, email, telefone, cpf) AGAINST(p_termo IN NATURAL LANGUAGE MODE)
    ORDER BY relevancia DESC
    LIMIT 20;
END$$

DELIMITER ;

-- ============================================================
-- 16. MYSQL EVENTS (Jobs nativos - alternativa ao Spring @Scheduled)
-- ============================================================

SET GLOBAL event_scheduler = ON;

-- Marcar parcelas vencidas todo dia as 1:00
CREATE EVENT IF NOT EXISTS evt_parcelas_vencidas
ON SCHEDULE EVERY 1 DAY
STARTS CONCAT(CURDATE() + INTERVAL 1 DAY, ' 01:00:00')
DO
    UPDATE parcelas
    SET status = 'VENCIDA'
    WHERE status = 'PENDENTE' AND data_vencimento < CURDATE();

-- Expirar pacotes todo dia as 2:00
CREATE EVENT IF NOT EXISTS evt_expirar_pacotes
ON SCHEDULE EVERY 1 DAY
STARTS CONCAT(CURDATE() + INTERVAL 1 DAY, ' 02:00:00')
DO
    UPDATE cliente_pacotes
    SET status = 'EXPIRADO'
    WHERE status = 'ATIVO' AND data_validade < CURDATE();

-- ============================================================
-- 17. UNIQUE CONSTRAINTS ADICIONAIS
-- ============================================================

ALTER TABLE clientes ADD CONSTRAINT uq_cliente_email UNIQUE (email);
ALTER TABLE clientes ADD CONSTRAINT uq_cliente_cpf UNIQUE (cpf);
ALTER TABLE funcionarios ADD CONSTRAINT uq_funcionario_email UNIQUE (email);
ALTER TABLE usuarios ADD CONSTRAINT uq_usuario_email UNIQUE (email);
