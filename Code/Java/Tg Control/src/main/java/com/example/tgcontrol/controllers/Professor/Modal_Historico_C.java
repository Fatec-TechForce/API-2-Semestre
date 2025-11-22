package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.model.HistoricoVersao;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

public class Modal_Historico_C {

    @FXML private Label lblTituloVersao;
    @FXML private WebView webConteudo; // <-- Agora é WebView
    @FXML private TextArea txtFeedback;

    public void setDados(HistoricoVersao versao) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        lblTituloVersao.setText("Versão " + versao.getNumeroTentativa() + " (" + versao.getDataEnvio().format(fmt) + ")");

        // Preenche Feedback
        if (versao.getFeedbackProfessor() != null && !versao.getFeedbackProfessor().isEmpty()) {
            txtFeedback.setText(versao.getFeedbackProfessor());
        } else {
            txtFeedback.setText("Nenhum feedback registrado para esta versão.");
        }

        // Carrega e Converte o Arquivo
        carregarConteudoArquivo(versao.getCaminhoArquivo());
    }

    private void carregarConteudoArquivo(String caminho) {
        if (caminho == null) {
            carregarHtmlErro("Caminho do arquivo não encontrado.");
            return;
        }
        File arquivo = new File(caminho);
        if (arquivo.exists()) {
            try {
                String markdown = Files.readString(arquivo.toPath(), StandardCharsets.UTF_8);
                String html = converterMarkdownParaHtml(markdown);
                webConteudo.getEngine().loadContent(html);
            } catch (Exception e) {
                carregarHtmlErro("Erro ao ler arquivo: " + e.getMessage());
            }
        } else {
            carregarHtmlErro("Arquivo físico não encontrado no servidor.");
        }
    }

    // Método de conversão (usando concatenação para evitar erro de %)
    private String converterMarkdownParaHtml(String markdown) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        String htmlConteudo = renderer.render(parser.parse(markdown));

        return "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: 'Segoe UI', Arial, sans-serif;" +
                "            font-size: 14px;" +
                "            color: #333;" +
                "            padding: 15px;" +
                "            line-height: 1.6;" +
                "        }" +
                "        h1, h2, h3 { color: #2c3e50; margin-top: 20px; }" +
                "        h1 { border-bottom: 1px solid #eee; padding-bottom: 10px; }" +
                "        code { background-color: #f4f4f4; padding: 2px 5px; border-radius: 3px; font-family: 'Consolas', monospace; }" +
                "        pre { background-color: #f4f4f4; padding: 10px; border-radius: 5px; overflow-x: auto; }" +
                "        blockquote { border-left: 4px solid #ccc; margin: 0; padding-left: 10px; color: #666; }" +
                "        img { max-width: 100%; height: auto; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                htmlConteudo +
                "</body>" +
                "</html>";
    }

    private void carregarHtmlErro(String mensagem) {
        String htmlErro = "<html><body style='font-family: sans-serif; color: red; padding: 20px;'>" +
                "<h3>Erro</h3><p>" + mensagem + "</p></body></html>";
        webConteudo.getEngine().loadContent(htmlErro);
    }
}