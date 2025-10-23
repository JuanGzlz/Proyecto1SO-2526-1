package main;

import primitivas.*;
import classes.ProcessImage;
import classes.ProcessImageCSV;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

public class CreateProcess extends javax.swing.JFrame {

    public List<Integer> instructions;
    public W1 father;
    
    public CreateProcess(W1 w1) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        father = w1;
        instructions = new List();
        
        // Set default state
        instructionsTextArea.setEnabled(false);
        typeComboBox.setSelectedIndex(1); // Default to CPU Bound
    }
    
    public CreateProcess() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
    
    /**
     * Checks if the ID already exists in procesos.csv
     */
    private boolean isIdUnique(int id) {
        Set<Integer> existingIds = new HashSet<>();
        String filePath = "procesos.csv";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] values = line.split(",");
                if (values.length > 0) {
                    try {
                        int existingId = Integer.parseInt(values[0]);
                        existingIds.add(existingId);
                    } catch (NumberFormatException e) {
                        // Skip invalid lines
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read procesos.csv: " + e.getMessage());
        }
        
        return !existingIds.contains(id);
    }
    
    /**
     * Validates the ID field
     */
    private boolean validateId() {
        String idText = idTextField.getText().trim();
        
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Process ID cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            int id = Integer.parseInt(idText);
            
            if (id < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Process ID must be a positive number", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (!isIdUnique(id)) {
                JOptionPane.showMessageDialog(this, 
                    "Process ID " + id + " already exists. Please use a unique ID.", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            return true;
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Process ID must be a valid integer", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Validates the name field
     */
    private boolean validateName() {
        String name = nameTextField.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Process Name cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * Validates the duration field
     */
    private boolean validateDuration() {
        String durationText = durationTextField.getText().trim();
        
        if (durationText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Duration cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            int duration = Integer.parseInt(durationText);
            
            if (duration <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Duration must be greater than 0", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            return true;
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Duration must be a valid integer", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}