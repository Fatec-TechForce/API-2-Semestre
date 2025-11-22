package com.example.tgcontrol.controllers.ProfessorTG;

import com.example.tgcontrol.controllers.BaseNavbarController;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Navbar_ProfessorTG_C extends BaseNavbarController {

    @Override
    protected String getInitialFxmlPath() {
        return "ProfessorTGScenes/home_ProfessorTG.fxml";
    }

    @FXML
    public void home(ActionEvent actionEvent) {
        UIUtils.loadFxml("ProfessorTGScenes/home_ProfessorTG.fxml");
    }

    @FXML
    public void turmas(ActionEvent actionEvent) {
        UIUtils.loadFxml("ProfessorTGScenes/turmas_ProfessorTG.fxml");
    }

    @FXML
    public void coordinations(ActionEvent actionEvent) { UIUtils.loadFxml("ProfessorScenes/coordinations_Professor.fxml");
    }
}