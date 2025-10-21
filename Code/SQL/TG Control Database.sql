-- Remove o banco de dados se ele já existir, para garantir um script limpo
DROP DATABASE IF EXISTS TGControl;
CREATE DATABASE TGControl;
USE TGControl;

-- Tabela user_status: Define 'Active', 'Inactive'
CREATE TABLE IF NOT EXISTS `user_status` (
	`status` VARCHAR(50) NOT NULL,
	PRIMARY KEY(`status`),
	CHECK(`status` IN ('Active', 'Inactive'))
) ENGINE=InnoDB;

-- Tabela task_status: Define 'locked', 'in_progress', 'completed'
CREATE TABLE IF NOT EXISTS `task_status` (
	`status` VARCHAR(50) NOT NULL,
	PRIMARY KEY(`status`),
	CHECK(`status` IN ('locked', 'in_progress', 'completed'))
) ENGINE=InnoDB;

-- Tabela user: Tabela base para todos os usuários
CREATE TABLE IF NOT EXISTS `user` (
	`email` VARCHAR(255) NOT NULL,
	`FirstName` VARCHAR(255) NOT NULL,
	`LastName` VARCHAR(255) NOT NULL,
	`passwordHASH` VARCHAR(255) NOT NULL,
	`profile_picture_url` VARCHAR(255),
	`status` VARCHAR(50) NOT NULL DEFAULT 'Active',
	PRIMARY KEY(`email`),
	FOREIGN KEY (`status`) REFERENCES `user_status`(`status`)
	ON UPDATE NO ACTION ON DELETE NO ACTION
) ENGINE=InnoDB;

-- Tabela teacher: Especialização de 'user'
CREATE TABLE IF NOT EXISTS `teacher` (
	`email` VARCHAR(255) NOT NULL,
	`is_coordinator` BOOLEAN NOT NULL DEFAULT 0, -- 1 = Coordenador TG, 0 = Professor
	PRIMARY KEY(`email`),
	FOREIGN KEY (`email`) REFERENCES `user`(`email`)
	ON UPDATE NO ACTION ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabela class: Define as turmas
CREATE TABLE IF NOT EXISTS `class` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`nome` VARCHAR(255) NOT NULL,
	`year` INT NOT NULL,
	`semester` INT NOT NULL,
	`curricular_base_code` VARCHAR(255),
	`min_tasks` INT NOT NULL DEFAULT 1,
	`max_tasks` INT,
	PRIMARY KEY(`id`),
	CHECK(`semester` BETWEEN 1 AND 2),
	CHECK(`max_tasks` IN (5, 6))
) ENGINE=InnoDB;

-- Tabela student: Especialização de 'user'
CREATE TABLE IF NOT EXISTS `student` (
	`email` VARCHAR(255) NOT NULL,
	`class_id` INT NOT NULL,
	`advisor_email` VARCHAR(255), -- Pode ser NULL (aluno sem orientador)
	PRIMARY KEY(`email`),
	FOREIGN KEY (`email`) REFERENCES `user`(`email`)
	ON UPDATE NO ACTION ON DELETE CASCADE,
	FOREIGN KEY (`class_id`) REFERENCES `class`(`id`)
	ON UPDATE NO ACTION ON DELETE RESTRICT,
	FOREIGN KEY (`advisor_email`) REFERENCES `teacher`(`email`)
	ON UPDATE NO ACTION ON DELETE NO ACTION -- Professores não são deletados
) ENGINE=InnoDB;

