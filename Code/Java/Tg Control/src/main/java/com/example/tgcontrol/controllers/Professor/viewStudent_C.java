package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class viewStudent_C {

    @FXML
    public void goTurmas(ActionEvent actionEvent)
    {
        String caminho= "ProfessorScenes/ProfessorTGScenes/turmas_ProfessorTG.fxml";
        UIUtils.loadFxml(caminho);
    }
}