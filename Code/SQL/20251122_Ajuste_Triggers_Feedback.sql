USE TGControl;

-- 1. APAGAR OS GATILHOS ANTIGOS (Isso remove o conflito)
DROP TRIGGER IF EXISTS trg_unlock_next_task_on_complete; -- Esse é o que causava o erro
DROP TRIGGER IF EXISTS trg_complete_task_on_approval;    -- Vamos recriar este melhorado

-- 2. CRIAR O NOVO GATILHO UNIFICADO (Faz tudo de uma vez)
DELIMITER //

CREATE TRIGGER trg_complete_task_on_approval AFTER INSERT ON task_review FOR EACH ROW
BEGIN
    DECLARE next_sequence INT;
    DECLARE next_task_estagio INT;
    DECLARE student_current_estagio INT;

    -- Só executa se o professor aprovou
    IF NEW.status = 'approved' THEN
        
        -- AÇÃO 1: Marca a tarefa ATUAL como concluída
        UPDATE task 
        SET status = 'completed'
        WHERE student_email = NEW.student_email 
          AND sequence_order = NEW.sequence_order 
          AND status <> 'completed';

        -- AÇÃO 2: Lógica para desbloquear a PRÓXIMA tarefa
        SET next_sequence = NEW.sequence_order + 1;
        
        -- Verifica qual é o estágio da próxima tarefa
        SELECT estagio_task INTO next_task_estagio 
        FROM task 
        WHERE student_email = NEW.student_email AND sequence_order = next_sequence;
        
        -- Verifica qual o estágio atual do aluno
        IF next_task_estagio IS NOT NULL THEN
            SELECT estagio_tg_atual INTO student_current_estagio 
            FROM student 
            WHERE email = NEW.student_email;
            
            -- Se o aluno já estiver no estágio correto, desbloqueia a próxima
            IF next_task_estagio <= student_current_estagio THEN
                UPDATE task 
                SET status = 'in_progress' 
                WHERE student_email = NEW.student_email 
                  AND sequence_order = next_sequence 
                  AND status = 'locked';
            END IF;
        END IF;

    END IF;
END //

DELIMITER ;