package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils;
import com.example.tgcontrol.model.HistoricoVersao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.html.HtmlRenderer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Importante

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class Secao_Professor_C {

    @FXML private TextArea SecaoEnviada;
    @FXML private TextArea TxtFeedback;
    @FXML private Button BTfeedback;
    @FXML private Button BTaceitar;
    @FXML private ImageView imgFotoPerfil;
    @FXML private ListView<HistoricoVersao> listaHistorico;
    @FXML private WebView webSecaoEnviada;
    @FXML private Button btnVisualizarMD;
    @FXML private TextArea txtRawMarkdown;

    // Novos Labels injetados
    @FXML private Label lblNomeAluno;
    @FXML private Label lblEmailAluno;
    @FXML private Label lblAssunto;
    @FXML private Label lblDataEnvio;

    private String emailAluno;
    private int numeroSecao;
    private LocalDateTime dataEnvio;
    private boolean visualizandoMarkdown = false;

    // Atualizei a assinatura para receber o nomeAluno também
    public void setDadosSubmissao(String emailAluno, String nomeAluno, int numeroSecao, LocalDateTime dataEnvio) {
        this.emailAluno = emailAluno;
        this.numeroSecao = numeroSecao;
        this.dataEnvio = dataEnvio;

        // 1. Preencher os Labels da tela
        lblNomeAluno.setText(nomeAluno);
        lblEmailAluno.setText(emailAluno);

        // Formatar a data para ficar bonito (dd/MM/yyyy HH:mm)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        lblDataEnvio.setText("Enviado em: " + dataEnvio.format(formatter));

        // Buscar o Título da Seção no banco
        String tituloTask = DatabaseUtils.getTituloTask(emailAluno, numeroSecao);
        lblAssunto.setText(tituloTask);

        carregarFotoPerfil(emailAluno);

        // 2. Carregar o conteúdo do arquivo
        carregarConteudoDoArquivo();

        // CARREGAR O HISTÓRICO
        carregarHistorico(emailAluno, numeroSecao);
    }

    // ... Manter os métodos EnviarFeedback e AceitarSecao iguais ao anterior ...
    @FXML
    void EnviarFeedback(ActionEvent event) {
        salvarDecisao("revision_requested");
    }

    @FXML
    void AceitarSecao(ActionEvent event) {
        salvarDecisao("approved");
    }

    private void salvarDecisao(String status) {
        String comentario = TxtFeedback.getText();
        String emailProfessor = SessaoManager.getInstance().getEmailUsuario();

        if (emailProfessor == null) {
            UIUtils.showAlert("Erro", "Sessão do professor expirou.");
            return;
        }

        if ("revision_requested".equals(status) && (comentario == null || comentario.trim().isEmpty())) {
            UIUtils.showAlert("Atenção", "Para solicitar revisão, é obrigatório escrever um feedback.");
            return;
        }

        if (comentario == null) comentario = "";

        boolean sucesso = DatabaseUtils.salvarAvaliacaoProfessor(
                emailAluno,
                numeroSecao,
                dataEnvio,
                emailProfessor,
                status,
                comentario
        );

        if (sucesso) {
            String msg = status.equals("approved") ? "Seção ACEITA com sucesso!" : "Feedback enviado com sucesso!";
            UIUtils.showAlert("Sucesso", msg);
            UIUtils.loadFxml("ProfessorScenes/home_Professor.fxml");
        } else {
            UIUtils.showAlert("Erro", "Não foi possível salvar a avaliação.");
        }
    }
    private void carregarFotoPerfil(String emailAluno) {
        // 1. Usa o método que JÁ EXISTE no seu DatabaseUtils
        String caminhoFoto = DatabaseUtils.getProfilePictureUrl(emailAluno);

        Image imagemCarregada = null;

        // 2. Tenta carregar a imagem se o caminho for válido
        if (caminhoFoto != null && !caminhoFoto.isEmpty()) {
            File arquivoFoto = new File(caminhoFoto);
            if (arquivoFoto.exists()) {
                // Carrega do sistema de arquivos (fotos de upload)
                imagemCarregada = new Image(arquivoFoto.toURI().toString());
            } else {
                // Se não for arquivo físico, tenta carregar como recurso do projeto (fotos padrão)
                try {
                    imagemCarregada = new Image(getClass().getResourceAsStream(caminhoFoto));
                } catch (Exception e) {
                    System.out.println("Não foi possível carregar a imagem do caminho: " + caminhoFoto);
                }
            }
        }

        // 3. Se não achou nada, usa uma imagem padrão (opcional, mas recomendado)
        if (imagemCarregada == null) {
            try {
                // Ajuste este caminho para onde está sua imagem padrão de usuário sem foto
                imagemCarregada = new Image(getClass().getResourceAsStream("/com/example/tgcontrol/SceneImages/user_default.png"));
            } catch (Exception e) {
                // Se nem a padrão existir, não faz nada (fica vazio)
            }
        }

        // 4. Aplica a imagem e o recorte Redondo
        if (imagemCarregada != null) {
            imgFotoPerfil.setImage(imagemCarregada);

            // Centraliza a imagem se ela não for quadrada (opcional, ajuda na estética)
            imgFotoPerfil.setPreserveRatio(false); // Força preencher o quadrado

            // Cria o Círculo para recortar
            double raio = imgFotoPerfil.getFitWidth() / 2; // 50 / 2 = 25
            Circle clip = new Circle(raio, raio, raio);
            imgFotoPerfil.setClip(clip);
        }
    }

    private void carregarHistorico(String email, int sequencia) {
        // 1. Busca do banco
        List<HistoricoVersao> lista = DatabaseUtils.getHistoricoVersoes(email, sequencia);

        // 2. Popula a ListView
        listaHistorico.getItems().setAll(lista);

        // 3. Define como cada célula vai aparecer (Formatação)
        listaHistorico.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(HistoricoVersao item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    // Formato: "Versão 1 - 21/11 14:30"
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM HH:mm");
                    String texto = "Versão " + item.getNumeroTentativa() + " - " + item.getDataEnvio().format(fmt);

                    setText(texto);
                    setStyle("-fx-padding: 10; -fx-font-size: 12px;");

                    // Opcional: Mudar cor se já foi aprovado
                    if ("approved".equals(item.getStatus())) {
                        setStyle("-fx-padding: 10; -fx-text-fill: green; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // 4. Adiciona o evento de clique
        listaHistorico.setOnMouseClicked(this::abrirDetalhesHistorico);
    }

    private void abrirDetalhesHistorico(MouseEvent event) {
        HistoricoVersao selecionado = listaHistorico.getSelectionModel().getSelectedItem();

        // Só abre se tiver algo selecionado e for clique (não arrastar)
        if (selecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgcontrol/Scenes/ProfessorScenes/Modal_Historico.fxml"));
                Parent root = loader.load();

                // Passa os dados para o controller do modal
                Modal_Historico_C controller = loader.getController();
                controller.setDados(selecionado);

                // Cria e configura a janela (Stage)
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Detalhes da Versão " + selecionado.getNumeroTentativa());

                // Configura como MODAL (bloqueia a janela de trás enquanto aberta)
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(listaHistorico.getScene().getWindow()); // Define o "dono" da janela

                stage.setResizable(false);
                stage.showAndWait(); // Mostra e espera fechar

            } catch (IOException e) {
                e.printStackTrace();
                UIUtils.showAlert("Erro", "Não foi possível abrir os detalhes.");
            }
        }
    }

    private void carregarConteudoDoArquivo() {
        String caminhoArquivo = DatabaseUtils.getCaminhoArquivoSubmissao(emailAluno, numeroSecao, dataEnvio);

        if (caminhoArquivo == null || caminhoArquivo.isEmpty()) {
            carregarHtmlErro("Caminho do arquivo não encontrado.");
            return;
        }

        File arquivo = new File(caminhoArquivo);
        if (arquivo.exists()) {
            try {
                // Lê o Markdown cru
                String markdown = Files.readString(arquivo.toPath(), StandardCharsets.UTF_8);

                // 1. Configura o WebView (HTML Bonito)
                String htmlBonito = converterMarkdownParaHtml(markdown);
                webSecaoEnviada.getEngine().loadContent(htmlBonito);

                // 2. Configura o TextArea (MD Cru)
                txtRawMarkdown.setText(markdown);

            } catch (IOException e) {
                carregarHtmlErro("Erro ao ler o arquivo: " + e.getMessage());
            }
        } else {
            carregarHtmlErro("Arquivo físico não encontrado: " + caminhoArquivo);
        }
    }

    @FXML
    void alternarVisualizacao(ActionEvent event) {
        visualizandoMarkdown = !visualizandoMarkdown; // Inverte o estado

        if (visualizandoMarkdown) {
            // Modo Markdown
            webSecaoEnviada.setVisible(false);
            txtRawMarkdown.setVisible(true);
            btnVisualizarMD.setText("Visualizar HTML"); // Muda o texto do botão
            btnVisualizarMD.setStyle("-fx-background-color: #E04F5F; -fx-background-radius: 5px;"); // (Opcional) Muda cor para indicar alteração
        } else {
            // Modo HTML (Padrão)
            txtRawMarkdown.setVisible(false);
            webSecaoEnviada.setVisible(true);
            btnVisualizarMD.setText("Visualizar MD");
            btnVisualizarMD.setStyle("-fx-background-color: #0078D7; -fx-background-radius: 5px;"); // Volta cor original
        }
    }

    // --- NOVO MÉTODO: A Mágica da Conversão ---
    private String converterMarkdownParaHtml(String markdown) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        String htmlConteudo = renderer.render(parser.parse(markdown));

        // Aqui usamos concatenação simples (+) para evitar erro com o caractere % do CSS
        return "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: 'Segoe UI', Arial, sans-serif;" +
                "            font-size: 14px;" +
                "            color: #333;" +
                "            padding: 20px;" +
                "            line-height: 1.6;" +
                "        }" +
                "        h1, h2, h3 { color: #2c3e50; margin-top: 20px; }" +
                "        h1 { border-bottom: 1px solid #eee; padding-bottom: 10px; }" +
                "        code { background-color: #f4f4f4; padding: 2px 5px; border-radius: 3px; font-family: 'Consolas', monospace; }" +
                "        pre { background-color: #f4f4f4; padding: 10px; border-radius: 5px; overflow-x: auto; }" +
                "        blockquote { border-left: 4px solid #ccc; margin: 0; padding-left: 10px; color: #666; }" +
                "        img { max-width: 100%; height: auto; }" + // Agora o 100% funciona normal
                "    </style>" +
                "</head>" +
                "<body>" +
                htmlConteudo +
                "</body>" +
                "</html>";
    }

    // Método auxiliar para mostrar erros bonito no WebView
    private void carregarHtmlErro(String mensagem) {
        String htmlErro = """
            <html><body style='font-family: sans-serif; color: red; padding: 20px;'>
            <h3>Erro</h3>
            <p>%s</p>
            </body></html>
            """.formatted(mensagem);
        webSecaoEnviada.getEngine().loadContent(htmlErro);
    }

    @FXML
    void Visualizarhist(ActionEvent event) {
        UIUtils.showAlert("Em breve", "Histórico de versões será exibido aqui.");
    }
}