package com.example.tgcontrol.controllers.ProfessorTG;

import com.example.tgcontrol.model.Turma;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class turmas_ProfessorTG_C implements Initializable {

    @FXML private TableView<Turma> tvTurmas;
    @FXML private TableColumn<Turma, String> colDisciplina;
    @FXML private TableColumn<Turma, Integer> colAno;
    @FXML private TableColumn<Turma, Integer> colSemestre;
    @FXML private TableColumn<Turma, Void> colAcao;

    private static final Logger LOGGER = Logger.getLogger(turmas_ProfessorTG_C.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarTurmas();
    }

    private void configurarTabela() {
        colDisciplina.setCellValueFactory(new PropertyValueFactory<>("disciplina"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));

        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Ver Alunos");

            {
                btn.getStyleClass().add("action-button");
                btn.setPrefWidth(120);

                btn.setOnAction(event -> {
                    Turma turma = getTableView().getItems().get(getIndex());

                    SessaoManager.getInstance().setTurmaSelecionada(turma);

                    UIUtils.loadFxml("ProfessorScenes/coordinations_Professor.fxml");
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
    }

    private void carregarTurmas() {
        String emailProfessor = SessaoManager.getInstance().getEmailUsuario();

        List<Turma> turmas;
        if (emailProfessor == null || emailProfessor.isEmpty()) {
            turmas = Collections.emptyList();
            LOGGER.log(Level.WARNING, "Não foi possível carregar turmas coordenadas: email do usuário não encontrado na sessão.");
        } else {
            turmas = DatabaseUtils.getListaTurmasCoordenadas(emailProfessor);
        }

        if (turmas != null && !turmas.isEmpty()) {
            ObservableList<Turma> observableList = FXCollections.observableArrayList(turmas);
            tvTurmas.setItems(observableList);
        } else {
            tvTurmas.setPlaceholder(new Label("Nenhuma turma coordenada encontrada."));
        }
    }
}