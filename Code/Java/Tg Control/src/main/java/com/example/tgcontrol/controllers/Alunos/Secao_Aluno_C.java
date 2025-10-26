package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Secao_Aluno_C {
    @FXML Button btn_Arquivo;
    @FXML Label lblMensagemSucesso;

    private String formatarTamanhoArquivo(long tamanho) {
        if(tamanho <= 0) return null;
        final String[] unidades = new String[] {"bytes", "KB", "MB", "GB"};
        int grupos_digitos = (int) (Math.log10(tamanho) / Math.log10(1024));
        //Máximo de 999KB
        if(grupos_digitos <= 1) {
            return String.format("%.2f %s", tamanho / Math.pow(1024, grupos_digitos), unidades[grupos_digitos]);
        } else {return null;}
    }

    @FXML
    public void adicionarArquivo() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Adicionar Arquivo");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Texto", "*.txt", "*.pdf", "*.docx")
        );

        Stage stage = (Stage) btn_Arquivo.getScene().getWindow();
        File arquivo_selecionado = fc.showOpenDialog(stage);

        if(arquivo_selecionado != null) {
            try {
                Path caminho_Arquivo = arquivo_selecionado.toPath();
                long tamanho_bytes = Files.size(caminho_Arquivo);
                String tamanho_formatado = formatarTamanhoArquivo(tamanho_bytes);
                btn_Arquivo.setText(arquivo_selecionado.getName());
                lblMensagemSucesso.setText("Arquivo adicionado com sucesso! (" + tamanho_formatado + ")");
                lblMensagemSucesso.setVisible(true);

                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                Runnable atraso = () -> lblMensagemSucesso.setVisible(false);
                executor.schedule(atraso, 4, TimeUnit.SECONDS);
                executor.shutdown();

            } catch (IOException e) {
                e.printStackTrace();
                btn_Arquivo.setText(arquivo_selecionado.getAbsolutePath());
            }
        } else {
            btn_Arquivo.setText("Arquivo Selecionado");
        }
    }
    @FXML
    public void visualizarHist(ActionEvent event){
        String fxmlParaCarregar = "GeralScenes/historicoVers_User.fxml";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        UIUtils.loadFxml(fxmlParaCarregar);
    }
}
