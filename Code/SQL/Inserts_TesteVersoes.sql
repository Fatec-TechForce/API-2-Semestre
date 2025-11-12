USE TGControl;

SET SQL_SAFE_UPDATES = 0; -- Desabilita o modo de segurança
SET FOREIGN_KEY_CHECKS = 0; -- Desabilita a checagem de chaves estrangeiras

-- Agora seus deletes funcionarão
DELETE FROM notification;
DELETE FROM task_review;
DELETE FROM task_submission;
DELETE FROM task;
DELETE FROM tg_coordenacao_turma;
DELETE FROM student;
DELETE FROM class;
DELETE FROM teacher;
DELETE FROM user;

-- Reabilita as checagens
SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1; -- Reabilita o modo de segurança
USE TGControl;

INSERT INTO `user` (`email`, `FirstName`, `LastName`, `passwordHASH`, `profile_picture_url`, `status`) VALUES
('orientador.silva@fatec.sp.gov.br', 'Ana', 'Silva', 'Troca123', 'Server/profiles/orientador.silva_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('orientador.costa@fatec.sp.gov.br', 'Bruno', 'Costa', 'Troca123', 'Server/profiles/orientador.costa_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('orientador.lima@fatec.sp.gov.br', 'Carla', 'Lima', 'Troca123', 'Server/profiles/orientador.lima_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('coord.alves@fatec.sp.gov.br', 'Daniel', 'Alves', 'Troca123', 'Server/profiles/coord.alves_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('coord.borges@fatec.sp.gov.br', 'Elisa', 'Borges', 'Troca123', 'Server/profiles/coord.borges_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('leonardo.hashimoto@fatec.sp.gov.br', 'Leonardo Amon', 'Sumiyoshi Hashimoto', 'Troca123', 'Server/profiles/leonardo.hashimoto_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('maria.oliveira@fatec.sp.gov.br', 'Maria Eduarda', 'Teixeira Miller de Oliveira', 'Troca123', 'Server/profiles/maria.oliveira_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('gabriel.belarmino@fatec.sp.gov.br', 'Gabriel', 'Valente Belarmino', 'Troca123', 'Server/profiles/gabriel.belarmino_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('guilherme.arruda@fatec.sp.gov.br', 'Guilherme', 'Almeida de Arruda', 'Troca123', 'Server/profiles/guilherme.arruda_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('natalia.silva@fatec.sp.gov.br', 'Natália', 'Pereira da Silva', 'Troca123', 'Server/profiles/natalia.silva_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('niuan.souza@fatec.sp.gov.br', 'Niuan', 'Spolidorio da Rocha Souza', 'Troca123', 'Server/profiles/niuan.souza_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('vitor.souza@fatec.sp.gov.br', 'Vitor Samuel', 'Ribeiro de Souza', 'Troca123', 'Server/profiles/vitor.souza_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno08.gen@fatec.sp.gov.br', 'Fernanda', 'Gomes', 'Troca123', 'Server/profiles/aluno08.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno09.gen@fatec.sp.gov.br', 'Hugo', 'Martins', 'Troca123', 'Server/profiles/aluno09.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno10.gen@fatec.sp.gov.br', 'Isabela', 'Nunes', 'Troca123', 'Server/profiles/aluno10.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno11.gen@fatec.sp.gov.br', 'João', 'Pereira', 'Troca123', 'Server/profiles/aluno11.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno12.gen@fatec.sp.gov.br', 'Karla', 'Ramos', 'Troca123', 'Server/profiles/aluno12.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno13.gen@fatec.sp.gov.br', 'Lucas', 'Santana', 'Troca123', 'Server/profiles/aluno13.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno14.gen@fatec.sp.gov.br', 'Mariana', 'Tavares', 'Troca123', 'Server/profiles/aluno14.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno15.gen@fatec.sp.gov.br', 'Otávio', 'Vieira', 'Troca123', 'Server/profiles/aluno15.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno16.gen@fatec.sp.gov.br', 'Paula', 'Xavier', 'Troca123', 'Server/profiles/aluno16.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno17.gen@fatec.sp.gov.br', 'Ricardo', 'Andrade', 'Troca123', 'Server/profiles/aluno17.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno18.gen@fatec.sp.gov.br', 'Sofia', 'Barros', 'Troca123', 'Server/profiles/aluno18.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno19.gen@fatec.sp.gov.br', 'Thiago', 'Cardoso', 'Troca123', 'Server/profiles/aluno19.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno20.gen@fatec.sp.gov.br', 'Ursula', 'Dias', 'Troca123', 'Server/profiles/aluno20.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno21.gen@fatec.sp.gov.br', 'Vinicius', 'Esteves', 'Troca123', 'Server/profiles/aluno21.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno22.gen@fatec.sp.gov.br', 'Wagner', 'Freitas', 'Troca123', 'Server/profiles/aluno22.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno23.gen@fatec.sp.gov.br', 'Yasmin', 'Garcia', 'Troca123', 'Server/profiles/aluno23.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno24.gen@fatec.sp.gov.br', 'Zeca', 'Henriques', 'Troca123', 'Server/profiles/aluno24.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno25.gen@fatec.sp.gov.br', 'Alice', 'Ibanez', 'Troca123', 'Server/profiles/aluno25.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno26.gen@fatec.sp.gov.br', 'Beto', 'Jacinto', 'Troca123', 'Server/profiles/aluno26.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno27.gen@fatec.sp.gov.br', 'Celia', 'Lopes', 'Troca123', 'Server/profiles/aluno27.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno28.gen@fatec.sp.gov.br', 'Dario', 'Mendes', 'Troca123', 'Server/profiles/aluno28.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno29.gen@fatec.sp.gov.br', 'Erica', 'Neves', 'Troca123', 'Server/profiles/aluno29.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active'),
('aluno30.gen@fatec.sp.gov.br', 'Fabio', 'Oliveira', 'Troca123', 'Server/profiles/aluno30.gen_at_fatec.sp.gov.br_profilePhoto.png', 'Active');

INSERT INTO `teacher` (`email`, `is_coordinator`) VALUES
('orientador.silva@fatec.sp.gov.br', 0),
('orientador.costa@fatec.sp.gov.br', 0),
('orientador.lima@fatec.sp.gov.br', 0),
('coord.alves@fatec.sp.gov.br', 1),
('coord.borges@fatec.sp.gov.br', 1);

INSERT INTO `class` (`disciplina`, `year`, `semester`, `min_tasks`, `max_tasks`) VALUES
('Banco de Dados', 2025, 1, 1, 6),
('Analise e Desenvolvimento de Sistemas', 2025, 1, 1, 6),
('Educacao Ambiental', 2024, 2, 1, 6); -- Corrigido para 6 tasks

INSERT INTO `student` (`email`, `advisor_email`, `class_disciplina`, `class_year`, `class_semester`, `estagio_tg_atual`) VALUES
('leonardo.hashimoto@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 1),
('maria.oliveira@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 1),
('gabriel.belarmino@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 1),
('guilherme.arruda@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 2),
('natalia.silva@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 1),
('niuan.souza@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 1),
('vitor.souza@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 2),
('aluno08.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 1),
('aluno09.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 1),
('aluno10.gen@fatec.sp.gov.br', NULL, 'Banco de Dados', 2025, 1, 1),
('aluno11.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno12.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno13.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno14.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno15.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno16.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno17.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno18.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno19.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno20.gen@fatec.sp.gov.br', NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno21.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2),
('aluno22.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2),
('aluno23.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2),
('aluno24.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2),
('aluno25.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2),
('aluno26.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2),
('aluno27.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2),
('aluno28.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2),
('aluno29.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2),
('aluno30.gen@fatec.sp.gov.br', NULL, 'Educacao Ambiental', 2024, 2, 2);

INSERT INTO `task` (`student_email`, `sequence_order`, `title`, `description`, `due_date`, `status`, `estagio_task`) VALUES
('niuan.souza@fatec.sp.gov.br', 1, 'Seção 1: Introdução', 'Definir tema e problema.', '2025-09-15', 'in_progress', 1),
('niuan.souza@fatec.sp.gov.br', 2, 'Seção 2: Revisão Bibliográfica', 'Pesquisar artigos relacionados.', '2025-10-01', 'locked', 1),
('niuan.souza@fatec.sp.gov.br', 3, 'Seção 3: Metodologia', 'Descrever métodos.', '2025-10-15', 'locked', 1),
('niuan.souza@fatec.sp.gov.br', 4, 'Seção 4: Desenvolvimento Inicial', 'Primeira parte prática.', '2025-11-01', 'locked', 1),
('niuan.souza@fatec.sp.gov.br', 5, 'Seção 5: Desenvolvimento Final', 'Segunda parte prática.', '2026-03-15', 'locked', 2),
('niuan.souza@fatec.sp.gov.br', 6, 'Seção 6: Conclusão e Apresentação', 'Finalizar trabalho.', '2026-04-15', 'locked', 2),
('maria.oliveira@fatec.sp.gov.br', 1, 'Seção 1: Introdução', 'Definir tema e problema.', '2025-09-15', 'completed', 1),
('maria.oliveira@fatec.sp.gov.br', 2, 'Seção 2: Revisão Bibliográfica', 'Pesquisar artigos relacionados.', '2025-10-01', 'completed', 1),
('maria.oliveira@fatec.sp.gov.br', 3, 'Seção 3: Metodologia', 'Descrever métodos.', '2025-10-15', 'in_progress', 1),
('maria.oliveira@fatec.sp.gov.br', 4, 'Seção 4: Desenvolvimento Inicial', 'Primeira parte prática.', '2025-11-01', 'locked', 1),
('maria.oliveira@fatec.sp.gov.br', 5, 'Seção 5: Desenvolvimento Final', 'Segunda parte prática.', '2026-03-15', 'locked', 2),
('maria.oliveira@fatec.sp.gov.br', 6, 'Seção 6: Conclusão e Apresentação', 'Finalizar trabalho.', '2026-04-15', 'locked', 2),
('guilherme.arruda@fatec.sp.gov.br', 1, 'Seção 1: Introdução', '...', '2025-09-15', 'completed', 1),
('guilherme.arruda@fatec.sp.gov.br', 2, 'Seção 2: Revisão Bibliográfica', '...', '2025-10-01', 'completed', 1),
('guilherme.arruda@fatec.sp.gov.br', 3, 'Seção 3: Metodologia', '...', '2025-10-15', 'completed', 1),
('guilherme.arruda@fatec.sp.gov.br', 4, 'Seção 4: Desenvolvimento Inicial', '...', '2025-11-01', 'completed', 1),
('guilherme.arruda@fatec.sp.gov.br', 5, 'Seção 5: Desenvolvimento Final', '...', '2026-03-15', 'in_progress', 2),
('guilherme.arruda@fatec.sp.gov.br', 6, 'Seção 6: Conclusão e Apresentação', '...', '2026-04-15', 'locked', 2),
('vitor.souza@fatec.sp.gov.br', 1, 'Seção 1', '...', '2025-09-15', 'completed', 1),
('vitor.souza@fatec.sp.gov.br', 2, 'Seção 2', '...', '2025-10-01', 'completed', 1),
('vitor.souza@fatec.sp.gov.br', 3, 'Seção 3', '...', '2025-10-15', 'completed', 1),
('vitor.souza@fatec.sp.gov.br', 4, 'Seção 4', '...', '2025-11-01', 'completed', 1),
('vitor.souza@fatec.sp.gov.br', 5, 'Seção 5', '...', '2026-03-15', 'completed', 2),
('vitor.souza@fatec.sp.gov.br', 6, 'Seção 6', '...', '2026-04-15', 'completed', 2);

-- Geração de tasks iniciais para os demais alunos (task 1 in_progress, resto locked)
-- Certifique-se que as datas 'due_date' façam sentido (exemplo usa CURDATE() + meses)
INSERT INTO `task` (`student_email`, `sequence_order`, `title`, `description`, `due_date`, `status`, `estagio_task`)
SELECT email, seq, CONCAT('Seção ', seq), '', DATE_ADD(CURDATE(), INTERVAL seq MONTH), IF(seq=1, 'in_progress', 'locked'), IF(seq<=4, 1, 2)
FROM student, (SELECT 1 as seq UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) as sequences
WHERE email NOT IN ('niuan.souza@fatec.sp.gov.br', 'maria.oliveira@fatec.sp.gov.br', 'guilherme.arruda@fatec.sp.gov.br', 'vitor.souza@fatec.sp.gov.br'); -- Aplica a todos os outros

-- Adicionando a coluna `submission_title` e um valor de exemplo para ela
INSERT INTO `task_submission` (`student_email`, `sequence_order`, `submission_timestamp`, `file_path`, `submission_title`, `attempt_number`) 
VALUES ('maria.oliveira@fatec.sp.gov.br', 1, NOW() - INTERVAL 20 DAY, 'Server/submissions/maria_t1_v1.pdf', 'Entrega da Introdução (v1)', 1);

-- Este INSERT de task_review está correto e não muda
INSERT INTO `task_review` (`student_email`, `sequence_order`, `submission_timestamp`, `reviewer_email`, `review_timestamp`, `status`, `review_comment`) 
SELECT student_email, sequence_order, submission_timestamp, 'orientador.silva@fatec.sp.gov.br', NOW() - INTERVAL 19 DAY, 'approved', 'Ótima introdução!' 
FROM task_submission 
WHERE student_email='maria.oliveira@fatec.sp.gov.br' AND sequence_order=1 AND attempt_number=1;

-- --- Próxima Submissão ---
INSERT INTO `task_submission` (`student_email`, `sequence_order`, `submission_timestamp`, `file_path`, `submission_title`, `attempt_number`) 
VALUES ('maria.oliveira@fatec.sp.gov.br', 2, NOW() - INTERVAL 15 DAY, 'Server/submissions/maria_t2_v1.pdf', 'Entrega Revisão Bibliográfica (v1)', 1);

INSERT INTO `task_review` (`student_email`, `sequence_order`, `submission_timestamp`, `reviewer_email`, `review_timestamp`, `status`, `review_comment`) 
SELECT student_email, sequence_order, submission_timestamp, 'orientador.silva@fatec.sp.gov.br', NOW() - INTERVAL 14 DAY, 'revision_requested', 'Faltou citar autor X.' 
FROM task_submission 
WHERE student_email='maria.oliveira@fatec.sp.gov.br' AND sequence_order=2 AND attempt_number=1;

-- --- Próxima Submissão (Correção) ---
INSERT INTO `task_submission` (`student_email`, `sequence_order`, `submission_timestamp`, `file_path`, `submission_title`, `attempt_number`) 
VALUES ('maria.oliveira@fatec.sp.gov.br', 2, NOW() - INTERVAL 10 DAY, 'Server/submissions/maria_t2_v2.pdf', 'Correção Revisão Bibliográfica (v2)', 2);

INSERT INTO `task_review` (`student_email`, `sequence_order`, `submission_timestamp`, `reviewer_email`, `review_timestamp`, `status`, `review_comment`) 
SELECT student_email, sequence_order, submission_timestamp, 'orientador.silva@fatec.sp.gov.br', NOW() - INTERVAL 9 DAY, 'approved', 'Agora sim, perfeito.' 
FROM task_submission 
WHERE student_email='maria.oliveira@fatec.sp.gov.br' AND sequence_order=2 AND attempt_number=2;

-- --- Próxima Submissão ---
INSERT INTO `task_submission` (`student_email`, `sequence_order`, `submission_timestamp`, `file_path`, `submission_title`, `attempt_number`) 
VALUES ('maria.oliveira@fatec.sp.gov.br', 3, NOW() - INTERVAL 2 DAY, 'Server/submissions/maria_t3_v1.pdf', 'Entrega da Metodologia (v1)', 1);

-- --- Submissão do Niuan ---
INSERT INTO `task_submission` (`student_email`, `sequence_order`, `submission_timestamp`, `file_path`, `submission_title`, `attempt_number`) 
VALUES ('niuan.souza@fatec.sp.gov.br', 1, NOW() - INTERVAL 1 DAY, 'Server/submissions/niuan_t1_v1.docx', 'Definição do Tema e Problema', 1);

INSERT INTO `notification` (`user_email`, `timestamp`, `content`, `related_task_student_email`, `related_task_sequence_order`, `is_read`) VALUES
('maria.oliveira@fatec.sp.gov.br', NOW() - INTERVAL 14 DAY, 'Sua seção "Seção 2: Revisão Bibliográfica" foi revisada por Ana Silva e marcada para revisão.', 'maria.oliveira@fatec.sp.gov.br', 2, 1),
('orientador.silva@fatec.sp.gov.br', NOW() - INTERVAL 10 DAY, 'A aluna Maria Eduarda Teixeira Miller de Oliveira enviou a tentativa 2 para "Seção 2: Revisão Bibliográfica".', 'maria.oliveira@fatec.sp.gov.br', 2, 1),
('maria.oliveira@fatec.sp.gov.br', NOW() - INTERVAL 9 DAY, 'Sua seção "Seção 2: Revisão Bibliográfica" foi revisada por Ana Silva e aprovada.', 'maria.oliveira@fatec.sp.gov.br', 2, 0),
('orientador.lima@fatec.sp.gov.br', NOW() - INTERVAL 1 DAY, 'O aluno Niuan Spolidorio da Rocha Souza enviou a tentativa 1 para "Seção 1: Introdução".', 'niuan.souza@fatec.sp.gov.br', 1, 0);

INSERT INTO `tg_coordenacao_turma` (`teacher_email`, `class_disciplina`, `class_year`, `class_semester`, `etapa_supervisionada`) VALUES
('coord.alves@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 1),
('coord.alves@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('coord.borges@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 2),
('coord.borges@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 2),
('coord.borges@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 1),
('coord.borges@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2);
