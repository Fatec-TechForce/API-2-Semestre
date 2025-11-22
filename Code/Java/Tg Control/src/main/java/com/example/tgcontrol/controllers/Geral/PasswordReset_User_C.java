package com.example.tgcontrol.controllers.Geral;

import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class PasswordReset_User_C {

    @FXML
    public TextField tbx_Login;
    @FXML
    public PasswordField tbx_Senha;
    @FXML
    public PasswordField tbx_ConfirmarSenha;

    @FXML
    public void Login(MouseEvent mouseEvent)  throws IOException
    {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        UIUtils.loadNewScene(stage, "GeralScenes/login_User.fxml");
    }

    @FXML
    public void Redefinir(ActionEvent actionEvent) {
        String email = tbx_Login.getText();
        String senha = tbx_Senha.getText();
        String confirmarSenha = tbx_ConfirmarSenha.getText();

        String fxmlParaCarregar = null;


        if ((senha.equals(confirmarSenha)) & (senha != ""))
        {
            if(DatabaseUtils.redefinirSenha(email, senha))
            {
                UIUtils.showAlert("Sucesso", "A senha foi redefinida com sucesso.");
                fxmlParaCarregar = "GeralScenes/login_User.fxml";
            }
            else
            {
                UIUtils.showAlert("Erro", "Verifique seu email e tente novamente.");
            }
        }
        else
        {
            UIUtils.showAlert("Erro", "As senhas devem ser iguais e não vazias.");
        }

        if (fxmlParaCarregar != null) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            UIUtils.loadNewScene(stage, fxmlParaCarregar);
        }

    }
}
