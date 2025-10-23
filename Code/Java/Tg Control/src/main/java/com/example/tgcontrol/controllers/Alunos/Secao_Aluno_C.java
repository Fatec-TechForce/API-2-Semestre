package com.example.tgcontrol.controllers.Alunos;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.tgcontrol.model.*;

public class Secao_Aluno_C {
    @FXML Button btn_Arquivo;
    @FXML Label lblMensagemSucesso;
    //Esse método calcula o tamanho do arquivo
    private String formatarTamanhoArquivo(long tamanho) {
        if(tamanho <= 0) return null;
        final String[] unidades = new String[] {"bytes", "KB", "MB", "GB"};
        int grupos_digitos = (int) (Math.log10(tamanho) / Math.log10(1024));
        //O programa aceita um arquivo com no Máximo 999KB
        if(grupos_digitos <= 1) {
            return String.format("%.2f %s", tamanho / Math.pow(1024, grupos_digitos), unidades[grupos_digitos]);
        } else {return null;}
    }

    @FXML
    public File adicionarArquivo() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Adicionar Arquivo");
        fc.getExtensionFilters().addAll(
                //Esses são os tipos de arquivo aceito
                new FileChooser.ExtensionFilter("Texto", "*.txt", "*.pdf", "*.docx")
        );
        //Abre a tela de seleção de arquivo ao clicar o botão
        Stage stage = (Stage) btn_Arquivo.getScene().getWindow();
        File arquivo_selecionado = fc.showOpenDialog(stage);
        //Verifica se o usuário selecionou um arquivo
        if(arquivo_selecionado != null) {
            try {
                //Mostra o arquivo selecionado
                Path caminho_Arquivo = arquivo_selecionado.toPath();
                long tamanho_bytes = Files.size(caminho_Arquivo);
                String tamanho_formatado = formatarTamanhoArquivo(tamanho_bytes);
                btn_Arquivo.setText(arquivo_selecionado.getName());
                lblMensagemSucesso.setText("Arquivo adicionado com sucesso! (" + tamanho_formatado + ")");
                lblMensagemSucesso.setVisible(true);

                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                Runnable atraso = () -> lblMensagemSucesso.setVisible(false);
                executor.schedule(atraso, 4, TimeUnit.SECONDS);
                executor.shutdown();
                return arquivo_selecionado;

            } catch (IOException e) {
                e.printStackTrace();
                btn_Arquivo.setText(arquivo_selecionado.getAbsolutePath());
            }
        } else {
            lblMensagemSucesso.setText("Nenhum Arquivo Selecionado!");
            lblMensagemSucesso.setVisible(true);
        }
        return null;
    }

    private void converterArquivoParaHtml(Path arquivo_selecionado) {
        //Caso for um arquivo .txt
        if(arquivo_selecionado.endsWith(".txt")) {
            String text = Files.readString(arquivo_selecionado);
            String html = "<html><body><pre>" + text + "</pre></body></html>";
            Files.writeString(Path.of(arquivo_selecionado), html);
        } else if(arquivo_selecionado.endsWith(".docx")) {

        }
    }

    //Parte de envio de seção
    @FXML
    public void enviarSecao() {
        //SecaoAluno = new SecaoAluno();
    };
}
