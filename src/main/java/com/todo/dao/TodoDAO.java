package com.todo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.todo.model.Todo;
import com.todo.util.DatabaseConnection;

public class TodoDAO {
    private static final String GET_ALL_TODOS = "SELECT * FROM todos";
    private static final String ADD_TODO = "INSERT INTO todos(title, description, completed, created_at, updated_at) VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_TODO = "UPDATE todos SET title=?, description=?, completed=?, updated_at=? WHERE id=?";
    private static final String DELETE_TODO = "DELETE FROM todos WHERE id=?";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TodoDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS todos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "completed INTEGER DEFAULT 0, " +
                "created_at TEXT, " +
                "updated_at TEXT)";
        try (Connection conn = DatabaseConnection.getDBConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Todo> getAllTodos() throws SQLException {
        List<Todo> todos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_TODOS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Todo todo = new Todo();
                todo.setId(rs.getInt("id"));
                todo.setTitle(rs.getString("title"));
                todo.setDescription(rs.getString("description"));
                todo.setCompleted(rs.getBoolean("completed"));
                
                String createdAtStr = rs.getString("created_at");
                if (createdAtStr != null && !createdAtStr.isEmpty()) {
                    try {
                        todo.setCreated_at(LocalDateTime.parse(createdAtStr));
                    } catch (Exception e) {
                        todo.setCreated_at(LocalDateTime.now());
                    }
                }
                
                String updatedAtStr = rs.getString("updated_at");
                if (updatedAtStr != null && !updatedAtStr.isEmpty()) {
                    try {
                        todo.setUpdated_at(LocalDateTime.parse(updatedAtStr));
                    } catch (Exception e) {
                        todo.setUpdated_at(LocalDateTime.now());
                    }
                }

                todos.add(todo);
            }
        }
        return todos;
    }

    public void addTodo(Todo todo) throws SQLException {
        String title = todo.getTitle();
        String description = todo.getDescription();
        boolean completed = todo.isCompleted();
        LocalDateTime created_at = todo.getCreated_at() != null ? todo.getCreated_at() : LocalDateTime.now();
        LocalDateTime updated_at = todo.getUpdated_at() != null ? todo.getUpdated_at() : LocalDateTime.now();

        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(ADD_TODO, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setBoolean(3, completed);
            stmt.setString(4, created_at.format(DATE_FORMATTER));
            stmt.setString(5, updated_at.format(DATE_FORMATTER));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Adding todo failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    todo.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Adding todo failed, no ID obtained.");
                }
            }
        }
    }

    public boolean updateTodo(Todo todo) throws SQLException {
        int id = todo.getId();
        String title = todo.getTitle();
        String description = todo.getDescription();
        boolean completed = todo.isCompleted();
        LocalDateTime updated_At = todo.getUpdated_at() != null ? todo.getUpdated_at() : LocalDateTime.now();

        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_TODO)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setBoolean(3, completed);
            stmt.setString(4, updated_At.format(DATE_FORMATTER));
            stmt.setInt(5, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteTodo(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_TODO)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
