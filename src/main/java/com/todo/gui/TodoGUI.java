package com.todo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.todo.dao.TodoDAO;
import com.todo.model.Todo;

public class TodoGUI extends JFrame {
    private TodoDAO todoDAO;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox completedCheckBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private DefaultTableModel tableModel;
    private JTable todoTable;
    private JComboBox<String> filterComboBox;

    // Soft Sky Blue Color Palette
    private static final Color PRIMARY_SKY_BLUE = new Color(2, 136, 209);
    private static final Color BG_WINDOW = new Color(244, 248, 251);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(208, 225, 249);
    private static final Color TEXT_DARK = new Color(33, 37, 41);

    // Table Soft Indicator Colors
    private static final Color COMPLETED_BG = new Color(232, 248, 245); // Soft light green
    private static final Color PENDING_BG = new Color(253, 237, 236);   // Soft light red
    private static final Color SELECTED_BG = new Color(179, 229, 252);  // Soft sky blue selection

    public TodoGUI() {
        this.todoDAO = new TodoDAO();
        initializeComponents();
        setupLayout();
        loadTodos();
        setupEventListner();
    }

    private void initializeComponents() {
        setTitle("Todo List Application");
        setSize(850, 650);
        setMinimumSize(new Dimension(750, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_WINDOW);

        titleField = new JTextField(22);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 208, 235), 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        descriptionArea = new JTextArea(4, 22);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        completedCheckBox = new JCheckBox("Completed");
        completedCheckBox.setFont(new Font("Segoe UI", Font.BOLD, 13));
        completedCheckBox.setBackground(CARD_BG);
        completedCheckBox.setForeground(TEXT_DARK);
        completedCheckBox.setFocusPainted(false);

        addButton = createStyledButton("Add Task", PRIMARY_SKY_BLUE, Color.WHITE);
        updateButton = createStyledButton("Update", new Color(0, 150, 136), Color.WHITE);
        deleteButton = createStyledButton("Delete", new Color(229, 115, 115), Color.WHITE);
        refreshButton = createStyledButton("Refresh", new Color(120, 144, 156), Color.WHITE);

        String[] filterOptions = { "All", "Completed", "Pending" };
        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterComboBox.setBackground(Color.WHITE);
        filterComboBox.setPreferredSize(new Dimension(130, 30));

        String[] columnNames = { "ID", "Title", "Description", "Completed", "Created At", "Updated At" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        todoTable = new JTable(tableModel);
        todoTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        todoTable.setRowHeight(32);
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.setShowGrid(true);
        todoTable.setGridColor(new Color(230, 235, 240));

        JTableHeader header = todoTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(212, 230, 241));
        header.setForeground(TEXT_DARK);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 34));

        TodoTableCellRenderer renderer = new TodoTableCellRenderer();
        for (int i = 0; i < todoTable.getColumnCount(); i++) {
            todoTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        todoTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        todoTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        todoTable.getColumnModel().getColumn(2).setPreferredWidth(240);
        todoTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        todoTable.getColumnModel().getColumn(4).setPreferredWidth(130);
        todoTable.getColumnModel().getColumn(5).setPreferredWidth(130);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1, true),
                BorderFactory.createEmptyBorder(7, 16, 7, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));

        JPanel headerBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        headerBanner.setBackground(PRIMARY_SKY_BLUE);
        JLabel mainTitleLabel = new JLabel("Todo List Application");
        mainTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainTitleLabel.setForeground(Color.WHITE);
        headerBanner.add(mainTitleLabel);

        JPanel inputCard = new JPanel(new GridBagLayout());
        inputCard.setBackground(CARD_BG);
        inputCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(TEXT_DARK);
        inputCard.add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        inputCard.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        descriptionLabel.setForeground(TEXT_DARK);
        inputCard.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 208, 235), 1, true));
        inputCard.add(descriptionScrollPane, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputCard.add(completedCheckBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        filterPanel.setOpaque(false);
        JLabel filterLabel = new JLabel("Filter Status:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        filterLabel.setForeground(TEXT_DARK);
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);

        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setBackground(BG_WINDOW);
        topContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        topContainer.add(inputCard, BorderLayout.CENTER);

        JPanel topSubPanel = new JPanel(new BorderLayout());
        topSubPanel.setOpaque(false);
        topSubPanel.add(buttonPanel, BorderLayout.CENTER);
        topSubPanel.add(filterPanel, BorderLayout.EAST);

        topContainer.add(topSubPanel, BorderLayout.SOUTH);

        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.add(headerBanner, BorderLayout.NORTH);
        northWrapper.add(topContainer, BorderLayout.CENTER);

        add(northWrapper, BorderLayout.NORTH);

        JScrollPane tabJScrollPane = new JScrollPane(todoTable);
        tabJScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 15, 15, 15),
                BorderFactory.createLineBorder(BORDER_COLOR, 1)
        ));
        tabJScrollPane.getViewport().setBackground(Color.WHITE);
        add(tabJScrollPane, BorderLayout.CENTER);
    }

    private void setupEventListner() {
        addButton.addActionListener((e) -> addTodo());
        updateButton.addActionListener((e) -> updateTodo());
        deleteButton.addActionListener((e) -> deleteTodo());
        refreshButton.addActionListener((e) -> refreshTodo());
        filterComboBox.addActionListener((e) -> filterTodo());
        todoTable.getSelectionModel().addListSelectionListener((e) -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedTodo();
            }
        });
    }

    private void addTodo() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        boolean completed = completedCheckBox.isSelected();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Title cannot be empty",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setCompleted(completed);
        todo.setCreated_at(LocalDateTime.now());
        todo.setUpdated_at(LocalDateTime.now());
        try {
            todoDAO.addTodo(todo);
            titleField.setText("");
            descriptionArea.setText("");
            completedCheckBox.setSelected(false);
            loadTodos();
            JOptionPane.showMessageDialog(this, "Todo added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding todo: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTodo() {
        int selectedRow = todoTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a todo to update",
                    "Selection Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        int tableId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String tableTitle = (String) tableModel.getValueAt(selectedRow, 1);
        String tableDescription = (String) tableModel.getValueAt(selectedRow, 2);
        Boolean tableCompletedObj = (Boolean) tableModel.getValueAt(selectedRow, 3);
        boolean tableCompleted = tableCompletedObj != null ? tableCompletedObj : false;
        LocalDateTime tableCreatedAt = (LocalDateTime) tableModel.getValueAt(selectedRow, 4);

        String uiTitle = titleField.getText().trim();
        String uiDescription = descriptionArea.getText().trim();
        boolean uiCompleted = completedCheckBox.isSelected();

        if (uiTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Title cannot be empty",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Objects.equals(tableTitle, uiTitle) &&
                Objects.equals(tableDescription, uiDescription) &&
                tableCompleted == uiCompleted) {
            JOptionPane.showMessageDialog(this,
                    "No changes detected to update",
                    "Update Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Todo todo = new Todo();
        todo.setId(tableId);
        todo.setTitle(uiTitle);
        todo.setDescription(uiDescription);
        todo.setCompleted(uiCompleted);
        todo.setCreated_at(tableCreatedAt != null ? tableCreatedAt : LocalDateTime.now());
        todo.setUpdated_at(LocalDateTime.now());
        try {
            boolean status = todoDAO.updateTodo(todo);
            if (status) {
                loadTodos();
                JOptionPane.showMessageDialog(this,
                        "Todo updated successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this,
                    "Error updating todo",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating todo: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTodo() {
        int selectedRow = todoTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a todo to delete",
                    "Selection Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        int tableId = (Integer) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this todo?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean status = todoDAO.deleteTodo(tableId);
                if (status) {
                    titleField.setText("");
                    descriptionArea.setText("");
                    completedCheckBox.setSelected(false);
                    loadTodos();
                    JOptionPane.showMessageDialog(this,
                            "Todo deleted successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting todo",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting todo: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshTodo() {
        titleField.setText("");
        descriptionArea.setText("");
        completedCheckBox.setSelected(false);
        todoTable.clearSelection();
        filterComboBox.setSelectedIndex(0);
        loadTodos();
    }

    private void filterTodo() {
        tableModel.setRowCount(0);
        String filter = (String) filterComboBox.getSelectedItem();
        try {
            List<Todo> todos = todoDAO.getAllTodos();
            for (Todo todo : todos) {
                if ("Completed".equals(filter) && !todo.isCompleted()) {
                    continue;
                }
                if ("Pending".equals(filter) && todo.isCompleted()) {
                    continue;
                }
                Object[] rowData = {
                        todo.getId(),
                        todo.getTitle(),
                        todo.getDescription(),
                        todo.isCompleted(),
                        todo.getCreated_at(),
                        todo.getUpdated_at()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching todos: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedTodo() {
        int selectedRow = todoTable.getSelectedRow();
        if (selectedRow != -1) {
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String description = (String) tableModel.getValueAt(selectedRow, 2);
            Boolean completed = (Boolean) tableModel.getValueAt(selectedRow, 3);
            titleField.setText(title != null ? title : "");
            descriptionArea.setText(description != null ? description : "");
            completedCheckBox.setSelected(completed != null ? completed : false);
        }
    }

    private void loadTodos() {
        filterTodo();
    }

    private static class TodoTableCellRenderer extends DefaultTableCellRenderer {
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Object completedValue = table.getModel().getValueAt(row, 3);
            boolean isCompleted = completedValue instanceof Boolean && (Boolean) completedValue;

            if (isSelected) {
                c.setBackground(SELECTED_BG);
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(isCompleted ? COMPLETED_BG : PENDING_BG);
                c.setForeground(TEXT_DARK);
            }

            if (column == 3) {
                if (value instanceof Boolean) {
                    setText((Boolean) value ? "Completed" : "Pending");
                }
            } else if (value instanceof LocalDateTime) {
                setText(((LocalDateTime) value).format(DATE_FORMATTER));
            }

            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            return c;
        }
    }
}
