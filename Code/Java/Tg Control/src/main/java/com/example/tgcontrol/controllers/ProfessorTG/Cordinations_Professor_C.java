package com.example.tgcontrol.controllers.ProfessorTG;

import com.example.tgcontrol.controllers.Alunos.Card_Secao_Aluno_C; // Importa o controller do card
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class Cordinations_Professor_C {

    // --- @FXMLs da Esquerda (Lista de Alunos) ---
    @FXML private ListView<?> studentListView;

    // --- @FXMLs do Centro (Container das Seções) ---
    @FXML private VBox sectionsContainer;

    // --- @FXMLs da Direita (Perfil do Aluno) ---
    @FXML private ImageView profileImageView;
    @FXML private Label studentNameLabel;
    @FXML private Label courseLabel;


    /**
     * Carrega o FXML do card, pega seu controller e chama o 'configurar'.
     * (Sim, você pode reutilizar o card do aluno aqui, se o layout for o mesmo)
     */
    public void adicionarCard(String a, String b) throws IOException {

        // Caminho absoluto para o FXML do card
        String caminhoFxml = "/com/example/tgcontrol/Scenes/AlunoScenes/card_secao_Aluno.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));

        if (loader.getLocation() == null) {
            throw new IOException("FALHA AO ENCONTRAR O ARQUIVO DO CARD: " + caminhoFxml);
        }

        Parent card = loader.load();

        // Pega o controller do card
        Card_Secao_Aluno_C controller = loader.getController();

        // Configura o card
        controller.configurar(a, b);

        // Adiciona o card pronto ao VBox 'sectionsContainer'
        sectionsContainer.getChildren().add(card);
    }

    /**
     * Chamado quando o FXML é carregado.
     * Popula a tela com dados iniciais.
     */
    @FXML
    private void initialize() {
        try {
            // TODO: Aqui você deve carregar os dados do aluno selecionado

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