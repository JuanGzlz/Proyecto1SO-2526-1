/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Juan I Gonzalez
 */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.awt.Color;

public class UtilityGraph extends JFrame {
    private DefaultCategoryDataset dataset;

    public UtilityGraph(String title) {
        super(title);
        dataset = createDataset();
        
        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Number of Instructions Executed per CPU",
                "CPU",
                "Number of Instructions",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        
        CategoryPlot plot = chart.getCategoryPlot();
        StackedBarRenderer renderer = new StackedBarRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);
        plot.setRenderer(renderer);
        
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    
}
