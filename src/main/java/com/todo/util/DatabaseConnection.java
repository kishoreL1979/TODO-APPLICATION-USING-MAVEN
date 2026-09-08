package com.todo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:todo_db.db";

    public static Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
