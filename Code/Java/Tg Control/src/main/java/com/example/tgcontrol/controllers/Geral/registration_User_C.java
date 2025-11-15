package com.example.tgcontrol.controllers.Geral;

import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class registration_User_C {

    @FXML public TextField tbx_Login;
    @FXML public PasswordField tbx_Senha;
    @FXML public PasswordField tbx_ConfirmarSenha;

    @FXML public TextField tbx_Nome;
    @FXML public TextField tbx_Sobrenome;

    @FXML public Button btn_Resgister;

    @FXML
    public void Login(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        UIUtils.loadNewScene(stage, "GeralScenes/login_User.fxml");
    }

    @FXML
    public void Register(ActionEvent actionEvent) {
        String email = tbx_Login.getText();
        String nome = tbx_Nome.getText(); // Campo novo
        String sobrenome = tbx_Sobrenome.getText(); // Campo novo
        String senha = tbx_Senha.getText();
        String confirmarSenha = tbx_ConfirmarSenha.getText();

        if (email.isBlank() || nome.isBlank() || sobrenome.isBlank() || senha.isBlank()) {
            UIUtils.showAlert("Erro de Cadastro", "Todos os campos (Email, Nome, Sobrenome, Senha) são obrigatórios.");
            return;
        }

        if (senha.isEmpty()) {
            UIUtils.showAlert("Erro de Cadastro", "A senha não pode estar em branco.");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            UIUtils.showAlert("Erro de Cadastro", "As senhas não combinam.");
            return;
        }

        try {
            boolean sucesso = DatabaseUtils.registrarUsuario(email, nome, sobrenome, senha);

            if (sucesso) {
                UIUtils.showAlert("Sucesso", "Cadastro realizado com sucesso! Pode agora fazer o login.");

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                UIUtils.loadNewScene(stage, "GeralScenes/login_User.fxml");

            } else {
                UIUtils.showAlert("Erro de Cadastro", "O e-mail informado já está cadastrado no sistema.");
            }

        } catch (SQLException e) {
            UIUtils.showAlert("Erro de Banco de Dados", "Não foi possível completar o cadastro. Erro: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            UIUtils.showAlert("Erro Inesperado", "Ocorreu um erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}