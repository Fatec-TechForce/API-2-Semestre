-- Remove o banco de dados se ele já existir, para garantir um script limpo
DROP DATABASE IF EXISTS TGControl;
CREATE DATABASE TGControl;
USE TGControl;

-- ========================================================================== --
-- TABELAS PRINCIPAIS                                                         --
-- ========================================================================== --

-- Tabela: Armazena informações básicas de todos os usuários (alunos e professores).
-- PK: email
CREATE TABLE IF NOT EXISTS `user` (
                                      `email`               VARCHAR(255) NOT NULL,
    `FirstName`           VARCHAR(255) NOT NULL,
    `LastName`            VARCHAR(255) NOT NULL,
    `passwordHASH`        VARCHAR(255) NOT NULL,
    `profile_picture_url` VARCHAR(255),
    `status`              ENUM('Active', 'Inactive') NOT NULL DEFAULT 'Active',
    PRIMARY KEY(`email`)
    );

-- Tabela: Especialização da tabela user para professores. Indica se é Coordenador TG.
-- PK: email (FK para user)
CREATE TABLE IF NOT EXISTS `teacher` (
                                         `email`          VARCHAR(255) NOT NULL,
    `is_coordinator` BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY(`email`),
    FOREIGN KEY (`email`) REFERENCES `user`(`email`) ON DELETE CASCADE
    );

-- Tabela: Define as turmas baseadas na disciplina, ano e semestre de início.
-- PK: (disciplina, year, semester)
CREATE TABLE IF NOT EXISTS `class` (
                                       `disciplina` VARCHAR(255) NOT NULL,
    `year`       INT NOT NULL,
    `semester`   INT NOT NULL,
    `min_tasks`  INT NOT NULL DEFAULT 1,
    `max_tasks`  INT,
    PRIMARY KEY(`disciplina`, `year`, `semester`),
    CHECK(`semester` BETWEEN 1 AND 2),
    CHECK(`max_tasks` IN (5, 6))
    );

-- Tabela: Especialização da tabela user para alunos, ligando-os a uma turma e orientador. Armazena o estágio atual do TG.
-- PK: email (FK para user)
CREATE TABLE IF NOT EXISTS `student` (
                                         `email`                  VARCHAR(255) NOT NULL,
    `personal_email`         VARCHAR(255) NULL,
    `advisor_email`          VARCHAR(255),
    `agreement_document_url` VARCHAR(255) NULL,
    `class_disciplina`       VARCHAR(255) NOT NULL,
    `class_year`             INT NOT NULL,
    `class_semester`         INT NOT NULL,
    `estagio_tg_atual`       INT NOT NULL DEFAULT 1,
    PRIMARY KEY(`email`),
    FOREIGN KEY (`email`) REFERENCES `user`(`email`) ON DELETE CASCADE,
    FOREIGN KEY (`advisor_email`) REFERENCES `teacher`(`email`) ON DELETE NO ACTION,
    FOREIGN KEY (`class_disciplina`, `class_year`, `class_semester`) REFERENCES `class`(`disciplina`, `year`, `semester`) ON DELETE RESTRICT
    );

-- Tabela: Define as seções individuais do TG para cada aluno, incluindo a qual estágio pertencem.
-- PK: (student_email, sequence_order)
CREATE TABLE IF NOT EXISTS `task` (
                                      `student_email`  VARCHAR(255) NOT NULL,
    `sequence_order` INT NOT NULL,
    `title`          VARCHAR(255) NOT NULL,
    `description`    TEXT,
    `due_date`       DATE NOT NULL,
    `status`         ENUM('locked', 'in_progress', 'completed') NOT NULL DEFAULT 'locked',
    `estagio_task`   INT NOT NULL DEFAULT 1,
    PRIMARY KEY(`student_email`, `sequence_order`),
    FOREIGN KEY (`student_email`) REFERENCES `student`(`email`) ON DELETE CASCADE
    );

