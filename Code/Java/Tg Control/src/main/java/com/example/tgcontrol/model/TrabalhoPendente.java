package com.example.tgcontrol.model;

public class TrabalhoPendente {
    private final Double progresso;
    private final String nomeAluno;
    private final String emailAluno;
    private final String turma;
    private final String semestre;
    private final String status;

    public TrabalhoPendente(Double progresso, String nomeAluno, String emailAluno, String turma, String semestre, String status) {
        this.progresso = progresso;
        this.nomeAluno = nomeAluno;
        this.emailAluno = emailAluno;
        this.turma = turma;
        this.semestre = semestre;
        this.status = status;
    }

    public Double getProgresso() { return progresso; }
    public String getNomeAluno() { return nomeAluno; }
    public String getEmailAluno() { return emailAluno; }
    public String getTurma() { return turma; }
    public String getSemestre() { return semestre; }
    public String getStatus() { return status; }
}