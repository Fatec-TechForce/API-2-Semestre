package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.controllers.BaseNavbarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

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

    @FXML
    public void notifications(ActionEvent actionEvent) {
        carregarPagina("GeralScenes/notifications_User.fxml");
    }
}