-- Tabela: Registra cada entrega (versão) de uma seção feita pelo aluno.
-- PK: (student_email, sequence_order, submission_timestamp)
CREATE TABLE IF NOT EXISTS `task_submission` (
                                                 `student_email`        VARCHAR(255) NOT NULL,
    `sequence_order`       INT NOT NULL,
    `submission_timestamp` DATETIME NOT NULL DEFAULT NOW(),
    `file_path`            VARCHAR(255) NOT NULL,
    `submission_title`     VARCHAR(255),
    `attempt_number`       INT NOT NULL,
    PRIMARY KEY(`student_email`, `sequence_order`, `submission_timestamp`),
    FOREIGN KEY (`student_email`, `sequence_order`) REFERENCES `task`(`student_email`, `sequence_order`) ON DELETE CASCADE
    );

-- Tabela: Registra a avaliação feita por um professor sobre uma entrega específica, com comentário geral.
-- PK: (student_email, sequence_order, submission_timestamp, reviewer_email, review_timestamp)
CREATE TABLE IF NOT EXISTS `task_review` (
                                             `student_email`        VARCHAR(255) NOT NULL,
    `sequence_order`       INT NOT NULL,
    `submission_timestamp` DATETIME NOT NULL, -- FK para a submissão específica
    `reviewer_email`       VARCHAR(255) NOT NULL,
    `review_timestamp`     DATETIME NOT NULL DEFAULT NOW(),
    `status`               ENUM('revision_requested', 'approved') NOT NULL, -- Decisão da revisão
    `review_comment`       TEXT NULL DEFAULT NULL, -- Comentário geral do professor
    PRIMARY KEY(`student_email`, `sequence_order`, `submission_timestamp`, `reviewer_email`, `review_timestamp`),
    FOREIGN KEY (`student_email`, `sequence_order`, `submission_timestamp`)
    REFERENCES `task_submission`(`student_email`, `sequence_order`, `submission_timestamp`) ON DELETE CASCADE,
    FOREIGN KEY (`reviewer_email`) REFERENCES `teacher`(`email`) ON DELETE NO ACTION
    );

-- Tabela: Armazena notificações geradas para os usuários.
-- PK: notification_id (AUTO_INCREMENT)
CREATE TABLE IF NOT EXISTS `notification` (
                                              `notification_id`             INT NOT NULL AUTO_INCREMENT,
                                              `user_email`                  VARCHAR(255) NOT NULL,
    `timestamp`                   DATETIME NOT NULL DEFAULT NOW(),
    `content`                     TEXT NOT NULL,
    `related_task_student_email`  VARCHAR(255),
    `related_task_sequence_order` INT,
    `is_read`                     BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY(`notification_id`),
    FOREIGN KEY (`user_email`) REFERENCES `user`(`email`) ON DELETE CASCADE,
    FOREIGN KEY (`related_task_student_email`, `related_task_sequence_order`) REFERENCES `task`(`student_email`, `sequence_order`) ON DELETE SET NULL,
    INDEX `idx_notification_user_time` (`user_email`, `timestamp`)
    );

-- Tabela: Associa um Professor TG a uma turma específica e define qual etapa ele coordena.
-- PK: (teacher_email, class_disciplina, class_year, class_semester, etapa_supervisionada)
CREATE TABLE IF NOT EXISTS `tg_coordenacao_turma` (
                                                      `teacher_email`        VARCHAR(255) NOT NULL,
    `class_disciplina`     VARCHAR(255) NOT NULL,
    `class_year`           INT NOT NULL,
    `class_semester`       INT NOT NULL,
    `etapa_supervisionada` INT NOT NULL,
    PRIMARY KEY(`teacher_email`, `class_disciplina`, `class_year`, `class_semester`, `etapa_supervisionada`),
    FOREIGN KEY (`teacher_email`) REFERENCES `teacher`(`email`) ON DELETE NO ACTION,
    FOREIGN KEY (`class_disciplina`, `class_year`, `class_semester`) REFERENCES `class`(`disciplina`, `year`, `semester`) ON DELETE CASCADE,
    CHECK (`etapa_supervisionada` IN (1, 2))
    );

