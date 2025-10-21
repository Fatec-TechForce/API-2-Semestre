package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class Card_Secao_Aluno_C {

    @FXML private Label lbTituloSecao;
    @FXML private Label lbDataSecao;
    @FXML private Label lbNomeTarefa;
    @FXML private Label lbStatus;


    public void configurar(String titulo, String data) {
        lbTituloSecao.setText(titulo);
        lbDataSecao.setText(data);
    }

    //Adicionar um jeito de mudar para a tela "seção"
    @FXML
    public void exibirTarefa() {
        String caminho = "AlunoScenes/secao_Aluno.fxml";

        UIUtils.loadFxml(caminho);
    }
}