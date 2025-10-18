package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.controllers.BaseNavbarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class navbar_Aluno_C extends BaseNavbarController {

    @Override
    protected String getInitialFxmlPath() {
        return "AlunoScenes/home_Aluno.fxml";
    }

    @FXML
    public void home(ActionEvent actionEvent) {
        navigateTo("AlunoScenes/home_Aluno.fxml");
    }

    @FXML
    public void andamentoTG(ActionEvent actionEvent) {
        navigateTo("AlunoScenes/andamentoTG_Aluno.fxml");
    }

    @FXML
    @Override
    public void notifications(ActionEvent actionEvent) {
        navigateTo("GeralScenes/notifications_User.fxml");
    }
}