-- Tabela: Armazena os agendamentos de defesas de TG.
-- PK: student_email (Um aluno só tem uma defesa)
CREATE TABLE IF NOT EXISTS `defesa_tg` (
                                           `student_email`    VARCHAR(255) NOT NULL,
    `scheduler_email`  VARCHAR(255) NOT NULL,
    `data_hora_defesa` DATETIME NOT NULL,
    `local_defesa`     VARCHAR(255) NOT NULL,
    `banca_avaliadora` TEXT COMMENT 'Nomes dos membros da banca, separados por ponto-e-vírgula ou JSON.',
    `status_defesa`    ENUM('Agendada', 'Concluida', 'Cancelada') NOT NULL DEFAULT 'Agendada',
    PRIMARY KEY (`student_email`),
    FOREIGN KEY (`student_email`) REFERENCES `student`(`email`) ON DELETE CASCADE,
    FOREIGN KEY (`scheduler_email`) REFERENCES `teacher`(`email`) ON DELETE NO ACTION,
    UNIQUE INDEX `uq_defesa_data_local` (`data_hora_defesa`, `local_defesa`) COMMENT 'Impede duas defesas no mesmo local/horário'
    );


-- ========================================================================== --
-- VIEWS (Visões para Simplificar Consultas)                                  --
-- ========================================================================== --

-- VIEW: Mostra o status atual de cada seção para um aluno.
CREATE OR REPLACE VIEW `vw_secoes_aluno` AS
WITH LatestSubmission AS (
    SELECT ts.`student_email`, ts.`sequence_order`, ts.`submission_timestamp`, ROW_NUMBER() OVER(PARTITION BY ts.`student_email`, ts.`sequence_order` ORDER BY ts.`submission_timestamp` DESC) as rn
    FROM `task_submission` ts
),
LatestReview AS (
    SELECT tr.`student_email`, tr.`sequence_order`, tr.`submission_timestamp`, tr.`status`, tr.`review_timestamp`, ROW_NUMBER() OVER(PARTITION BY tr.`student_email`, tr.`sequence_order`, tr.`submission_timestamp` ORDER BY tr.`review_timestamp` DESC) as rn
    FROM `task_review` tr
)
SELECT
    t.student_email AS emailAluno,
    t.sequence_order AS taskSequence,
    t.title AS titulo,
    t.status AS status,
    t.due_date AS dataEntrega,
    CASE WHEN t.status = 'completed' THEN 'Aprovado'
         WHEN lr.status IS NOT NULL THEN lr.status
         WHEN ls.submission_timestamp IS NOT NULL THEN 'Pendente'
         ELSE '---'
        END AS statusRevisao,
    lr.review_timestamp AS dataUltimaRevisao
FROM `task` t
         LEFT JOIN LatestSubmission ls ON t.student_email = ls.student_email AND t.sequence_order = ls.sequence_order AND ls.rn = 1
         LEFT JOIN LatestReview lr ON ls.student_email = lr.student_email AND ls.sequence_order = lr.sequence_order AND ls.submission_timestamp = ls.submission_timestamp AND lr.rn = 1
ORDER BY t.student_email, t.sequence_order;

