package com.example.tgcontrol.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {

    // --- CONFIGURE SEUS DADOS AQUI ---

    // O banco de dados que você criou no script
    private static final String DATABASE_NAME = "TGControl";

    // O endereço do seu servidor MySQL
    private static final String HOST = "localhost"; // ou "127.0.0.1"
    private static final String PORT = "3306";

    // Suas credenciais do MySQL
    private static final String USER = "root"; // Usuário padrão do MySQL
    private static final String PASSWORD = "Fatec@2025"; // A senha que você definiu

    // --- FIM DA CONFIGURAÇÃO ---


    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE_NAME +
            "?useTimezone=true&serverTimezone=UTC";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC do MySQL não encontrado.");
            e.printStackTrace();
            throw new SQLException("Driver não encontrado", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}