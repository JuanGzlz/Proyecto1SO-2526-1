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
    
    /**
     * Validates the instructions text area for I/O Bound processes
     */
    private boolean validateInstructions() {
        // If CPU Bound, no instructions needed
        if (typeComboBox.getSelectedIndex() == 1) {
            instructions = new List<>();
            return true;
        }
        
        // For I/O Bound, validate instructions
        String input = instructionsTextArea.getText().trim();
        
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "I/O Bound processes must have instructions.\nFormat: position1,duration1,position2,duration2,...", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        instructions = new List<>();
        StringBuilder numberBuilder = new StringBuilder();

        try {
            for (int i = 0; i < input.length(); i++) {
                char currentChar = input.charAt(i);

                if (currentChar == ',') {
                    if (numberBuilder.length() > 0) {
                        int number = Integer.parseInt(numberBuilder.toString().trim());
                        if (number <= 0) {
                            JOptionPane.showMessageDialog(this, 
                                "All instruction values must be greater than 0", 
                                "Validation Error", 
                                JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        instructions.appendLast(number);
                        numberBuilder.setLength(0);
                    }
                } else if (Character.isDigit(currentChar)) {
                    numberBuilder.append(currentChar);
                } else if (!Character.isWhitespace(currentChar)) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid character found: '" + currentChar + "'\nOnly numbers and commas are allowed", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            // Add the last number
            if (numberBuilder.length() > 0) {
                int number = Integer.parseInt(numberBuilder.toString().trim());
                if (number <= 0) {
                    JOptionPane.showMessageDialog(this, 
                        "All instruction values must be greater than 0", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                instructions.appendLast(number);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Instructions must contain valid integers", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Verify even number of values (position, duration pairs)
        if (instructions.getSize() % 2 == 1) {
            JOptionPane.showMessageDialog(this, 
                "Instructions must be in pairs (position, duration).\nYou have " + instructions.getSize() + " values.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check if even-indexed values (positions) are in ascending order
        for (int i = 2; i < instructions.getSize(); i += 2) {
            if ((int)instructions.getNodoById(i).getValue() <= (int) instructions.getNodoById(i - 2).getValue()) {
                JOptionPane.showMessageDialog(this, 
                    "Instruction positions must be in ascending order", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }
}