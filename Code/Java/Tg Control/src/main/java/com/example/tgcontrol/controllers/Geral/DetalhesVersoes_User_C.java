package com.example.tgcontrol.controllers.Geral;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import com.example.tgcontrol.model.VersaoTG;

public class DetalhesVersoes_User_C {

    @FXML private Label lblNomeArquivo;
    @FXML private TextArea txtComentario;

    private VersaoTG versao;

    public void setVersao(VersaoTG versao) {
        this.versao = versao;
        lblNomeArquivo.setText(versao.getNomeArquivo());
        //txtComentario.setText(versao.getComentario());
    }

    @FXML
    private void fecharPopup() {
        Stage stage = (Stage) lblNomeArquivo.getScene().getWindow();
        stage.close();
    }
}