-- Tabela teacher_class: Relação M-N entre professores e turmas
CREATE TABLE IF NOT EXISTS `teacher_class` (
	`teacher_email` VARCHAR(255) NOT NULL,
	`class_id` INT NOT NULL,
	PRIMARY KEY(`teacher_email`, `class_id`),
	FOREIGN KEY (`teacher_email`) REFERENCES `teacher`(`email`)
	ON UPDATE NO ACTION ON DELETE NO ACTION,
	FOREIGN KEY (`class_id`) REFERENCES `class`(`id`)
	ON UPDATE NO ACTION ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabela task: As seções/etapas do TG de um aluno
CREATE TABLE IF NOT EXISTS `task` (
	`task_id` INT NOT NULL AUTO_INCREMENT,
	`student_email` VARCHAR(255) NOT NULL,
	`title` VARCHAR(255) NOT NULL,
	`description` TEXT,
	`due_date` DATE NOT NULL,
	`status` VARCHAR(50) NOT NULL DEFAULT 'locked',
	`sequence_order` INT NOT NULL,
	PRIMARY KEY(`task_id`),
	FOREIGN KEY (`student_email`) REFERENCES `student`(`email`)
	ON UPDATE NO ACTION ON DELETE CASCADE,
	FOREIGN KEY (`status`) REFERENCES `task_status`(`status`)
	ON UPDATE NO ACTION ON DELETE NO ACTION
) ENGINE=InnoDB;

-- Tabela task_submission: Histórico de entregas (versões)
CREATE TABLE IF NOT EXISTS `task_submission` (
	`submission_id` INT NOT NULL AUTO_INCREMENT,
	`task_id` INT NOT NULL,
	`file_path` VARCHAR(255) NOT NULL,
	`submission_title` VARCHAR(255),
	`attempt_number` INT NOT NULL,
	`timestamp` DATETIME NOT NULL DEFAULT NOW(),
	PRIMARY KEY(`submission_id`),
	FOREIGN KEY (`task_id`) REFERENCES `task`(`task_id`)
	ON UPDATE NO ACTION ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabela task_review: "Capa" da revisão (Aprovado/Rejeitado)
CREATE TABLE IF NOT EXISTS `task_review` (
	`review_id` INT NOT NULL AUTO_INCREMENT,
	`task_id` INT NOT NULL,
	`reviewer_email` VARCHAR(255) NOT NULL, 
	`status` VARCHAR(50) NOT NULL,
	`timestamp` DATETIME NOT NULL DEFAULT NOW(),
	PRIMARY KEY(`review_id`),
	FOREIGN KEY (`task_id`) REFERENCES `task`(`task_id`)
	ON UPDATE NO ACTION ON DELETE CASCADE,
	FOREIGN KEY (`reviewer_email`) REFERENCES `teacher`(`email`)
	ON UPDATE NO ACTION ON DELETE NO ACTION,
	CHECK(`status` IN ('revision_requested', 'approved'))
) ENGINE=InnoDB;

-- Tabela task_review_comment: Correções granulares do professor
CREATE TABLE IF NOT EXISTS `task_review_comment` (
    `comment_id` INT NOT NULL AUTO_INCREMENT,
    `review_id` INT NOT NULL,
    `comment_location` VARCHAR(255), -- Ex: "Página 5, Parágrafo 2"
    `comment_text` TEXT NOT NULL,
    `timestamp` DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY(`comment_id`),
    FOREIGN KEY (`review_id`) REFERENCES `task_review`(`review_id`)
        ON UPDATE NO ACTION ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabela notification: Notificações para usuários
CREATE TABLE IF NOT EXISTS `notification` (
	`notification_id` INT NOT NULL AUTO_INCREMENT,
	`user_email` VARCHAR(255) NOT NULL,
	`content` TEXT NOT NULL,
	`related_task_id` INT,
	`is_read` BOOLEAN NOT NULL DEFAULT 0,
	`timestamp` DATETIME NOT NULL DEFAULT NOW(),
	PRIMARY KEY(`notification_id`),
	FOREIGN KEY (`user_email`) REFERENCES `user`(`email`)
	ON UPDATE NO ACTION ON DELETE CASCADE,
	FOREIGN KEY (`related_task_id`) REFERENCES `task`(`task_id`)
	ON UPDATE NO ACTION ON DELETE SET NULL
) ENGINE=InnoDB;


/*******************************************************************************
 * * VIEW 1: vw_secoes_aluno
 * * Função: Corresponde ao DTO 'SecaoAluno.java'.
 * * Simplifica a busca de seções para a tela do aluno.
 * *******************************************************************************/
CREATE VIEW `vw_secoes_aluno` AS
WITH LatestReview AS (
    SELECT
        `task_id`, `status`, `timestamp`,
        ROW_NUMBER() OVER(PARTITION BY `task_id` ORDER BY `timestamp` DESC) as rn
    FROM `task_review`
)
SELECT 
    t.task_id AS taskId,
    t.student_email AS emailAluno,
    t.title AS titulo,
    t.status AS status,
    t.due_date AS dataEntrega,
    COALESCE(lr.status, IF(t.status = 'locked', '---', 'Pendente')) AS statusRevisao
FROM `task` t
LEFT JOIN LatestReview lr ON t.task_id = lr.task_id AND lr.rn = 1
ORDER BY t.student_email, t.sequence_order;
    
    
/*******************************************************************************
 * * VIEW 2: vw_professor_dashboard
 * * Função: Corresponde ao DTO 'TrabalhoPendente.java'.
 * * Lista tarefas pendentes para o dashboard do professor orientador.
 * *******************************************************************************/
CREATE VIEW `vw_professor_dashboard` AS
WITH StudentProgress AS (
    SELECT
        `student_email`,
        COUNT(*) AS total_tasks,
        SUM(CASE WHEN `status` = 'completed' THEN 1 ELSE 0 END) AS completed_tasks
    FROM `task`
    GROUP BY `student_email`
),
LatestReview AS (
    SELECT
        `task_id`, `status`,
        ROW_NUMBER() OVER(PARTITION BY `task_id` ORDER BY `timestamp` DESC) as rn
    FROM `task_review`
)
SELECT 
    s.`advisor_email` AS teacher_email,
    (COALESCE(sp.completed_tasks, 0) / sp.total_tasks) AS progresso,
    CONCAT(u.FirstName, ' ', u.LastName) AS nomeAluno,
    s.email AS emailAluno,
    c.nome AS turma,
    CAST(c.year AS CHAR) AS semestre,
    COALESCE(lr.status, IF(t.status = 'in_progress', 'Pendente', t.status)) AS status,
    t.task_id,
    s.class_id
FROM `task` t
JOIN `student` s ON t.student_email = s.email
JOIN `user` u ON s.email = u.email
JOIN `class` c ON s.class_id = c.id
LEFT JOIN StudentProgress sp ON s.email = sp.student_email
LEFT JOIN LatestReview lr ON t.task_id = lr.task_id AND lr.rn = 1
WHERE
    s.advisor_email IS NOT NULL
    AND t.status != 'locked'
    AND COALESCE(lr.status, 'Pendente') != 'approved';
    

/*******************************************************************************
 * * VIEW 3: vw_user_login
 * * Função: Simplifica a query de login.
 * * Pré-calcula o 'TipoUsuario' (ex: ALUNO, PROFESSOR) para todos
 * * os usuários que estão com status 'Active'.
 * *******************************************************************************/
CREATE VIEW `vw_user_login` AS
SELECT
    u.email,
    u.passwordHASH,
    -- A lógica de tipo de usuário é movida para o banco
    CASE
        WHEN t.email IS NOT NULL THEN
            CASE
                WHEN t.is_coordinator = 1 THEN 'PROFESSOR_TG'
                ELSE 'PROFESSOR'
            END
        WHEN s.email IS NOT NULL THEN 'ALUNO'
        ELSE 'NAO_AUTENTICADO'
    END AS tipoUsuario -- Esta string DEVE corresponder ao seu Enum Java
FROM
    `user` u
LEFT JOIN
    `teacher` t ON u.email = t.email
LEFT JOIN
    `student` s ON u.email = s.email
WHERE
    u.status = 'Active';
