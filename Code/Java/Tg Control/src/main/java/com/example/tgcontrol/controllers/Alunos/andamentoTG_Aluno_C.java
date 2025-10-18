package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class andamentoTG_Aluno_C {
    @FXML
    public void irsessao_btn(ActionEvent actionEvent)
    {
        String caminho = "AlunoScenes/secao_Aluno.fxml";

        UIUtils.loadFxml(caminho);
    }
}
