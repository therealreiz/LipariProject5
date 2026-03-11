package com.lipari.bank.persistence;

import java.sql.*;

/**
 * Gestisce la connessione al database H2.
 */
public class DatabaseManager {

    private static final String URL      = "jdbc:h2:/home/mario.rossi/projects/liparibank/liparibank_db";
    private static final String USER     = "sa";
    private static final String PASSWORD = "";

    private DatabaseManager() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeSchema() throws SQLException {
        try (Connection conn = getConnection();
             Statement  stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS customers (
                        id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fiscal_code VARCHAR(16)  NOT NULL UNIQUE,
                        first_name  VARCHAR(100) NOT NULL,
                        last_name   VARCHAR(100) NOT NULL
                    )""");
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS accounts (
                        id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                        iban        VARCHAR(34)   NOT NULL UNIQUE,
                        balance     DECIMAL(15,2) NOT NULL,
                        customer_id BIGINT        NOT NULL
                            REFERENCES customers(id)
                    )""");
        }
    }
}
