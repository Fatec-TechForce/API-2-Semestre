package com.example.tgcontrol;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavbarApplication {

    private static double xOffset = 0;
    private static double yOffset = 0;
    public static void load(Stage stage, String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NavbarApplication.class.getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());

        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        stage.setTitle("TgControl");
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
    }
}