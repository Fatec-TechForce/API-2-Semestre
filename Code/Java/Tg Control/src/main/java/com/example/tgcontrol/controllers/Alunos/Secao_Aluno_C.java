package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import javafx.fxml.FXML;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.io.*;
import java.nio.file.*;

import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.example.tgcontrol.model.*;

public class Secao_Aluno_C {
    @FXML Button btn_Arquivo;
    @FXML Label lblMensagemSucesso;
    @FXML HTMLEditor htmlEditor;

    private String formatarTamanhoArquivo(long tamanho) {
        if(tamanho <= 0) return null;
        final String[] unidades = new String[] {"bytes", "KB", "MB", "GB"};
        int grupos_digitos = (int) (Math.log10(tamanho) / Math.log10(1024));
        //Máximo de 999KB
        if(grupos_digitos <= 1) {
            return String.format("%.2f %s", tamanho / Math.pow(1024, grupos_digitos), unidades[grupos_digitos]);
        } else {return null;}
    }

    private String converterArquivoParaHtml(File arquivo_selecionado) throws IOException {
        Path caminho_arquivo = arquivo_selecionado.toPath();
        //Caso for um arquivo .txt
        if(caminho_arquivo.endsWith(".txt")) {
            try {
                String text = Files.readString(caminho_arquivo);
                //String html = "<html><body><pre>" + text + "</pre></body></html>";
                //Files.writeString(Path.of(caminho_arquivo + ".html"), html);
                return "<html><body><pre>" + text + "</pre></body></html>";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Caso for um arquivo .docx
        else if(caminho_arquivo.endsWith(".docx")) {
            DocumentConverter conversor = new DocumentConverter();
            Result<String> resultado = conversor.convertToHtml(arquivo_selecionado);
            String html = resultado.getValue();
            //Caso haja algum aviso na conversão
            Set<String> avisos = resultado.getWarnings();
            return html;
        }
        //Caso for um arquivo .pdf
        else if(caminho_arquivo.endsWith(".pdf")) {
            try (PDDocument document = PDDocument.load(arquivo_selecionado)) {
                 //caminho_arquivo.getFileName();
                 //BufferedWriter writer = new BufferedWriter(new FileWriter("arquivo.html"))) {

                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);

                //writer.write("<html><body><pre>");
                //writer.write(text);
                //writer.write("</pre></body></html>");
                String html = "<html><body><pre>" + text + "</pre></body></html>";
                return html;
                //System.out.println("Conversão simples concluída!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @FXML
    public void adicionarArquivo() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Adicionar Arquivo");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Texto", "*.txt", "*.pdf", "*.docx")
        );

        Stage stage = (Stage) btn_Arquivo.getScene().getWindow();
        File arquivo_selecionado = fc.showOpenDialog(stage);

        if(arquivo_selecionado != null) {
            try {
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
                //Chama o método para converter para html
                htmlEditor.setHtmlText(converterArquivoParaHtml(arquivo_selecionado));


            } catch (IOException e) {
                e.printStackTrace();
                btn_Arquivo.setText(arquivo_selecionado.getAbsolutePath());
            }
        } else {
            btn_Arquivo.setText("Arquivo Selecionado");
        }
    }

    @FXML
    public void enviarSecao() {
        String html = htmlEditor.getHtmlText();
        CriaMd_Aluno_C md = new  CriaMd_Aluno_C();
        md.converterParaMD(html);
    }

    @FXML
    public void visualizarHist(ActionEvent event){
        String fxmlParaCarregar = "GeralScenes/historicoVers_User.fxml";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        UIUtils.loadFxml(fxmlParaCarregar);
    }
}
