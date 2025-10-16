package com.example.tgcontrol.model;

import java.util.List;

public class DashboardData {
    private final int totalAlunos;
    private final int tgsConcluidos;
    private final int pendentes;
    private final List<TrabalhoPendente> trabalhos;

    public DashboardData(int totalAlunos, int tgsConcluidos, int pendentes, List<TrabalhoPendente> trabalhos) {
        this.totalAlunos = totalAlunos;
        this.tgsConcluidos = tgsConcluidos;
        this.pendentes = pendentes;
        this.trabalhos = trabalhos;
    }

    public int getTotalAlunos() {
        return totalAlunos;
    }

    public int getTgsConcluidos() {
        return tgsConcluidos;
    }

    public int getPendentes() {
        return pendentes;
    }

    public List<TrabalhoPendente> getTrabalhos() {
        return trabalhos;
    }
}