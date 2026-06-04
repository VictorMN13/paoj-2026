package com.pao.proiect.bank_app.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new IOException("Nu gasesc db.properties in resources/");
            }
            props.load(is);
        }
        String url  = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        this.connection = DriverManager.getConnection(url, user, pass);
    }

    /**
     * Returneaza unica instanta DatabaseConnection.
     * Thread-safe cu synchronized (suficient pentru proiect single-threaded).
     */
    public static synchronized DatabaseConnection getInstance()
            throws IOException, SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /** Expune conexiunea pentru repository-uri. */
    public Connection getConnection() {
        return connection;
    }

    /** Inchide conexiunea la finalul aplicatiei. */
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}