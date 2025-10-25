package com.example.tgcontrol.controllers.Alunos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class Secoes_Aluno_C {
    @FXML private VBox vbSecoes;

    /**
     * Carrega o FXML do card, pega seu controller e chama o 'configurar'.
     */
    public void adicionarCard(String a, String b) throws IOException {

        // Caminho absoluto para o FXML do card (baseado nos seus logs de erro)
        String caminhoFxml = "/com/example/tgcontrol/Scenes/AlunoScenes/card_secao_Aluno.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));

        if (loader.getLocation() == null) {
            throw new IOException("FALHA AO ENCONTRAR O ARQUIVO DO CARD: " + caminhoFxml);
        }

        Parent card = loader.load();
        Card_Secao_Aluno_C controller = loader.getController();

        // Chama a configuração simples (sem ID)
        controller.configurar(a, b);

        vbSecoes.getChildren().add(card);
    }

    @FXML
    private void initialize() {
        try {
            // Adicionando 6 cards conforme solicitado
            adicionarCard("Seção 1","Última atualização: 12/10");
            adicionarCard("Seção 2","Última atualização: 12/10");
            adicionarCard("Seção 3","Última atualização: 13/10");
            adicionarCard("Seção 4","Última atualização: 14/10");
            adicionarCard("Seção 5","Última atualização: 15/10");
            adicionarCard("Seção 6","Última atualização: 16/10");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}