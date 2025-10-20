package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class turmas_C {
    @FXML
    public void StudentList(MouseEvent mouseEvent)
    {
        String caminho = "ProfessorScenes/ProfessorTGScenes/viewStudents_ProfessorTG.fxml";

        UIUtils.loadFxml(caminho);
    }
}