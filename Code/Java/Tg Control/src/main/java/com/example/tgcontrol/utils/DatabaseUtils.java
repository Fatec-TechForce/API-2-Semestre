package com.example.tgcontrol.utils;

import com.example.tgcontrol.model.DashboardData;
import com.example.tgcontrol.model.TipoUsuario;
import com.example.tgcontrol.model.TrabalhoPendente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseUtils {

    public static TipoUsuario autenticarUsuario(String login, String senha) {
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

    public static DashboardData getProfessorDashboardData(String emailProfessor) {
        if (emailProfessor == null || emailProfessor.isEmpty()) {
            return new DashboardData(0, 0, 0, Collections.emptyList());
        }

        if ("professor1.escola@fatec.sp.gov.br".equals(emailProfessor)) {
            List<TrabalhoPendente> trabalhos = new ArrayList<>();
            trabalhos.add(new TrabalhoPendente(1.0, "Guilherme de Almeida", "guilherme.arruda@aluno.fatec.sp.gov.br", "YT_BANCO_DE_DADOS", "2025", "Pendente"));
            trabalhos.add(new TrabalhoPendente(0.9, "Maria Oliveira", "maria.oliveira@aluno.fatec.sp.gov.br", "IA_PARA_JOGOS", "2025", "Pendente"));
            trabalhos.add(new TrabalhoPendente(1.0, "João Silva", "joao.silva@aluno.fatec.sp.gov.br", "ENGENHARIA_REVERSA", "2025", "Pendente"));

            return new DashboardData(90, 15, 3, trabalhos);
        }

        if ("professor2.escola@fatec.sp.gov.br".equals(emailProfessor)) {
            return new DashboardData(45, 10, 0, Collections.emptyList());
        }

        return new DashboardData(0, 0, 0, Collections.emptyList());
    }
}