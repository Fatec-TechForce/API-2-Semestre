package com.example.tgcontrol.controllers.Geral;

import com.example.tgcontrol.model.VersaoTG;
import com.example.tgcontrol.utils.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class Versoes_User_C {

    @FXML private TableView<VersaoTG> tableView;
    @FXML private TableColumn<VersaoTG, String> colNome;
    @FXML private TableColumn<VersaoTG, String> colData;
    @FXML private Button btnCarregar;
    @FXML private Button btnBaixar;

    // exemplo: o e-mail do aluno logado (pode vir de uma tela anterior ou sessão)
    private String emailAluno = "guilherme.arruda@aluno.fatec.sp.gov.br";

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomeArquivo()));
        colData.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDataUpload().toString()
        ));
    }

    @FXML
    public void onCarregar(ActionEvent event) {
        List<VersaoTG> versoes = DatabaseUtils.listarVersoesPorAluno(emailAluno);

        if (versoes.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Nenhuma versão encontrada para este aluno.");
            alert.showAndWait();
        }

        ObservableList<VersaoTG> observableList = FXCollections.observableArrayList(versoes);
        tableView.setItems(observableList);
    }

    @FXML
    public void onBaixar(ActionEvent event) {
        VersaoTG selecionado = tableView.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Download Simulado");
            alert.setContentText("Baixando arquivo: " + selecionado.getNomeArquivo());
            alert.showAndWait();

            System.out.println("Simulando download de: " + selecionado.getCaminhoArquivo());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Selecione uma versão antes de baixar!");
            alert.showAndWait();
        }
    }
}