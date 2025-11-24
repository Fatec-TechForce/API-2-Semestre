package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.controllers.BaseNavbarController;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Navbar_Aluno_C extends BaseNavbarController {

    @Override
    protected String getInitialFxmlPath() {
        return "AlunoScenes/home_Aluno.fxml";
    }

    @FXML
    public void home(ActionEvent actionEvent) {
        UIUtils.loadFxml("AlunoScenes/home_Aluno.fxml");
    }

    @FXML
    public void andamentoTG(ActionEvent actionEvent) {UIUtils.loadFxml("AlunoScenes/secoes_Aluno.fxml");}

    //Muda a aparência do botão ao passar em cima
    @FXML
    public void escurecerBotaoNavbar(MouseEvent mouseEvent) {
        Button btn = (Button) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #D3D5E0;");
    }

    @FXML
    public void  esbranquecerBotaoNavBar(MouseEvent mouseEvent) {
        Button btn = (Button) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #FFFFFF;");
    }
}