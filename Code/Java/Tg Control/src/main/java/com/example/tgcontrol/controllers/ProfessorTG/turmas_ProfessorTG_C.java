package com.example.tgcontrol.controllers.ProfessorTG;

import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class turmas_ProfessorTG_C {
    @FXML
    public void StudentList(MouseEvent mouseEvent)
    {
        String caminho = "ProfessorTGScenes/StudentsList_ProfessorTG.fxml";

        UIUtils.loadFxml(caminho);
    }
}