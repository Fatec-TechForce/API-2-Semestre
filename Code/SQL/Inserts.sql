USE TGControl;
SET FOREIGN_KEY_CHECKS = 0;
-- Limpa as tabelas na ordem correta para evitar conflitos de FK
DELETE FROM notification;
DELETE FROM task_review;
DELETE FROM task_submission;
DELETE FROM task;
DELETE FROM defesa_tg;
DELETE FROM tg_coordenacao_turma;
DELETE FROM student;
DELETE FROM class;
DELETE FROM teacher;
DELETE FROM user;
SET FOREIGN_KEY_CHECKS = 1;

USE TGControl;

-- ========================================================================== --
-- Tabela: user
-- ========================================================================== --
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

-- ========================================================================== --
-- Tabela: teacher
-- ========================================================================== --
INSERT INTO `teacher` (`email`, `is_coordinator`) VALUES
('orientador.silva@fatec.sp.gov.br', 0),
('orientador.costa@fatec.sp.gov.br', 0),
('orientador.lima@fatec.sp.gov.br', 0),
('coord.alves@fatec.sp.gov.br', 1),
('coord.borges@fatec.sp.gov.br', 1);

-- ========================================================================== --
-- Tabela: class
-- ========================================================================== --
INSERT INTO `class` (`disciplina`, `year`, `semester`, `min_tasks`, `max_tasks`) VALUES
('Banco de Dados', 2025, 1, 1, 6),
('Analise e Desenvolvimento de Sistemas', 2025, 1, 1, 6),
('Educacao Ambiental', 2024, 2, 1, 6);

-- ========================================================================== --
-- Tabela: student
-- ========================================================================== --
INSERT INTO `student` (`email`, `advisor_email`, `personal_email`, `agreement_document_url`, `class_disciplina`, `class_year`, `class_semester`, `estagio_tg_atual`) VALUES
-- Cenário 1: Início TG1 (Apenas Seção 1 liberada)
('leonardo.hashimoto@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', NULL, NULL, 'Banco de Dados', 2025, 1, 1),
-- Cenário 2: Meio TG1 (Seção 3 em andamento)
('maria.oliveira@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', 'maria.personal@email.com', NULL, 'Banco de Dados', 2025, 1, 1),
-- Cenário 3: Fim TG1 (Seção 4 em andamento)
('gabriel.belarmino@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', NULL, NULL, 'Banco de Dados', 2025, 1, 1),
-- Cenário 4: Início TG2 (Seção 5 liberada)
('guilherme.arruda@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', NULL, NULL, 'Banco de Dados', 2025, 1, 2),
-- Cenário 5: Fim TG2 (Seção 6 em andamento)
('natalia.silva@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', NULL, NULL, 'Banco de Dados', 2025, 1, 2),
-- Cenário 6: Meio TG1 (Seção 1 em andamento, mas para submissão)
('niuan.souza@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', NULL, NULL, 'Banco de Dados', 2025, 1, 1),
-- Cenário 7: TG Concluído (Todas as seções completas)
('vitor.souza@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', NULL, NULL, 'Banco de Dados', 2025, 1, 2),

