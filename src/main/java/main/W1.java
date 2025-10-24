package main;

import primitivas.*;
import classes.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public class W1 extends javax.swing.JFrame {
    public Semaphore onPlay;
    public Semaphore onPlayClock;
    public List readyList;
    public List allProcessList;
    public UtilityGraph w2;
    private Dispatcher dispatcher;
    private MetricsCollector metrics;
    
    private void loadConfig() {
        String filePath = "configuracion.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();

            if (line != null) {
                String[] values = line.split(",");
                if (values.length < 3) {
                    System.out.println("Error: El archivo de configuración no tiene el formato correcto.");
                    return;
                }

                int selectedAlgorithm = Integer.parseInt(values[0]);
                int numberOfInstructions = Integer.parseInt(values[1]);
                int quantum = Integer.parseInt(values[2]);

                selectDispatcher.setSelectedIndex(selectedAlgorithm);
                timeSlider.setValue(numberOfInstructions);
                quantumSlider.setValue(quantum);
                this.instructionTime.setText(this.timeSlider.getValue() + " ms");
                this.quantumLabel.setText("Quantum: " + this.quantumSlider.getValue());
                System.out.println("Configuración cargada desde CSV.");
            }
        } catch (IOException e) {
            System.out.println("No se encontró el archivo de configuración. Se usarán valores por defecto.");
        }
    }
    
    public W1(Semaphore onPlay, Semaphore onPlay1, List readyList, List allProcess, MetricsCollector metrics) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.onPlay = onPlay;
        this.onPlayClock = onPlay1;
        this.readyList = readyList;
        this.allProcessList = allProcess;
        this.metrics = metrics;
        w2 = new UtilityGraph("CPU usage");
        
        customizeComponents();
        loadConfig();
        this.updatePCBs();
        this.updateMetrics(metrics);
    }

    public W1() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        customizeComponents();
    }
}