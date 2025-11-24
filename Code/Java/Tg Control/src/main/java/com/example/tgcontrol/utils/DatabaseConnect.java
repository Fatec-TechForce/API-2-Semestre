package com.example.tgcontrol.utils;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnect {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnect.class.getName()); // 🔑 Novo Logger
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        loadCredentials();
    }

    private static void loadCredentials() {
        Properties props = new Properties();
        final String resourcePath = "Server/db.properties"; // O caminho que o ClassLoader tenta buscar

        try {
            LOGGER.log(Level.INFO, "Tentando carregar o recurso: {0}", resourcePath);
            InputStream input = DatabaseConnect.class.getClassLoader().getResourceAsStream(resourcePath);

            if (input == null) {
                LOGGER.log(Level.WARNING, "Recurso NÃO encontrado no Classpath. Tentando Fallback File System...");

                Path fallbackPath = Paths.get("Server", "db.properties");

                if (fallbackPath.toFile().exists()) {
                    input = new java.io.FileInputStream(fallbackPath.toFile());
                    LOGGER.log(Level.INFO, "Fallback bem-sucedido! Arquivo encontrado em: {0}", fallbackPath.toAbsolutePath());
                } else {
                    LOGGER.log(Level.SEVERE, "❌ ERRO GRAVE: Arquivo 'db.properties' não encontrado em: {0}", fallbackPath.toAbsolutePath());
                    LOGGER.severe("Certifique-se de que a pasta 'Server' está no Working Directory da sua IDE.");
                    return;
                }
            }

            try {
                props.load(input);
            } finally {
                if (input != null) {
                    input.close();
                }
            }

            String host = props.getProperty("db.host");
            String port = props.getProperty("db.port");
            String dbName = props.getProperty("db.name");

            URL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";

            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");

            LOGGER.info("✅ Credenciais do BD carregadas com sucesso.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ERRO fatal ao carregar propriedades: " + e.getMessage(), e);
        }
    }

    /**
     * Retorna a Connection ativa com o banco de dados.
     * @return Uma nova conexão JDBC.
     * @throws SQLException Se a conexão falhar ou as credenciais não estiverem definidas.
     */
    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null || PASSWORD == null) {
            throw new SQLException("Credenciais do banco de dados não carregadas. Verifique o arquivo 'db.properties' e a sua sintaxe.");
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}