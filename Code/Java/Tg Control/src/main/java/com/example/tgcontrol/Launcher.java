package com.example.tgcontrol;

import com.example.tgcontrol.utils.UIUtils;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    @Override
    public void start(Stage loginStage) throws IOException {
        UIUtils.loadNewScene(loginStage, "GeralScenes/login_User.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}