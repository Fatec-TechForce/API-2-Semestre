package com.example.tgcontrol.utils;

import com.example.tgcontrol.Launcher; // Usado como referência para localizar FXMLs no classpath
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UIUtils {

    private UIUtils() {
    }

    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void loadFxmlInPane(Pane pane, String fxmlFilename) {
        if (pane == null) {
            System.err.println("Erro: O painel (Pane) fornecido é nulo.");
            return;
        }
        try {
            URL fxmlLocation = UIUtils.class.getResource(fxmlFilename);
            if (fxmlLocation == null) {
                throw new IOException("Não foi possível encontrar o arquivo FXML: " + fxmlFilename);
            }
            Parent fxml = FXMLLoader.load(fxmlLocation);
            pane.getChildren().setAll(fxml);
        } catch (IOException e) {
            Logger.getLogger(UIUtils.class.getName()).log(Level.SEVERE, "Falha ao carregar o FXML: " + fxmlFilename, e);
        }
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void loadNewScene(Stage stage, String fxmlPath) {
        try {
            URL fxmlLocation = Launcher.class.getResource(fxmlPath);

            if (fxmlLocation == null) {
                throw new IOException("Não foi possível encontrar o arquivo FXML: " + fxmlPath);
            }

            Parent root = FXMLLoader.load(fxmlLocation);
            Scene scene = new Scene(root);

            scene.setOnMousePressed(mouseEvent -> {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            });

            scene.setOnMouseDragged(mouseEvent -> {
                stage.setX(mouseEvent.getScreenX() - xOffset);
                stage.setY(mouseEvent.getScreenY() - yOffset);
            });

            stage.setScene(scene);
            stage.setTitle("TgControl");
            stage.resizableProperty().setValue(false);
            stage.show();

        } catch (IOException e) {
            Logger.getLogger(UIUtils.class.getName()).log(Level.SEVERE, "Falha ao carregar a nova Scene: " + fxmlPath, e);
            showAlert("Erro de Carregamento", "Ocorreu um erro ao carregar a tela principal.");
        }
    }
}