-- VIEW: Lista tarefas pendentes de revisão para o professor orientador.
CREATE OR REPLACE VIEW `vw_professor_dashboard` AS
WITH StudentProgress AS (
    SELECT `student_email`, COUNT(*) AS total_tasks, SUM(CASE WHEN `status` = 'completed' THEN 1 ELSE 0 END) AS completed_tasks FROM `task` GROUP BY `student_email`
),
LatestSubmission AS (
    SELECT ts.`student_email`, ts.`sequence_order`, ts.`submission_timestamp`, ROW_NUMBER() OVER(PARTITION BY ts.`student_email`, ts.`sequence_order` ORDER BY ts.`submission_timestamp` DESC) as rn FROM `task_submission` ts
),
LatestReview AS (
    SELECT tr.`student_email`, tr.`sequence_order`, tr.`submission_timestamp`, tr.`status`, ROW_NUMBER() OVER(PARTITION BY tr.`student_email`, tr.`sequence_order`, tr.`submission_timestamp` ORDER BY tr.`review_timestamp` DESC) as rn FROM `task_review` tr
)
SELECT
    s.`advisor_email` AS teacher_email, (COALESCE(sp.completed_tasks, 0) / NULLIF(sp.total_tasks, 0)) AS progresso,
    CONCAT(u.FirstName, ' ', u.LastName) AS nomeAluno, s.email AS emailAluno, c.disciplina AS turma, CONCAT(c.year, '/', c.semester) AS semestre,
    CASE WHEN lr.status = 'revision_requested' THEN 'Revisão Solicitada' ELSE 'Pendente de Revisão' END AS status,
    t.student_email, t.sequence_order, ls.submission_timestamp
FROM `task` t
         JOIN `student` s ON t.student_email = s.email
         JOIN `user` u ON s.email = u.email
         JOIN `class` c ON s.class_disciplina = c.disciplina AND s.class_year = c.year AND s.class_semester = c.semester
         LEFT JOIN StudentProgress sp ON s.email = sp.student_email
         JOIN LatestSubmission ls ON t.student_email = ls.student_email AND t.sequence_order = ls.sequence_order AND ls.rn = 1
         LEFT JOIN LatestReview lr ON ls.student_email = lr.student_email AND ls.sequence_order = lr.sequence_order AND ls.submission_timestamp = lr.submission_timestamp AND lr.rn = 1
WHERE s.advisor_email IS NOT NULL AND t.status = 'in_progress' AND (lr.status IS NULL OR lr.status = 'revision_requested');

-- VIEW: Reúne detalhes do aluno e sua turma.
CREATE OR REPLACE VIEW vw_student_details AS
SELECT
    s.email,
    s.personal_email,
    u.FirstName,
    u.LastName,
    CONCAT(u.FirstName, ' ', u.LastName) AS nomeCompleto,
    u.profile_picture_url,
    u.status AS user_status,
    s.advisor_email,
    s.agreement_document_url,
    s.class_disciplina,
    s.class_year,
    s.class_semester,
    s.estagio_tg_atual,
    CONCAT(c.disciplina, ' - ', c.year, '/', c.semester) AS turma_descricao
FROM student s
         JOIN user u ON s.email = u.email
         JOIN class c ON s.class_disciplina = c.disciplina AND s.class_year = c.year AND s.class_semester = c.semester;

-- VIEW: Reúne detalhes do professor.
CREATE OR REPLACE VIEW vw_teacher_details AS
SELECT
    t.email,
    u.FirstName,
    u.LastName,
    CONCAT(u.FirstName, ' ', u.LastName) AS nomeCompleto,
    u.profile_picture_url,
    u.status AS user_status,
    t.is_coordinator,
    CASE WHEN t.is_coordinator = 1 THEN 'Coordenador TG' ELSE 'Professor/Orientador' END AS tipoProfessor
FROM teacher t
         JOIN user u ON t.email = u.email;

-- VIEW: Calcula estatísticas agregadas por turma.
CREATE OR REPLACE VIEW vw_class_summary AS
WITH StudentTGProgress AS (
    SELECT
        s.email AS student_email,
        s.class_disciplina,
        s.class_year,
        s.class_semester,
        COALESCE(AVG(CASE WHEN t.status = 'completed' THEN 1.0 ELSE 0.0 END), 0.0) AS progresso_individual
    FROM student s
    LEFT JOIN task t ON s.email = t.student_email
    GROUP BY s.email, s.class_disciplina, s.class_year, s.class_semester
)
SELECT
    c.disciplina,
    c.year,
    c.semester,
    COUNT(DISTINCT stp.student_email) AS numero_alunos,
    SUM(CASE WHEN stp.progresso_individual = 1.0 THEN 1 ELSE 0 END) AS tgs_concluidos,
    AVG(stp.progresso_individual) AS progresso_medio_turma
