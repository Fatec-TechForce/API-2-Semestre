package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.model.SecaoAluno;
import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Card_Secao_Aluno_C {

    @FXML private Label lbTituloSecao;
    @FXML private Label lbDataSecao;
    @FXML private Label lbNomeTarefa;
    @FXML private Label lbStatus;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void configurar(SecaoAluno secao) {
        if (secao == null) return;

        lbTituloSecao.setText(secao.getTitulo());

        if (secao.getDataUltimaRevisao() != null) {
            lbDataSecao.setText("Última revisão: " + secao.getDataUltimaRevisao().format(DATETIME_FORMATTER));
        } else if (secao.getStatusRevisao() != null && secao.getStatusRevisao().equalsIgnoreCase("Pendente")) {
            lbDataSecao.setText("Aguardando primeira revisão");
        } else if (secao.getStatus() != null && secao.getStatus().equalsIgnoreCase("in_progress")) {
            lbDataSecao.setText("Seção em andamento");
        } else if (secao.getStatus() != null && secao.getStatus().equalsIgnoreCase("locked")){
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
        String caminho = "AlunoScenes/secao_Aluno.fxml";
        UIUtils.loadFxml(caminho);
    }
}