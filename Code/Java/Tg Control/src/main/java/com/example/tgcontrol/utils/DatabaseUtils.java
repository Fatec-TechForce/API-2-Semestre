package com.example.tgcontrol.utils;

import com.example.tgcontrol.model.*;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitário: Classe principal para consultas ao banco de dados TGControl.
 */
public class DatabaseUtils {

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtils.class.getName());

    /**
     * Função: Autentica um usuário no sistema.
     * 1. Verifica se o email e senha correspondem na tabela 'user'.
     * 2. Se sim, verifica o tipo (Professor, Aluno).
     * 3. Se não for Professor nem Aluno, retorna PERFIL_INCOMPLETO.
     * Necessita: Email (login) e senha.
     * Retorna: O TipoUsuario correspondente.
     */
    public static TipoUsuario autenticarUsuario(String login, String senha) {
        String sqlUser = "SELECT passwordHASH FROM user WHERE email = ? AND status = 'Active'";
        String userPasswordHash = null;

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(sqlUser)) {

            stmtUser.setString(1, login);
            try (ResultSet rs = stmtUser.executeQuery()) {
                if (rs.next()) {
                    userPasswordHash = rs.getString("passwordHASH");
                } else {
                    LOGGER.log(Level.WARNING, "Falha no login: Usuário não encontrado - " + login);
                    return TipoUsuario.NAO_AUTENTICADO;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Autenticação - Etapa 1): " + e.getMessage(), e);
            return TipoUsuario.NAO_AUTENTICADO;
        }

        if (userPasswordHash == null || !userPasswordHash.equals(senha)) {
            LOGGER.log(Level.WARNING, "Falha no login: Senha incorreta para - " + login);
            return TipoUsuario.NAO_AUTENTICADO;
        }

        try (Connection conn = DatabaseConnect.getConnection()) {
            String sqlTeacher = "SELECT is_coordinator FROM teacher WHERE email = ?";
            try (PreparedStatement stmtTeacher = conn.prepareStatement(sqlTeacher)) {
                stmtTeacher.setString(1, login);
                try (ResultSet rs = stmtTeacher.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean("is_coordinator") ? TipoUsuario.PROFESSOR_TG : TipoUsuario.PROFESSOR;
                    }
                }
            }

            String sqlStudent = "SELECT email FROM student WHERE email = ?";
            try (PreparedStatement stmtStudent = conn.prepareStatement(sqlStudent)) {
                stmtStudent.setString(1, login);
                try (ResultSet rs = stmtStudent.executeQuery()) {
                    if (rs.next()) {
                        return TipoUsuario.ALUNO;
                    }
                }
            }

            return TipoUsuario.PERFIL_INCOMPLETO;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Autenticação - Etapa 2): " + e.getMessage(), e);
            return TipoUsuario.NAO_AUTENTICADO;
        }
    }

    /**
     * Função: Busca as seções do TG para um aluno específico.
     * Necessita: Email do aluno.
     * Retorna: Uma lista de objetos SecaoAluno com os detalhes de cada seção, ordenada pela sequência, ou lista vazia em caso de erro.
     */
    public static List<SecaoAluno> getSecoesAluno(String emailAluno) {
        List<SecaoAluno> secoes = new ArrayList<>();
        String sql = "SELECT emailAluno, taskSequence, titulo, status, dataEntrega, statusRevisao, dataUltimaRevisao " +
                "FROM vw_secoes_aluno WHERE emailAluno = ? ORDER BY taskSequence";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailAluno);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    secoes.add(mapResultSetToSecaoAluno(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (getSecoesAluno): Falha ao buscar seções para o aluno " + emailAluno, e);
            return Collections.emptyList();
        }
        return secoes;
    }

    //public static SecaoAluno getSecaoAluno(String titulo) {

    //}

    /**
     * Função: Busca os dados para o dashboard do professor orientador.
     * Necessita: Email do professor orientador.
     * Retorna: Um objeto DashboardData contendo totais e lista de trabalhos pendentes dos seus orientandos, ou dados zerados em caso de erro.
     */
    public static DashboardData getProfessorDashboardData(String emailProfessor) {
        List<TrabalhoPendente> trabalhosPendentes = new ArrayList<>();
        int totalAlunosOrientados = 0;
        int tgsConcluidosOrientados = 0;
        String sqlTabela = "SELECT * FROM vw_professor_dashboard WHERE teacher_email = ?";
        String sqlTotalAlunos = "SELECT COUNT(DISTINCT email) AS total FROM student WHERE advisor_email = ?";
        String sqlTgsConcluidos = "SELECT COUNT(s.email) AS concluidos FROM student s WHERE s.advisor_email = ? AND NOT EXISTS (SELECT 1 FROM task t WHERE t.student_email = s.email AND t.status <> 'completed') AND EXISTS (SELECT 1 FROM task t2 WHERE t2.student_email = s.email);";

        try (Connection conn = DatabaseConnect.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sqlTabela)) {
                stmt.setString(1, emailProfessor);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        trabalhosPendentes.add(new TrabalhoPendente(
                                rs.getDouble("progresso"),
                                rs.getString("nomeAluno"),
                                rs.getString("emailAluno"),
                                rs.getString("turma"),
                                rs.getString("semestre"),
                                rs.getString("status"),
                                // NOVOS CAMPOS:
                                rs.getInt("sequence_order"),
                                rs.getTimestamp("submission_timestamp").toLocalDateTime()
                        ));
                    }
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement(sqlTotalAlunos)) {
                stmt.setString(1, emailProfessor);
                try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) totalAlunosOrientados = rs.getInt("total"); }
            }
            try (PreparedStatement stmt = conn.prepareStatement(sqlTgsConcluidos)) {
                stmt.setString(1, emailProfessor);
                try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) tgsConcluidosOrientados = rs.getInt("concluidos"); }
            }
            return new DashboardData(totalAlunosOrientados, tgsConcluidosOrientados, trabalhosPendentes.size(), trabalhosPendentes);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Dashboard Professor): " + e.getMessage(), e);
            return new DashboardData(0, 0, 0, Collections.emptyList());
        }
    }

    /**
     * Função: Busca os dados para o dashboard do Coordenador de TG.
     * Necessita: Email do Professor TG logado.
     * Retorna: Um objeto DashboardTgData contendo totais gerais, progresso agregado dos alunos das turmas coordenadas e lista DE PENDÊNCIAS DE SEUS ORIENTANDOS, ou dados zerados em caso de erro.
     * * NOTA: A lista de pendências é filtrada por orientação própria (advisor_email) para ser mais acionável (REQUISITO DA TAREFA).
     */
    public static DashboardTgData getProfessorTGDashboardData(String emailProfessorTg) {
        int totalAlunosGeral = 0; int tgsConcluidosGeral = 0; int totalOrientandosGeral = 0;
        Map<String, Integer> progressoAlunos = new LinkedHashMap<>(Map.of("Concluído", 0, "Em Dia", 0, "Atrasado", 0, "Não Iniciado", 0));

        // REUSANDO LÓGICA: Pendências de orientação própria (teacher_email == advisor_email)
        List<TrabalhoPendente> trabalhosPendentesOrientacaoPropria = new ArrayList<>();

        List<String> turmasCoordenadasFiltro = new ArrayList<>();

        String sqlResumoGeral = "SELECT SUM(numero_alunos) AS total_alunos, SUM(tgs_concluidos) AS tgs_concluidos FROM vw_class_summary";
        String sqlTotalOrientandos = "SELECT COUNT(DISTINCT advisor_email) AS total FROM student WHERE advisor_email IS NOT NULL";
        String sqlTurmasCoordenadas = "SELECT DISTINCT CONCAT(\"('\", class_disciplina, \"', \", class_year, \", \", class_semester, \")\") AS turma_tupla FROM tg_coordenacao_turma WHERE teacher_email = ?";

        // Query de pendências filtrada pelo email do Professor TG como Orientador
        String sqlPendentesOrientacaoPropria = "SELECT * FROM vw_professor_dashboard WHERE teacher_email = ?";

        try (Connection conn = DatabaseConnect.getConnection()) {

            // 1. DADOS GERAIS DO DASHBOARD
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlResumoGeral)) { if (rs.next()) { totalAlunosGeral = rs.getInt("total_alunos"); tgsConcluidosGeral = rs.getInt("tgs_concluidos"); } }
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlTotalOrientandos)) { if (rs.next()) totalOrientandosGeral = rs.getInt("total"); }

            // 2. BUSCA TURMAS COORDENADAS PARA FILTRO
            try(PreparedStatement stmt = conn.prepareStatement(sqlTurmasCoordenadas)) {
                stmt.setString(1, emailProfessorTg);
                try(ResultSet rs = stmt.executeQuery()) { while(rs.next()) turmasCoordenadasFiltro.add(rs.getString("turma_tupla")); }
            }

            // 3. SE NÃO HOUVER TURMAS COORDENADAS, RETORNA APENAS DADOS GERAIS
            if(turmasCoordenadasFiltro.isEmpty()) {
                // Mesmo que não coordene turma, ele pode ter pendências próprias de orientação
                // Então, buscamos apenas as pendências de orientação própria e retornamos
                try (PreparedStatement stmt = conn.prepareStatement(sqlPendentesOrientacaoPropria)) {
                    stmt.setString(1, emailProfessorTg);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            trabalhosPendentesOrientacaoPropria.add(new TrabalhoPendente(
                                    rs.getDouble("progresso"), rs.getString("nomeAluno"), rs.getString("emailAluno"),
                                    rs.getString("turma"), rs.getString("semestre"), rs.getString("status"),
                                    rs.getInt("sequence_order"), rs.getTimestamp("submission_timestamp").toLocalDateTime()
                            ));
                        }
                    }
                }
                Map<String, Integer> progressoVazio = new LinkedHashMap<>(Map.of("Concluído", 0, "Em Dia", 0, "Atrasado", 0, "Não Iniciado", 0));
                return new DashboardTgData(totalAlunosGeral, tgsConcluidosGeral, totalOrientandosGeral, progressoVazio, trabalhosPendentesOrientacaoPropria);
            }

            // 4. CÁLCULO DO GRÁFICO DE PROGRESSO (para alunos em turmas coordenadas)
            String filtroTurmasSql = String.join(",", turmasCoordenadasFiltro);
            String sqlDetalhesTasksCompleta = "SELECT emailAluno, estagio_aluno, estagio_task, task_status FROM vw_professortg_dashboard_tasks WHERE (turma_disciplina, turma_year, turma_semester) IN (" + filtroTurmasSql + ")";
            Map<String, String> statusAlunoMap = new HashMap<>();

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlDetalhesTasksCompleta)) {
                while (rs.next()) {
                    String emailAluno = rs.getString("emailAluno");
                    int estagioAluno = rs.getInt("estagio_aluno"); int estagioTask = rs.getInt("estagio_task"); String taskStatus = rs.getString("task_status");
                    String statusAtualAluno = statusAlunoMap.getOrDefault(emailAluno, "Concluído");
                    if (!"completed".equals(taskStatus)) {
                        if (estagioAluno > estagioTask) statusAtualAluno = "Atrasado";
                        else if (estagioAluno == estagioTask && !"locked".equals(taskStatus)) { if (!"Atrasado".equals(statusAtualAluno)) statusAtualAluno = "Em Dia"; }
                        else if (estagioAluno < estagioTask || "locked".equals(taskStatus)) { if (!"Atrasado".equals(statusAtualAluno) && !"Em Dia".equals(statusAtualAluno)) statusAtualAluno = "Não Iniciado"; }
                    }
                    statusAlunoMap.put(emailAluno, statusAtualAluno);
                }
            }
            for (String statusAgregado : statusAlunoMap.values()) {
                if (progressoAlunos.containsKey(statusAgregado)) {
                    progressoAlunos.put(statusAgregado, progressoAlunos.get(statusAgregado) + 1);
                }
            }

            // 5. LISTA DE PENDÊNCIAS (Filtrada por Orientação Própria)
            try (PreparedStatement stmt = conn.prepareStatement(sqlPendentesOrientacaoPropria)) {
                stmt.setString(1, emailProfessorTg);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        trabalhosPendentesOrientacaoPropria.add(new TrabalhoPendente(
                                rs.getDouble("progresso"), rs.getString("nomeAluno"), rs.getString("emailAluno"),
                                rs.getString("turma"), rs.getString("semestre"), rs.getString("status"),
                                rs.getInt("sequence_order"), rs.getTimestamp("submission_timestamp").toLocalDateTime()
                        ));
                    }
                }
            }

            return new DashboardTgData(totalAlunosGeral, tgsConcluidosGeral, totalOrientandosGeral, progressoAlunos, trabalhosPendentesOrientacaoPropria);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Dashboard Coordenador): " + e.getMessage(), e);
            Map<String, Integer> progressoVazio = new LinkedHashMap<>(Map.of("Concluído", 0, "Em Dia", 0, "Atrasado", 0, "Não Iniciado", 0));
            return new DashboardTgData(0, 0, 0, progressoVazio, Collections.emptyList());
        }
    }

    /**
     * Função: Busca a URL da foto de perfil para um usuário específico.
     * Necessita: Email do usuário.
     * Retorna: A string com o caminho relativo da imagem ou null se não encontrada ou em caso de erro.
     */
    public static String getProfilePictureUrl(String email) {
        String sql = "SELECT profile_picture_url FROM user WHERE email = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("profile_picture_url");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Buscar Foto Perfil): " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Função: Busca o nome completo de um usuário.
     * Necessita: Email do usuário.
     * Retorna: O nome completo ou null se não encontrado ou em caso de erro.
     */
    public static String getNomeUsuario(String email) {
        String sql = "SELECT CONCAT(FirstName, ' ', LastName) AS nomeCompleto FROM user WHERE email = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nomeCompleto");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Buscar Nome Usuário): " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Função: Busca o estágio atual do TG de um aluno e o número máximo de tarefas.
     * Necessita: Email do aluno.
     * Retorna: Um Map contendo "estagioAtual" e "maxTasks", ou null em caso de erro.
     */
    public static Map<String, Integer> getEstagioEConfigAluno(String emailAluno) {
        String sql = "SELECT s.estagio_tg_atual, c.max_tasks FROM student s JOIN class c ON s.class_disciplina = c.disciplina AND s.class_year = c.year AND s.class_semester = c.semester WHERE s.email = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emailAluno);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Integer> result = new HashMap<>();
                    result.put("estagioAtual", rs.getInt("estagio_tg_atual"));
                    result.put("maxTasks", rs.getObject("max_tasks") != null ? rs.getInt("max_tasks") : 6);
                    return result;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Buscar Estágio Aluno): " + e.getMessage(), e);
            return null;
        }
    }


    /**
     * Função: Busca a última seção ativa ou relevante para o aluno.
     * Prioriza 'in_progress', depois a última 'completed', depois a primeira 'locked'.
     * Necessita: Email do aluno.
     * Retorna: Um objeto SecaoAluno com os detalhes da seção, ou null se não houver seções ou em caso de erro.
     */
    public static SecaoAluno getUltimaSecaoAtiva(String emailAluno) {
        String sqlInProgress = "SELECT * FROM vw_secoes_aluno WHERE emailAluno = ? AND status = 'in_progress' ORDER BY taskSequence LIMIT 1";
        String sqlLastCompleted = "SELECT * FROM vw_secoes_aluno WHERE emailAluno = ? AND status = 'completed' ORDER BY taskSequence DESC LIMIT 1";
        String sqlFirstLocked = "SELECT * FROM vw_secoes_aluno WHERE emailAluno = ? AND status = 'locked' ORDER BY taskSequence LIMIT 1";
        String sqlAny = "SELECT * FROM vw_secoes_aluno WHERE emailAluno = ? ORDER BY taskSequence DESC LIMIT 1";

        SecaoAluno secao = null;

        try (Connection conn = DatabaseConnect.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sqlInProgress)) {
                stmt.setString(1, emailAluno);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        secao = mapResultSetToSecaoAluno(rs);
                    }
                }
            }

            if (secao == null) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlLastCompleted)) {
                    stmt.setString(1, emailAluno);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            secao = mapResultSetToSecaoAluno(rs);
                        }
                    }
                }
            }

            if (secao == null) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlFirstLocked)) {
                    stmt.setString(1, emailAluno);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            secao = mapResultSetToSecaoAluno(rs);
                        }
                    }
                }
            }

            if (secao == null) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlAny)) {
                    stmt.setString(1, emailAluno);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            secao = mapResultSetToSecaoAluno(rs);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Buscar Última Seção Ativa): " + e.getMessage(), e);
            return null;
        }

        return secao;
    }

    /**
     * Função auxiliar: Mapeia uma linha do ResultSet para um objeto SecaoAluno.
     * Necessita: ResultSet posicionado na linha desejada.
     * Retorna: Um objeto SecaoAluno populado.
     * Lança: SQLException se ocorrer erro ao acessar colunas.
     */
    private static SecaoAluno mapResultSetToSecaoAluno(ResultSet rs) throws SQLException {
        java.sql.Date sqlDate = rs.getDate("dataEntrega");
        LocalDate dataEntrega = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        java.sql.Timestamp sqlTimestamp = rs.getTimestamp("dataUltimaRevisao");
        LocalDateTime dataUltimaRevisao = (sqlTimestamp != null) ? sqlTimestamp.toLocalDateTime() : null;

        return new SecaoAluno(
                rs.getString("emailAluno"),
                rs.getInt("taskSequence"),
                rs.getString("titulo"),
                rs.getString("status"),
                dataEntrega,
                rs.getString("statusRevisao"),
                dataUltimaRevisao
        );
    }

    /**
     * Função: Lista todas as versões (submissões) de uma seção (task) específica para um aluno.
     * Necessita: Email do aluno e o número de sequência da tarefa (sequence_order).
     * Retorna: Uma lista de objetos VersaoTG, ordenada pela data de envio (mais recente primeiro).
     */
    public static List<VersaoTG> listarVersoesPorTask(String emailAluno, int sequence_order) {
        List<VersaoTG> versoes = new ArrayList<>();
        String sql = "SELECT attempt_number, submission_title, submission_timestamp, file_path " +
                "FROM task_submission " +
                "WHERE student_email = ? AND sequence_order = ? " +
                "ORDER BY submission_timestamp DESC";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailAluno);
            stmt.setInt(2, sequence_order);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    versoes.add(new VersaoTG(
                            rs.getInt("attempt_number"),
                            rs.getString("submission_title"),
                            rs.getTimestamp("submission_timestamp").toLocalDateTime(),
                            rs.getString("file_path")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Listar Versões por Task): " + e.getMessage(), e);
            return Collections.emptyList();
        }
        return versoes;
    }

    /**
     * Função: Realiza o upload de uma nova versão (submissão) de uma tarefa.
     * Necessita: Email do aluno, número de sequência da tarefa, nome do arquivo e caminho de armazenamento.
     * Retorna: O número da nova tentativa (versão) criada ou -1 em caso de erro.
     */
    public static int uploadNovaVersao(String emailAluno, int sequence_order, String nomeArquivo, String caminhoArquivo) {
        int proximoNumeroVersao = 1;

        String sqlMaxVersao = "SELECT MAX(attempt_number) AS max_attempt FROM task_submission " +
                "WHERE student_email = ? AND sequence_order = ?";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmtMax = conn.prepareStatement(sqlMaxVersao)) {

            stmtMax.setString(1, emailAluno);
            stmtMax.setInt(2, sequence_order);

            try (ResultSet rs = stmtMax.executeQuery()) {
                if (rs.next() && rs.getObject("max_attempt") != null) {
                    proximoNumeroVersao = rs.getInt("max_attempt") + 1;
                }
            }

            String sqlInsert = "INSERT INTO task_submission " +
                    "(student_email, sequence_order, submission_timestamp, file_path, submission_title, attempt_number) " +
                    "VALUES (?, ?, NOW(), ?, ?, ?)";

            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                stmtInsert.setString(1, emailAluno);
                stmtInsert.setInt(2, sequence_order);
                stmtInsert.setString(3, caminhoArquivo);
                stmtInsert.setString(4, nomeArquivo);
                stmtInsert.setInt(5, proximoNumeroVersao);
                stmtInsert.executeUpdate();
            }

            return proximoNumeroVersao;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Upload Nova Versão): " + e.getMessage(), e);
            return -1;
        }
    }

    /**
     * Função: Redefine a senha de um usuário no banco de dados.
     * Necessita: O email do usuário e a nova senha.
     * Retorna: true se a senha foi atualizada (usuário encontrado), false caso contrário (usuário não encontrado ou erro).
     */
    public static boolean redefinirSenha(String email, String novaSenha) {
        String sql = "UPDATE user SET passwordHASH = ? WHERE email = ?";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaSenha);
            stmt.setString(2, email);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Senha redefinida com sucesso para o usuário: " + email);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Tentativa de redefinir senha falhou: Usuário não encontrado - " + email);
                return false;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Redefinir Senha): " + e.getMessage(), e);
            return false;
        }

    }

    /**
     * Função: Registra um novo usuário na tabela 'user'.
     * Necessita: Email, Nome, Sobrenome e Senha.
     * Retorna: true se o registro for bem-sucedido.
     * Retorna: false se o usuário já existir.
     * Lança: SQLException em caso de erro no banco de dados.
     */
    public static boolean registrarUsuario(String email, String firstName, String lastName, String password) throws SQLException {
        String sqlCheck = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {

            stmtCheck.setString(1, email);
            try (ResultSet rs = stmtCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    LOGGER.log(Level.WARNING, "Falha no registro: Email já cadastrado - " + email);
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Registrar Usuário - Verificação): " + e.getMessage(), e);
            throw e;
        }

        String sqlInsert = "INSERT INTO user (email, FirstName, LastName, passwordHASH, status) VALUES (?, ?, ?, ?, 'Active')";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {

            stmtInsert.setString(1, email);
            stmtInsert.setString(2, firstName);
            stmtInsert.setString(3, lastName);
            stmtInsert.setString(4, password); // A senha em texto (como está no seu login)

            int rowsAffected = stmtInsert.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Registrar Usuário - Inserção): " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Função: Busca uma lista de nomes e emails de professores para ComboBoxes.
     * Retorna: Uma lista de strings no formato "Nome Sobrenome (email@exemplo.com)".
     */
    public static List<String> getListaProfessores() {
        List<String> professores = new ArrayList<>();
        String sql = "SELECT nomeCompleto, email FROM vw_teacher_details ORDER BY nomeCompleto";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                professores.add(String.format("%s (%s)", rs.getString("nomeCompleto"), rs.getString("email")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (getListaProfessores): " + e.getMessage(), e);
        }
        return professores;
    }

    /**
     * Função: Conclui o cadastro de um novo aluno (MODIFICADO)
     * Insere na tabela 'student' e cria suas 6 seções padrão.
     * A foto de perfil agora é tratada externamente pelo controlador.
     */
    public static boolean completarCadastroAluno(String emailAluno, Map<String, String> dadosCadastro, File arquivoAcordo) {
        Connection conn = null;
        String urlAcordo = null;

        if (arquivoAcordo != null) {
            urlAcordo = FileStorageUtils.salvarAcordoOrientacao(arquivoAcordo, emailAluno);
            if (urlAcordo == null) {
                return false;
            }
        } else {
            LOGGER.log(Level.WARNING, "completarCadastroAluno foi chamado sem um arquivoAcordo, embora seja obrigatório.");
            UIUtils.showAlert("Erro Interno", "O arquivo de acordo não foi recebido pelo servidor.");
            return false;
        }

        String problemaResolvido = dadosCadastro.getOrDefault("problema", "");
        String descricaoTask1 = "Apresentação Pessoal e Acadêmica.\n" +
                "E-mail Pessoal: " + dadosCadastro.get("emailPessoal") + "\n" +
                "Tipo de TG: " + dadosCadastro.get("tipoTG") + "\n" +
                "Problema a Resolver: " + (problemaResolvido.isEmpty() ? "N/A" : problemaResolvido);

        try {
            conn = DatabaseConnect.getConnection();
            conn.setAutoCommit(false);

            String sqlInsertStudent = "INSERT INTO student (email, personal_email, advisor_email, agreement_document_url, class_disciplina, class_year, class_semester, estagio_tg_atual) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
            try (PreparedStatement stmtStudent = conn.prepareStatement(sqlInsertStudent)) {
                stmtStudent.setString(1, emailAluno);
                stmtStudent.setString(2, dadosCadastro.get("emailPessoal"));
                stmtStudent.setString(3, dadosCadastro.get("emailOrientador"));
                stmtStudent.setString(4, urlAcordo);
                stmtStudent.setString(5, dadosCadastro.get("disciplina"));
                stmtStudent.setInt(6, Integer.parseInt(dadosCadastro.get("ano")));
                stmtStudent.setInt(7, Integer.parseInt(dadosCadastro.get("semestre")));
                stmtStudent.executeUpdate();
            }

            String sqlInsertTask = "INSERT INTO task (student_email, sequence_order, title, description, due_date, status, estagio_task) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            String[] titulos = {
                    "Apresentação Pessoal e Acadêmica",
                    "Relatório PIM II", "Relatório PIM III", "Relatório PIM IV",
                    "Relatório PIM V", "Relatório PIM VI"
            };
            String[] descricoes = {
                    descricaoTask1,
                    "Relatório referente ao PIM II", "Relatório referente ao PIM III", "Relatório referente ao PIM IV",
                    "Relatório referente ao PIM V", "Relatório referente ao PIM VI"
            };
            int[] estagios = {1, 1, 1, 1, 2, 2};
            LocalDate dataBase = LocalDate.now();

            try (PreparedStatement stmtTask = conn.prepareStatement(sqlInsertTask)) {
                for (int i = 0; i < 6; i++) {
                    stmtTask.setString(1, emailAluno);
                    stmtTask.setInt(2, i + 1);
                    stmtTask.setString(3, titulos[i]);
                    stmtTask.setString(4, descricoes[i]);
                    stmtTask.setDate(5, java.sql.Date.valueOf(dataBase.plusMonths(i + 1)));
                    stmtTask.setString(6, (i == 0) ? "in_progress" : "locked");
                    stmtTask.setInt(7, estagios[i]);
                    stmtTask.addBatch();
                }
                stmtTask.executeBatch();
            }


            conn.commit();
            return true;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Completar Cadastro Aluno - Transação): " + e.getMessage(), e);
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { LOGGER.log(Level.SEVERE, "DB FALHA (Rollback): " + ex.getMessage(), ex); }
            }
            UIUtils.showAlert("Erro de Cadastro", "Não foi possível salvar os dados no banco: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Função: Atualiza a URL da foto de perfil de um usuário.
     * Usado pelos Forms_Aluno_C e Forms_Professor_C.
     * Necessita: Email do usuário e o caminho relativo da nova foto.
     */
    public static void atualizarFotoPerfil(String email, String urlFoto) {
        String sqlUpdateUser = "UPDATE user SET profile_picture_url = ? WHERE email = ?";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateUser)) {

            stmtUpdate.setString(1, urlFoto);
            stmtUpdate.setString(2, email);
            stmtUpdate.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Atualizar Foto Perfil): Não foi possível atualizar a foto para " + email, e);
        }
    }

    /**
     * Função: Busca uma lista de turmas disponíveis.
     * Retorna: Uma lista de objetos Turma, ideal para ComboBoxes e TableViews.
     */
    public static List<Turma> getListaTurmas() {
        List<Turma> turmas = new ArrayList<>();
        String sql = "SELECT disciplina, year, semester FROM class ORDER BY year DESC, semester DESC, disciplina";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                turmas.add(new Turma( // Cria o objeto Turma único
                        rs.getString("disciplina"),
                        rs.getInt("year"),
                        rs.getInt("semester")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (getListaTurmas): " + e.getMessage(), e);
        }
        return turmas;
    }

    /**
     * Função: Conclui o cadastro de um novo professor.
     */
    public static boolean completarCadastroProfessor(String emailProfessor, boolean isCoordenador, Map<Turma, List<Integer>> coordenacoes) {
        Connection conn = null;

        try {
            conn = DatabaseConnect.getConnection();
            conn.setAutoCommit(false);

            String sqlTeacher = "INSERT INTO teacher (email, is_coordinator) VALUES (?, ?)";
            try (PreparedStatement stmtTeacher = conn.prepareStatement(sqlTeacher)) {
                stmtTeacher.setString(1, emailProfessor);
                stmtTeacher.setBoolean(2, isCoordenador);
                stmtTeacher.executeUpdate();
            }

            if (isCoordenador && coordenacoes != null && !coordenacoes.isEmpty()) {
                String sqlCoord = "INSERT INTO tg_coordenacao_turma (teacher_email, class_disciplina, class_year, class_semester, etapa_supervisionada) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement stmtCoord = conn.prepareStatement(sqlCoord)) {

                    for (Map.Entry<Turma, List<Integer>> entry : coordenacoes.entrySet()) {
                        Turma turma = entry.getKey();
                        List<Integer> etapas = entry.getValue();

                        for (Integer etapa : etapas) {
                            stmtCoord.setString(1, emailProfessor);
                            stmtCoord.setString(2, turma.getDisciplina());
                            stmtCoord.setInt(3, turma.getAno());
                            stmtCoord.setInt(4, turma.getSemestre());
                            stmtCoord.setInt(5, etapa);
                            stmtCoord.addBatch();
                        }
                    }
                    stmtCoord.executeBatch();
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Completar Cadastro Professor - Transação): " + e.getMessage(), e);
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { LOGGER.log(Level.SEVERE, "DB FALHA (Rollback): " + ex.getMessage(), ex); }
            }
            UIUtils.showAlert("Erro de Cadastro", "Não foi possível salvar os dados no banco: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Função: Busca uma lista de turmas que um Professor TG específico coordena.
     * Necessita: Email do Professor TG.
     * Retorna: Uma lista de objetos Turma que o professor coordena.
     */
    public static List<Turma> getListaTurmasCoordenadas(String emailProfessorTg) {
        List<Turma> turmas = new ArrayList<>();
        String sql = "SELECT DISTINCT c.disciplina, c.year, c.semester " +
                "FROM class c " +
                "JOIN tg_coordenacao_turma coord ON c.disciplina = coord.class_disciplina " +
                "  AND c.year = coord.class_year " +
                "  AND c.semester = coord.class_semester " +
                "WHERE coord.teacher_email = ? " +
                "ORDER BY c.year DESC, c.semester DESC, c.disciplina";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailProfessorTg);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    turmas.add(new Turma(
                            rs.getString("disciplina"),
                            rs.getInt("year"),
                            rs.getInt("semester")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (getListaTurmasCoordenadas): " + e.getMessage(), e);
        }
        return turmas;
    }

    /**
     * Função: Busca uma lista de alunos orientados por um professor para exibição.
     * Necessita: Email do Professor.
     * Retorna: Uma lista de Mapas contendo nomeCompleto, email, turma_descricao e profile_picture_url.
     */
    public static List<Map<String, String>> getAdviseesDisplayInfo(String emailProfessor) {
        List<Map<String, String>> students = new ArrayList<>();
        String sql = "SELECT nomeCompleto, email, turma_descricao, profile_picture_url " +
                "FROM vw_student_details WHERE advisor_email = ? ORDER BY nomeCompleto";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailProfessor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> student = new HashMap<>();
                    student.put("email", rs.getString("email"));
                    student.put("nomeCompleto", rs.getString("nomeCompleto"));
                    student.put("turma_descricao", rs.getString("turma_descricao"));
                    student.put("profile_picture_url", rs.getString("profile_picture_url"));
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (getAdviseesDisplayInfo): " + e.getMessage(), e);
        }
        return students;
    }

    /**
     * Função: Busca uma lista de alunos de uma turma específica para exibição.
     * Necessita: Disciplina, ano e semestre da turma.
     * Retorna: Uma lista de Mapas contendo nomeCompleto, email, turma_descricao e profile_picture_url.
     */
    public static List<Map<String, String>> getStudentsByClass(String disciplina, int year, int semester) {
        List<Map<String, String>> students = new ArrayList<>();
        String sql = "SELECT nomeCompleto, email, turma_descricao, profile_picture_url " +
                "FROM vw_student_details WHERE class_disciplina = ? AND class_year = ? AND class_semester = ? ORDER BY nomeCompleto";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, disciplina);
            stmt.setInt(2, year);
            stmt.setInt(3, semester);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> student = new HashMap<>();
                    student.put("email", rs.getString("email"));
                    student.put("nomeCompleto", rs.getString("nomeCompleto"));
                    student.put("turma_descricao", rs.getString("turma_descricao"));
                    student.put("profile_picture_url", rs.getString("profile_picture_url"));
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (getStudentsByClass): " + e.getMessage(), e);
        }
        return students;
    }
    // --- Métodos de Notificação ---

    /**
     * Função: Grava uma nova notificação no banco de dados.
     * Necessita: Email do usuário, conteúdo da mensagem, e opcionalmente, detalhes da tarefa relacionada.
     * Retorna: true se a notificação foi gravada com sucesso, false caso contrário.
     */
    public static boolean enviarNotificacao(String userEmail, String content, String relatedTaskStudentEmail, int relatedTaskSequenceOrder) {
        String sql = "INSERT INTO notification (user_email, content, related_task_student_email, related_task_sequence_order) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userEmail);
            stmt.setString(2, content);
            // Os campos de tarefa relacionada podem ser NULL
            if (relatedTaskStudentEmail != null && !relatedTaskStudentEmail.trim().isEmpty()) {
                stmt.setString(3, relatedTaskStudentEmail);
            } else {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            }
            if (relatedTaskSequenceOrder > 0) {
                stmt.setInt(4, relatedTaskSequenceOrder);
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();
            // Envio opcional por e-mail (simulação)
            enviarEmailOpcional(userEmail, "Nova Notificação no TGControl", content);

            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (enviarNotificacao): " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Função: Lista todas as notificações para um usuário, ordenadas da mais recente para a mais antiga.
     * Necessita: Email do usuário.
     * Retorna: Uma lista de objetos Notification.
     */
    public static List<Notification> listarNotificacoes(String userEmail) {
        List<Notification> notificacoes = new ArrayList<>();
        String sql = "SELECT notification_id, user_email, timestamp, content, related_task_student_email, related_task_sequence_order, is_read " +
                "FROM notification WHERE user_email = ? ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("notification_id");
                    String email = rs.getString("user_email");
                    // O timestamp é um DATETIME no MySQL, que mapeia para Timestamp no JDBC
                    Timestamp ts = rs.getTimestamp("timestamp");
                    LocalDateTime timestamp = ts != null ? ts.toLocalDateTime() : null;
                    String content = rs.getString("content");
                    String relatedStudentEmail = rs.getString("related_task_student_email");
                    // O getInt retorna 0 se o valor for NULL, o que pode ser problemático.
                    // Usaremos o wrapper para verificar se é NULL.
                    int relatedSequence = rs.getObject("related_task_sequence_order") != null ? rs.getInt("related_task_sequence_order") : 0;
                    boolean isRead = rs.getBoolean("is_read");

                    notificacoes.add(new Notification(
                            id, email, timestamp, content, relatedStudentEmail, relatedSequence, isRead
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (listarNotificacoes): " + e.getMessage(), e);
        }
        return notificacoes;
    }

    /**
     * Função: Marca uma notificação específica como lida.
     * Necessita: ID da notificação.
     * Retorna: true se a atualização foi bem-sucedida, false caso contrário.
     */
    public static boolean marcarComoLida(int notificationId) {
        String sql = "UPDATE notification SET is_read = TRUE WHERE notification_id = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificationId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (marcarComoLida): " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Função: Marca todas as notificações de um usuário como lidas.
     * Necessita: Email do usuário.
     * Retorna: true se a atualização foi bem-sucedida, false caso contrário.
     */
    public static boolean marcarTodasComoLidas(String userEmail) {
        String sql = "UPDATE notification SET is_read = TRUE WHERE user_email = ? AND is_read = FALSE";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userEmail);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (marcarTodasComoLidas): " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Função: Conta o número de notificações não lidas para um usuário.
     * Necessita: Email do usuário.
     * Retorna: O número de notificações não lidas.
     */
    public static int contarNotificacoesNaoLidas(String userEmail) {
        String sql = "SELECT COUNT(*) AS total FROM notification WHERE user_email = ? AND is_read = FALSE";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (contarNotificacoesNaoLidas): " + e.getMessage(), e);
        }
        return 0;
    }
    /**
     * Função: Estrutura o envio opcional de e-mail (método dummy).
     * Necessita: Email do destinatário, assunto e corpo do e-mail.
     * Retorna: true (simulando sucesso).
     */
    public static boolean enviarEmailOpcional(String destinatario, String assunto, String corpo) {
        // Log para simular o envio de e-mail, conforme requisito
        LOGGER.log(Level.INFO, "SIMULAÇÃO DE E-MAIL ENVIADO para: " + destinatario +
                " | Assunto: " + assunto +
                " | Corpo: " + corpo.substring(0, Math.min(corpo.length(), 50)) + "...");
        return true;
    }

    public static String getCaminhoArquivoSubmissao(String emailAluno, int sequencia, LocalDateTime dataEnvio) {
        String sql = "SELECT file_path FROM task_submission WHERE student_email = ? AND sequence_order = ? AND submission_timestamp = ?";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailAluno);
            stmt.setInt(2, sequencia);
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(dataEnvio));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("file_path");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Buscar Caminho Arquivo): " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Registra a avaliação do professor e envia uma notificação automática para o aluno.
     */
    public static boolean salvarAvaliacaoProfessor(String emailAluno, int sequencia, LocalDateTime dataSubmissao,
                                                   String emailProfessor, String status, String comentario) {

        String sql = "INSERT INTO task_review (student_email, sequence_order, submission_timestamp, reviewer_email, status, review_comment, review_timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailAluno);
            stmt.setInt(2, sequencia);
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(dataSubmissao));
            stmt.setString(4, emailProfessor);
            stmt.setString(5, status);
            stmt.setString(6, comentario);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                // --- INTEGRAÇÃO COM SEU MÉTODO DE NOTIFICAÇÃO ---

                // 1. Busca o título da tarefa para a mensagem ficar mais bonita
                String tituloTask = getTituloTask(emailAluno, sequencia);
                String mensagem;

                // 2. Define a mensagem baseada na decisão
                if ("approved".equals(status)) {
                    mensagem = "Parabéns! Sua entrega para '" + tituloTask + "' foi APROVADA pelo orientador.";
                } else {
                    mensagem = "Atenção: Sua entrega para '" + tituloTask + "' requer revisão. Veja o feedback do orientador.";
                }

                // 3. Chama o SEU método existente enviarNotificacao
                enviarNotificacao(
                        emailAluno,             // Destinatário (Aluno)
                        mensagem,               // Conteúdo
                        emailAluno,             // Email relacionado à task
                        sequencia               // ID da task relacionada
                );

                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Salvar Avaliação): " + e.getMessage(), e);
        }
        return false;
    }
    /**
     * Busca o título de uma Task baseado no email do aluno e no número da sequência.
     */
    public static String getTituloTask(String emailAluno, int sequencia) {
        String sql = "SELECT title FROM task WHERE student_email = ? AND sequence_order = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailAluno);
            stmt.setInt(2, sequencia);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("title");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar título da task: " + e.getMessage(), e);
        }
        return "Seção " + sequencia; // Retorno padrão caso falhe
    }

    /**
     * Busca o histórico de versões (submissões e revisões) de uma tarefa.
     */
    public static List<HistoricoVersao> getHistoricoVersoes(String emailAluno, int sequencia) {
        List<HistoricoVersao> historico = new ArrayList<>();

        // Fazemos LEFT JOIN com review porque pode ter submissão sem revisão ainda
        String sql = "SELECT ts.attempt_number, ts.submission_timestamp, ts.file_path, " +
                "       tr.review_comment, tr.status " +
                "FROM task_submission ts " +
                "LEFT JOIN task_review tr " +
                "  ON ts.student_email = tr.student_email " +
                "  AND ts.sequence_order = tr.sequence_order " +
                "  AND ts.submission_timestamp = tr.submission_timestamp " +
                "WHERE ts.student_email = ? AND ts.sequence_order = ? " +
                "ORDER BY ts.submission_timestamp DESC"; // Do mais recente para o mais antigo

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailAluno);
            stmt.setInt(2, sequencia);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    historico.add(new HistoricoVersao(
                            rs.getInt("attempt_number"),
                            rs.getTimestamp("submission_timestamp").toLocalDateTime(),
                            rs.getString("file_path"),
                            rs.getString("review_comment"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar histórico: " + e.getMessage(), e);
        }
        return historico;
    }

    /**
     * Busca a data/hora da última submissão de uma tarefa para abrir na correção.
     */
    public static LocalDateTime getUltimaDataSubmissao(String emailAluno, int sequencia) {
        String sql = "SELECT MAX(submission_timestamp) as ultima_data FROM task_submission WHERE student_email = ? AND sequence_order = ?";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailAluno);
            stmt.setInt(2, sequencia);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getTimestamp("ultima_data") != null) {
                    return rs.getTimestamp("ultima_data").toLocalDateTime();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar última data de submissão: " + e.getMessage(), e);
        }
        return null;
    }


    /**
     * Função: Verifica se todas as tarefas de um aluno foram marcadas como 'completed'.
     * @param emailAluno Email do aluno.
     * @return true se o número de tarefas completas for igual ao máximo de tarefas configurado, false caso contrário.
     */
    public static boolean isTgConcluido(String emailAluno) {
        String sql = "SELECT COUNT(*) as completed_tasks, c.max_tasks " +
                "FROM task t " +
                "JOIN student s ON t.student_email = s.email " +
                "JOIN class c ON s.class_disciplina = c.disciplina AND s.class_year = c.year AND s.class_semester = c.semester " +
                "WHERE t.student_email = ? AND t.status = 'completed' " +
                "GROUP BY c.max_tasks";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailAluno);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int completedTasks = rs.getInt("completed_tasks");
                    int maxTasks = rs.getInt("max_tasks");
                    return completedTasks >= maxTasks;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (isTgConcluido): Falha ao verificar conclusão do TG para o aluno " + emailAluno, e);
        }
        return false;
    }

    /**
     * Função: Busca detalhes de exibição de um aluno (nome do orientador e descrição da turma) usando a view vw_student_details.
     * @param emailAluno Email do aluno.
     * @return Um Map com os detalhes ou null se o email não for de um aluno.
     */
    public static Map<String, String> getStudentDisplayDetails(String emailAluno) {
        Map<String, String> details = new HashMap<>();
        // Usa a VIEW vw_student_details que já tem a descrição da turma
        String sql = "SELECT vsd.turma_descricao, u_adv.FirstName, u_adv.LastName, vsd.advisor_email " +
                "FROM vw_student_details vsd " +
                "LEFT JOIN user u_adv ON vsd.advisor_email = u_adv.email " +
                "WHERE vsd.email = ?";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailAluno);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String advisorName = null;
                    if (rs.getString("FirstName") != null) {
                        advisorName = rs.getString("FirstName") + " " + rs.getString("LastName");
                    }

                    details.put("turma_descricao", rs.getString("turma_descricao"));
                    details.put("advisor_name", advisorName != null ? advisorName : "Não Atribuído");
                    details.put("advisor_email", rs.getString("advisor_email"));
                    return details;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (getStudentDisplayDetails): Falha ao buscar detalhes do aluno " + emailAluno, e);
        }
        return null;
    }
    /**
     * Função: Registra o agendamento da defesa do TG.
     */
    public static boolean agendarDefesa(String studentEmail, String schedulerEmail, LocalDateTime dataHora, String localDefesa, String bancaAvaliadora) {
        String sql = "INSERT INTO defesa_tg (student_email, scheduler_email, data_hora_defesa, local_defesa, banca_avaliadora, status_defesa) " +
                "VALUES (?, ?, ?, ?, ?, 'Agendada')";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentEmail);
            stmt.setString(2, schedulerEmail);
            stmt.setTimestamp(3, Timestamp.valueOf(dataHora));
            stmt.setString(4, localDefesa);
            stmt.setString(5, bancaAvaliadora);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                String content = "Sua defesa de TG foi agendada para: " + dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")) + " no local: " + localDefesa;
                enviarNotificacao(studentEmail, content, null, 0);
                LOGGER.log(Level.INFO, "Defesa agendada para: " + studentEmail);
                return true;
            }

        } catch (SQLException e) {
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                LOGGER.log(Level.WARNING, "Conflito de agendamento: Possível data/local já ocupado.", e);
                UIUtils.showAlert("Erro de Agendamento", "O horário ou local selecionado já está ocupado. Verifique a agenda.");
            } else {
                LOGGER.log(Level.SEVERE, "DB FALHA (Agendar Defesa): " + e.getMessage(), e);
                UIUtils.showAlert("Erro de Banco de Dados", "Não foi possível salvar o agendamento.");
            }
        }
        return false;
    }
}