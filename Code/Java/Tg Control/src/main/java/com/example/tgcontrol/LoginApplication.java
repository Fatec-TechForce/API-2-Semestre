package com.example.tgcontrol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {

    @Override
    public void start(Stage loginStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NavbarApplication.class.getResource("GeralScenes/login_User.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        loginStage.setScene(scene);
        loginStage.setTitle("TgControl");
        loginStage.resizableProperty().setValue(false);
        loginStage.show();
    }
}
