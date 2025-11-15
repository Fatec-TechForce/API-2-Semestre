package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.model.TipoUsuario;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forms_Aluno_C {

    @FXML private ImageView imgFotoPerfil;
    @FXML private Button btnCarregarFoto;
    @FXML private TextField txtEmailPessoal;
    @FXML private ComboBox<String> cbTurma;
    @FXML private ComboBox<String> cbOrientador;
    @FXML private ChoiceBox<String> cbTipoTG;
    @FXML private VBox vboxProblema;
    @FXML private TextField txtProblema;
    @FXML private Button btnCarregarAcordo;
    @FXML private Label lblNomeArquivoAcordo;
    @FXML private Button btnSalvar;

    private File fotoPerfilFile;
    private File acordoFile;

    private static final Pattern TURMA_PATTERN = Pattern.compile("^(.*) \\((\\d{4})/(\\d)\\)$");
    private static final Pattern PROF_PATTERN = Pattern.compile("\\(([^)]+)\\)");

    @FXML
    public void initialize() {
        try {
            Image placeholder = new Image(getClass().getResourceAsStream("/com/example/tgcontrol/SceneImages/Task Images/fotoPerfil2Symbol.png"));
            imgFotoPerfil.setImage(placeholder);
        } catch (Exception e) { e.printStackTrace(); }

        cbTurma.setItems(FXCollections.observableArrayList(DatabaseUtils.getListaTurmas()));
        cbOrientador.setItems(FXCollections.observableArrayList(DatabaseUtils.getListaProfessores()));

        cbTipoTG.setItems(FXCollections.observableArrayList("Artigo Tecnológico", "Relatório Técnico", "Outro"));
        cbTipoTG.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isArtigo = "Artigo Tecnológico".equals(newVal);
            vboxProblema.setVisible(isArtigo);
            vboxProblema.setManaged(isArtigo);
        });
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
    void handleAnexarAcordo(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Anexar Acordo de Orientação");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Documentos", "*.pdf", "*.docx"));
        File selectedFile = fc.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            acordoFile = selectedFile;
            lblNomeArquivoAcordo.setText(selectedFile.getName());
        } else {
            acordoFile = null; lblNomeArquivoAcordo.setText("Nenhum arquivo selecionado");
        }
    }

    @FXML
    void handleSalvar(ActionEvent event) {
        String emailAluno = SessaoManager.getInstance().getEmailUsuario();
        if (emailAluno == null) {
            UIUtils.showAlert("Erro Crítico", "Sessão expirada. Faça login novamente.");
            return;
        }

        String orientadorStr = extrairEmail(cbOrientador.getValue(), PROF_PATTERN);
        Map<String, String> turmaMap = extrairDadosTurma(cbTurma.getValue());

        if (txtEmailPessoal.getText().isBlank() || cbTipoTG.getValue() == null || orientadorStr == null || turmaMap == null || acordoFile == null) {
            UIUtils.showAlert("Erro", "Todos os campos e o 'Acordo de Orientação' são obrigatórios.");
            return;
        }

        String tipoTG = cbTipoTG.getValue();
        String problema = "Artigo Tecnológico".equals(tipoTG) ? txtProblema.getText() : "";
        if ("Artigo Tecnológico".equals(tipoTG) && problema.isBlank()) {
            UIUtils.showAlert("Erro", "Para 'Artigo Tecnológico', o campo 'Problema a ser resolvido' é obrigatório.");
            return;
        }

        Map<String, String> dadosCadastro = new HashMap<>();
        dadosCadastro.put("emailPessoal", txtEmailPessoal.getText().trim());
        dadosCadastro.put("tipoTG", tipoTG);
        dadosCadastro.put("problema", problema.trim());
        dadosCadastro.put("emailOrientador", orientadorStr);
        dadosCadastro.putAll(turmaMap);

        boolean sucesso = DatabaseUtils.completarCadastroAluno(
                emailAluno,
                dadosCadastro,
                fotoPerfilFile,
                acordoFile
        );

        if (sucesso) {
            SessaoManager.getInstance().iniciarSessao(emailAluno, TipoUsuario.ALUNO);
            UIUtils.showAlert("Sucesso", "Cadastro concluído! Bem-vindo(a).");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            UIUtils.loadNewScene(stage, "AlunoScenes/navbar_Aluno.fxml");
        } else {
        }
    }

    private String extrairEmail(String valorComboBox, Pattern pattern) {
        if (valorComboBox == null) return null;
        Matcher matcher = pattern.matcher(valorComboBox);
        return matcher.find() ? matcher.group(1) : null;
    }

    private Map<String, String> extrairDadosTurma(String valorComboBox) {
        if (valorComboBox == null) return null;
        Matcher matcher = TURMA_PATTERN.matcher(valorComboBox);
        if (matcher.find()) {
            Map<String, String> map = new HashMap<>();
            map.put("disciplina", matcher.group(1));
            map.put("ano", matcher.group(2));
            map.put("semestre", matcher.group(3));
            return map;
        }
        return null;
    }
}