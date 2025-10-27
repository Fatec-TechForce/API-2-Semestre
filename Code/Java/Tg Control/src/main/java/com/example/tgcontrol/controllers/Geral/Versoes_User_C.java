package com.example.tgcontrol.controllers.Geral;

import com.example.tgcontrol.model.VersaoTG;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    @FXML private Button btnVoltar;

    // Exemplo: e-mail do aluno logado (deve ser obtido do SessaoManager)
    // private final String emailAluno = SessaoManager.getInstance().getUsuarioLogado().getEmail();
    private final String emailAluno = "maria.oliveira@fatec.sp.gov.br"; // Mock para teste

    // Simulação: A tarefa/seção que está sendo visualizada (deve ser obtida do contexto da tela anterior)
    private final int sequence_order = 1; // Mock para teste

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

        // Desabilita os botões até selecionar uma linha
        btnVerDetalhes.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        btnBaixar.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void carregarVersoes() {
        // Agora, o método requer o taskSequence. O valor '1' é um mock.
        List<VersaoTG> versoes = DatabaseUtils.listarVersoesPorTask(emailAluno, sequence_order);

        ObservableList<VersaoTG> observableList = FXCollections.observableArrayList(versoes);
        tableView.setItems(observableList);

        if (versoes.isEmpty()) {
            tableView.setPlaceholder(new Label("Nenhuma versão encontrada para esta seção."));
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

    // Em desenvolvimento
    @FXML
    public void onVerDetalhes(ActionEvent event) {
        //VersaoTG selecionado = tableView.getSelectionModel().getSelectedItem();

        //if (selecionado == null) {
        //    UIUtils.showAlert("Aviso", "Selecione uma versão para ver os detalhes.");
        //    return;
        //}

        //  abrirPopupDetalhes(selecionado);
    }

    private void abrirPopupDetalhes(VersaoTG versao) {
        UIUtils.openPopupWindow("/com/example/tgcontrol/Scenes/GeralScenes/detalhesVersao_User.fxml", "Detalhes da Versão");
    }

    @FXML
    private void voltar(ActionEvent event){
        String fxmlParaCarregar = "AlunoScenes/secao_Aluno.fxml";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        UIUtils.loadFxml(fxmlParaCarregar);
    }
}
