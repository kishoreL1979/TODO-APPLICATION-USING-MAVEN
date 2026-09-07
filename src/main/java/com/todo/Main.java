package com.todo;

import javax.swing.SwingUtilities;
import com.todo.gui.TodoGUI;

public class Main {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            TodoGUI app = new TodoGUI();
            app.setVisible(true);
        });
    }
}

