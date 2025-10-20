package com.example.tgcontrol.controllers.Alunos;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import javafx.fxml.FXML;
import java.io.IOException;

public class Secoes_Aluno_C {
    @FXML private VBox vbSecoes;

    public void adicionarSecao(int quantidade) {
        AnchorPane secao = new AnchorPane();
        StackPane secao_conteudo_top = new StackPane();
        Label titulo_secao = new Label();
        Label data_secao = new Label();
        StackPane secao_conteudo_bottom = new StackPane();
        ///AnchorPane (corpo)
        //  (Width, height)
        secao.setPrefSize(400.0, 150.0);
        secao.setStyle("-fx-background-radius: 20px; -fx-background-color: f5f5f5;");
        ///StackPane (parte de cima)
        secao_conteudo_top.setPrefSize(400.0, 70.0);
        AnchorPane.setLeftAnchor(secao_conteudo_top, 0.0);
        AnchorPane.setRightAnchor(secao_conteudo_top, 0.0);
        AnchorPane.setTopAnchor(secao_conteudo_top, 0.0);
        ///Label (título)
        titulo_secao.setText("Seção 1");
        //  (fonte, tipo, tamanho)
        titulo_secao.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        StackPane.setAlignment(titulo_secao, Pos.TOP_LEFT);
        //  (top, right, bottom, left)
        StackPane.setMargin(titulo_secao, new Insets(20.0, 0.0, 0.0, 50.0));
        ///Label (Última atualização)
        data_secao.setText("Última atualização");
        data_secao.setTextFill(Paint.valueOf("#a1a1a1"));
        StackPane.setAlignment(data_secao, Pos.TOP_LEFT);
        StackPane.setMargin(data_secao, new Insets(40.0, 0.0, 0.0, 50.0));


        secao_conteudo_top.getChildren().add(titulo_secao);
        secao_conteudo_top.getChildren().add(data_secao);
        secao.getChildren().add(secao_conteudo_top);
        vbSecoes.getChildren().add(secao);
    }
    //Esse método adiciona um novo card com os parâmetros colocados
    public void adicionarCard(String a, String b) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgcontrol/AlunoScenes/card_secao_Aluno.fxml"));
        Parent card = loader.load();

        Card_Secao_Aluno_C controller = loader.getController();
        controller.configurar(a, b);

        vbSecoes.getChildren().add(card);
    }
    //Será trocado para quando o BD estiver pronto, coletando informações dele para criar os cards
    @FXML
    private void initialize() {
        try {
            adicionarCard("Secao 1","12/10");
            adicionarCard("Secao 2","12/10");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
