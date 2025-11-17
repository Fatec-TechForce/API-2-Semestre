package com.example.tgcontrol.utils;

import com.example.tgcontrol.Launcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UIUtils {

    private static final Logger LOGGER = Logger.getLogger(UIUtils.class.getName());
    private static final String FXML_BASE_PATH = "/com/example/tgcontrol/Scenes/";

    private static double xOffset = 0;
    private static double yOffset = 0;

    private UIUtils() {
    }

    public static void loadFxmlInPane(Pane targetPane, String fxmlPath) {
        if (targetPane == null) {
            LOGGER.log(Level.WARNING, "Falha ao carregar FXML: Painel de destino é nulo.");
            return;
        }

        try {
            URL fxmlLocation = UIUtils.class.getResource(FXML_BASE_PATH + fxmlPath);

            if (fxmlLocation == null) {
                throw new IOException("Não foi possível encontrar o arquivo FXML: " + FXML_BASE_PATH + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            targetPane.getChildren().clear();
            targetPane.getChildren().add(root);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Falha ao carregar o FXML em painel: " + fxmlPath, e);
            showAlert("Erro de Carregamento FXML", "Não foi possível carregar a página: " + fxmlPath);
        }
    }

    public static void loadFxml(String fxmlPath) {
        Stage stage = Launcher.getPrimaryStage();
        if (stage == null || stage.getScene() == null) {
            LOGGER.log(Level.SEVERE, "Falha: Stage principal ou Scene não estão definidos.");
            showAlert("Erro de Navegação", "A tela principal não está pronta.");
            return;
        }

        StackPane contentArea = (StackPane) stage.getScene().lookup("#contentArea");

        if (contentArea == null) {
            LOGGER.log(Level.SEVERE, "Erro: Não foi encontrado o StackPane com fx:id='contentArea' na Scene.");
            showAlert("Erro de Navegação", "Não foi possível encontrar a área de conteúdo (contentArea) para carregar a página.");
            return;
        }

        loadFxmlInPane(contentArea, fxmlPath);
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
            URL fxmlLocation = UIUtils.class.getResource(FXML_BASE_PATH + fxmlPath);

            if (fxmlLocation == null) {
                throw new IOException("Não foi possível encontrar o arquivo FXML: " + FXML_BASE_PATH + fxmlPath);
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
            //stage.resizableProperty().setValue(false);
            stage.setHeight(524.0);
            stage.setWidth(889.0);
            stage.show();
            setStageIcon(stage);

        } catch (IOException e) {
            Logger.getLogger(UIUtils.class.getName()).log(Level.SEVERE, "Falha ao carregar a nova Scene: " + fxmlPath, e);
            showAlert("Erro de Carregamento", "Ocorreu um erro ao carregar a tela principal.");
        }
    }

    public static void openPopupWindow(String fxmlPath, String tituloJanela) {
        try {
            URL fxmlLocation = Launcher.class.getResource(fxmlPath);

            if (fxmlLocation == null) {
                throw new IOException("Não foi possível encontrar o arquivo FXML: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Stage popupStage = new Stage();
            setStageIcon(popupStage);
            popupStage.setScene(new Scene(root));
            popupStage.setTitle(tituloJanela);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);

            popupStage.showAndWait();

        } catch (IOException e) {
            Logger.getLogger(UIUtils.class.getName()).log(Level.SEVERE, "Falha ao abrir popup: " + fxmlPath, e);
            showAlert("Erro de Carregamento", "Ocorreu um erro ao abrir a janela: " + fxmlPath);
        }
    }

    public static void setStageIcon(Stage stage) {
        String iconPath = "/com/example/tgcontrol/SceneImages/TgIcon.png";
        try {
            InputStream iconStream = UIUtils.class.getResourceAsStream(iconPath);

            if (iconStream != null) {
                Image icon = new Image(iconStream);
                stage.getIcons().add(icon);
            } else {
                LOGGER.log(Level.WARNING, "Ícone não encontrado no caminho: " + iconPath);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar o ícone: " + iconPath, e);
        }
    }
}