package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.model.DashboardData;
import com.example.tgcontrol.model.SessaoManager;
import com.example.tgcontrol.model.TrabalhoPendente;
import com.example.tgcontrol.utils.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class home_Professor_C implements Initializable {

    @FXML private Label lblTotalAlunos;
    @FXML private Label lblTgsConcluidos;
    @FXML private Label lblPendentes;
    @FXML private VBox placeholderContainer;
    @FXML private TableView<TrabalhoPendente> tabelaTgs;
    @FXML private TableColumn<TrabalhoPendente, Double> colProgresso;
    @FXML private TableColumn<TrabalhoPendente, String> colAluno;
    @FXML private TableColumn<TrabalhoPendente, String> colTurma;
    @FXML private TableColumn<TrabalhoPendente, String> colSemestre;
    @FXML private TableColumn<TrabalhoPendente, Void> colAcao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarDadosDoDashboard();
    }

    private void configurarTabela() {
        colAluno.setCellValueFactory(new PropertyValueFactory<>("nomeAluno"));
        colTurma.setCellValueFactory(new PropertyValueFactory<>("turma"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));

        colProgresso.setCellValueFactory(new PropertyValueFactory<>("progresso"));
        colProgresso.setCellFactory(column -> new TableCell<>() {
            private final ProgressBar progressBar = new ProgressBar();
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    progressBar.setProgress(item);
                    setGraphic(progressBar);
                }
            }
        });

        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Pendente");
            {
                btn.getStyleClass().add("action-button");
                btn.setOnAction(event -> {
                    TrabalhoPendente trabalho = getTableView().getItems().get(getIndex());
                    try {
                        String fxmlPath = "/com/example/tgcontrol/ProfessorScenes/correcao_View.fxml";
                        URL fxmlLocation = getClass().getResource(fxmlPath);
                        FXMLLoader loader = new FXMLLoader(fxmlLocation);
                        Parent novaTela = loader.load();


                        StackPane contentArea = (StackPane) tabelaTgs.getScene().lookup("#contentArea");
                        if (contentArea != null) {
                            contentArea.getChildren().clear();
                            contentArea.getChildren().add(novaTela);
                        } else {
                            System.err.println("Erro Crítico: #contentArea não foi encontrado na cena.");
                        }
                    } catch (IOException e) {
                        Logger.getLogger(home_Professor_C.class.getName()).log(Level.SEVERE, "Falha ao carregar FXML.", e);
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(btn);
            }
        });

        tabelaTgs.managedProperty().bind(tabelaTgs.visibleProperty());
        placeholderContainer.managedProperty().bind(placeholderContainer.visibleProperty());
    }

    private void carregarDadosDoDashboard() {
        String emailUsuario = SessaoManager.getInstance().getEmailUsuario();
        DashboardData dados = DatabaseUtils.getProfessorDashboardData(emailUsuario);

        lblTotalAlunos.setText(String.valueOf(dados.getTotalAlunos()));
        lblTgsConcluidos.setText(String.valueOf(dados.getTgsConcluidos()));
        lblPendentes.setText(String.valueOf(dados.getPendentes()));

        boolean haTrabalhosPendentes = dados.getTrabalhos() != null && !dados.getTrabalhos().isEmpty();

        if (haTrabalhosPendentes) {
            tabelaTgs.setItems(FXCollections.observableArrayList(dados.getTrabalhos()));
            tabelaTgs.setVisible(true);
            placeholderContainer.setVisible(false);
        } else {
            tabelaTgs.setVisible(false);
            placeholderContainer.setVisible(true);
        }
    }
}