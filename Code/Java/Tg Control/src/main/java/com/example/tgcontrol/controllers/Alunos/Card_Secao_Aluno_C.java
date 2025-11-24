package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.model.SecaoAluno;
import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Card_Secao_Aluno_C {

    @FXML private Label lbTituloSecao;
    @FXML private Label lbDataSecao;
    @FXML private Label lbNomeTarefa;
    @FXML private Label lbStatus;
    @FXML private Button btnExibirTarefa;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Logger LOGGER = Logger.getLogger(Card_Secao_Aluno_C.class.getName());

    private SecaoAluno secao;

    public void configurar(SecaoAluno secao) {
        if (secao == null) {
            LOGGER.log(Level.SEVERE, "Erro de Configuração: Objeto SecaoAluno nulo ao configurar o card.");
            return;
        }
        this.secao = secao;

        lbTituloSecao.setText(secao.getTitulo());

        if (secao.getDataUltimaRevisao() != null) {
            lbDataSecao.setText("Última revisão: " + secao.getDataUltimaRevisao().format(DATETIME_FORMATTER));
        } else if (secao.getStatusRevisao() != null && secao.getStatusRevisao().equalsIgnoreCase("Pendente")) {
            lbDataSecao.setText("Aguardando primeira revisão");
        } else if (secao.getStatus() != null && secao.getStatus().equalsIgnoreCase("in_progress")) {
            lbDataSecao.setText("Seção em andamento");
        } else if (secao.getStatus() != null && secao.getStatus().equalsIgnoreCase("locked")){
            btnExibirTarefa.setVisible(false);
            btnExibirTarefa.setDisable(true);
            lbDataSecao.setText("Seção bloqueada");
        } else {
            lbDataSecao.setText("Sem atualizações recentes");
        }

        lbNomeTarefa.setText(secao.getTitulo());

        String statusRevisao = secao.getStatusRevisao() != null ? secao.getStatusRevisao().toLowerCase() : "---";
        String statusGeral = secao.getStatus() != null ? secao.getStatus().toLowerCase() : "locked";
        String textoStatus = "Status Desconhecido";
        String styleStatus = "-fx-background-radius: 5px; -fx-padding: 3 8 3 8;";

        switch (statusRevisao) {
            case "approved":
            case "aprovado":
                textoStatus = "Aprovado";
                styleStatus += "-fx-background-color: #4CAF50;";
                break;
            case "revision_requested":
                textoStatus = "Revisão Solicitada";
                styleStatus += "-fx-background-color: #FF9800;";
                break;
            case "pendente":
                textoStatus = "Pendente";
                styleStatus += "-fx-background-color: #FFC107;";
                break;
            case "---":
            default:
                if (statusGeral.equals("in_progress")) {
                    textoStatus = "Em Andamento";
                    styleStatus += "-fx-background-color: #2196F3;";
                } else if (statusGeral.equals("locked")) {
                    textoStatus = "Bloqueado";
                    styleStatus += "-fx-background-color: #9E9E9E;";
                } else if (statusGeral.equals("completed") && statusRevisao.equals("---")) {
                    textoStatus = "Concluído";
                    styleStatus += "-fx-background-color: #4CAF50;";
                } else {
                    textoStatus = "Não Enviado";
                    styleStatus += "-fx-background-color: #9E9E9E;";
                }
                break;
        }

        lbStatus.setText(textoStatus);
        lbStatus.setStyle(styleStatus);
        lbStatus.setTextFill(javafx.scene.paint.Color.WHITE);
    }

    @FXML
    public void exibirTarefa() {
        if (secao == null) {
            LOGGER.log(Level.SEVERE, "Falha na navegação: Objeto SecaoAluno é nulo ao clicar no card.");
            UIUtils.showAlert("Aviso", "Não foi possível carregar os dados desta seção.");
            return;
        }

        try {
            String caminho = "/com/example/tgcontrol/Scenes/AlunoScenes/secao_Aluno.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Parent root = loader.load();

            Secao_Aluno_C controller = loader.getController();
            controller.setDadosSecao(this.secao);

            LOGGER.log(Level.INFO, "Navegando para Secao_Aluno_C com Email: " + this.secao.getEmailAluno() + ", Seção: " + this.secao.getTaskSequence());

            StackPane contentArea = (StackPane) lbTituloSecao.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);
            } else {
                LOGGER.log(Level.SEVERE, "Erro de Navegação: #contentArea não encontrado na Scene.");
                UIUtils.showAlert("Erro", "Não foi possível encontrar a área de conteúdo (contentArea).");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar FXML da seção do aluno.", e);
            UIUtils.showAlert("Erro", "Não foi possível abrir a tela da seção.");
        }
    }
}