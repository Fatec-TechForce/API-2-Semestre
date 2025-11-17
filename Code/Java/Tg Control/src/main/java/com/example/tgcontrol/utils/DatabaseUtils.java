package com.example.tgcontrol.utils;

import com.example.tgcontrol.model.*;
import java.sql.*;
import java.time.LocalDate;
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
     * Necessita: Email (login) e senha.
     * Retorna: O TipoUsuario correspondente (ALUNO, PROFESSOR, etc.) ou NAO_AUTENTICADO se falhar.
     */
    public static TipoUsuario autenticarUsuario(String login, String senha) {
        String sql = "SELECT tipoUsuario FROM vw_user_login WHERE email = ? AND passwordHASH = ?";
        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipoUsuarioString = rs.getString("tipoUsuario");
                    try {
                        return TipoUsuario.valueOf(tipoUsuarioString);
                    } catch (IllegalArgumentException e) {
                        LOGGER.log(Level.SEVERE, "DB ERRO: Tipo de usuário inválido encontrado: " + tipoUsuarioString, e);
                        return TipoUsuario.NAO_AUTENTICADO;
                    }
                } else {
                    return TipoUsuario.NAO_AUTENTICADO;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Autenticação): " + e.getMessage(), e);
            // Retorna não autenticado em caso de erro de DB
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
        String sql = "SELECT emailAluno, taskSequence, titulo, status, dataEntrega, statusRevisao " +
                "FROM vw_secoes_aluno WHERE emailAluno = ? ORDER BY taskSequence";
            return secoes;
    }

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
                                rs.getDouble("progresso"), rs.getString("nomeAluno"), rs.getString("emailAluno"),
                                rs.getString("turma"), rs.getString("semestre"), rs.getString("status")
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
            // Retorna dados zerados em caso de erro de DB
            return new DashboardData(0, 0, 0, Collections.emptyList());
        }
    }

    /**
     * Função: Busca os dados para o dashboard do Coordenador de TG.
     * Necessita: Email do Professor TG logado.
     * Retorna: Um objeto DashboardTgData contendo totais gerais, progresso agregado dos alunos das turmas coordenadas e lista geral de pendências, ou dados zerados em caso de erro.
     */
    public static DashboardTgData getProfessorTGDashboardData(String emailProfessorTg) {
        int totalAlunosGeral = 0; int tgsConcluidosGeral = 0; int totalOrientandosGeral = 0;
        Map<String, Integer> progressoAlunos = new LinkedHashMap<>(Map.of("Concluído", 0, "Em Dia", 0, "Atrasado", 0, "Não Iniciado", 0));
        List<TrabalhoPendente> trabalhosPendentesGeral = new ArrayList<>();
        List<String> turmasCoordenadasFiltro = new ArrayList<>();

        String sqlResumoGeral = "SELECT SUM(numero_alunos) AS total_alunos, SUM(tgs_concluidos) AS tgs_concluidos FROM vw_class_summary";
        String sqlTotalOrientandos = "SELECT COUNT(DISTINCT advisor_email) AS total FROM student WHERE advisor_email IS NOT NULL";
        String sqlTurmasCoordenadas = "SELECT DISTINCT CONCAT(\"('\", class_disciplina, \"', \", class_year, \", \", class_semester, \")\") AS turma_tupla FROM tg_coordenacao_turma WHERE teacher_email = ?";
        String sqlDetalhesTasksBase = "SELECT emailAluno, estagio_aluno, estagio_task, task_status FROM vw_professortg_dashboard_tasks WHERE (turma_disciplina, turma_year, turma_semester) IN ";
        String sqlPendentesGeral = "SELECT * FROM vw_professor_dashboard";

        try (Connection conn = DatabaseConnect.getConnection()) {
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlResumoGeral)) { if (rs.next()) { totalAlunosGeral = rs.getInt("total_alunos"); tgsConcluidosGeral = rs.getInt("tgs_concluidos"); } }
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlTotalOrientandos)) { if (rs.next()) totalOrientandosGeral = rs.getInt("total"); }
            try(PreparedStatement stmt = conn.prepareStatement(sqlTurmasCoordenadas)) {
                stmt.setString(1, emailProfessorTg);
                try(ResultSet rs = stmt.executeQuery()) { while(rs.next()) turmasCoordenadasFiltro.add(rs.getString("turma_tupla")); }
            }

            if(turmasCoordenadasFiltro.isEmpty()) {
                // Se o professor não coordena turmas, retorna dados padrão (vazio/zero)
                return new DashboardTgData(totalAlunosGeral, tgsConcluidosGeral, totalOrientandosGeral, progressoAlunos, trabalhosPendentesGeral);
            }

            String filtroTurmasSql = String.join(",", turmasCoordenadasFiltro);
            String sqlDetalhesTasksCompleta = sqlDetalhesTasksBase + "(" + filtroTurmasSql + ")";
            Map<String, String> statusAlunoMap = new HashMap<>(); // email -> Status Agregado

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
            // Contabiliza os status agregados dos alunos
            for (String statusAgregado : statusAlunoMap.values()) {
                if (progressoAlunos.containsKey(statusAgregado)) { // Garante que a chave existe
                    progressoAlunos.put(statusAgregado, progressoAlunos.get(statusAgregado) + 1);
                }
            }

            // Busca trabalhos pendentes GERAL
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlPendentesGeral)) {
                while (rs.next()) {
                    trabalhosPendentesGeral.add(new TrabalhoPendente(
                            rs.getDouble("progresso"), rs.getString("nomeAluno"), rs.getString("emailAluno"),
                            rs.getString("turma"), rs.getString("semestre"), rs.getString("status")
                    ));
                }
            }
            return new DashboardTgData(totalAlunosGeral, tgsConcluidosGeral, totalOrientandosGeral, progressoAlunos, trabalhosPendentesGeral);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Dashboard Coordenador): " + e.getMessage(), e);
            // Retorna dados zerados em caso de erro de DB
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
                    // Retorna o valor da coluna, que pode ser null se não houver foto definida
                    return rs.getString("profile_picture_url");
                } else {
                    // Usuário não encontrado (pouco provável se já logado, mas por segurança)
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB FALHA (Buscar Foto Perfil): " + e.getMessage(), e);
            // Retorna null em caso de erro de DB
            return null;
        }
    }

    // Dentro da classe DatabaseUtils

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
                    // max_tasks pode ser null no DB, default para 6 se for
                    result.put("maxTasks", rs.getObject("max_tasks") != null ? rs.getInt("max_tasks") : 6);
                    return result;
                } else {
                    return null; // Aluno não encontrado
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

    private static SecaoAluno mapResultSetToSecaoAluno(ResultSet rs) throws SQLException {
        java.sql.Date sqlDate = rs.getDate("dataEntrega");
        LocalDate dataEntrega = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        return new SecaoAluno(
                rs.getString("emailAluno"),
                rs.getInt("taskSequence"),
                rs.getString("titulo"),
                rs.getString("status"),
                dataEntrega,
                rs.getString("statusRevisao")
        );
    }

    /**
     * Função: Lista todas as versões (submissões) de uma seção (task) específica para um aluno.
     * Necessita: Email do aluno e o número de sequência da tarefa (sequence_order).
     * Retorna: Uma lista de objetos VersaoTG, ordenada pela data de envio (mais recente primeiro).
     */
    public static List<VersaoTG> listarVersoesPorTask(String emailAluno, int sequence_order) {
        List<VersaoTG> versoes = new ArrayList<>();
        // Agora buscamos da tabela task_submission
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
                            rs.getInt("attempt_number"), //
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
     * Retorna: O número da nova tentativa (versão) criada.
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

            // Inserir nova submissão na tabela task_submission
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
}