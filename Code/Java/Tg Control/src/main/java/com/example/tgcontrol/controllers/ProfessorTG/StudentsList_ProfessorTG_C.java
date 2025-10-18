package com.example.tgcontrol.controllers.ProfessorTG;

import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StudentsList_ProfessorTG_C {

    @FXML
    public void goTurmas(ActionEvent actionEvent)
    {
        String caminho= "ProfessorTGScenes/turmas_ProfessorTG.fxml";
        UIUtils.loadFxml(caminho);
    }
}