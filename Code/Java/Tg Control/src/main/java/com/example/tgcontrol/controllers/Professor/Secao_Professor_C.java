package com.example.tgcontrol.controllers.Professor;

import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;

public class Secao_Professor_C {

    @FXML private TextArea SecaoEnviada;
    @FXML private TextArea TxtFeedback;
    @FXML private Button BTfeedback;
    @FXML private Button BTaceitar;

    // Dados de contexto recebidos da tela anterior
    private String emailAluno;
    private int numeroSecao;
    private LocalDateTime dataEnvio;

    /**
     * Método chamado pela tela anterior (Dashboard) para passar os dados da submissão.
     * Carrega automaticamente o conteúdo do arquivo assim que os dados chegam.
     */
    public void setDadosSubmissao(String emailAluno, int numeroSecao, LocalDateTime dataEnvio) {
        this.emailAluno = emailAluno;
        this.numeroSecao = numeroSecao;
        this.dataEnvio = dataEnvio;

        carregarConteudoDoArquivo();
    }

    private void carregarConteudoDoArquivo() {
        // 1. Busca o caminho exato salvo no banco
        String caminhoArquivo = DatabaseUtils.getCaminhoArquivoSubmissao(emailAluno, numeroSecao, dataEnvio);

        if (caminhoArquivo == null || caminhoArquivo.isEmpty()) {
            SecaoEnviada.setText("Erro: Caminho do arquivo não encontrado no banco de dados.");
            return;
        }

        // 2. Tenta ler o arquivo do disco
        File arquivo = new File(caminhoArquivo);
        if (arquivo.exists()) {
            try {
                String conteudo = Files.readString(arquivo.toPath(), StandardCharsets.UTF_8);
                SecaoEnviada.setText(conteudo);
            } catch (IOException e) {
                SecaoEnviada.setText("Erro ao ler o arquivo: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            SecaoEnviada.setText("Erro: O arquivo físico não foi encontrado no servidor.\nCaminho buscado: " + caminhoArquivo);
        }
    }

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

        if (comentario == null) comentario = ""; // Evita null no banco se for aprovado vazio

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

            // Atualizar página anterior (recarregando o dashboard)
            UIUtils.loadFxml("ProfessorScenes/home_Professor.fxml");

        } else {
            UIUtils.showAlert("Erro", "Não foi possível salvar a avaliação no banco de dados.");
        }
    }

    @FXML
    void Visualizarhist(ActionEvent event) {
        // Lógica para abrir histórico (opcional ou implementar depois)
        UIUtils.showAlert("Em breve", "Histórico de versões será exibido aqui.");
    }
}