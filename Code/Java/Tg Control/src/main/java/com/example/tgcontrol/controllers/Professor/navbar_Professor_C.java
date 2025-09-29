package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.controllers.BaseNavbarController;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class navbar_Professor_C extends BaseNavbarController {

    @Override
    protected String getInitialFxmlPath() {
        return "ProfessorScenes/home_Professor.fxml";
    }

    @FXML
    public void home(ActionEvent actionEvent) {
        carregarPagina("ProfessorScenes/home_Professor.fxml");
    }

    @FXML
    public void turmas(ActionEvent actionEvent) {
        carregarPagina("ProfessorScenes/ProfessorTGScenes/turmas_ProfessorTG.fxml");
    }

    @FXML
    public void coordinations(ActionEvent actionEvent) {
        carregarPagina("ProfessorScenes/coordinations_Professor.fxml");
    }
}