FROM class c
         LEFT JOIN StudentTGProgress stp ON c.disciplina = stp.class_disciplina AND c.year = stp.class_year AND c.semester = stp.class_semester
GROUP BY c.disciplina, c.year, c.semester;

-- VIEW: Visão completa das tarefas para o dashboard do Coordenador TG.
CREATE OR REPLACE VIEW `vw_professortg_dashboard_tasks` AS
WITH StudentProgress AS (
    SELECT `student_email`, COUNT(*) AS total_tasks, SUM(CASE WHEN `status` = 'completed' THEN 1 ELSE 0 END) AS completed_tasks FROM `task` GROUP BY `student_email`
), LatestSubmission AS (
    SELECT ts.`student_email`, ts.`sequence_order`, ts.`submission_timestamp`, ROW_NUMBER() OVER(PARTITION BY ts.`student_email`, ts.`sequence_order` ORDER BY ts.`submission_timestamp` DESC) as rn FROM `task_submission` ts
), LatestReview AS (
    SELECT tr.`student_email`, tr.`sequence_order`, tr.`submission_timestamp`, tr.`status`, tr.`review_timestamp`, ROW_NUMBER() OVER(PARTITION BY tr.`student_email`, tr.`sequence_order`, tr.`submission_timestamp` ORDER BY tr.`review_timestamp` DESC) as rn FROM `task_review` tr
)
SELECT
    CONCAT(u.FirstName, ' ', u.LastName) AS nomeAluno,
    s.email AS emailAluno,
    c.disciplina AS turma_disciplina, c.year AS turma_year, c.semester AS turma_semester,
    s.estagio_tg_atual AS estagio_aluno,
    t.sequence_order, t.title AS task_title, t.status AS task_status, t.estagio_task,
    (COALESCE(sp.completed_tasks, 0) / NULLIF(sp.total_tasks, 0)) AS progresso_geral_aluno,
    CASE WHEN t.status = 'completed' THEN 'Aprovado'
         WHEN lr.status IS NOT NULL THEN lr.status
         WHEN ls.submission_timestamp IS NOT NULL THEN 'Pendente'
         ELSE '---'
        END AS status_revisao_ultima_submissao,
    ls.submission_timestamp AS timestamp_ultima_submissao,
    lr.review_timestamp AS timestamp_ultima_revisao,
    s.advisor_email
FROM `task` t
         JOIN `student` s ON t.student_email = s.email
         JOIN `user` u ON s.email = u.email
         JOIN `class` c ON s.class_disciplina = c.disciplina AND s.class_year = c.year AND s.class_semester = c.semester
         LEFT JOIN StudentProgress sp ON s.email = sp.student_email
         LEFT JOIN LatestSubmission ls ON t.student_email = ls.student_email AND t.sequence_order = ls.sequence_order AND ls.rn = 1
         LEFT JOIN LatestReview lr ON ls.student_email = lr.student_email AND ls.sequence_order = lr.sequence_order AND ls.submission_timestamp = ls.submission_timestamp AND lr.rn = 1;

-- VIEW: Unifica submissões e revisões recentes para o feed de atividades do Professor TG.
CREATE OR REPLACE VIEW vw_professortg_recent_activity AS
SELECT
    'SUBMISSION' AS event_type,
    ts.student_email,
    ts.submission_timestamp AS event_timestamp,
    s.class_disciplina, s.class_year, s.class_semester,
    CONCAT(u.FirstName, ' ', u.LastName) AS nomeCompleto,
    CONCAT('Enviou a tentativa ', ts.attempt_number, ' para "', t.title, '"') AS acao_descricao
FROM task_submission ts
         JOIN task t ON ts.student_email = t.student_email AND ts.sequence_order = t.sequence_order
         JOIN student s ON ts.student_email = s.email
         JOIN user u ON ts.student_email = u.email
