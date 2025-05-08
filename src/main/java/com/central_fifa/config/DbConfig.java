package com.central_fifa.config;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Component

public class DbConfig {
    private final static int defaultPort = 5435;
    private final static String host = System.getenv("dbHost");
    private final static String user = System.getenv("dbUsername");
    private final static String password = System.getenv("dbPassword");
    private final static String dbName = System.getenv("dbName");
    private final String url;

    public DbConfig() {
        url = "jdbc:postgresql://" + host + ":" + defaultPort + "/" + dbName;
    }

    public Connection getConnection() {
        try{
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
