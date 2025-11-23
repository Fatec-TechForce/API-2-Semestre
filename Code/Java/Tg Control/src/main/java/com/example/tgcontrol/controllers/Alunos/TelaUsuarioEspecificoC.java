package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.utils.DatabaseConnect;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TelaUsuarioEspecificoC {

        // Email que será recebido da tela anterior
        private String emailUsuario;

        @FXML
        private Button profileMenuButton;

        @FXML
        private ImageView profileImageView;

        @FXML
        private Button notifications;

        @FXML
        private StackPane contentArea;

        @FXML
        private GridPane gridPane;

        // Labels coluna esquerda
        @FXML
        private Label labelNome;
        @FXML
        private Label labelEmail;
        @FXML
        private Label labelCurso;
        @FXML
        private Label labelTipoUsuario;
        @FXML
        private Label labelDataCadastro;

        // Labels valores
        @FXML
        private Label labelValor1;
        @FXML
        private Label labelValor2;
        @FXML
        private Label labelValor3;
        @FXML
        private Label labelValor4;
        @FXML
        private Label labelValor5;


        // ======================================================
        // Método chamado pela tela anterior para enviar o email
        // ======================================================
        public void setEmailUsuario(String email) {
            this.emailUsuario = email;
            carregarDadosUsuario();
        }


        // ======================================================
        // Inicialização visual
        // ======================================================
        @FXML
        private void initialize() {

            try {
                Circle clip = new Circle(12.5, 12.5, 12.5);
                profileImageView.setClip(clip);

                Image img = new Image(
                        getClass().getResource("/SceneImages/Navbar Images/UserSymbol.png").toExternalForm()
                );

                profileImageView.setImage(img);

            } catch (Exception e) {
                System.out.println("⚠ Imagem de perfil não encontrada. Ignorando...");
            }
        }


        // ======================================================
        // Carregar dados do usuário do banco
        // ======================================================
        private void carregarDadosUsuario() {

            if (emailUsuario == null) {
                System.out.println("⚠ Nenhum email recebido da tela anterior!");
                return;
            }

            try (Connection conn = DatabaseConnect.getConnection()) {

                // ==========================
                // BUSCA NA TABELA USER
                // ==========================
                String sqlUser = "SELECT FirstName, LastName, email, registration_timestamp FROM user WHERE email = ?";
                PreparedStatement stmt = conn.prepareStatement(sqlUser);
                stmt.setString(1, emailUsuario);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {

                    String nomeCompleto = rs.getString("FirstName") + " " + rs.getString("LastName");
                    String email = rs.getString("email");
                    String timestamp = rs.getString("registration_timestamp");

                    DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    LocalDateTime ldt = LocalDateTime.parse(timestamp, inputFormat);
                    String dataCadastro = ldt.format(outputFormat);

                    // Preenche tela
                    labelValor1.setText(nomeCompleto);
                    labelValor2.setText(email);
                    labelValor5.setText(dataCadastro);
                }


                // ======================================================
                // VERIFICA SE É PROFESSOR OU ALUNO
                // ======================================================

                // --- VERIFICAR SE ESTÁ NA TABELA TEACHER ---
                String sqlTeacher = "SELECT is_coordinator FROM teacher WHERE email = ?";
                PreparedStatement stmtTeacher = conn.prepareStatement(sqlTeacher);
                stmtTeacher.setString(1, emailUsuario);
                ResultSet rsTeacher = stmtTeacher.executeQuery();

                if (rsTeacher.next()) {

                    // ---------- USUÁRIO É PROFESSOR ----------
                    labelValor4.setText("PROFESSOR");

                    boolean isCoordinator = rsTeacher.getBoolean("is_coordinator");

                    if (isCoordinator) {
                        labelValor3.setText("Coordenador");
                    } else {
                        labelValor3.setText("Professor");
                    }

                    return;
                }


                // --- VERIFICAR SE ESTÁ NA TABELA STUDENT ---
                String sqlStudent = "SELECT class_disciplina FROM student WHERE email = ?";
                PreparedStatement stmtStudent = conn.prepareStatement(sqlStudent);
                stmtStudent.setString(1, emailUsuario);
                ResultSet rsStudent = stmtStudent.executeQuery();

                if (rsStudent.next()) {

                    // ---------- USUÁRIO É ALUNO ----------
                    labelValor4.setText("ALUNO");

                    String disciplina = rsStudent.getString("class_disciplina");
                    labelValor3.setText(disciplina);

                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // ======================================================
        // Ações da navbar
        // ======================================================
        @FXML
        private void home() {
            System.out.println("Botão Início clicado!");
        }

        @FXML
        private void andamentoTG() {
            System.out.println("Botão Alunos/Mentorados clicado!");
        }

        @FXML
        private void showProfileMenu() {
            System.out.println("Abrir menu do perfil");
        }

        @FXML
        private void notifications() {
            System.out.println("Notificações abertas");
        }


        // ======================================================
        // Hover dos botões da navbar
        // ======================================================
        @FXML
        private void escurecerBotaoNavbar(MouseEvent event) {
            Button btn = (Button) event.getSource();
            btn.setStyle("-fx-background-color: #D3D3D3; -fx-border-radius: 25px;");
        }

        @FXML
        private void esbranquecerBotaoNavBar(MouseEvent event) {
            Button btn = (Button) event.getSource();
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 25px;");
        }
    }

