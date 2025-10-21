package com.example.tgcontrol.controllers;

import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseNavbarController implements Initializable {

    @FXML
    protected StackPane contentArea;

    protected abstract String getInitialFxmlPath();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (contentArea == null) {
            System.err.println("GRAVE: StackPane com fx:id='contentArea' não foi injetado. Verifique o FXML da Navbar.");
            return;
        }

        String initialPath = getInitialFxmlPath();
        if (initialPath != null && !initialPath.isEmpty()) {
            navigateTo(initialPath);
        }
    }

    protected void navigateTo(String fxmlPath) {
        if (contentArea != null) {
            UIUtils.loadFxmlInPane(contentArea, fxmlPath);
        } else {
            UIUtils.showAlert("Erro de Navegação", "O Content Area da Navbar não foi inicializado corretamente. Falha ao carregar: " + fxmlPath);
        }
    }

    @FXML
    public void sair(ActionEvent actionEvent)
    {
        SessaoManager.getInstance().encerrarSessao();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        UIUtils.loadNewScene(stage, "GeralScenes/login_User.fxml");
    }

    @FXML
    public void notifications(ActionEvent actionEvent) {
        navigateTo("GeralScenes/notifications_User.fxml");
    }
}