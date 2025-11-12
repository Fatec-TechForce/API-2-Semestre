package com.example.tgcontrol.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SecaoAluno {
    private final String emailAluno;
    private final int taskSequence;
    private final String titulo;
    private final String status;
    private final LocalDate dataEntrega;
    private final String statusRevisao;
    private final LocalDateTime dataUltimaRevisao;

    public SecaoAluno(String emailAluno, int taskSequence, String titulo, String status, LocalDate dataEntrega, String statusRevisao, LocalDateTime dataUltimaRevisao) {
        this.emailAluno = emailAluno;
        this.taskSequence = taskSequence;
        this.titulo = titulo;
        this.status = status;
        this.dataEntrega = dataEntrega;
        this.statusRevisao = statusRevisao;
        this.dataUltimaRevisao = dataUltimaRevisao;
    }

    public String getEmailAluno() { return emailAluno; }
    public int getTaskSequence() { return taskSequence; }
    public String getTitulo() { return titulo; }
    public String getStatus() { return status; }
    public LocalDate getDataEntrega() { return dataEntrega; }
    public String getStatusRevisao() { return statusRevisao; }
    public LocalDateTime getDataUltimaRevisao() { return dataUltimaRevisao; }
}