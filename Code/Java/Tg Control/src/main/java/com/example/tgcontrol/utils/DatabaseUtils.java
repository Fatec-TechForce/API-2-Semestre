package com.example.tgcontrol.utils;

import com.example.tgcontrol.model.TipoUsuario;

public class DatabaseUtils {

    public static TipoUsuario autenticarUsuario(String login, String senha) {

        if (!senha.equals("Troca123")) {
            return TipoUsuario.NAO_AUTENTICADO;
        }

        switch (login) {
            case "aluno.escola@fatec.sp.gov.br":
                return TipoUsuario.ALUNO;
            case "professor.escola@fatec.sp.gov.br":
                return TipoUsuario.PROFESSOR;
            case "professortg.escola@fatec.sp.gov.br":
                return TipoUsuario.PROFESSOR_TG;
            default:
                return TipoUsuario.NAO_AUTENTICADO;
        }
    }
}