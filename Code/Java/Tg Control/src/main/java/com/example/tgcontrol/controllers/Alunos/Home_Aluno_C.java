package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.model.SecaoAluno;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Home_Aluno_C implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(Home_Aluno_C.class.getName());

    @FXML private Label lblBemVindo;
    @FXML private ProgressBar pbProgressoGeral;
    @FXML private Label lblProgressoEtapas;
    @FXML private Label lblProgressoPorcentagem;
    @FXML private Label lblTituloUltimaSecao;
    @FXML private Label lblStatusUltimaSecao;
    @FXML private Button btnVerUltimaSecao;
    @FXML private VBox ultimaSecaoContainer;
    @FXML private HBox ultimaSecaoHBox;
    @FXML private Label lblNenhumaSecao;

    private SecaoAluno ultimaSecao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarInformacoesIniciais();
    }

    private void carregarInformacoesIniciais() {
        String emailAluno = SessaoManager.getInstance().getEmailUsuario();
        if (emailAluno == null) {
            LOGGER.log(Level.SEVERE, "Email do aluno não encontrado na sessão.");
            lblBemVindo.setText("Bem-vindo(a)!");
            pbProgressoGeral.setProgress(0);
            lblProgressoEtapas.setText("Etapa ? de ?");
            lblProgressoPorcentagem.setText("(0%)");
            mostrarMensagemNenhumaSecao();
            return;
        }

        String nomeAluno = DatabaseUtils.getNomeUsuario(emailAluno);
        if (nomeAluno != null && !nomeAluno.isEmpty()) {
            lblBemVindo.setText("Bem-vindo(a), " + nomeAluno.split(" ")[0] + "!");
        } else {
            lblBemVindo.setText("Bem-vindo(a)!");
        }

        Map<String, Integer> configAluno = DatabaseUtils.getEstagioEConfigAluno(emailAluno);
        ultimaSecao = DatabaseUtils.getUltimaSecaoAtiva(emailAluno);

        if (configAluno != null && ultimaSecao != null) {
            int maxTasks = configAluno.getOrDefault("maxTasks", 6);
            int etapaAtual = ultimaSecao.getTaskSequence();
            double progresso;
            String statusGeral = ultimaSecao.getStatus() != null ? ultimaSecao.getStatus().toLowerCase() : "locked";

            if (statusGeral.equals("completed") && etapaAtual == maxTasks) {
                progresso = 1.0;
            } else if (statusGeral.equals("in_progress")) {
                progresso = (double) (etapaAtual - 1) / maxTasks;
            } else if (statusGeral.equals("completed")) {
                progresso = (double) etapaAtual / maxTasks; // Se completou, conta como progresso da etapa
            } else {
                progresso = (double) (etapaAtual - 1) / maxTasks;
            }


            pbProgressoGeral.setProgress(progresso);
            if (statusGeral.equals("completed")) {
                lblProgressoEtapas.setText("Etapa " + (etapaAtual) + " de " + maxTasks);
            }
            else {
                lblProgressoEtapas.setText("Etapa " + (etapaAtual - 1) + " de " + maxTasks);
            }
            lblProgressoPorcentagem.setText(String.format("(%.0f%%)", progresso * 100));

            lblTituloUltimaSecao.setText(ultimaSecao.getTitulo());

            String statusRevisao = ultimaSecao.getStatusRevisao() != null ? ultimaSecao.getStatusRevisao().toLowerCase() : "---";
            String textoStatus = "Status Desconhecido";
            String styleStatusButton = "-fx-background-radius: 5px; -fx-border-radius: 5px;";

            switch (statusRevisao) {
                case "approved":
                case "aprovado":
                    textoStatus = "Status: Aprovado";
                    styleStatusButton += "-fx-background-color: #4CAF50;"; // Verde
                    break;
                case "revision_requested":
                    textoStatus = "Status: Revisão Solicitada";
                    styleStatusButton += "-fx-background-color: #FF9800;"; // Laranja
                    break;
                case "pendente":
                    textoStatus = "Status: Aguardando Revisão";
                    styleStatusButton += "-fx-background-color: #FFC107;"; // Amarelo
                    break;
                case "---":
                default:
                    if (statusGeral.equals("in_progress")) {
                        textoStatus = "Status: Em Andamento";
                        styleStatusButton += "-fx-background-color: #2196F3;"; // Azul
                    } else if (statusGeral.equals("locked")) {
                        textoStatus = "Status: Bloqueado";
                        styleStatusButton += "-fx-background-color: #9E9E9E;"; // Cinza
                    } else if (statusGeral.equals("completed") && statusRevisao.equals("---")) {
                        textoStatus = "Status: Concluído";
                        styleStatusButton += "-fx-background-color: #4CAF50;"; // Verde
                    } else {
                        textoStatus = "Status: Não Enviado";
                        styleStatusButton += "-fx-background-color: #9E9E9E;"; // Cinza
                    }
                    break;
            }

            lblStatusUltimaSecao.setText(textoStatus);
            btnVerUltimaSecao.setStyle(styleStatusButton);
            mostrarUltimaSecao();

        } else {
            LOGGER.log(Level.WARNING, "Não foi possível carregar dados de progresso ou seção para o aluno: " + emailAluno);
            pbProgressoGeral.setProgress(0);
            lblProgressoEtapas.setText("Etapa ? de ?");
            lblProgressoPorcentagem.setText("(0%)");
            mostrarMensagemNenhumaSecao();
        }
    }

    @FXML
    void abrirUltimaSecao(ActionEvent event) {
        if (ultimaSecao != null) {
            UIUtils.loadFxml("AlunoScenes/secao_Aluno.fxml");
        } else {
            UIUtils.showAlert("Informação", "Não há uma seção ativa para visualizar.");
        }
    }

    private void mostrarUltimaSecao() {
        if (ultimaSecaoContainer != null) ultimaSecaoContainer.setVisible(true);
        if (ultimaSecaoHBox != null) ultimaSecaoHBox.setVisible(true);
        if (lblNenhumaSecao != null) {
            lblNenhumaSecao.setVisible(false);
            lblNenhumaSecao.setManaged(false);
        }
        if (btnVerUltimaSecao != null) btnVerUltimaSecao.setDisable(false);
    }

    private void mostrarMensagemNenhumaSecao() {
        if (ultimaSecaoContainer != null) ultimaSecaoContainer.setVisible(true);
        if (ultimaSecaoHBox != null) {
            ultimaSecaoHBox.setVisible(false);
            ultimaSecaoHBox.setManaged(false);
        }
        if (lblNenhumaSecao != null) {
            lblNenhumaSecao.setVisible(true);
            lblNenhumaSecao.setManaged(true);
        }
        if (btnVerUltimaSecao != null) btnVerUltimaSecao.setDisable(true);
    }
}