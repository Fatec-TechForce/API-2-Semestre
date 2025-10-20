package com.example.tgcontrol.controllers.Geral;

import com.example.tgcontrol.model.VersaoTG;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Versoes_User_C {

    @FXML private TableView<VersaoTG> tableView;
    @FXML private TableColumn<VersaoTG, String> colNome;
    @FXML private TableColumn<VersaoTG, String> colData;
    @FXML private Button btnBaixar;
    @FXML private Button btnVerDetalhes;

    // Exemplo: e-mail do aluno logado
    private final String emailAluno = "guilherme.arruda@aluno.fatec.sp.gov.br";

    @FXML
    public void initialize() {
        // Configura as colunas
        colNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomeArquivo()));
        colData.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDataUpload().toString()));

        // Aplica o estilo da tabela
        String css = getClass().getResource("/com/example/tgcontrol/SceneStyles/tableStyle.css").toExternalForm();
        tableView.getStylesheets().add(css);

        // Carrega automaticamente as versões
        carregarVersoes();

        // Desabilita o botão "Ver Detalhes" até selecionar uma linha
        btnVerDetalhes.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void carregarVersoes() {
        List<VersaoTG> versoes = DatabaseUtils.listarVersoesPorAluno(emailAluno);

        ObservableList<VersaoTG> observableList = FXCollections.observableArrayList(versoes);
        tableView.setItems(observableList);

        if (versoes.isEmpty()) {
            UIUtils.showAlert("Informação", "Nenhuma versão encontrada para este aluno.");
        }
    }

    @FXML
    public void onBaixar(ActionEvent event) {
        VersaoTG selecionado = tableView.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            UIUtils.showAlert("Download Simulado", "Baixando arquivo: " + selecionado.getNomeArquivo());
            System.out.println("Simulando download de: " + selecionado.getCaminhoArquivo());
        } else {
            UIUtils.showAlert("Aviso", "Selecione uma versão antes de baixar!");
        }
    }

    @FXML
    public void onVerDetalhes(ActionEvent event) {
        VersaoTG selecionado = tableView.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            UIUtils.showAlert("Aviso", "Selecione uma versão para ver os detalhes.");
            return;
        }

        abrirPopupDetalhes(selecionado);
    }

    private void abrirPopupDetalhes(VersaoTG versao) {
        UIUtils.openPopupWindow("/com/example/tgcontrol/GeralScenes/detalhesVersao_User.fxml", "Detalhes da Versão");
    }
}