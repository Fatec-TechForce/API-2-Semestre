package com.example.tgcontrol.controllers;

import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils; // UIUtils is used for FXML loading
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller: Classe base abstrata para as barras de navegação (Navbars).
 * Gerencia a área de conteúdo principal e funções comuns como sair e notificações.
 * Carrega a imagem de perfil do usuário ao inicializar.
 */
public abstract class BaseNavbarController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(BaseNavbarController.class.getName());

    @FXML
    protected ImageView profileImageView;

    @FXML
    protected StackPane contentArea;

    private static final String DEFAULT_PROFILE_IMAGE_RESOURCE_PATH = "/com/example/tgcontrol/SceneImages/Navbar Images/UserSymbol.png";

    /**
     * Função: Método abstrato que define o FXML inicial a ser carregado.
     * Retorna: String com o caminho relativo do FXML inicial.
     */
    protected abstract String getInitialFxmlPath();

    /**
     * Função: Inicializa o controller da Navbar. Carrega a view inicial e a foto de perfil.
     * Necessita: location e resources injetados pelo JavaFX.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (contentArea == null) {
            LOGGER.log(Level.SEVERE,"GRAVE: StackPane com fx:id='contentArea' não foi injetado. Verifique o FXML da Navbar.");
        }

        String initialPath = getInitialFxmlPath();
        if (initialPath != null && !initialPath.isEmpty() && contentArea != null) {
            UIUtils.loadFxmlInPane(contentArea, initialPath);
        }

        loadProfileImage();
    }

    /**
     * Função: Carrega a imagem de perfil do usuário logado ou uma imagem padrão.
     */
    private void loadProfileImage() {
        if (profileImageView == null) {
            LOGGER.log(Level.WARNING,"ImageView com fx:id='profileImageView' não foi injetado.");
            return;
        }

        String userEmail = SessaoManager.getInstance().getEmailUsuario();
        if (userEmail == null || userEmail.isEmpty()) {
            loadDefaultProfileImage();
            return;
        }

        String imagePathUrl = DatabaseUtils.getProfilePictureUrl(userEmail);
        Image profileImage = null;

        if (imagePathUrl != null && !imagePathUrl.isBlank()) {
            try {
                File imageFile = new File(imagePathUrl);
                if (imageFile.exists() && imageFile.isFile()) {
                    profileImage = new Image(imageFile.toURI().toString());
                } else {
                    LOGGER.log(Level.WARNING, "Arquivo da foto de perfil não encontrado: " + imagePathUrl);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao carregar foto de perfil do caminho: " + imagePathUrl, e);
            }
        }

        if (profileImage == null) {
            loadDefaultProfileImage();
        } else {
            profileImageView.setImage(profileImage);
        }
    }

    /**
     * Função: Carrega a imagem de perfil padrão a partir dos resources.
     */
    private void loadDefaultProfileImage() {
        try {
            URL defaultImageURL = getClass().getResource(DEFAULT_PROFILE_IMAGE_RESOURCE_PATH);
            if (defaultImageURL != null) {
                profileImageView.setImage(new Image(defaultImageURL.toExternalForm()));
            } else {
                LOGGER.log(Level.SEVERE,"Imagem de perfil padrão NÃO encontrada: " + DEFAULT_PROFILE_IMAGE_RESOURCE_PATH);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Erro ao carregar imagem de perfil padrão.", e);
        }
    }

    /**
     * Função: Realiza o logout do usuário e redireciona para a tela de login.
     * Necessita: ActionEvent do botão.
     */
    @FXML
    public void sair(ActionEvent actionEvent) {
        SessaoManager.getInstance().encerrarSessao();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        UIUtils.loadNewScene(stage, "GeralScenes/login_User.fxml");
    }

    /**
     * Função: Navega para a tela de notificações dentro do contentArea.
     * Necessita: ActionEvent do botão.
     */
    @FXML
    public void notifications(ActionEvent actionEvent) {
            UIUtils.loadFxml("GeralScenes/notifications_User.fxml");
    }
}