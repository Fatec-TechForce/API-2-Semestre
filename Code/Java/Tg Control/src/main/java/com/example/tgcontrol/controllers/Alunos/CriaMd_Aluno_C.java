package com.example.tgcontrol.controllers.Alunos;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

import java.io.FileWriter;
import java.io.IOException;

public class CriaMd_Aluno_C {
    public void converterParaMD(String htmlInput) {


        //String htmlInput = alterar;

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
        salvarEmArquivo(markdownOutput, "secao_2.md");
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
