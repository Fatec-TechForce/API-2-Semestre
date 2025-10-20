package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class Coord_Professor_C {

    @FXML
    public void irParaHistorico(ActionEvent event) throws IOException {
        String fxmlParaCarregar = "/com/example/tgcontrol/GeralScenes/historicoVers_User.fxml";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        UIUtils.loadFxml(fxmlParaCarregar);
    }
}
