package com.example.tgcontrol.model;

import java.util.List;
import java.util.Map;

public class DashboardTgData extends DashboardData {

    private final int totalOrientandos;
    private final Map<String, Integer> progressoAlunos;

    public DashboardTgData(int totalAlunos, int tgsConcluidos, int totalOrientandos, Map<String, Integer> progressoAlunos, List<TrabalhoPendente> trabalhosPendentes) {
        super(totalAlunos, tgsConcluidos, trabalhosPendentes.size(), trabalhosPendentes);

        this.totalOrientandos = totalOrientandos;
        this.progressoAlunos = progressoAlunos;
    }

    public int getTotalOrientandos() {
        return totalOrientandos;
    }

    public Map<String, Integer> getProgressoAlunos() {
        return progressoAlunos;
    }
}