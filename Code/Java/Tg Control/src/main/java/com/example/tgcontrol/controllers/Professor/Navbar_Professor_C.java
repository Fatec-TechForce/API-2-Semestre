package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.controllers.BaseNavbarController;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Navbar_Professor_C extends BaseNavbarController {

    @Override
    protected String getInitialFxmlPath() {
        return "ProfessorScenes/home_Professor.fxml";
    }

    @FXML
    public void home(ActionEvent actionEvent) {
        UIUtils.loadFxml("ProfessorScenes/home_Professor.fxml");
    }

    @FXML
    public void coordinations(ActionEvent actionEvent) {
        UIUtils.loadFxml("ProfessorScenes/coordinations_Professor.fxml");
    }
}