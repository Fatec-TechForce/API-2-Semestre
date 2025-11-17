package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.model.SecaoAluno;
import com.example.tgcontrol.model.Turma;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cordinations_Professor_C {

    @FXML private ListView<String> studentListView;
    @FXML private ImageView profileImageView;
    @FXML private Label studentNameLabel;
    @FXML private Label courseLabel;
    @FXML private VBox sectionsContainer;

    private static final Logger LOGGER = Logger.getLogger(Cordinations_Professor_C.class.getName());

    private List<Map<String, String>> allStudentsData = new ArrayList<>();
    private static final String DEFAULT_PROFILE_IMAGE_PATH = "/com/example/tgcontrol/SceneImages/Task Images/fotoPerfil2Symbol.png";


    @FXML
    private void initialize() {
        carregarListaDeAlunos();

        studentListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        Map<String, String> selectedStudentData = findStudentData(newSelection);

                        if (selectedStudentData != null) {
                            atualizarPainelDireito(selectedStudentData);
                            carregarSecoesDoAluno(selectedStudentData.get("email"));
                        }
                    }
                }
        );

        if (!allStudentsData.isEmpty()) {
            studentListView.getSelectionModel().selectFirst();
        } else {
            studentNameLabel.setText("[Nenhum Aluno]");
            courseLabel.setText("");
            loadDefaultProfileImage();
        }
    }

    private void carregarListaDeAlunos() {
        String emailProfessor = SessaoManager.getInstance().getEmailUsuario();
        Turma turmaSelecionada = SessaoManager.getInstance().getTurmaSelecionada();

        SessaoManager.getInstance().setTurmaSelecionada(null);

        if (emailProfessor == null || emailProfessor.isEmpty()) {
            return;
        }

        if (turmaSelecionada != null) {
            allStudentsData = DatabaseUtils.getStudentsByClass(
                    turmaSelecionada.getDisciplina(),
                    turmaSelecionada.getAno(),
                    turmaSelecionada.getSemestre()
            );
        } else {
            allStudentsData = DatabaseUtils.getAdviseesDisplayInfo(emailProfessor);
        }

        studentListView.setItems(FXCollections.observableArrayList(
                allStudentsData.stream().map(d -> d.get("nomeCompleto")).toList()
        ));

        if (allStudentsData.isEmpty()) {
            studentListView.setPlaceholder(new Label("Nenhum aluno encontrado neste filtro."));
        }
    }

    private Map<String, String> findStudentData(String nomeAluno) {
        return allStudentsData.stream()
                .filter(data -> nomeAluno.equals(data.get("nomeCompleto")))
                .findFirst()
                .orElse(null);
    }

    private void atualizarPainelDireito(Map<String, String> studentData) {
        String nomeCompleto = studentData.get("nomeCompleto");
        String turmaDescricao = studentData.get("turma_descricao");
        String imageUrl = studentData.get("profile_picture_url");

        studentNameLabel.setText(nomeCompleto);
        courseLabel.setText(turmaDescricao);

        if (profileImageView != null && imageUrl != null && !imageUrl.isBlank()) {
            try {
                File imageFile = new File(imageUrl);
                if (imageFile.exists() && imageFile.isFile()) {
                    Image image = new Image(imageFile.toURI().toString());
                    profileImageView.setImage(image);
                } else {
                    loadDefaultProfileImage();
                }
            } catch (Exception e) {
                loadDefaultProfileImage();
                LOGGER.log(Level.SEVERE, "Erro ao carregar foto de perfil de aluno: " + imageUrl, e);
            }
        } else {
            loadDefaultProfileImage();
        }
    }

    private void loadDefaultProfileImage() {
        if (profileImageView != null) {
            try {
                Image placeholder = new Image(getClass().getResourceAsStream(DEFAULT_PROFILE_IMAGE_PATH));
                profileImageView.setImage(placeholder);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Não foi possível carregar o placeholder da imagem de perfil.");
            }
        }
    }

    private void carregarSecoesDoAluno(String emailAluno) {
        sectionsContainer.getChildren().clear();

        List<SecaoAluno> listaSecoes = DatabaseUtils.getSecoesAluno(emailAluno);

        if (listaSecoes == null || listaSecoes.isEmpty()) {
            sectionsContainer.getChildren().add(new Label("Nenhuma seção cadastrada para este aluno."));
            return;
        }

        for (SecaoAluno secao : listaSecoes) {
            try {
                adicionarCard(secao);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Erro ao carregar o FXML do card para a seção: " + secao.getTitulo(), e);
            }
        }
    }

    public void adicionarCard(SecaoAluno secao) throws IOException {
        String caminhoFxml = "/com/example/tgcontrol/Scenes/ProfessorScenes/card_secao_Professor.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));

        if (loader.getLocation() == null) {
            throw new IOException("FALHA AO ENCONTRAR O ARQUIVO DO CARD: " + caminhoFxml);
        }

        Parent card = loader.load();
        Card_Secao_Professor_C controller = loader.getController();

        controller.configurar(secao);

        sectionsContainer.getChildren().add(card);
    }
}