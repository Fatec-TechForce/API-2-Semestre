package com.example.tgcontrol.scenes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class navbar implements Initializable {

    @FXML
    private StackPane contentArea;

    String fxmlPath = "/com/example/tgcontrol/home.fxml";


    @Override
    public void initialize(URL location, ResourceBundle resources){

        try {
            URL fxmlLocation = getClass().getResource(fxmlPath);

            if (fxmlLocation == null) {
                throw new IOException("Não foi possível encontrar o arquivo FXML: home.fxml");
            }

            Parent fxml = FXMLLoader.load(fxmlLocation);

            contentArea.getChildren().setAll(fxml);

        } catch (IOException ex) {
            Logger.getLogger(navbar.class.getName()).log(Level.SEVERE, "Falha ao carregar o FXML.", ex);
        }

    }

    private void carregarPagina(String nomeArquivo) {
        String caminhoCompleto = "/com/example/tgcontrol/" + nomeArquivo;

        try {
            URL fxmlLocation = getClass().getResource(caminhoCompleto);

            if (fxmlLocation == null) {
                throw new IOException("Não foi possível encontrar o arquivo FXML: " + caminhoCompleto);
            }

            Parent fxml = FXMLLoader.load(fxmlLocation);

            contentArea.getChildren().setAll(fxml);

        } catch (IOException ex) {
            System.err.println("Erro ao carregar a página: " + nomeArquivo);
            ex.printStackTrace();
        }
    }

    @FXML
    public void home(javafx.event.ActionEvent actionEvent) {
        carregarPagina("home.fxml");
    }

    @FXML
    public void andamentoTG(javafx.event.ActionEvent actionEvent) {
        carregarPagina("andamentoTG.fxml");
    }

    @FXML
    public void coordinations(javafx.event.ActionEvent actionEvent) {
        carregarPagina("coordinations.fxml");
    }

    @FXML
    public void notifications(javafx.event.ActionEvent actionEvent) {
        carregarPagina("notifications.fxml");
    }
}
