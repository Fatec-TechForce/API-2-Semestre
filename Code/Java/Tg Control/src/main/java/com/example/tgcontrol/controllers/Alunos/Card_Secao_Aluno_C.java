package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.controllers.BaseNavbarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Card_Secao_Aluno_C extends BaseNavbarController {
    @FXML private Label lbTituloSecao;
    @FXML private Label lbDataSecao;

    @Override
    protected String getInitialFxmlPath() {
        return "AlunoScenes/secao_Professor.fxml";
    }

    public void configurar(String titulo, String data) {
        lbTituloSecao.setText(titulo);
        lbDataSecao.setText(data);
    }
    //Adicionar um jeito de mudar para a tela "seção"
    @FXML
    public void exibirTarefa() {

    }
}
