package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.controllers.BaseNavbarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class navbar_ProfessorTG_C extends BaseNavbarController {

    @Override
    protected String getInitialFxmlPath() {
        return "ProfessorScenes/ProfessorTGScenes/home_ProfessorTG.fxml";
    }

    @FXML
    public void home(ActionEvent actionEvent) {
        navigateTo("ProfessorScenes/ProfessorTGScenes/home_ProfessorTG.fxml");
    }

    @FXML
    public void turmas(ActionEvent actionEvent) {
        navigateTo("ProfessorScenes/ProfessorTGScenes/turmas_ProfessorTG.fxml");
    }

    @FXML
    public void coordinations(ActionEvent actionEvent) {
        navigateTo("ProfessorScenes/coordinations_Professor.fxml");
    }
}