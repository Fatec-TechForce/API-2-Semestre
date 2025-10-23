package com.example.tgcontrol.utils;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class HTMLconversor {

    private static final Logger LOGGER = Logger.getLogger(HTMLconversor.class.getName());

    private HTMLconversor() {

    }

    public static boolean converterESalvarMarkdown(String htmlInput, String nomeArquivoSaida, String diretorioSaida) {
        if (htmlInput == null || htmlInput.isBlank()) {
            LOGGER.log(Level.WARNING, "Conversão HTML->MD: Conteúdo HTML de entrada está vazio.");
            return false;
        }
        if (nomeArquivoSaida == null || nomeArquivoSaida.isBlank() || !nomeArquivoSaida.endsWith(".md")) {
            LOGGER.log(Level.WARNING, "Conversão HTML->MD: Nome do arquivo de saída inválido ou sem extensão .md.");
            return false;
        }
        if (diretorioSaida == null || diretorioSaida.isBlank()) {
            LOGGER.log(Level.WARNING, "Conversão HTML->MD: Diretório de saída não especificado.");
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

            // Salva o conteúdo Markdown no arquivo-->

            try (FileWriter writer = new FileWriter(caminhoCompleto)) {
                writer.write(markdownOutput);
                LOGGER.log(Level.INFO, "Arquivo Markdown '" + nomeArquivoSaida + "' salvo com sucesso em " + diretorioSaida);
                return true; // Sucesso
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao converter ou salvar HTML para Markdown.", e);
            UIUtils.showAlert("Erro na Conversão", "Não foi possível salvar o arquivo Markdown: " + e.getMessage());
            return false; // Falha
        } catch (Exception e) {
            // Captura outras exceções inesperadas (ex: da biblioteca Flexmark)
            LOGGER.log(Level.SEVERE, "Erro inesperado durante a conversão HTML para Markdown.", e);
            UIUtils.showAlert("Erro na Conversão", "Não foi possível salvar o arquivo Markdown: " + e.getMessage());
            return false; // Falha
        }
    }
}