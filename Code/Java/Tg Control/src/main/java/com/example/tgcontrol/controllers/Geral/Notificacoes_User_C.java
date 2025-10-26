package com.example.tgcontrol.controllers.Geral;

import com.example.tgcontrol.model.Notification;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class Notificacoes_User_C implements Initializable {

    @FXML
    private BorderPane rootPane;
    @FXML
    private TableView<Notification> tvNotificacoes;
    @FXML
    private TableColumn<Notification, String> colStatus;
    @FXML
    private TableColumn<Notification, String> colConteudo;
    @FXML
    private TableColumn<Notification, LocalDateTime> colData;
    @FXML
    private Label lblContadorNaoLidas;
    @FXML
    private Button btnMarcarTodas;

    private final ObservableList<Notification> listaNotificacoes = FXCollections.observableArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialização das colunas
        colStatus.setCellValueFactory(new PropertyValueFactory<>("isRead"));
        colConteudo.setCellValueFactory(new PropertyValueFactory<>("content"));
        colData.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        // Formatação da coluna de Data/Hora
        colData.setCellFactory(column -> new TableCell<Notification, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // Customização da coluna de Status (Ícone e Destaque)
        colStatus.setCellFactory(column -> new TableCell<Notification, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    Notification notification = getTableView().getItems().get(getIndex());
                    if (notification.isIsRead()) {
                        setText("Lida");
                        // Opcional: Ícone de lido (Material Design - simulado com texto)
                        // setGraphic(new FontIcon("mdi2c-check-circle-outline"));
                        setStyle("-fx-text-fill: gray;");
                    } else {
                        setText("Nova");
                        // Opcional: Ícone de não lido (Material Design - simulado com texto)
                        // setGraphic(new FontIcon("mdi2b-bell-badge"));
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #1976D2;"); // Cor de destaque
                    }
                }
            }
        });

        // Adiciona um listener para marcar como lida ao selecionar (opcional, mas útil)
        tvNotificacoes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && !newSelection.isIsRead()) {
                marcarComoLida(newSelection);
            }
        });

        tvNotificacoes.setItems(listaNotificacoes);
        carregarNotificacoes();

        // Animação leve ao abrir a tela (Fade-in)
        FadeTransition ft = new FadeTransition(Duration.millis(500), rootPane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    /**
     * Função: Busca no banco e exibe as notificações na tela.
     */
    public void carregarNotificacoes() {
        String emailUsuario = SessaoManager.getEmailUsuario();
        if (emailUsuario == null) {
            // Lidar com erro de sessão
            lblContadorNaoLidas.setText("Erro: Usuário não logado.");
            return;
        }

        List<Notification> notificacoes = DatabaseUtils.listarNotificacoes(emailUsuario);
        listaNotificacoes.clear();
        listaNotificacoes.addAll(notificacoes);

        atualizarContadorNaoLidas();
    }

    /**
     * Função: Atualiza o campo is_read para TRUE para uma notificação.
     * @param notification A notificação a ser marcada como lida.
     */
    private void marcarComoLida(Notification notification) {
        if (DatabaseUtils.marcarComoLida(notification.getNotificationId())) {
            notification.setIsRead(true);
            tvNotificacoes.refresh(); // Atualiza a visualização da linha
            atualizarContadorNaoLidas();
        } else {
            // Exibir erro
            System.err.println("Falha ao marcar notificação como lida: " + notification.getNotificationId());
        }
    }

    /**
     * Função: Atualiza todas as notificações como lidas.
     */
    @FXML
    public void marcarTodasComoLidas() {
        String emailUsuario = SessaoManager.getEmailUsuario();
        if (emailUsuario != null) {
            if (DatabaseUtils.marcarTodasComoLidas(emailUsuario)) {
                // Atualiza o estado na lista local sem recarregar tudo
                listaNotificacoes.forEach(n -> n.setIsRead(true));
                tvNotificacoes.refresh();
                atualizarContadorNaoLidas();
            } else {
                // Exibir erro
                System.err.println("Falha ao marcar todas como lidas.");
            }
        }
    }

    /**
     * Função: Conta e exibe o número de notificações não lidas.
     */
    private void atualizarContadorNaoLidas() {
        long naoLidas = listaNotificacoes.stream().filter(n -> !n.isIsRead()).count();
        lblContadorNaoLidas.setText(String.format("Você tem %d notificações não lidas.", naoLidas));
        btnMarcarTodas.setDisable(naoLidas == 0);
        // Opcional: Aqui seria o ponto de integração com o menu principal para exibir o contador.
    }
}
