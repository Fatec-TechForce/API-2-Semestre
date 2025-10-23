package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.utils.HTMLconversor;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.HTMLEditor;



public class HTMLEditorPage_Aluno_C{

    @FXML
    private HTMLEditor  htmlEditor;

    @FXML
    private Button SalvaArquivo;


    @FXML
    void SalvaArquivoAction(ActionEvent event) {


        String htmlContent = htmlEditor.getHtmlText();

        if (htmlContent != null && !htmlContent.isBlank()) {


            // Nome do diretório onde será gerado o arquivo -->
            String nomeArquivoMd = "secao_editada_" + System.currentTimeMillis() + ".md";
            String diretorio = "Server/TGs_Markdown/";


            boolean sucesso = HTMLconversor.converterESalvarMarkdown(htmlContent, nomeArquivoMd, diretorio);


            if (sucesso) {
                UIUtils.showAlert("Sucesso", "Seção salva como '" + nomeArquivoMd + "'!");

            }


        } else {

            UIUtils.showAlert("Atenção", "O editor está vazio. Não há nada para salvar.");
        }
    }


    @FXML
    public void initialize() {

    }

}