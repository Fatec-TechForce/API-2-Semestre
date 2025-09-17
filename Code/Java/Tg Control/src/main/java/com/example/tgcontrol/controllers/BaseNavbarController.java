package com.example.tgcontrol.controllers;

import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseNavbarController implements Initializable {

    @FXML
    protected StackPane contentArea;

    protected abstract String getInitialFxmlPath();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String initialPath = getInitialFxmlPath();
        if (initialPath != null && !initialPath.isEmpty()) {
            carregarPagina(initialPath);
        }
    }

    protected void carregarPagina(String nomeArquivo) {
        String caminhoCompleto = "/com/example/tgcontrol/" + nomeArquivo;
        UIUtils.loadFxmlInPane(contentArea, caminhoCompleto);
    }
}