package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.model.SecaoAluno;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Card_Secao_Professor_C {

    @FXML private Label lbTituloSecao;
    @FXML private Label lbDataSecao;
    @FXML private Label lbNomeTarefa;
    @FXML private Label lbStatus;
    @FXML private Button btnExibirTarefa;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // --- VARIÁVEIS GLOBAIS (Essenciais para o botão funcionar) ---
    private SecaoAluno secaoAtual;
    private String nomeAluno;

    public void configurar(SecaoAluno secao, String nomeAluno) {
        if (secao == null) return;

        this.secaoAtual = secao; // Guarda a seção para usar depois
        this.nomeAluno = nomeAluno; // Guarda o nome para usar depois

        lbTituloSecao.setText(secao.getTitulo());
        lbNomeTarefa.setText(secao.getTitulo());

        // Configuração Visual do Status
        String statusRevisao = secao.getStatusRevisao() != null ? secao.getStatusRevisao().toLowerCase() : "---";
        String statusGeral = secao.getStatus() != null ? secao.getStatus().toLowerCase() : "locked";

        String textoStatus;
        String styleStatus = "-fx-background-radius: 5px; -fx-padding: 3 8 3 8;";
        Color corTexto;

        if (statusGeral.equals("locked")) {
            textoStatus = "Bloqueado";
            styleStatus += "-fx-background-color: #9E9E9E;";
            corTexto = Color.WHITE;
        } else if (statusGeral.equals("completed") || statusRevisao.equals("approved")) {
            textoStatus = "Aceito";
            styleStatus += "-fx-background-color: #4CAF50;";
            corTexto = Color.WHITE;
        } else if (statusRevisao.equals("pendente")) {
            textoStatus = "Pendente";
            styleStatus += "-fx-background-color: #FFC107;";
            corTexto = Color.BLACK;
        } else {
            if (statusRevisao.equals("revision_requested")) {
                textoStatus = "Aberto (Revisão Sol.)";
            } else {
                textoStatus = "Aberto (Em And.)";
            }
            styleStatus += "-fx-background-color: #2196F3;";
            corTexto = Color.WHITE;
        }

        lbStatus.setText(textoStatus);
        lbStatus.setStyle(styleStatus);
        lbStatus.setTextFill(corTexto);

        // Configuração Visual da Data
        if (secao.getDataUltimaRevisao() != null) {
            lbDataSecao.setText("Última revisão: " + secao.getDataUltimaRevisao().format(DATETIME_FORMATTER));
        } else if (secao.getDataEntrega() != null) {
            lbDataSecao.setText("Data Limite: " + secao.getDataEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            lbDataSecao.setText("Sem data de referência");
        }
    }

    @FXML
    public void exibirTarefa(ActionEvent event) {
        if (secaoAtual == null) return;

        // 1. CORREÇÃO AQUI: Usando .getTaskSequence() em vez de .getSequenceOrder()
        LocalDateTime dataUltimoEnvio = DatabaseUtils.getUltimaDataSubmissao(secaoAtual.getEmailAluno(), secaoAtual.getTaskSequence());

        if (dataUltimoEnvio == null) {
            UIUtils.showAlert("Aviso", "O aluno ainda não realizou nenhuma entrega para esta seção.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgcontrol/Scenes/ProfessorScenes/secao_Professor.fxml"));
            Parent root = loader.load();

            Secao_Professor_C controller = loader.getController();

            // 2. CORREÇÃO AQUI TAMBÉM: Usando .getTaskSequence()
            controller.setDadosSubmissao(
                    secaoAtual.getEmailAluno(),
                    this.nomeAluno,
                    secaoAtual.getTaskSequence(),
                    dataUltimoEnvio
            );

            // Navegação
            StackPane contentArea = (StackPane) ((javafx.scene.Node) event.getSource()).getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);
            } else {
                System.err.println("Erro Crítico: #contentArea não encontrado.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            UIUtils.showAlert("Erro", "Não foi possível abrir a tela de correção.");
        }
    }
}