package com.example.tgcontrol.controllers.ProfessorTG;

import com.example.tgcontrol.controllers.BaseNavbarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class navbar_ProfessorTG_C extends BaseNavbarController {

    @Override
    protected String getInitialFxmlPath() {
        return "ProfessorTGScenes/home_ProfessorTG.fxml";
    }

    @FXML
    public void home(ActionEvent actionEvent) {
        navigateTo("ProfessorTGScenes/home_ProfessorTG.fxml");
    }

    @FXML
    public void turmas(ActionEvent actionEvent) {
        navigateTo("ProfessorTGScenes/turmas_ProfessorTG.fxml");
    }

    @FXML
    public void coordinations(ActionEvent actionEvent) {
        navigateTo("ProfessorScenes/coordinations_Professor.fxml");
    }
}