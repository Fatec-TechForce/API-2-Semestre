package com.example.tgcontrol.model;

import java.time.LocalDateTime;

public class VersaoTG {
    private int id;
    private String nomeArquivo;
    private LocalDateTime dataUpload;
    private String caminhoArquivo;

    public VersaoTG(int id, String nomeArquivo, LocalDateTime dataUpload, String caminhoArquivo) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.dataUpload = dataUpload;
        this.caminhoArquivo = caminhoArquivo;
    }

    public int getId() { return id; }
    public String getNomeArquivo() { return nomeArquivo; }
    public LocalDateTime getDataUpload() { return dataUpload; }
    public String getCaminhoArquivo() { return caminhoArquivo; }
}
