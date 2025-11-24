package com.example.tgcontrol.controllers.Alunos;

import com.example.tgcontrol.utils.FileStorageUtils;
import com.example.tgcontrol.utils.UIUtils;
import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.model.SecaoAluno;
import com.example.tgcontrol.model.HistoricoVersao;
import com.example.tgcontrol.controllers.Professor.Modal_Historico_C;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class Secao_Aluno_C {
    @FXML private Button btn_Arquivo;
    @FXML private Button enviarSecao;
    @FXML private Label lblMensagemSucesso;
    @FXML private HTMLEditor htmlEditor;

    @FXML private Label lblTitulo;
    @FXML private Label lblData;
    @FXML private VBox vboxFeedbackProfessor;
    @FXML private TextArea txtFeedbackProfessor;
    @FXML private ListView<HistoricoVersao> listaHistorico; // Do FXML

    private SecaoAluno secaoAtual;
    private String emailAluno;
    private int numeroSecao;
    private int proximaTentativa;
    private String caminhoUltimaSubmissao;
    private static final Logger LOGGER = Logger.getLogger(Secao_Aluno_C.class.getName());

    /**
     * Função: Configura a tela com os dados da seção selecionada, carregando o último
     * conteúdo e verificando o status da revisão.
     */
    public void setDadosSecao(SecaoAluno secao) {
        if (secao == null) {
            LOGGER.log(Level.SEVERE, "ERRO CRÍTICO: Objeto SecaoAluno é nulo. A navegação falhou.");
            UIUtils.showAlert("Erro", "Dados da seção estão vazios (Objeto SecaoAluno é nulo).");
            return;
        }

        this.secaoAtual = secao;
        this.emailAluno = secao.getEmailAluno();
        this.numeroSecao = secao.getTaskSequence();

        if (this.emailAluno == null || this.emailAluno.isEmpty() || this.numeroSecao == 0) {
            LOGGER.log(Level.SEVERE, "ERRO CRÍTICO: Dados essenciais ausentes. Email: " + this.emailAluno + ", Seção: " + this.numeroSecao);
            UIUtils.showAlert("Erro Crítico", "Os dados essenciais da seção (Email do Aluno/Número da Seção) não foram carregados. Impossível continuar.");
            return;
        }

        LOGGER.log(Level.INFO, "Secao_Aluno_C carregada com sucesso para: " + this.emailAluno + ", Seção: " + this.numeroSecao);

        lblTitulo.setText(secao.getTitulo());

        if (secao.getDataEntrega() != null) {
            lblData.setText("Entrega: " + secao.getDataEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            lblData.setText("Data de entrega não definida");
        }

        Map<String, Object> submissionData = DatabaseUtils.getLatestSubmissionDetails(emailAluno, numeroSecao);

        if (submissionData != null) {
            this.proximaTentativa = (int) submissionData.getOrDefault("proximaTentativa", 1);
            this.caminhoUltimaSubmissao = (String) submissionData.get("caminhoArquivo");
            String statusRevisao = (String) submissionData.get("statusRevisao");
            String feedback = (String) submissionData.get("feedbackProfessor");

            carregarConteudoNoEditor();

            if ("approved".equalsIgnoreCase(statusRevisao)) {
                bloquearEdicao("Seção APROVADA. Você não pode mais editar este conteúdo.");
                esconderFeedbackProfessor();
            } else if ("revision_requested".equalsIgnoreCase(statusRevisao)) {
                desbloquearEdicao();
                mostrarFeedbackProfessor(feedback, statusRevisao);
            } else if ("Pendente".equals(statusRevisao)) {
                bloquearEdicao("Seção enviada para revisão. A edição está bloqueada enquanto aguarda a correção do professor.");
                mostrarFeedbackProfessor(null, statusRevisao);
            } else {
                desbloquearEdicao();
                esconderFeedbackProfessor();
            }

        } else {
            this.proximaTentativa = 1;
            this.caminhoUltimaSubmissao = null;
            desbloquearEdicao();
            esconderFeedbackProfessor();
        }

        if (listaHistorico != null) {
            carregarHistorico(emailAluno, numeroSecao);
        }
    }

    private void carregarConteudoNoEditor() {
        if (caminhoUltimaSubmissao != null) {
            File arquivo = new File(caminhoUltimaSubmissao);
            if (arquivo.exists()) {
                try {
                    String fileExtension = caminhoUltimaSubmissao.substring(caminhoUltimaSubmissao.lastIndexOf('.') + 1);
                    String htmlContent = "";

                    if (fileExtension.equalsIgnoreCase("md")) {
                        String markdown = Files.readString(arquivo.toPath(), StandardCharsets.UTF_8);
                        htmlContent = converterMarkdownParaHtml(markdown);
                    } else {
                        LOGGER.log(Level.WARNING, "Arquivo de submissão não é Markdown (.md): " + caminhoUltimaSubmissao);
                        String textoSimples = Files.readString(arquivo.toPath(), StandardCharsets.UTF_8);
                        htmlContent = "<html><body><pre>" + textoSimples + "</pre></body></html>";
                    }

                    if (!htmlContent.isEmpty()) {
                        htmlEditor.setHtmlText(htmlContent);
                        LOGGER.log(Level.INFO, "Conteúdo anterior carregado com sucesso do caminho: " + caminhoUltimaSubmissao);
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Erro de I/O ao carregar conteúdo anterior.", e);
                    UIUtils.showAlert("Erro de Leitura", "Não foi possível carregar o conteúdo do arquivo anterior: " + e.getMessage());
                }
            } else {
                LOGGER.log(Level.WARNING, "Caminho do arquivo de submissão encontrado no DB, mas o arquivo físico não existe: " + caminhoUltimaSubmissao);
            }
        }
    }

    private void bloquearEdicao(String mensagem) {
        htmlEditor.setDisable(true);
        btn_Arquivo.setDisable(true);
        enviarSecao.setDisable(true);

        if (txtFeedbackProfessor != null) {
            txtFeedbackProfessor.setText(mensagem);
            txtFeedbackProfessor.setEditable(false);
            vboxFeedbackProfessor.setVisible(true);
            vboxFeedbackProfessor.setManaged(true);
        }
        LOGGER.log(Level.INFO, "Edição bloqueada: " + mensagem);
    }

    private void desbloquearEdicao() {
        htmlEditor.setDisable(false);
        btn_Arquivo.setDisable(false);
        enviarSecao.setDisable(false);
        LOGGER.log(Level.INFO, "Edição desbloqueada: Seção em andamento ou requer revisão.");
    }

    private void mostrarFeedbackProfessor(String feedback, String statusRevisao) {
        if (vboxFeedbackProfessor != null && txtFeedbackProfessor != null) {
            String statusTexto;
            String corBorda;
            String textoAExibir;

            if ("revision_requested".equalsIgnoreCase(statusRevisao)) {
                statusTexto = "REVISÃO SOLICITADA";
                corBorda = "#FF9800"; // Laranja
                textoAExibir = feedback != null ? feedback : "O professor solicitou revisão. Verifique o conteúdo para mais detalhes.";
            } else if ("Pendente".equals(statusRevisao)) {
                statusTexto = "AGUARDANDO AVALIAÇÃO";
                corBorda = "#2196F3"; // Azul
                textoAExibir = "O seu envio mais recente está aguardando correção e avaliação do professor.";
            } else {
                esconderFeedbackProfessor();
                return;
            }

            String header = "Status Atual: " + statusTexto;

            txtFeedbackProfessor.setText(header + "\n\n" + textoAExibir);

            vboxFeedbackProfessor.setStyle("-fx-border-color: " + corBorda + "; -fx-border-width: 2 0 0 0; -fx-padding: 10 0 0 0;");

            vboxFeedbackProfessor.setVisible(true);
            vboxFeedbackProfessor.setManaged(true);
            txtFeedbackProfessor.setEditable(false);
        }
        LOGGER.log(Level.INFO, "Painel de Feedback exibido. Status: " + statusRevisao);
    }

    private void esconderFeedbackProfessor() {
        if (vboxFeedbackProfessor != null) {
            vboxFeedbackProfessor.setVisible(false);
            vboxFeedbackProfessor.setManaged(false);
        }
        LOGGER.log(Level.INFO, "Painel de Feedback ocultado (Aprovado ou Sem Submissão).");
    }

    private String converterMarkdownParaHtml(String markdown) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        String htmlConteudo = renderer.render(parser.parse(markdown));

        return "<html>" +
                "<head>" +
                "    <style>" +
                "        body { font-family: 'Segoe UI', Arial, sans-serif; font-size: 14px; color: #333; padding: 20px; line-height: 1.6; }" +
                "        h1, h2, h3 { color: #2c3e50; margin-top: 20px; }" +
                "        h1 { border-bottom: 1px solid #eee; padding-bottom: 10px; }" +
                "        code { background-color: #f4f4f4; padding: 2px 5px; border-radius: 3px; font-family: 'Consolas', monospace; }" +
                "        pre { background-color: #f4f4f4; padding: 10px; border-radius: 5px; overflow-x: auto; }" +
                "        blockquote { border-left: 4px solid #ccc; margin: 0; padding-left: 10px; color: #666; }" +
                "        img { max-width: 100%; height: auto; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                htmlConteudo +
                "</body>" +
                "</html>";
    }

    private String formatarTamanhoArquivo(long tamanho) {
        if(tamanho <= 0) return null;
        final String[] unidades = new String[] {"bytes", "KB", "MB", "GB"};
        int grupos_digitos = (int) (Math.log10(tamanho) / Math.log10(1024));
        if (grupos_digitos < 0 || grupos_digitos >= unidades.length) return tamanho + " bytes";

        return String.format(java.util.Locale.US, "%.2f %s", tamanho / Math.pow(1024, grupos_digitos), unidades[grupos_digitos]);
    }

    private String converterArquivoParaHtml(File arquivo_selecionado) {
        Path caminho_arquivo = arquivo_selecionado.toPath();
        String resultadoHtml = null;
        String fileName = arquivo_selecionado.getName().toLowerCase();
        try {
            if(fileName.endsWith(".txt")) {
                String text = Files.readString(caminho_arquivo);
                resultadoHtml = "<html><body><pre>" + text + "</pre></body></html>";
                LOGGER.log(Level.INFO, "Conversão TXT concluída.");
            }
            else if(fileName.endsWith(".docx")) {
                DocumentConverter conversor = new DocumentConverter();
                Result<String> resultado = conversor.convertToHtml(arquivo_selecionado);
                resultadoHtml = resultado.getValue();
                Set<String> avisos = resultado.getWarnings();
                if (!avisos.isEmpty()) {
                    LOGGER.log(Level.WARNING, "Aviso na conversão DOCX: " + String.join(", ", avisos));
                }
                LOGGER.log(Level.INFO, "Conversão DOCX concluída.");
            }
            else if(fileName.endsWith(".pdf")) {
                try (PDDocument document = PDDocument.load(arquivo_selecionado)) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    String text = stripper.getText(document);
                    resultadoHtml = "<html><body><pre>" + text + "</pre></body></html>";
                }
                LOGGER.log(Level.INFO, "Conversão PDF concluída.");
            } else {
                LOGGER.log(Level.WARNING, "Formato de arquivo não suportado para conversão: " + arquivo_selecionado.getName());
            }
        } catch (IOException e) {
            UIUtils.showAlert("Erro de Conversão", "Erro de leitura ou I/O ao converter o arquivo: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro de I/O na conversão de arquivo.", e);
        } catch (Exception e) {
            UIUtils.showAlert("Erro de Biblioteca", "Não foi possível processar o arquivo. Verifique se o formato está correto e o arquivo não está corrompido.");
            LOGGER.log(Level.SEVERE, "Erro na biblioteca de conversão de arquivo.", e);
        }

        return resultadoHtml;
    }

    /**
     * Função: Permite que o usuário selecione um arquivo (DOCX, PDF, TXT) e
     * insira seu conteúdo no HTMLEditor após conversão.
     */
    @FXML
    public void adicionarArquivo() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Adicionar Arquivo");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Texto e Documentos", "*.txt", "*.pdf", "*.docx")
        );

        Stage stage = (Stage) btn_Arquivo.getScene().getWindow();
        File arquivo_selecionado = fc.showOpenDialog(stage);

        if(arquivo_selecionado != null) {
            String htmlContent = converterArquivoParaHtml(arquivo_selecionado);

            if (htmlContent != null && !htmlContent.isBlank()) {
                try {
                    Path caminho_Arquivo = arquivo_selecionado.toPath();
                    long tamanho_bytes = Files.size(caminho_Arquivo);
                    String tamanho_formatado = formatarTamanhoArquivo(tamanho_bytes);
                    btn_Arquivo.setText(arquivo_selecionado.getName());
                    lblMensagemSucesso.setText("Arquivo adicionado com sucesso! (" + tamanho_formatado + ")");
                    lblMensagemSucesso.setVisible(true);

                    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                    Runnable atraso = () -> lblMensagemSucesso.setVisible(false);
                    executor.schedule(atraso, 4, TimeUnit.SECONDS);
                    executor.shutdown();

                    htmlEditor.setHtmlText(htmlContent); // Atualiza o editor
                    LOGGER.log(Level.INFO, "Conteúdo do arquivo carregado no HTMLEditor.");
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Erro ao obter tamanho do arquivo.", e);
                    UIUtils.showAlert("Erro", "Erro ao processar o arquivo.");
                }
            } else {

                btn_Arquivo.setText("Adicionar arquivo");
            }
        } else {
            btn_Arquivo.setText("Adicionar arquivo");
        }
    }

    /**
     * Função: Realiza o envio da seção atual, salvando o conteúdo e registrando a submissão no banco.
     */
    @FXML
    public void enviarSecao() {
        String htmlContent = htmlEditor.getHtmlText();

        if (emailAluno == null || numeroSecao == 0) {
            LOGGER.log(Level.SEVERE, "Falha no envio: Contexto da seção perdido.");
            UIUtils.showAlert("Erro", "Os dados da seção não foram carregados corretamente. Impossível enviar.");
            return;
        }

        if (htmlContent == null || htmlContent.isBlank()) {
            UIUtils.showAlert("Atenção", "O editor está vazio. Não há nada para enviar.");
            return;
        }

        String emailClean = emailAluno.replace("@", "_at_");
        String nomeArquivoMd = String.format("%s_t%d_v%d.md", emailClean, numeroSecao, proximaTentativa);
        String tituloSubmissao = lblTitulo.getText() + " (Tentativa " + proximaTentativa + ")";

        boolean sucessoSalvamento = FileStorageUtils.converterESalvarMarkdown(htmlContent, nomeArquivoMd, true);

        if (sucessoSalvamento) {
            String caminhoArquivo = "Server/submissions/" + nomeArquivoMd;

            int novaTentativa = DatabaseUtils.uploadNovaVersao(
                    emailAluno,
                    numeroSecao,
                    tituloSubmissao,
                    caminhoArquivo
            );

            if (novaTentativa > 0) {
                UIUtils.showAlert("Sucesso", "Seção enviada para revisão com sucesso! Notificação enviada ao professor.");
                // Recarrega a tela de seções para atualizar o status
                Stage stage = (Stage) enviarSecao.getScene().getWindow();
                UIUtils.loadFxml("AlunoScenes/secoes_Aluno.fxml");

            } else {
                LOGGER.log(Level.SEVERE, "Falha ao registrar submissão no DB. Aluno: " + emailAluno + ", Seção: " + numeroSecao);
                UIUtils.showAlert("Erro de Banco de Dados", "Ocorreu um erro ao registrar a submissão. Tente novamente.");
            }
        } else {
            LOGGER.log(Level.SEVERE, "Falha ao salvar arquivo Markdown: " + nomeArquivoMd);
            UIUtils.showAlert("Erro de Arquivo", "Não foi possível salvar o conteúdo no formato Markdown. Verifique as permissões de pasta.");
        }
    }

    /**
     * Implementa a lógica de carregamento do histórico de versões na ListView.
     */
    private void carregarHistorico(String email, int sequencia) {
        List<HistoricoVersao> lista = DatabaseUtils.getHistoricoVersoes(email, sequencia);

        listaHistorico.getItems().setAll(lista);

        listaHistorico.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(HistoricoVersao item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM HH:mm");
                    String status = item.getStatus() != null ? item.getStatus().toUpperCase() : "PENDENTE";
                    String texto = String.format("Versão %d - %s [%s]", item.getNumeroTentativa(), item.getDataEnvio().format(fmt), status);

                    setText(texto);
                    setStyle("-fx-padding: 10; -fx-font-size: 12px;");

                    if ("APPROVED".equals(item.getStatus())) {
                        setStyle("-fx-padding: 10; -fx-text-fill: green; -fx-font-weight: bold;");
                    } else if ("REVISION_REQUESTED".equals(item.getStatus())) {
                        setStyle("-fx-padding: 10; -fx-text-fill: orange; -fx-font-weight: bold;");
                    }
                }
            }
        });

        listaHistorico.setOnMouseClicked(this::abrirDetalhesHistorico);
    }

    /**
     * Abre o Modal_Historico.fxml (do professor) para exibir os detalhes da versão selecionada.
     */
    private void abrirDetalhesHistorico(MouseEvent event) {
        if (event.getClickCount() == 2) { // Requer duplo clique
            HistoricoVersao selecionado = listaHistorico.getSelectionModel().getSelectedItem();

            if (selecionado != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tgcontrol/Scenes/ProfessorScenes/Modal_Historico.fxml"));
                    Parent root = loader.load();

                    Modal_Historico_C controller = loader.getController();
                    controller.setDados(selecionado);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Detalhes da Versão " + selecionado.getNumeroTentativa());
                    UIUtils.setStageIcon(stage);

                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(listaHistorico.getScene().getWindow());

                    stage.setResizable(true);
                    stage.showAndWait();

                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Erro ao abrir o modal de histórico.", e);
                    UIUtils.showAlert("Erro", "Não foi possível abrir os detalhes da versão.");
                }
            }
        }
    }
}