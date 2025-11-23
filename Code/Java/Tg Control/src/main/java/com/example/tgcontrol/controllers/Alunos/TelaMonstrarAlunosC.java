package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.utils.DatabaseConnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TelaMonstrarAlunosC {

    public class TelaMostrarAlunosC {

        @FXML
        private VBox VBoxListaAlunos;

        @FXML
        private TextField TextFieldNomePesquisa;

        @FXML
        public void initialize() {
            List<Aluno> alunos = carregarAlunosDoBanco();
            carregarAlunos(alunos);
        }

        private List<Aluno> carregarAlunosDoBanco() {
            List<Aluno> alunos = new ArrayList<>();
            String sql = "SELECT FirstName, LastName, registration_timestamp, status, email FROM user";

            try (Connection conn = DatabaseConnect.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                while (rs.next()) {

                    String nome = rs.getString("FirstName") + " " + rs.getString("LastName");

                    String timestamp = rs.getString("registration_timestamp");
                    LocalDateTime ldt = LocalDateTime.parse(timestamp, inputFormat);
                    String data = ldt.format(outputFormat);

                    String tipo = rs.getString("status");
                    String email = rs.getString("email");

                    alunos.add(new Aluno(nome, data, tipo, email));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return alunos;
        }

        private void carregarAlunos(List<Aluno> alunos) {

            VBoxListaAlunos.getChildren().removeIf(
                    node -> node instanceof HBox && VBoxListaAlunos.getChildren().indexOf(node) != 0
            );

            for (Aluno aluno : alunos) {

                HBox alunoBox = new HBox();
                alunoBox.setAlignment(Pos.CENTER_LEFT);
                alunoBox.setPrefHeight(60);
                alunoBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: transparent transparent #999999 transparent;");
                alunoBox.setPadding(new Insets(0, 0, 0, 20));
                alunoBox.setSpacing(10);

                Label nome = new Label(aluno.getNome());
                nome.setPrefWidth(438);
                nome.setPrefHeight(60);
                nome.setFont(Font.font(24));

                Label data = new Label(aluno.getData());
                data.setPrefWidth(153);
                data.setPrefHeight(60);
                data.setFont(Font.font(24));

                Label tipo = new Label(aluno.getTipo());
                tipo.setPrefWidth(472);
                tipo.setPrefHeight(60);
                tipo.setFont(Font.font(24));
                tipo.setAlignment(Pos.CENTER);

                Button btnAnalisar = new Button("Analisar");
                btnAnalisar.setPrefWidth(187);
                btnAnalisar.setPrefHeight(40);
                btnAnalisar.setStyle(
                        "-fx-background-color: #FFFFFF; -fx-border-color: #E0D6D6; " +
                                "-fx-background-radius: 8px; -fx-border-radius: 8px;"
                );

                btnAnalisar.setOnAction(e -> analisarAluno(aluno));

                alunoBox.getChildren().addAll(nome, data, tipo, btnAnalisar);
                VBoxListaAlunos.getChildren().add(alunoBox);
            }
        }

        private void analisarAluno(Aluno aluno) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/exemple/tgcontrol/TelaUsuarioEspecifico.fxml")
                );

                Parent root = loader.load();

                TelaUsuarioEspecificoC controller = loader.getController();
                controller.setEmailUsuario(aluno.getEmail());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static class Aluno {
            private final String nome;
            private final String data;
            private final String tipo;
            private final String email;

            public Aluno(String nome, String data, String tipo, String email) {
                this.nome = nome;
                this.data = data;
                this.tipo = tipo;
                this.email = email;
            }

            public String getNome() {
                return nome;
            }

            public String getData() {
                return data;
            }

            public String getTipo() {
                return tipo;
            }

            public String getEmail() {
                return email;
            }
        }

    }

}


