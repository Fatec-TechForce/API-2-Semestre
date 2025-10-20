package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.controllers.BaseNavbarController;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class navbar_Aluno_C extends BaseNavbarController {

    @Override
    protected String getInitialFxmlPath() {
        return "AlunoScenes/home_Aluno.fxml";
    }

    @FXML
    public void home(ActionEvent actionEvent) {
        carregarPagina("AlunoScenes/home_Aluno.fxml");
    }

    @FXML
    public void andamentoTG(ActionEvent actionEvent) {
        carregarPagina("AlunoScenes/secoes_Aluno.fxml");
    }

    @FXML
    public void notifications(ActionEvent actionEvent) {
        carregarPagina("AlunoScenes/notifications_Aluno.fxml");
    }
}