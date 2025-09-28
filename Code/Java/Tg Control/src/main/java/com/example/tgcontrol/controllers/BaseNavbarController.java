package com.example.tgcontrol.controllers;

import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseNavbarController implements Initializable {

    protected abstract String getInitialFxmlPath();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String initialPath = getInitialFxmlPath();
        if (initialPath != null && !initialPath.isEmpty()) {
            carregarPagina(initialPath);
        }
    }

    @FXML
    public void sair(ActionEvent actionEvent)
    {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        UIUtils.loadNewScene(stage, "GeralScenes/login_User.fxml");
    }

    protected void carregarPagina(String nomeArquivo) {
        UIUtils.loadFxml(nomeArquivo);
    }
}