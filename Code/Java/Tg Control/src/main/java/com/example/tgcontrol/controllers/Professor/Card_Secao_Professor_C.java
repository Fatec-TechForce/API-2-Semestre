package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class Card_Secao_Professor_C {

    @FXML
    private Label lbTituloSecao;
    @FXML private Label lbDataSecao;
    @FXML private Label lbNomeTarefa;
    @FXML private Label lbStatus;


    public void configurar(String titulo, String data) {
        lbTituloSecao.setText(titulo);
        lbDataSecao.setText(data);
    }

    @FXML
    public void exibirTarefa() {
        String caminho = "ProfessorScenes/secao_Professor.fxml";

        UIUtils.loadFxml(caminho);
    }
}
