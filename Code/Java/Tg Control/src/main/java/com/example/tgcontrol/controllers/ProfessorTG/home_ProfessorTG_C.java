package com.example.tgcontrol.controllers.ProfessorTG;

import com.example.tgcontrol.model.DashboardTgData;
import com.example.tgcontrol.model.TrabalhoPendente;
import com.example.tgcontrol.utils.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class home_ProfessorTG_C implements Initializable {

    @FXML private Label lblTotalAlunos;
    @FXML private Label lblTgsConcluidos;
    @FXML private Label lblTotalOrientandos;
    @FXML private BarChart<String, Number> graficoProgressoAlunos;
    @FXML private VBox placeholderContainer;
    @FXML private TableView<TrabalhoPendente> tabelaPendentes;
    @FXML private TableColumn<TrabalhoPendente, Double> colProgresso;
    @FXML private TableColumn<TrabalhoPendente, String> colAluno;
    @FXML private TableColumn<TrabalhoPendente, String> colTurma;
    @FXML private TableColumn<TrabalhoPendente, String> colSemestre;
    @FXML private TableColumn<TrabalhoPendente, Void> colAcao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarDadosDashboard();
    }

    private void configurarTabela() {
        colAluno.setCellValueFactory(new PropertyValueFactory<>("nomeAluno"));
        colTurma.setCellValueFactory(new PropertyValueFactory<>("turma"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));

        colProgresso.setCellValueFactory(new PropertyValueFactory<>("progresso"));
        colProgresso.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String percentageText = String.format("%.0f%%", item * 100);
                    setText(percentageText);
                    setGraphic(null);
                }
            }
        });

        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Pendente");
            {
                btn.getStyleClass().add("action-button");
                btn.setOnAction(event -> {
                    try {
                        String fxmlPath = "/com/example/tgcontrol/Scenes/ProfessorScenes/correcao_View.fxml";
                        URL fxmlLocation = getClass().getResource(fxmlPath);
                        FXMLLoader loader = new FXMLLoader(fxmlLocation);
                        Parent novaTela = loader.load();

                        StackPane contentArea = (StackPane) tabelaPendentes.getScene().lookup("#contentArea");
                        if (contentArea != null) {
                            contentArea.getChildren().clear();
                            contentArea.getChildren().add(novaTela);
                        } else {
                            System.err.println("Erro Crítico: #contentArea não foi encontrado na cena.");
                        }
                    } catch (IOException e) {
                        Logger.getLogger(home_ProfessorTG_C.class.getName()).log(Level.SEVERE, "Falha ao carregar FXML.", e);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erro de Navegação");
                        alert.setHeaderText("Não foi possível carregar a tela de correção.");
                        alert.showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        tabelaPendentes.managedProperty().bind(tabelaPendentes.visibleProperty());
        placeholderContainer.managedProperty().bind(placeholderContainer.visibleProperty());
    }

    private void carregarDadosDashboard() {
        DashboardTgData dados = DatabaseUtils.getProfessorTGDashboardData();

        lblTotalAlunos.setText(String.valueOf(dados.getTotalAlunos()));
        lblTgsConcluidos.setText(String.valueOf(dados.getTgsConcluidos()));
        lblTotalOrientandos.setText(String.valueOf(dados.getTotalOrientandos()));

        configurarGraficoProgresso(dados.getProgressoAlunos());
        popularTabela(dados.getTrabalhos());
    }

    private void configurarGraficoProgresso(Map<String, Integer> dadosGrafico) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total de Alunos");

        for (Map.Entry<String, Integer> entry : dadosGrafico.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        graficoProgressoAlunos.getData().clear();
        graficoProgressoAlunos.getData().add(series);
        graficoProgressoAlunos.setLegendVisible(false);
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