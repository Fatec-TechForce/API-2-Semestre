package com.example.tgcontrol.controllers.Professor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Cordinations_Professor_C {

    @FXML private VBox sectionsContainer;

    private static final Logger LOGGER = Logger.getLogger(Cordinations_Professor_C.class.getName());

    public void adicionarCard(String titulo, String dataInfo) throws IOException {
        String caminhoFxml = "/com/example/tgcontrol/Scenes/ProfessorScenes/card_secao_Professor.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));

        if (loader.getLocation() == null) {
            throw new IOException("FALHA AO ENCONTRAR O ARQUIVO DO CARD: " + caminhoFxml);
        }

        Parent card = loader.load();
        Card_Secao_Professor_C controller = loader.getController();

        controller.configurar(titulo, dataInfo);

        sectionsContainer.getChildren().add(card);
    }

    @FXML
    private void initialize() {
        String emailAlunoExemplo = "niuan.souza@fatec.sp.gov.br";

        try {
            adicionarCard("Seção 1 (Prof)", "Última atualização: 12/10");
            adicionarCard("Seção 2 (Prof)", "Última atualização: 12/10");
            adicionarCard("Seção 3 (Prof)", "Última atualização: 13/10");
            adicionarCard("Seção 4 (Prof)", "Última atualização: 14/10");
            adicionarCard("Seção 5 (Prof)", "Última atualização: 15/10");
            adicionarCard("Seção 6 (Prof)", "Última atualização: 16/10");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar cards na tela de coordenação do professor.", e);
        }
    }
}