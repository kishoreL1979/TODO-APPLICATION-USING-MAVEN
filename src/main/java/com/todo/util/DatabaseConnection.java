package com.todo.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String getDatabaseUrl() {
        String userHome = System.getProperty("user.home");
        File dbDir = new File(userHome, "TodoApplication");
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }
        File dbFile = new File(dbDir, "todo.db");
        return "jdbc:sqlite:" + dbFile.getAbsolutePath();
    }

    public static Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(getDatabaseUrl());
    }
}
