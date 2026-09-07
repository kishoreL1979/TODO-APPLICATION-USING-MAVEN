package com.todo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.todo.model.Todo;
import com.todo.util.DatabaseConnection;

public class TodoDAO {
    private static final String GET_ALL_TODOS = "SELECT * FROM todos";
    private static final String ADD_TODO = "INSERT INTO todos(title, description, completed, created_at, updated_at) VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_TODO = "UPDATE todos SET title=?, description=?, completed=?, updated_at=? WHERE id=?";
    private static final String DELETE_TODO = "DELETE FROM todos WHERE id=?";

    public TodoDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS todos (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "title VARCHAR(255) NOT NULL, " +
                "description TEXT, " +
                "completed BOOLEAN DEFAULT FALSE, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
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
                
                Timestamp createdAtTs = rs.getTimestamp("created_at");
                if (createdAtTs != null) {
                    todo.setCreated_at(createdAtTs.toLocalDateTime());
                }
                
                Timestamp updatedAtTs = rs.getTimestamp("updated_at");
                if (updatedAtTs != null) {
                    todo.setUpdated_at(updatedAtTs.toLocalDateTime());
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
            stmt.setTimestamp(4, Timestamp.valueOf(created_at));
            stmt.setTimestamp(5, Timestamp.valueOf(updated_at));

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
            stmt.setTimestamp(4, Timestamp.valueOf(updated_At));
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
