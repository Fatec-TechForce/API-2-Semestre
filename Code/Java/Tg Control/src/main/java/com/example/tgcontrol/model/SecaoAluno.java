package com.example.tgcontrol.model;

import java.time.LocalDate;

public class SecaoAluno {

    private final int taskId;
    private final String titulo;
    private final String status; // Status da TAREFA: "Concluído", "Em Progresso", "Bloqueado"
    private final LocalDate dataEntrega; //Até os minutos
    private final String statusRevisao; // Status da REVISÃO: "Aprovado", "Revisão Solicitada", "Pendente"

    public SecaoAluno(int taskId, String titulo, String status, LocalDate dataEntrega, String statusRevisao) {
        this.taskId = taskId;
        this.titulo = titulo;
        this.status = status;
        this.dataEntrega = dataEntrega;
        this.statusRevisao = statusRevisao;
    }

    // Getters são necessários para que o JavaFX TableView possa ler as propriedades
    public int getTaskId() {
        return taskId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public String getStatusRevisao() {
        return statusRevisao;
    }
}