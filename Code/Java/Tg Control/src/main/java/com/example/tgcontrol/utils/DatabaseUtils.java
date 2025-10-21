package com.example.tgcontrol.utils;

import com.example.tgcontrol.model.*; // Importa todos os seus models
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe utilitária principal para consultas ao banco de dados.
 * Cada método público tenta executar uma consulta SQL real.
 * Se a conexão falhar (SQLException), ele chama um método _Mock privado
 * como fallback, garantindo que a aplicação continue funcionando para demonstração.
 */
public class DatabaseUtils {

    // ---------------------------------------------------------------------------------
    // MÉTODO 1: AUTENTICAÇÃO (SIMPLIFICADO COM A VIEW)
    // ---------------------------------------------------------------------------------

    /**
     * Tenta autenticar o usuário contra o banco de dados real.
     * Esta versão usa a VIEW 'vw_user_login' para simplificar a lógica.
     * Se o banco falhar, usa a simulação.
     */
    public static TipoUsuario autenticarUsuario(String login, String senha) {

        // A query agora é trivial. Nós pedimos o 'tipoUsuario' direto da VIEW.
        String sql = "SELECT tipoUsuario FROM vw_user_login WHERE email = ? AND passwordHASH = ?";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            System.out.println("Autenticação: Conectado ao DB, executando query (via VIEW)...");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 1. Pega a string do tipo de usuário (ex: "ALUNO", "PROFESSOR_TG")
                    String tipoUsuarioString = rs.getString("tipoUsuario");
                    System.out.println("Autenticação: Usuário encontrado no DB. Tipo: " + tipoUsuarioString);

                    // 2. Converte a String do banco para o Enum Java
                    try {
                        return TipoUsuario.valueOf(tipoUsuarioString);
                    } catch (IllegalArgumentException e) {
                        // Caso a string no DB (ex: 'ADMIN') não exista no Enum Java
                        System.err.println("Erro: Tipo de usuário no DB inválido: " + tipoUsuarioString);
                        return TipoUsuario.NAO_AUTENTICADO;
                    }

                } else {
                    System.out.println("Autenticação: Usuário/senha inválidos ou inativo.");
                    return TipoUsuario.NAO_AUTENTICADO; // Falha no login
                }
            }
        } catch (SQLException e) {
            System.err.println("!!! FALHA DE CONEXÃO COM O BANCO DE DADOS (Autenticação) !!!");
            System.err.println("Causa: " + e.getMessage());
            System.err.println("Usando dados de simulação (MOCK) como fallback...");
            return autenticarUsuario_Mock(login, senha); // FALLBACK
        }
    }

    /**
     * SIMULAÇÃO (Mock) de autenticação
     */
    private static TipoUsuario autenticarUsuario_Mock(String login, String senha) {
        if (!senha.equals("Troca123")) {
            return TipoUsuario.NAO_AUTENTICADO;
        }
        switch (login) {
            case "aluno.escola@fatec.sp.gov.br":
                return TipoUsuario.ALUNO;
            case "professor1.escola@fatec.sp.gov.br":
                return TipoUsuario.PROFESSOR;
            case "professor2.escola@fatec.sp.gov.br":
                return TipoUsuario.PROFESSOR;
            case "professortg.escola@fatec.sp.gov.br":
                return TipoUsuario.PROFESSOR_TG;
            default:
                return TipoUsuario.NAO_AUTENTICADO;
        }
    }


    // ---------------------------------------------------------------------------------
    // MÉTODO 2: SEÇÕES DO ALUNO (Secoes_Aluno_C)
    // ---------------------------------------------------------------------------------

    /**
     * Tenta buscar as seções do aluno logado do banco de dados (usando a VIEW vw_secoes_aluno).
     * Se o banco falhar, usa a simulação.
     */
    public static List<SecaoAluno> getSecoesAluno(String emailAluno) {
        List<SecaoAluno> secoes = new ArrayList<>();

        // A VIEW 'vw_secoes_aluno' faz todo o trabalho pesado.
        String sql = "SELECT taskId, titulo, status, dataEntrega, statusRevisao " +
                "FROM vw_secoes_aluno " +
                "WHERE emailAluno = ?";

        try (Connection conn = DatabaseConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emailAluno);
            System.out.println("Seções Aluno: Conectado ao DB, buscando seções para " + emailAluno);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SecaoAluno secao = new SecaoAluno(
                            rs.getInt("taskId"),
                            rs.getString("titulo"),
                            rs.getString("status"),
                            rs.getObject("dataEntrega", LocalDate.class), // Converte DATE do SQL para LocalDate do Java
                            rs.getString("statusRevisao")
                    );
                    secoes.add(secao);
                }
            }
            System.out.println("Seções Aluno: Encontradas " + secoes.size() + " seções no DB.");
            return secoes;

        } catch (SQLException e) {
            System.err.println("!!! FALHA DE CONEXÃO COM O BANCO DE DADOS (Seções Aluno) !!!");
            System.err.println("Causa: " + e.getMessage());
            System.err.println("Usando dados de simulação (MOCK) como fallback...");
            return getSecoesAluno_Mock(emailAluno); // FALLBACK
        }
    }

    /**
     * SIMULAÇÃO (Mock) de seções do aluno
     */
    private static List<SecaoAluno> getSecoesAluno_Mock(String emailAluno) {
        List<SecaoAluno> secoes = new ArrayList<>();
        secoes.add(new SecaoAluno(1, "1. (MOCK) Definição do Tema", "completed", LocalDate.of(2025, 8, 30), "Aprovado"));
        secoes.add(new SecaoAluno(2, "2. (MOCK) Revisão Bibliográfica", "in_progress", LocalDate.of(2025, 9, 15), "Revisão Solicitada"));
        secoes.add(new SecaoAluno(3, "3. (MOCK) Metodologia", "in_progress", LocalDate.of(2025, 10, 1), "Pendente"));
        secoes.add(new SecaoAluno(4, "4. (MOCK) Desenvolvimento", "locked", LocalDate.of(2025, 10, 30), "---"));
        secoes.add(new SecaoAluno(5, "5. (MOCK) Conclusão", "locked", LocalDate.of(2025, 11, 15), "---"));
        return secoes;
    }


    // ---------------------------------------------------------------------------------
    // MÉTODO 3: DASHBOARD PROFESSOR (DashboardData)
    // ---------------------------------------------------------------------------------

    /**
     * Tenta buscar os dados do dashboard do professor do banco de dados (usando a VIEW vw_professor_dashboard).
     * Se o banco falhar, usa a simulação.
     */
    public static DashboardData getProfessorDashboardData(String emailProfessor) {

        // Esta VIEW alimenta a tabela de 'TrabalhoPendente'
        String sqlTabela = "SELECT * FROM vw_professor_dashboard WHERE teacher_email = ?";

        List<TrabalhoPendente> trabalhos = new ArrayList<>();
        int totalAlunos = 0;
        int tgsConcluidos = 0;

        try (Connection conn = DatabaseConnect.getConnection()) {

            System.out.println("Dashboard Professor: Conectado ao DB, buscando dados para " + emailProfessor);

            // Query 1: Popula a tabela de trabalhos pendentes
            try (PreparedStatement stmt = conn.prepareStatement(sqlTabela)) {
                stmt.setString(1, emailProfessor);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        TrabalhoPendente tp = new TrabalhoPendente(
                                rs.getDouble("progresso"),
                                rs.getString("nomeAluno"),
                                rs.getString("emailAluno"),
                                rs.getString("turma"),
                                rs.getString("semestre"),
                                rs.getString("status")
                        );
                        trabalhos.add(tp);
                    }
                }
            }

            // TODO: Implementar queries para os K-Cards (Total Alunos, TGs Concluídos)
            // A VIEW é ótima para a tabela, mas ineficiente para os contadores.
            // Você deve fazer consultas separadas, mais simples, para isso.
            // Ex: "SELECT COUNT(DISTINCT student.email) FROM student JOIN teacher_class..."

            System.out.println("Dashboard Professor: Encontrados " + trabalhos.size() + " trabalhos pendentes no DB.");

            if (trabalhos.isEmpty()) {
                // Se o DB não retornou nada, pode ser um professor sem alunos
                // Vamos usar o mock para ter dados de visualização
                throw new SQLException("Nenhum dado encontrado no DB, usando mock.");
            }

            // Retorna os dados REAIS do banco (com contadores zerados)
            return new DashboardData(totalAlunos, tgsConcluidos, trabalhos.size(), trabalhos);

        } catch (SQLException e) {
            System.err.println("!!! FALHA DE CONEXÃO OU QUERY (Dashboard Professor) !!!");
            System.err.println("Causa: " + e.getMessage());
            System.err.println("Usando dados de simulação (MOCK) como fallback...");
            return getProfessorDashboardData_Mock(emailProfessor); // FALLBACK
        }
    }

    /**
     * SIMULAÇÃO (Mock) de dados do dashboard do professor
     */
    private static DashboardData getProfessorDashboardData_Mock(String emailProfessor) {
        if (emailProfessor == null || emailProfessor.isEmpty()) {
            return new DashboardData(0, 0, 0, Collections.emptyList());
        }
        if ("professor1.escola@fatec.sp.gov.br".equals(emailProfessor)) {
            List<TrabalhoPendente> trabalhos = new ArrayList<>();
            trabalhos.add(new TrabalhoPendente(1.0, "(MOCK) Guilherme", "guilherme.arruda@aluno.fatec.sp.gov.br", "YT_BANCO", "2025", "Pendente"));
            trabalhos.add(new TrabalhoPendente(0.9, "(MOCK) Maria", "maria.oliveira@aluno.fatec.sp.gov.br", "IA_JOGOS", "2025", "Pendente"));
            return new DashboardData(90, 15, 3, trabalhos); // Mock com contadores
        }
        if ("professor2.escola@fatec.sp.gov.br".equals(emailProfessor)) {
            return new DashboardData(45, 10, 0, Collections.emptyList());
        }
        return new DashboardData(0, 0, 0, Collections.emptyList());
    }


    // ---------------------------------------------------------------------------------
    // MÉTODO 4: DASHBOARD COORDENADOR TG (DashboardTgData)
    // ---------------------------------------------------------------------------------

    /**
     * Tenta buscar os dados do dashboard do Coordenador de TG do banco de dados.
     * Se o banco falhar, usa a simulação.
     */
    public static DashboardTgData getProfessorTGDashboardData() {

        try (Connection conn = DatabaseConnect.getConnection()) {
            System.out.println("Dashboard Coordenador: Conectado ao DB...");

            // TODO: IMPLEMENTAR AS CONSULTAS SQL REAIS AQUI
            // Este é o mais complexo. Requer múltiplas consultas SQL para:
            // 1. O Map 'progressoAlunos' (4 consultas COUNT diferentes)
            // 2. A List 'trabalhosPendentes' (pode reusar a vw_professor_dashboard sem filtro de email)
            // 3. Os K-Cards (totalAlunos, tgsConcluidos, totalOrientandos)

            // Como as queries não estão prontas, forçamos o fallback
            throw new SQLException("Queries do Dashboard do Coordenador ainda não implementadas.");

        } catch (SQLException e) {
            System.err.println("!!! FALHA DE CONEXÃO OU QUERY (Dashboard Coordenador) !!!");
            System.err.println("Causa: " + e.getMessage());
            System.err.println("Usando dados de simulação (MOCK) como fallback...");
            return getProfessorTGDashboardData_Mock(); // FALLBACK
        }
    }

    /**
     * SIMULAÇÃO (Mock) de dados do dashboard do Coordenador de TG
     */
    private static DashboardTgData getProfessorTGDashboardData_Mock() {
        Map<String, Integer> progressoAlunos = new LinkedHashMap<>();
        progressoAlunos.put("Concluído", 15);
        progressoAlunos.put("Em Dia", 45);
        progressoAlunos.put("Atrasado", 18);
        progressoAlunos.put("Não Iniciado", 12);

        List<TrabalhoPendente> trabalhosPendentes = new ArrayList<>();
        trabalhosPendentes.add(new TrabalhoPendente(0.5, "(MOCK) Ana Beatriz", "ana.b@aluno.fatec.sp.gov.br", "ADS", "2025", "Atrasado"));
        trabalhosPendentes.add(new TrabalhoPendente(0.8, "(MOCK) Carlos Daniel", "carlos.d@aluno.fatec.sp.gov.br", "DSM", "2025", "Correção"));
        trabalhosPendentes.add(new TrabalhoPendente(0.2, "(MOCK) Juliana Lima", "juliana.l@aluno.fatec.sp.gov.br", "GTI", "2025", "Revisão"));

        return new DashboardTgData(90, 15, 6, progressoAlunos, trabalhosPendentes);
    }
}