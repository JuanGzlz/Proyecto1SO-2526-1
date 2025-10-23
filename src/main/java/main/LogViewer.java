package main;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogViewer extends javax.swing.JFrame {
    
    public LogViewer() {
        initComponents();
        setLocationRelativeTo(null);
        loadLog();
    }
    
    private void loadLog() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("simulation_log.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            logTextArea.setText(content.toString());
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        } catch (IOException e) {
            logTextArea.setText("Error loading log file: " + e.getMessage());
        }
    }
}