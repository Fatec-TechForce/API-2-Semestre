package com.example.tgcontrol.controllers;

import com.example.tgcontrol.model.TipoUsuario;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem; // Import SeparatorMenuItem
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class BaseNavbarController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(BaseNavbarController.class.getName());

    @FXML
    protected ImageView profileImageView;

    @FXML
    protected StackPane contentArea;

    @FXML
    protected Button profileMenuButton;

    private static final String DEFAULT_PROFILE_IMAGE_RESOURCE_PATH = "/com/example/tgcontrol/SceneImages/Navbar Images/UserSymbol.png";


    protected abstract String getInitialFxmlPath();


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
        applyCircularClipToProfileImage();
    }


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

    private void applyCircularClipToProfileImage() {
        if (profileImageView != null) {
            Circle clip = new Circle(12.5, 12.5, 12.5); // centerX, centerY, radius
            profileImageView.setClip(clip);
        }
    }


    @FXML
    private void showProfileMenu(ActionEvent event) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem historicoItem = null; // Declare outside the if

        MenuItem perfilItem = new MenuItem("Perfil");
        perfilItem.setOnAction(e -> {
            UIUtils.loadFxml("GeralScenes/profile_User.fxml");
        });

        if (SessaoManager.getInstance().getTipoUsuario() == TipoUsuario.ALUNO) {
            historicoItem = new MenuItem("Histórico");
            historicoItem.setOnAction(e -> {
                UIUtils.loadFxml("GeralScenes/notifications_User.fxml");
            });
        }

        MenuItem sairItem = new MenuItem("Sair");
        sairItem.setOnAction(e -> {
            sair(event);
        });

        contextMenu.getItems().add(perfilItem);
        if (historicoItem != null) {
            contextMenu.getItems().add(historicoItem);
        }
        contextMenu.getItems().addAll(new SeparatorMenuItem(), sairItem);


        contextMenu.show(profileMenuButton, Side.BOTTOM, 0, 5);
    }


    @FXML
    public void sair(ActionEvent actionEvent) {
        SessaoManager.getInstance().encerrarSessao();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        UIUtils.loadNewScene(stage, "GeralScenes/login_User.fxml");
    }


    @FXML
    public void notifications(ActionEvent actionEvent) {

        UIUtils.loadFxml("GeralScenes/notifications_User.fxml");
    }
}