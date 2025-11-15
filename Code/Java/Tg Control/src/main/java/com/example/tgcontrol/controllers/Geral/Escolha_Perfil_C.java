package com.example.tgcontrol.controllers.Geral;

import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Escolha_Perfil_C {

    @FXML
    void irParaCadastroAluno(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        UIUtils.loadNewScene(stage, "AlunoScenes/forms_Aluno.fxml");
    }

    @FXML
    void irParaCadastroProfessor(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        UIUtils.loadNewScene(stage, "ProfessorScenes/forms_Professor.fxml");
    }
}