package com.example.tgcontrol.controllers.Geral;

import com.example.tgcontrol.model.TipoUsuario;
import com.example.tgcontrol.model.Turma;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.FileStorageUtils;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Profile_User_Details_C {

    private static final Logger LOGGER = Logger.getLogger(Profile_User_Details_C.class.getName());

    @FXML private ImageView imgFotoPerfil;
    @FXML private Button btnCarregarFoto;
    @FXML private Label lblNome;
    @FXML private Label lblEmail;
    @FXML private Label lblPerfil;
    @FXML private VBox vboxStudentDetails;
    @FXML private Label lblTurma;
    @FXML private Label lblOrientador;
    @FXML private Label lblStatusTG;
    @FXML private Button btnSolicitarDefesa;
    @FXML private Button btnBloquearUsuario;

    private String targetUserEmail;
    private TipoUsuario viewerTipo;
    private String viewerEmail;
    private static final String DEFAULT_PROFILE_IMAGE_PATH = "/com/example/tgcontrol/SceneImages/Task Images/fotoPerfil2Symbol.png";

    @FXML
    public void initialize() {
        setUserData(null);
    }

    public void setUserData(String targetUserEmail) {
        this.viewerTipo = SessaoManager.getInstance().getTipoUsuario();
        this.viewerEmail = SessaoManager.getInstance().getEmailUsuario();

        this.targetUserEmail = (targetUserEmail == null || targetUserEmail.isEmpty()) ? this.viewerEmail : targetUserEmail;

        boolean isViewingOwnProfile = this.targetUserEmail.equals(this.viewerEmail);

        String nomeCompleto = DatabaseUtils.getNomeUsuario(this.targetUserEmail);
        String urlFoto = DatabaseUtils.getProfilePictureUrl(this.targetUserEmail);

        lblNome.setText(nomeCompleto != null ? nomeCompleto : "[Usuário Não Encontrado]");
        lblEmail.setText(this.targetUserEmail);
        carregarFotoPerfil(urlFoto);

        Map<String, String> studentDetails = DatabaseUtils.getStudentDisplayDetails(this.targetUserEmail);
        boolean isTargetUserStudent = studentDetails != null;

        vboxStudentDetails.setVisible(isTargetUserStudent);
        vboxStudentDetails.setManaged(isTargetUserStudent);

        if (isTargetUserStudent) {
            lblPerfil.setText("Aluno");
            lblTurma.setText(studentDetails.getOrDefault("turma_descricao", "N/A"));
            lblOrientador.setText(studentDetails.getOrDefault("advisor_name", "N/A"));
            configurarStatusEBotaoDefesa(this.targetUserEmail);
        } else {
            TipoUsuario tipoUser = DatabaseUtils.autenticarUsuario(this.targetUserEmail, "Troca123");
            String tipoProfessor = "Usuário sem Perfil Completo";

            if (tipoUser == TipoUsuario.PROFESSOR_TG) {
                tipoProfessor = "Professor Coordenador de TG";
            } else if (tipoUser == TipoUsuario.PROFESSOR) {
                tipoProfessor = "Professor Orientador";
            }
            lblPerfil.setText(tipoProfessor);

            btnSolicitarDefesa.setVisible(false);
            btnSolicitarDefesa.setManaged(false);
        }

        configurarBotaoBloqueio();

        configurarBotaoTrocaFoto(isViewingOwnProfile);
    }

    private void configurarStatusEBotaoDefesa(String emailAluno) {
        boolean tgConcluido = DatabaseUtils.isTgConcluido(emailAluno);

        if (tgConcluido) {
            lblStatusTG.setText("Concluído (Pronto para Defesa)");
            lblStatusTG.setStyle("-fx-font-weight: bold; -fx-text-fill: #4CAF50;");

            if (viewerTipo == TipoUsuario.PROFESSOR_TG) {
                btnSolicitarDefesa.setText("Agendar Defesa");
                btnSolicitarDefesa.setVisible(true);
                btnSolicitarDefesa.setManaged(true);
                btnSolicitarDefesa.setStyle("-fx-background-color: #6A1B9A; -fx-background-radius: 5px;");
            } else {
                btnSolicitarDefesa.setVisible(false);
                btnSolicitarDefesa.setManaged(false);
            }
        } else {
            lblStatusTG.setText("Em Andamento");
            lblStatusTG.setStyle("-fx-font-weight: bold; -fx-text-fill: #FF9800;");
            btnSolicitarDefesa.setVisible(false);
            btnSolicitarDefesa.setManaged(false);
        }
    }

    private void configurarBotaoBloqueio() {
        if (viewerEmail.equals(targetUserEmail)) {
            btnBloquearUsuario.setVisible(false);
            btnBloquearUsuario.setManaged(false);
            return;
        }

        Turma turmaSelecionada = SessaoManager.getInstance().getTurmaSelecionada();
        boolean isViewingFromCoordination = (turmaSelecionada != null);

        if (viewerTipo == TipoUsuario.PROFESSOR_TG && isViewingFromCoordination) {
            btnBloquearUsuario.setVisible(true);
            btnBloquearUsuario.setManaged(true);
        } else {
            btnBloquearUsuario.setVisible(false);
            btnBloquearUsuario.setManaged(false);
        }
    }

    private void configurarBotaoTrocaFoto(boolean isViewingOwnProfile) {
        boolean isManageable = isViewingOwnProfile;
        btnCarregarFoto.setVisible(isManageable);
        btnCarregarFoto.setManaged(isManageable);
    }

    @FXML
    void handleCarregarFoto(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.setTitle("Selecionar Nova Foto de Perfil");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fc.showOpenDialog(stage);

        if (selectedFile != null) {
            String caminhoRelativo = FileStorageUtils.salvarFotoPerfil(selectedFile, this.viewerEmail);

            if (caminhoRelativo != null) {
                DatabaseUtils.atualizarFotoPerfil(this.viewerEmail, caminhoRelativo);
                UIUtils.showAlert("Sucesso", "Foto de perfil atualizada com sucesso!");

                carregarFotoPerfil(caminhoRelativo);
            } else {
                UIUtils.showAlert("Erro", "Não foi possível salvar a foto no servidor.");
            }
        }
    }

    @FXML
    void handleSolicitarDefesa(ActionEvent event) {
        UIUtils.showAlert("Agendamento de Defesa", "Funcionalidade: Abrir modal/tela para agendar a defesa do TG do aluno " + lblNome.getText());
    }

    @FXML
    void handleBloquearUsuario(ActionEvent event) {
        UIUtils.showAlert("Bloqueio de Usuário", "Funcionalidade: Enviar requisição para bloquear o usuário " + lblNome.getText() + ".");
    }

    private void carregarFotoPerfil(String caminhoFoto) {
        Image imagemCarregada = null;

        if (caminhoFoto != null && !caminhoFoto.isEmpty()) {
            File arquivoFoto = new File(caminhoFoto);
            if (arquivoFoto.exists() && arquivoFoto.isFile()) {
                imagemCarregada = new Image(arquivoFoto.toURI().toString());
            } else {
                try {
                    imagemCarregada = new Image(getClass().getResourceAsStream(caminhoFoto));
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Caminho de foto não encontrado no sistema de arquivos ou recursos: " + caminhoFoto);
                }
            }
        }

        if (imagemCarregada == null) {
            try {
                imagemCarregada = new Image(getClass().getResourceAsStream(DEFAULT_PROFILE_IMAGE_PATH));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Não foi possível carregar a imagem padrão: " + DEFAULT_PROFILE_IMAGE_PATH, e);
            }
        }

        if (imagemCarregada != null) {
            imgFotoPerfil.setImage(imagemCarregada);
            Circle clip = new Circle(60, 60, 60);
            imgFotoPerfil.setClip(clip);
        }
    }
}