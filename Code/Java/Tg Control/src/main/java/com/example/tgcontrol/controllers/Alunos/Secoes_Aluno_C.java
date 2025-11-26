package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.model.SecaoAluno;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Secoes_Aluno_C {

    @FXML private VBox vbSecoes;
    private static final Logger LOGGER = Logger.getLogger(Secoes_Aluno_C.class.getName());

    @FXML
    public void initialize() {
        carregarSecoesDoAluno();
    }

    private void carregarSecoesDoAluno() {
        vbSecoes.getChildren().clear();

        String emailAluno = SessaoManager.getInstance().getEmailUsuario();
        if (emailAluno == null || emailAluno.isEmpty()) {
            LOGGER.log(Level.WARNING, "Não foi possível obter o email do aluno da sessão.");
            vbSecoes.getChildren().add(new Label("Não foi possível carregar as seções. Usuário não logado."));
            return;
        }

        List<SecaoAluno> listaSecoes = DatabaseUtils.getSecoesAluno(emailAluno);

        if (listaSecoes == null || listaSecoes.isEmpty()) {
            LOGGER.log(Level.INFO, "Nenhuma seção encontrada para o aluno: " + emailAluno);
            vbSecoes.getChildren().add(new Label("Nenhuma seção cadastrada para você ainda."));
            return;
        }

        for (SecaoAluno secao : listaSecoes) {
            try {
                adicionarCard(secao);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Erro ao carregar o FXML do card para a seção: " + secao.getTitulo(), e);
                vbSecoes.getChildren().add(new Label("Erro ao carregar a seção: " + secao.getTitulo()));
            }
        }
    }

    public void adicionarCard(SecaoAluno secao) throws IOException {
        String caminhoFxml = "/com/example/tgcontrol/Scenes/AlunoScenes/card_secao_Aluno.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));

        if (loader.getLocation() == null) {
            throw new IOException("FALHA AO ENCONTRAR O ARQUIVO DO CARD: " + caminhoFxml);
        }

        Parent cardNode = loader.load();
        Card_Secao_Aluno_C controller = loader.getController();

        controller.configurar(secao);

        vbSecoes.getChildren().add(cardNode);
    }
}