-- Alunos Genéricos
('aluno08.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', NULL, NULL, 'Banco de Dados', 2025, 1, 1),
('aluno09.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', NULL, NULL, 'Banco de Dados', 2025, 1, 1),
('aluno10.gen@fatec.sp.gov.br', NULL, NULL, NULL, 'Banco de Dados', 2025, 1, 1),
('aluno11.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', NULL, NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno12.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', NULL, NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno13.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', NULL, NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno14.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', NULL, NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno15.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', NULL, NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno16.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', NULL, NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno17.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', NULL, NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno18.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', NULL, NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno19.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', NULL, NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno20.gen@fatec.sp.gov.br', NULL, NULL, NULL, 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('aluno21.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', NULL, NULL, 'Educacao Ambiental', 2024, 2, 2),
('aluno22.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', NULL, NULL, 'Educacao Ambiental', 2024, 2, 2),
('aluno23.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', NULL, NULL, 'Educacao Ambiental', 2024, 2, 2),
('aluno24.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', NULL, NULL, 'Educacao Ambiental', 2024, 2, 2),
('aluno25.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', NULL, NULL, 'Educacao Ambiental', 2024, 2, 2),
('aluno26.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', NULL, NULL, 'Educacao Ambiental', 2024, 2, 2),
('aluno27.gen@fatec.sp.gov.br', 'orientador.silva@fatec.sp.gov.br', NULL, NULL, 'Educacao Ambiental', 2024, 2, 2),
('aluno28.gen@fatec.sp.gov.br', 'orientador.costa@fatec.sp.gov.br', NULL, NULL, 'Educacao Ambiental', 2024, 2, 2),
('aluno29.gen@fatec.sp.gov.br', 'orientador.lima@fatec.sp.gov.br', NULL, NULL, 'Educacao Ambiental', 2024, 2, 2),
('aluno30.gen@fatec.sp.gov.br', NULL, NULL, NULL, 'Educacao Ambiental', 2024, 2, 2);

-- ========================================================================== --
-- Tabela: task
-- ========================================================================== --
-- Insere as 6 tarefas padrão para TODOS os 30 alunos, com status variados para o grupo principal
INSERT INTO `task` (`student_email`, `sequence_order`, `title`, `description`, `due_date`, `status`, `estagio_task`) VALUES
-- Cenário 1: Leonardo (Início TG1)
('leonardo.hashimoto@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('leonardo.hashimoto@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('leonardo.hashimoto@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('leonardo.hashimoto@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('leonardo.hashimoto@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('leonardo.hashimoto@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
-- Cenário 2: Maria (Meio TG1)
('maria.oliveira@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('maria.oliveira@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('maria.oliveira@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'in_progress', 1),
('maria.oliveira@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('maria.oliveira@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('maria.oliveira@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
-- Cenário 3: Gabriel (Fim TG1)
('gabriel.belarmino@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('gabriel.belarmino@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('gabriel.belarmino@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('gabriel.belarmino@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'in_progress', 1),
('gabriel.belarmino@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('gabriel.belarmino@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
-- Cenário 4: Guilherme (Início TG2) - Conforme original
('guilherme.arruda@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('guilherme.arruda@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('guilherme.arruda@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('guilherme.arruda@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('guilherme.arruda@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('guilherme.arruda@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
-- Cenário 5: Natalia (Fim TG2)
('natalia.silva@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('natalia.silva@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('natalia.silva@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('natalia.silva@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('natalia.silva@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'completed', 2),
('natalia.silva@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'in_progress', 2),
-- Cenário 6: Niuan (Início TG1 - Aguardando 1ª revisão)
('niuan.souza@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('niuan.souza@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('niuan.souza@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('niuan.souza@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('niuan.souza@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('niuan.souza@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
-- Cenário 7: Vitor (TG Concluído)
('vitor.souza@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('vitor.souza@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('vitor.souza@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('vitor.souza@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('vitor.souza@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'completed', 2),
('vitor.souza@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'completed', 2),

-- Alunos Genéricos (Turma BD - 2025/1)
('aluno08.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno08.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno08.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno08.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno08.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno08.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno09.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno09.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno09.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno09.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno09.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno09.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno10.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno10.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno10.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno10.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno10.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno10.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),

-- Alunos Genéricos (Turma ADS - 2025/1)
('aluno11.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno11.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno11.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno11.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno11.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno11.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno12.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno12.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno12.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno12.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno12.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno12.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno13.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno13.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno13.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno13.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno13.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno13.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno14.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno14.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno14.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno14.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno14.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno14.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno15.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno15.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno15.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno15.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno15.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno15.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno16.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno16.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno16.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno16.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno16.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno16.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno17.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno17.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno17.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno17.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno17.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno17.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno18.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno18.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno18.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno18.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno18.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno18.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno19.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno19.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno19.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno19.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno19.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno19.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno20.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'in_progress', 1),
('aluno20.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'locked', 1),
('aluno20.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'locked', 1),
('aluno20.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'locked', 1),
('aluno20.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'locked', 2),
('aluno20.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),

-- Alunos Genéricos (Turma EA - 2024/2) - Já estão no estágio 2
('aluno21.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('aluno21.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('aluno21.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('aluno21.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('aluno21.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('aluno21.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno22.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('aluno22.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('aluno22.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('aluno22.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('aluno22.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('aluno22.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno23.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('aluno23.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('aluno23.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('aluno23.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('aluno23.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('aluno23.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno24.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('aluno24.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('aluno24.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('aluno24.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('aluno24.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('aluno24.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno25.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('aluno25.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('aluno25.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('aluno25.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('aluno25.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('aluno25.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno26.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('aluno26.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('aluno26.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('aluno26.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('aluno26.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('aluno26.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno27.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('aluno27.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('aluno27.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('aluno27.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('aluno27.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('aluno27.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno28.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('aluno28.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('aluno28.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('aluno28.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('aluno28.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('aluno28.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno29.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('aluno29.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('aluno29.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('aluno29.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('aluno29.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('aluno29.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2),
('aluno30.gen@fatec.sp.gov.br', 1, 'Apresentação Pessoal e Acadêmica', 'Dados pessoais e acadêmicos do aluno.', CURDATE() + INTERVAL 1 MONTH, 'completed', 1),
('aluno30.gen@fatec.sp.gov.br', 2, 'Relatório PIM II', 'Relatório referente ao PIM II.', CURDATE() + INTERVAL 2 MONTH, 'completed', 1),
('aluno30.gen@fatec.sp.gov.br', 3, 'Relatório PIM III', 'Relatório referente ao PIM III.', CURDATE() + INTERVAL 3 MONTH, 'completed', 1),
('aluno30.gen@fatec.sp.gov.br', 4, 'Relatório PIM IV', 'Relatório referente ao PIM IV.', CURDATE() + INTERVAL 4 MONTH, 'completed', 1),
('aluno30.gen@fatec.sp.gov.br', 5, 'Relatório PIM V', 'Relatório referente ao PIM V.', CURDATE() + INTERVAL 5 MONTH, 'in_progress', 2),
('aluno30.gen@fatec.sp.gov.br', 6, 'Relatório PIM VI', 'Relatório referente ao PIM VI.', CURDATE() + INTERVAL 6 MONTH, 'locked', 2);

-- ========================================================================== --
-- Tabela: task_submission
-- ========================================================================== --
-- Submissões da Maria (Cenário 2)
INSERT INTO `task_submission` (`student_email`, `sequence_order`, `submission_timestamp`, `file_path`, `submission_title`, `attempt_number`) 
VALUES ('maria.oliveira@fatec.sp.gov.br', 1, NOW() - INTERVAL 20 DAY, 'Server/submissions/maria_t1_v1.md', 'Entrega Apresentação (v1)', 1);

INSERT INTO `task_submission` (`student_email`, `sequence_order`, `submission_timestamp`, `file_path`, `submission_title`, `attempt_number`) 
VALUES ('maria.oliveira@fatec.sp.gov.br', 2, NOW() - INTERVAL 15 DAY, 'Server/submissions/maria_t2_v1.md', 'Entrega PIM II (v1)', 1);

INSERT INTO `task_submission` (`student_email`, `sequence_order`, `submission_timestamp`, `file_path`, `submission_title`, `attempt_number`) 
VALUES ('maria.oliveira@fatec.sp.gov.br', 2, NOW() - INTERVAL 10 DAY, 'Server/submissions/maria_t2_v2.md', 'Correção PIM II (v2)', 2);

INSERT INTO `task_submission` (`student_email`, `sequence_order`, `submission_timestamp`, `file_path`, `submission_title`, `attempt_number`) 
VALUES ('maria.oliveira@fatec.sp.gov.br', 3, NOW() - INTERVAL 2 DAY, 'Server/submissions/maria_t3_v1.md', 'Entrega PIM III (v1)', 1);

-- Submissão do Niuan (Cenário 6)
INSERT INTO `task_submission` (`student_email`, `sequence_order`, `submission_timestamp`, `file_path`, `submission_title`, `attempt_number`) 
VALUES ('niuan.souza@fatec.sp.gov.br', 1, NOW() - INTERVAL 1 DAY, 'Server/submissions/niuan_t1_v1.md', 'Apresentação Pessoal e Acadêmica', 1);

-- ========================================================================== --
-- Tabela: task_review
-- ========================================================================== --
-- Reviews da Maria
INSERT INTO `task_review` (`student_email`, `sequence_order`, `submission_timestamp`, `reviewer_email`, `review_timestamp`, `status`, `review_comment`) 
SELECT student_email, sequence_order, submission_timestamp, 'orientador.silva@fatec.sp.gov.br', NOW() - INTERVAL 19 DAY, 'approved', 'Ótima apresentação!' 
FROM task_submission 
WHERE student_email='maria.oliveira@fatec.sp.gov.br' AND sequence_order=1 AND attempt_number=1;

INSERT INTO `task_review` (`student_email`, `sequence_order`, `submission_timestamp`, `reviewer_email`, `review_timestamp`, `status`, `review_comment`) 
SELECT student_email, sequence_order, submission_timestamp, 'orientador.silva@fatec.sp.gov.br', NOW() - INTERVAL 14 DAY, 'revision_requested', 'Faltou citar autor X no PIM II.' 
FROM task_submission 
WHERE student_email='maria.oliveira@fatec.sp.gov.br' AND sequence_order=2 AND attempt_number=1;

INSERT INTO `task_review` (`student_email`, `sequence_order`, `submission_timestamp`, `reviewer_email`, `review_timestamp`, `status`, `review_comment`) 
SELECT student_email, sequence_order, submission_timestamp, 'orientador.silva@fatec.sp.gov.br', NOW() - INTERVAL 9 DAY, 'approved', 'Agora sim, PIM II perfeito.' 
FROM task_submission 
WHERE student_email='maria.oliveira@fatec.sp.gov.br' AND sequence_order=2 AND attempt_number=2;

-- ========================================================================== --
-- Tabela: notification
-- ========================================================================== --
INSERT INTO `notification` (`user_email`, `timestamp`, `content`, `related_task_student_email`, `related_task_sequence_order`, `is_read`) VALUES
('maria.oliveira@fatec.sp.gov.br', NOW() - INTERVAL 14 DAY, 'Sua seção "Relatório PIM II" foi revisada por Ana Silva e marcada para revisão.', 'maria.oliveira@fatec.sp.gov.br', 2, 1),
('orientador.silva@fatec.sp.gov.br', NOW() - INTERVAL 10 DAY, 'A aluna Maria Eduarda Teixeira Miller de Oliveira enviou a tentativa 2 para "Relatório PIM II".', 'maria.oliveira@fatec.sp.gov.br', 2, 1),
('maria.oliveira@fatec.sp.gov.br', NOW() - INTERVAL 9 DAY, 'Sua seção "Relatório PIM II" foi revisada por Ana Silva e aprovada.', 'maria.oliveira@fatec.sp.gov.br', 2, 0),
('orientador.lima@fatec.sp.gov.br', NOW() - INTERVAL 1 DAY, 'O aluno Niuan Spolidorio da Rocha Souza enviou a tentativa 1 para "Apresentação Pessoal e Acadêmica".', 'niuan.souza@fatec.sp.gov.br', 1, 0);

-- ========================================================================== --
-- Tabela: tg_coordenacao_turma
-- ========================================================================== --
INSERT INTO `tg_coordenacao_turma` (`teacher_email`, `class_disciplina`, `class_year`, `class_semester`, `etapa_supervisionada`) VALUES
('coord.alves@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 1),
('coord.alves@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 1),
('coord.borges@fatec.sp.gov.br', 'Banco de Dados', 2025, 1, 2),
('coord.borges@fatec.sp.gov.br', 'Analise e Desenvolvimento de Sistemas', 2025, 1, 2),
('coord.borges@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 1),
('coord.borges@fatec.sp.gov.br', 'Educacao Ambiental', 2024, 2, 2);
