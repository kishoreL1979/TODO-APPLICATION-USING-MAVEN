package com.todo.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    // Updated DB name to match user's database and added common connection params
    private static final String URL = "jdbc:mysql://localhost:3306/todo_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Loki#Logan07";

    public static Connection getDBConnection() throws SQLException{
        return DriverManager.getConnection(URL,USERNAME,PASSWORD);
    }
}
