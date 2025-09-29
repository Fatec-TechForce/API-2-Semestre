package com.example.tgcontrol.controllers.Geral;

import com.example.tgcontrol.utils.UIUtils; // 👈 Adicionar esta importação
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class login_User_C {

    @FXML
    public TextField tbx_Login;
    @FXML
    public PasswordField tbx_Senha;
    @FXML
    public Button btn_Login;

    @FXML
    public void Login(ActionEvent actionEvent) throws IOException {
        String login = tbx_Login.getText();
        String senha = tbx_Senha.getText();

        String fxmlParaCarregar = null;

        if (senha.equals("Troca123")) {
            if (login.equals("aluno.escola@fatec.sp.gov.br"))
            {
                fxmlParaCarregar = "AlunoScenes/navbar_Aluno.fxml";
            } else if (login.equals("professor.escola@fatec.sp.gov.br"))
            {
                fxmlParaCarregar = "ProfessorScenes/navbar_Professor.fxml";
            }
        }

        if (fxmlParaCarregar != null) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            UIUtils.loadNewScene(stage, fxmlParaCarregar);

        } else {
            UIUtils.showAlert("Erro de Login", "Usuário ou senha inválidos.");
        }
    }
    @FXML
    public void irParaCadastro(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/tgcontrol/AlunoScenes/forms_Aluno.fxml"));
        Scene cadastroScene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(cadastroScene);
        stage.setTitle("TgControl - Cadastro de Aluno");
        stage.show();
    }
}
