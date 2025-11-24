package com.example.tgcontrol.utils;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitário: Classe para manipulação e salvamento de arquivos locais (Markdown, Imagens, Acordos).
 */
public final class FileStorageUtils {

    private static final Logger LOGGER = Logger.getLogger(FileStorageUtils.class.getName());
    private static final String SERVER_BASE_DIR = "Server";
    private static final String PROFILE_PICS_DIR = SERVER_BASE_DIR + File.separator + "profiles";

    /** Diretório para submissões de tarefas. */
    private static final String SUBMISSIONS_DIR = SERVER_BASE_DIR + File.separator + "submissions";
    private static final String DRAFT_DIR = SERVER_BASE_DIR + File.separator + "TGs_Markdown";

    private static final String AGREEMENTS_DIR = SERVER_BASE_DIR + File.separator + "agreements";

    private FileStorageUtils() {
    }

    /**
     * Função: Salva uma imagem de perfil para um usuário.
     * Necessita: O arquivo da imagem e o login (email) do usuário.
     * Retorna: O caminho relativo do arquivo salvo (ex: "Server/profiles/usuario_at_email.com_profilePhoto.png") ou null se ocorrer erro. Substitui arquivo existente com o mesmo nome.
     */
    public static String salvarFotoPerfil(File imagemArquivo, String login) {
        if (imagemArquivo == null || !imagemArquivo.exists() || !imagemArquivo.isFile()) {
            LOGGER.log(Level.WARNING, "Salvar Foto Perfil: Arquivo de imagem inválido ou não encontrado.");
            return null;
        }
        if (login == null || login.isBlank()) {
            LOGGER.log(Level.WARNING, "Salvar Foto Perfil: Login (email) do usuário inválido.");
            return null;
        }

        String nomeOriginal = imagemArquivo.getName();
        String extensao = "";
        int lastDotIndex = nomeOriginal.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < nomeOriginal.length() - 1) {
            extensao = nomeOriginal.substring(lastDotIndex);
        } else {
            LOGGER.log(Level.WARNING, "Salvar Foto Perfil: Não foi possível determinar a extensão do arquivo: " + nomeOriginal);
            return null;
        }

        String nomeArquivoDestino = login.replace("@", "_at_") + "_profilePhoto" + extensao;
        Path diretorioDestinoPath = Paths.get(PROFILE_PICS_DIR);
        Path arquivoDestinoPath = diretorioDestinoPath.resolve(nomeArquivoDestino);
        String caminhoRelativo = PROFILE_PICS_DIR + File.separator + nomeArquivoDestino;

