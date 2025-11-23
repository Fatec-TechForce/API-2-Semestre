package com.example.tgcontrol.controllers.ProfessorTG;

import com.example.tgcontrol.model.DashboardTgData;
import com.example.tgcontrol.model.TrabalhoPendente;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.LinkedHashMap;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Home_ProfessorTG_C implements Initializable {

    @FXML private Label lblTotalAlunos;
    @FXML private Label lblTgsConcluidos;
    @FXML private Label lblTotalOrientandos;
    @FXML private BarChart<String, Number> graficoProgressoAlunos;
    @FXML private VBox placeholderContainer;
    @FXML private TableView<TrabalhoPendente> tabelaPendentes;
    @FXML private TableColumn<TrabalhoPendente, Double> colProgresso;
    @FXML private TableColumn<TrabalhoPendente, String> colAluno;
    @FXML private TableColumn<TrabalhoPendente, String> colTurma;
    @FXML private TableColumn<TrabalhoPendente, Void> colAcao;

    private static final Logger LOGGER = Logger.getLogger(Home_ProfessorTG_C.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarDadosDashboard();
    }

    private void configurarTabela() {
        colAluno.setCellValueFactory(new PropertyValueFactory<>("nomeAluno"));
        colTurma.setCellValueFactory(new PropertyValueFactory<>("turma"));

        colProgresso.setCellValueFactory(new PropertyValueFactory<>("progresso"));
        colProgresso.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); setGraphic(null);
                    this.setAlignment(Pos.CENTER);
                } else {
                    setText(String.format("%.0f%%", item * 100)); setGraphic(null);
                    this.setAlignment(Pos.CENTER);
                }
            }
        });

        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Ver Aluno");
            {
                btn.getStyleClass().add("action-button");
                btn.setMaxWidth(Double.MAX_VALUE);

                btn.setOnAction(event -> {
                    TrabalhoPendente trabalho = getTableView().getItems().get(getIndex());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    this.setAlignment(Pos.CENTER);
                    setGraphic(btn);
                }
            }
        });

        tabelaPendentes.managedProperty().bind(tabelaPendentes.visibleProperty());
        placeholderContainer.managedProperty().bind(placeholderContainer.visibleProperty());
    }

    private void carregarDadosDashboard() {
        String emailProfessorTg = SessaoManager.getInstance().getEmailUsuario();
        if (emailProfessorTg == null) {
            LOGGER.log(Level.SEVERE, "Não foi possível obter o email do usuário logado.");
            DashboardTgData dadosVazios = new DashboardTgData(0,0,0, new LinkedHashMap<>(), Collections.emptyList());
            atualizarUI(dadosVazios);
            return;
        }

        DashboardTgData dados = DatabaseUtils.getProfessorTGDashboardData(emailProfessorTg);
        atualizarUI(dados);
    }

    private void atualizarUI(DashboardTgData dados) {
        lblTotalAlunos.setText(String.valueOf(dados.getTotalAlunos()));
        lblTgsConcluidos.setText(String.valueOf(dados.getTgsConcluidos()));
        lblTotalOrientandos.setText(String.valueOf(dados.getTotalOrientandos()));

        configurarGraficoProgresso(dados.getProgressoAlunos());
        popularTabela(dados.getTrabalhos());
    }


    private void configurarGraficoProgresso(Map<String, Integer> dadosGrafico) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total de Alunos");
        series.getData().add(new XYChart.Data<>("Concluído", dadosGrafico.getOrDefault("Concluído", 0)));
        series.getData().add(new XYChart.Data<>("Em Dia", dadosGrafico.getOrDefault("Em Dia", 0)));
        series.getData().add(new XYChart.Data<>("Atrasado", dadosGrafico.getOrDefault("Atrasado", 0)));
        series.getData().add(new XYChart.Data<>("Não Iniciado", dadosGrafico.getOrDefault("Não Iniciado", 0)));

        graficoProgressoAlunos.getData().setAll(series);
        graficoProgressoAlunos.setLegendVisible(false);

        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-bar-fill: #6A1B9A;");
                }
            }
        });
    }

    private void popularTabela(List<TrabalhoPendente> trabalhos) {
        boolean haPendencias = trabalhos != null && !trabalhos.isEmpty();
        if (haPendencias) {
            tabelaPendentes.setItems(FXCollections.observableArrayList(trabalhos));
            tabelaPendentes.setVisible(true);
            placeholderContainer.setVisible(false);
        } else {
            tabelaPendentes.setVisible(false);
            placeholderContainer.setVisible(true);
        }
    }
}