package com.example.tgcontrol.utils;

import com.example.tgcontrol.model.TipoUsuario;
import com.example.tgcontrol.model.Turma;

public class SessaoManager {
    private static SessaoManager instance;
    private TipoUsuario tipoUsuario;
    private String emailUsuario;
    private Turma turmaSelecionada;

    private SessaoManager() {
        this.tipoUsuario = TipoUsuario.NAO_AUTENTICADO;
    }

    public static SessaoManager getInstance() {
        if (instance == null) {
            instance = new SessaoManager();
        }
        return instance;
    }

    public void iniciarSessao(String email, TipoUsuario tipo) {
        this.emailUsuario = email;
        this.tipoUsuario = tipo;
    }

    public void encerrarSessao() {
        this.emailUsuario = null;
        this.tipoUsuario = TipoUsuario.NAO_AUTENTICADO;
        this.turmaSelecionada = null;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public boolean estaLogado() {
        return tipoUsuario != TipoUsuario.NAO_AUTENTICADO;
    }

    public Turma getTurmaSelecionada() { return turmaSelecionada; }
    public void setTurmaSelecionada(Turma turmaSelecionada) { this.turmaSelecionada = turmaSelecionada; }
}