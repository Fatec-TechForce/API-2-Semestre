package com.example.tgcontrol.controllers.Geral;

import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.model.TipoUsuario;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

        TipoUsuario tipo = DatabaseUtils.autenticarUsuario(login, senha);
        String fxmlParaCarregar = null;

        if (tipo != TipoUsuario.NAO_AUTENTICADO) {
            SessaoManager.getInstance().iniciarSessao(login, tipo);
            switch (tipo) {
                case ALUNO:
                    fxmlParaCarregar = "AlunoScenes/navbar_Aluno.fxml";
                    break;
                case PROFESSOR:
                    fxmlParaCarregar = "ProfessorScenes/navbar_Professor.fxml";
                    break;
                case PROFESSOR_TG:
                    fxmlParaCarregar = "ProfessorTGScenes/navbar_ProfessorTG.fxml";
                    break;
                case PERFIL_INCOMPLETO:
                    UIUtils.showAlert("Perfil Incompleto", "Olá! Parece que é seu primeiro acesso. Por favor, complete seu perfil.");
                    fxmlParaCarregar = "GeralScenes/escolha_Perfil.fxml";
                    break;
                default:
                    break;
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
        String fxmlParaCarregar = "GeralScenes/registration_User.fxml";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        UIUtils.loadNewScene(stage, fxmlParaCarregar);
    }

    public void irParaEsqueceuSenha(ActionEvent actionEvent)
    {
        String fxmlParaCarregar = "GeralScenes/passwordReset_User.fxml";
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        UIUtils.loadNewScene(stage, fxmlParaCarregar);
    }
}