package com.example.tgcontrol.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Turma {

    protected String disciplina;
    protected int ano;
    protected int semestre;
    protected String descricaoTurma;

    private BooleanProperty etapa1;
    private BooleanProperty etapa2;

    public Turma(String disciplina, int ano, int semestre) {
        this.disciplina = disciplina;
        this.ano = ano;
        this.semestre = semestre;
        this.descricaoTurma = String.format("%s (%d/%d)", disciplina, ano, semestre);

        this.etapa1 = new SimpleBooleanProperty(false);
        this.etapa2 = new SimpleBooleanProperty(false);
    }

    public String getDisciplina() { return disciplina; }
    public int getAno() { return ano; }
    public int getSemestre() { return semestre; }
    public String getDescricaoTurma() { return descricaoTurma; }

    public BooleanProperty etapa1Property() { return etapa1; }
    public BooleanProperty etapa2Property() { return etapa2; }

    public StringProperty descricaoTurmaProperty() {
        return new SimpleStringProperty(this.descricaoTurma);
    }

    public boolean isEtapa1() { return etapa1.get(); }
    public boolean isEtapa2() { return etapa2.get(); }

    @Override
    public String toString() {
        return this.descricaoTurma;
    }
}