package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.model.SecaoAluno;
import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import javafx.scene.paint.Color;

public class Card_Secao_Professor_C {

    @FXML private Label lbTituloSecao;
    @FXML private Label lbDataSecao;
    @FXML private Label lbNomeTarefa;
    @FXML private Label lbStatus;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void configurar(SecaoAluno secao) {
        if (secao == null) return;

        lbTituloSecao.setText(secao.getTitulo());
        lbNomeTarefa.setText(secao.getTitulo());

        String statusRevisao = secao.getStatusRevisao() != null ? secao.getStatusRevisao().toLowerCase() : "---";
        String statusGeral = secao.getStatus() != null ? secao.getStatus().toLowerCase() : "locked";

        String textoStatus;
        String styleStatus = "-fx-background-radius: 5px; -fx-padding: 3 8 3 8;";
        Color corTexto;

        if (statusGeral.equals("locked")) {
            textoStatus = "Bloqueado";
            styleStatus += "-fx-background-color: #9E9E9E;"; // Cinza
            corTexto = Color.WHITE;
        } else if (statusGeral.equals("completed") || statusRevisao.equals("approved")) {
            textoStatus = "Aceito";
            styleStatus += "-fx-background-color: #4CAF50;"; // Verde
            corTexto = Color.WHITE;
        } else if (statusRevisao.equals("pendente")) {
            textoStatus = "Pendente";
            styleStatus += "-fx-background-color: #FFC107;"; // Amarelo
            corTexto = Color.BLACK;
        } else {
            if (statusRevisao.equals("revision_requested")) {
                textoStatus = "Aberto (Revisão Sol.)";
            } else {
                textoStatus = "Aberto (Em And.)";
            }
            styleStatus += "-fx-background-color: #2196F3;"; // Azul
            corTexto = Color.WHITE;
        }

        lbStatus.setText(textoStatus);
        lbStatus.setStyle(styleStatus);
        lbStatus.setTextFill(corTexto);

        if (secao.getDataUltimaRevisao() != null) {
            lbDataSecao.setText("Última revisão: " + secao.getDataUltimaRevisao().format(DATETIME_FORMATTER));
        } else if (secao.getDataEntrega() != null) {
            lbDataSecao.setText("Data Limite: " + secao.getDataEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            lbDataSecao.setText("Sem data de referência");
        }
    }

    @FXML
    public void exibirTarefa() {
        String caminho = "ProfessorScenes/secao_Professor.fxml";
        UIUtils.loadFxml(caminho);
    }
}