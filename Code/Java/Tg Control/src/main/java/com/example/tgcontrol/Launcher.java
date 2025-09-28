package com.example.tgcontrol;

import com.example.tgcontrol.utils.UIUtils;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        Launcher.primaryStage = stage;

        UIUtils.loadNewScene(stage, "GeralScenes/login_User.fxml");
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}