        try {
            if (!Files.exists(diretorioDestinoPath)) {
                Files.createDirectories(diretorioDestinoPath);
            }
            Files.copy(imagemArquivo.toPath(), arquivoDestinoPath, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.log(Level.INFO, "Foto de perfil salva com sucesso em: " + caminhoRelativo);
            return caminhoRelativo;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar foto de perfil para " + login + " em " + caminhoRelativo, e);
            UIUtils.showAlert("Erro ao Salvar Imagem", "Não foi possível salvar a foto de perfil: " + e.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro inesperado ao salvar foto de perfil.", e);
            UIUtils.showAlert("Erro Inesperado", "Ocorreu um erro ao salvar a foto de perfil: " + e.getMessage());
            return null;
        }
    }

    /**
     * Função: Salva um documento de acordo de orientação (PDF, DOCX) para um usuário.
     * Necessita: O arquivo do acordo e o login (email) do usuário.
     * Retorna: O caminho relativo do arquivo salvo (ex: "Server/agreements/usuario_at_email.com_agreement.pdf") ou null se ocorrer erro.
     */
    public static String salvarAcordoOrientacao(File acordoArquivo, String login) {
        if (acordoArquivo == null || !acordoArquivo.exists() || !acordoArquivo.isFile()) {
            LOGGER.log(Level.WARNING, "Salvar Acordo: Arquivo inválido ou não encontrado.");
            return null;
        }
        if (login == null || login.isBlank()) {
            LOGGER.log(Level.WARNING, "Salvar Acordo: Login (email) do usuário inválido.");
            return null;
        }

        String nomeOriginal = acordoArquivo.getName();
        String extensao = "";
        int lastDotIndex = nomeOriginal.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < nomeOriginal.length() - 1) {
            extensao = nomeOriginal.substring(lastDotIndex);
        } else {
            LOGGER.log(Level.WARNING, "Salvar Acordo: Não foi possível determinar a extensão do arquivo: " + nomeOriginal);
            return null;
        }

        if (!extensao.equalsIgnoreCase(".pdf") && !extensao.equalsIgnoreCase(".docx")) {
            LOGGER.log(Level.WARNING, "Salvar Acordo: Tipo de arquivo não permitido: " + extensao);
            UIUtils.showAlert("Erro de Arquivo", "Tipo de arquivo não permitido. Envie apenas PDF ou DOCX.");
            return null;
        }

        String nomeArquivoDestino = login.replace("@", "_at_") + "_agreement" + extensao;
        Path diretorioDestinoPath = Paths.get(AGREEMENTS_DIR);
        Path arquivoDestinoPath = diretorioDestinoPath.resolve(nomeArquivoDestino);
        String caminhoRelativo = AGREEMENTS_DIR + File.separator + nomeArquivoDestino;

        try {
            if (!Files.exists(diretorioDestinoPath)) {
                Files.createDirectories(diretorioDestinoPath);
            }
            Files.copy(acordoArquivo.toPath(), arquivoDestinoPath, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.log(Level.INFO, "Acordo de orientação salvo com sucesso em: " + caminhoRelativo);
            return caminhoRelativo;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar acordo para " + login + " em " + caminhoRelativo, e);
            UIUtils.showAlert("Erro ao Salvar Arquivo", "Não foi possível salvar o acordo de orientação: " + e.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro inesperado ao salvar acordo.", e);
            UIUtils.showAlert("Erro Inesperado", "Ocorreu um erro ao salvar o acordo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Função: Converte conteúdo HTML para Markdown e salva em um arquivo .md no diretório especificado.
     * @param htmlInput O texto HTML.
     * @param nomeArquivoSaida O nome desejado para o arquivo de saída.
     * @param isSubmission Indica se é uma submissão (salva em SUBMISSIONS_DIR) ou rascunho (salva em DRAFT_DIR).
     * @return true se sucesso, false caso contrário.
     */
    public static boolean converterESalvarMarkdown(String htmlInput, String nomeArquivoSaida, boolean isSubmission) {
        // Usa o diretório de submissions ou o diretório de rascunhos, conforme a flag
        String diretorioSaida = isSubmission ? SUBMISSIONS_DIR : DRAFT_DIR;

        if (htmlInput == null || htmlInput.isBlank()) {
            LOGGER.log(Level.WARNING, "Conversão HTML->MD: Conteúdo HTML de entrada está vazio.");
            return false;
        }
        if (nomeArquivoSaida == null || nomeArquivoSaida.isBlank() || !nomeArquivoSaida.endsWith(".md")) {
            LOGGER.log(Level.WARNING, "Conversão HTML->MD: Nome do arquivo de saída inválido ou sem extensão .md.");
            return false;
        }

        try {
            FlexmarkHtmlConverter conversor = FlexmarkHtmlConverter.builder().build();
            String markdownOutput = conversor.convert(htmlInput);

            File pasta = new File(diretorioSaida);
            if (!pasta.exists()) {
                if (!pasta.mkdirs()) {
                    throw new IOException("Não foi possível criar o diretório de saída: " + diretorioSaida);
                }
            }

            String caminhoCompleto = diretorioSaida + File.separator + nomeArquivoSaida;

            try (FileWriter writer = new FileWriter(caminhoCompleto)) {
                writer.write(markdownOutput);
                LOGGER.log(Level.INFO, "Arquivo Markdown '" + nomeArquivoSaida + "' salvo com sucesso em " + diretorioSaida);
                return true;
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao converter ou salvar HTML para Markdown.", e);
            UIUtils.showAlert("Erro na Conversão", "Não foi possível salvar o arquivo Markdown: " + e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro inesperado durante a conversão HTML para Markdown.", e);
            UIUtils.showAlert("Erro na Conversão", "Não foi possível salvar o arquivo Markdown: " + e.getMessage());
            return false;
        }
    }
}