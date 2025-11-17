package com.example.tgcontrol.controllers.Geral;

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

public class registration_User_C {

    @FXML
    public TextField tbx_Login;
    @FXML
    public PasswordField tbx_Senha;
    @FXML
    public PasswordField tbx_ConfirmarSenha;
    @FXML
    public Button btn_Login;
    @FXML
    public void Login(MouseEvent mouseEvent)  throws IOException
    {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        UIUtils.loadNewScene(stage, "GeralScenes/login_User.fxml");
    }

    public void Register(ActionEvent actionEvent)
    {
        String login = tbx_Login.getText();
        String senha = tbx_Senha.getText();
        String confirmarSenha = tbx_ConfirmarSenha.getText();

        String fxmlParaCarregar = null;

        if (login.contains("@fatec.sp.gov.br"))
        {
            if ((senha.equals(confirmarSenha)) & (senha != ""))
            {
                UIUtils.showAlert("Sucesso", "O cadastro foi realizado com sucesso.");
                fxmlParaCarregar = "GeralScenes/login_User.fxml";
            }
            else if(senha == "")
            {
                UIUtils.showAlert("Erro de Login", "Digite uma senha.");

            }
            else
            {
                UIUtils.showAlert("Erro de Login", "As senhas não combinam.");
            }
        }
        else
        {
            UIUtils.showAlert("Erro de Login", "Informe um email válido.");
        }

        if (fxmlParaCarregar != null) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            UIUtils.loadNewScene(stage, fxmlParaCarregar);

        }
    }
}