UNION ALL
SELECT
    'REVIEW' AS event_type,
    tr.student_email,
    tr.review_timestamp AS event_timestamp,
    s.class_disciplina, s.class_year, s.class_semester,
    CONCAT(u.FirstName, ' ', u.LastName) AS nomeCompleto,
    CONCAT('Teve a seção "', t.title, '" ', CASE tr.status WHEN 'approved' THEN 'aprovada' WHEN 'revision_requested' THEN 'marcada para revisão' ELSE 'revisada' END, ' pelo orientador') AS acao_descricao
FROM task_review tr
         JOIN task_submission ts ON tr.student_email = ts.student_email AND tr.sequence_order = ts.sequence_order AND tr.submission_timestamp = ts.submission_timestamp
         JOIN task t ON ts.student_email = t.student_email AND ts.sequence_order = t.sequence_order
         JOIN student s ON tr.student_email = s.email
         JOIN user u ON tr.student_email = u.email;


-- ========================================================================== --
-- TRIGGERS (Ações Automáticas)                                               --
-- ========================================================================== --

DELIMITER //

-- TRIGGER: Desbloqueia a próxima task sequencial se o aluno estiver no estágio correto, quando a atual é completada.
CREATE TRIGGER trg_unlock_next_task_on_complete AFTER UPDATE ON task FOR EACH ROW
BEGIN
    DECLARE next_sequence INT;
    DECLARE next_task_estagio INT;
    DECLARE student_current_estagio INT;
    IF NEW.status = 'completed' AND OLD.status <> 'completed' THEN
        SET next_sequence = NEW.sequence_order + 1;
    SELECT estagio_task INTO next_task_estagio FROM task WHERE student_email = NEW.student_email AND sequence_order = next_sequence;
    IF next_task_estagio IS NOT NULL THEN
    SELECT estagio_tg_atual INTO student_current_estagio FROM student WHERE email = NEW.student_email;
    IF next_task_estagio = student_current_estagio THEN
    UPDATE task SET status = 'in_progress' WHERE student_email = NEW.student_email AND sequence_order = next_sequence AND status = 'locked';
END IF;
END IF;
END IF;
END //

-- TRIGGER: Desbloqueia a primeira task do novo estágio ao promover aluno, se a última do anterior estiver completa.
CREATE TRIGGER trg_unlock_tasks_on_stage_promotion AFTER UPDATE ON student FOR EACH ROW
BEGIN
    DECLARE previous_stage_last_task_status ENUM('locked', 'in_progress', 'completed');
    DECLARE new_stage_first_task_sequence INT;
    IF NEW.estagio_tg_atual > OLD.estagio_tg_atual THEN
    SELECT status INTO previous_stage_last_task_status FROM task WHERE student_email = NEW.email AND estagio_task = OLD.estagio_tg_atual ORDER BY sequence_order DESC LIMIT 1;
    IF previous_stage_last_task_status = 'completed' THEN
    SELECT MIN(sequence_order) INTO new_stage_first_task_sequence FROM task WHERE student_email = NEW.email AND estagio_task = NEW.estagio_tg_atual;
    IF new_stage_first_task_sequence IS NOT NULL THEN
    UPDATE task SET status = 'in_progress' WHERE student_email = NEW.email AND sequence_order = new_stage_first_task_sequence AND status = 'locked';
END IF;
END IF;
END IF;
END //

-- TRIGGER: Marca a task como 'completed' quando uma revisão é inserida com status 'approved'.
CREATE TRIGGER trg_complete_task_on_approval AFTER INSERT ON task_review FOR EACH ROW
BEGIN
    IF NEW.status = 'approved' THEN
    UPDATE task SET status = 'completed'
    WHERE student_email = NEW.student_email AND sequence_order = NEW.sequence_order AND status <> 'completed';
END IF;
END //

DELIMITER ;

-- ========================================================================== --
-- FIM DO SCRIPT                                                              --
-- ========================================================================== --