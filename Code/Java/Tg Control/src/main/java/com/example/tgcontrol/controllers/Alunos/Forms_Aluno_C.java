package com.example.tgcontrol.controllers.Alunos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Forms_Aluno_C {

    @FXML
    public void enviarCadastro(ActionEvent event) throws IOException {
        // Mostra alerta
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cadastro realizado");
        alert.setHeaderText(null);
        alert.setContentText("As informações de primeiro acesso foram encaminhadas por e-mail.");
        alert.showAndWait();

        // Volta para tela de login
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgcontrol/GeralScenes/login_User.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("TgControl - Login");
        stage.show();
    }
    @FXML
    public void voltarLogin(ActionEvent event) throws IOException {
        // Depois de "cadastrar", redireciona para tela de login
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgcontrol/GeralScenes/login_User.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.setTitle("TgControl - Login");
        stage.show();
    }
}
