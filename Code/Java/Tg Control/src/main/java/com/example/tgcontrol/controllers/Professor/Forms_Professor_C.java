package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.model.TipoUsuario;
import com.example.tgcontrol.model.Turma;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.FileStorageUtils;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Forms_Professor_C {

    @FXML private ImageView imgFotoPerfil;
    @FXML private Button btnCarregarFoto;
    @FXML private ChoiceBox<String> cbTipoPerfil;
    @FXML private VBox vboxCoordenacao;
    @FXML private TableView<Turma> tvTurmas;
    @FXML private TableColumn<Turma, String> colTurmaNome;
    @FXML private TableColumn<Turma, Boolean> colEtapaTG1;
    @FXML private TableColumn<Turma, Boolean> colEtapaTG2;
    @FXML private Button btnSalvar;

    private File fotoPerfilFile;
    private ObservableList<Turma> turmasDisponiveis = FXCollections.observableArrayList();

    private final String PERFIL_ORIENTADOR = "Professor Orientador";
    private final String PERFIL_COORDENADOR = "Professor Coordenador de TG";

    @FXML
    public void initialize() {
        try {
            Image placeholder = new Image(getClass().getResourceAsStream("/com/example/tgcontrol/SceneImages/Task Images/fotoPerfil2Symbol.png"));
            imgFotoPerfil.setImage(placeholder);
        } catch (Exception e) { e.printStackTrace(); }

        cbTipoPerfil.setItems(FXCollections.observableArrayList(PERFIL_ORIENTADOR, PERFIL_COORDENADOR));
        cbTipoPerfil.setValue(PERFIL_ORIENTADOR);

        cbTipoPerfil.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isCoordenador = PERFIL_COORDENADOR.equals(newVal);
            vboxCoordenacao.setVisible(isCoordenador);
            vboxCoordenacao.setManaged(isCoordenador);
        });

        configurarTabelaCoordenacao();

        carregarTurmas();
    }

    private void configurarTabelaCoordenacao() {
        colTurmaNome.setCellValueFactory(data -> data.getValue().descricaoTurmaProperty());

        colEtapaTG1.setCellValueFactory(data -> data.getValue().etapa1Property());
        colEtapaTG1.setCellFactory(CheckBoxTableCell.forTableColumn(colEtapaTG1));
        colEtapaTG1.setEditable(true);

        colEtapaTG2.setCellValueFactory(data -> data.getValue().etapa2Property());
        colEtapaTG2.setCellFactory(CheckBoxTableCell.forTableColumn(colEtapaTG2));
        colEtapaTG2.setEditable(true);

        tvTurmas.setEditable(true);
        tvTurmas.setItems(turmasDisponiveis);
    }

    private void carregarTurmas() {
        List<Turma> turmasFromDB = DatabaseUtils.getListaTurmas();
        turmasDisponiveis.setAll(turmasFromDB);
    }

    @FXML
    void handleCarregarFoto(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecionar Foto de Perfil");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fc.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            fotoPerfilFile = selectedFile;
            try {
                imgFotoPerfil.setImage(new Image(selectedFile.toURI().toString()));
            } catch (Exception e) { UIUtils.showAlert("Erro", "Não foi possível carregar a imagem."); fotoPerfilFile = null; }
        }
    }

    @FXML
    void handleSalvar(ActionEvent event) {
        String emailProfessor = SessaoManager.getInstance().getEmailUsuario();
        if (emailProfessor == null) {
            UIUtils.showAlert("Erro Crítico", "Sessão expirada. Faça login novamente.");
            return;
        }

        String tipoPerfil = cbTipoPerfil.getValue();
        boolean isCoordenador = PERFIL_COORDENADOR.equals(tipoPerfil);

        if (fotoPerfilFile != null) {
            String caminhoRelativo = FileStorageUtils.salvarFotoPerfil(fotoPerfilFile, emailProfessor);
            if (caminhoRelativo != null) {
                DatabaseUtils.atualizarFotoPerfil(emailProfessor, caminhoRelativo);
            }
        }

        Map<Turma, List<Integer>> coordenacoes = new HashMap<>();
        if (isCoordenador) {
            for (Turma turma : turmasDisponiveis) {
                List<Integer> etapas = new ArrayList<>();
                if (turma.isEtapa1()) etapas.add(1);
                if (turma.isEtapa2()) etapas.add(2);

                if (!etapas.isEmpty()) {
                    coordenacoes.put(turma, etapas);
                }
            }
            if (coordenacoes.isEmpty()) {
                UIUtils.showAlert("Erro", "Como Coordenador, você deve selecionar pelo menos uma turma e etapa para coordenar.");
                return;
            }
        }

        boolean sucesso = DatabaseUtils.completarCadastroProfessor(emailProfessor, isCoordenador, coordenacoes);

        if (sucesso) {
            TipoUsuario tipoUsuario = isCoordenador ? TipoUsuario.PROFESSOR_TG : TipoUsuario.PROFESSOR;
            SessaoManager.getInstance().iniciarSessao(emailProfessor, tipoUsuario);
            UIUtils.showAlert("Sucesso", "Cadastro de professor concluído!");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            String fxmlDashboard = isCoordenador ? "ProfessorTGScenes/navbar_ProfessorTG.fxml" : "ProfessorScenes/navbar_Professor.fxml";
            UIUtils.loadNewScene(stage, fxmlDashboard);

        } else {
        }
    }
}