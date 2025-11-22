package com.example.tgcontrol.model;

import java.time.LocalDateTime;

public class HistoricoVersao {
    private int numeroTentativa;
    private LocalDateTime dataEnvio;
    private String caminhoArquivo;
    private String feedbackProfessor;
    private String status; // 'approved', 'revision_requested', etc.

    public HistoricoVersao(int numeroTentativa, LocalDateTime dataEnvio, String caminhoArquivo, String feedbackProfessor, String status) {
        this.numeroTentativa = numeroTentativa;
        this.dataEnvio = dataEnvio;
        this.caminhoArquivo = caminhoArquivo;
        this.feedbackProfessor = feedbackProfessor;
        this.status = status;
    }

    // Getters
    public int getNumeroTentativa() { return numeroTentativa; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public String getCaminhoArquivo() { return caminhoArquivo; }
    public String getFeedbackProfessor() { return feedbackProfessor; }
    public String getStatus() { return status; }

    // O ListView usa o toString para exibir texto simples se não definirmos uma CellFactory,
    // mas vamos definir uma CellFactory bonita no Controller.
    @Override
    public String toString() {
        return "Versão " + numeroTentativa;
    }
}