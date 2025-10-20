package com.example.tgcontrol.controllers.Alunos;

import java.io.FileWriter;
import java.io.IOException;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

public class CriaMd_Aluno_C {
    public static void main(String[] args) {


        String htmlInput = "<h1>Título Principal da Seção</h1>" +
                "<p>Este é o primeiro parágrafo do trabalho do aluno. Ele pode conter <strong>texto em negrito</strong> e <em>texto em itálico</em>.</p>" +
                "<h2>Um Subtítulo</h2>" +
                "<ul>" +
                "    <li>Primeiro item da lista.</li>" +
                "    <li>Segundo item da lista.</li>" +
                "</ul>" +
                "<p>Um link de exemplo para o <a href=\"https://www.google.com\">Google</a>.</p>";

        // 2. Crie uma instância do conversor.
        //    O .builder() permite configurar opções, mas o padrão já é ótimo.
        FlexmarkHtmlConverter conversor = FlexmarkHtmlConverter.builder().build();

        // 3. Chame o método 'convert' para fazer a mágica.
        String markdownOutput = conversor.convert(htmlInput);

        // 4. Imprima o resultado no console para ver como ficou.
        System.out.println("--- CONTEÚDO HTML DE ENTRADA ---");
        System.out.println(htmlInput);
        System.out.println("\n--- CONTEÚDO MARKDOWN GERADO ---");
        System.out.println(markdownOutput);

        // 5. (Opcional, mas necessário para seu projeto) Salve o resultado em um arquivo .md
        salvarEmArquivo(markdownOutput, "secao_1.md");
    }

    public static void salvarEmArquivo(String conteudo, String nomeDoArquivo) {
        try (FileWriter writer = new FileWriter(nomeDoArquivo)) {
            writer.write(conteudo);
            System.out.println("\nArquivo '" + nomeDoArquivo + "' salvo com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}
