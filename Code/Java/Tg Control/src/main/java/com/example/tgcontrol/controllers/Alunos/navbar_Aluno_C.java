package com.example.tgcontrol.controllers.Alunos;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class navbar_Aluno_C implements Initializable {

    @FXML
    private StackPane contentArea;

    String fxmlPath = "/com/example/tgcontrol/AlunoScenes/home_Aluno.fxml";


    @Override
    public void initialize(URL location, ResourceBundle resources){

        try {
            URL fxmlLocation = getClass().getResource(fxmlPath);

            if (fxmlLocation == null) {
                throw new IOException("Não foi possível encontrar o arquivo FXML: home_Aluno.fxml");
            }

            Parent fxml = FXMLLoader.load(fxmlLocation);

            contentArea.getChildren().setAll(fxml);

        } catch (IOException ex) {
            Logger.getLogger(navbar_Aluno_C.class.getName()).log(Level.SEVERE, "Falha ao carregar o FXML.", ex);
        }

    }

    private void carregarPagina(String nomeArquivo) {
        String caminhoCompleto = "/com/example/tgcontrol/" + nomeArquivo;

        try {
            URL fxmlLocation = getClass().getResource(caminhoCompleto);

            if (fxmlLocation == null) {
                throw new IOException("Não foi possível encontrar o arquivo FXML: " + caminhoCompleto);
            }

            Parent fxml = FXMLLoader.load(fxmlLocation);

            contentArea.getChildren().setAll(fxml);

        } catch (IOException ex) {
            System.err.println("Erro ao carregar a página: " + nomeArquivo);
            ex.printStackTrace();
        }
    }

    @FXML
    public void home(javafx.event.ActionEvent actionEvent) {
        carregarPagina("AlunoScenes/home_Aluno.fxml");
    }

    @FXML
    public void andamentoTG(javafx.event.ActionEvent actionEvent) {
        carregarPagina("AlunoScenes/andamentoTG_Aluno.fxml");
    }

    @FXML
    public void notifications(javafx.event.ActionEvent actionEvent) {
        carregarPagina("GeralScenes/notifications_User.fxml");
    }
}
