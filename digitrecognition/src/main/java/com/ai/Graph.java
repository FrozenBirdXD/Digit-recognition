package com.ai;

import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graph {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("training.txt"));
        // Create a list to hold the data
        List<DataPoint> data = new ArrayList<>();
        
        for (int i = 0; i < 297; i++) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(", ");
            
            String learningRate = lineScanner.next().split(": ")[1];
            String hiddenNeurons = lineScanner.next().split(": ")[1];
            String epochs = lineScanner.next().split(": ")[1];
            String accuracy = lineScanner.next().split(": ")[1];
            String loss = lineScanner.next().split(": ")[1];
            
            data.add(new DataPoint(Double.parseDouble(learningRate), Integer.parseInt(hiddenNeurons), Integer.parseInt(epochs), Double.parseDouble(accuracy), Double.parseDouble(loss)));
            
            lineScanner.close();
        }
        
        XYDataset datasetLossOverAcc = createDatasetLossOverAcc(data);
        JFreeChart chartLossOverAcc = createLossOverAcc(datasetLossOverAcc);
        ChartPanel chartPanelLossOverAcc = new ChartPanel(chartLossOverAcc);
        
        JFrame frame = new JFrame("Scatter Plot LossOverAcc");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanelLossOverAcc);
        frame.pack();
        frame.setVisible(true);

        XYDataset datasetAccOverEpochs = createDatasetAccOverEpochs(data);
        JFreeChart chartAccOverEpochs = createAccOverEpochs(datasetAccOverEpochs);
        ChartPanel chartPanelAccOverEpochs = new ChartPanel(chartAccOverEpochs);

        JFrame frame1 = new JFrame("Scatter Plot AccOverEpochs");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.add(chartPanelAccOverEpochs);
        frame1.pack();
        frame1.setVisible(true);
    }
    
    private static XYDataset createDatasetLossOverAcc(List<DataPoint> dataPoints) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        XYSeries series = new XYSeries("Training Results");
        for (DataPoint dataPoint : dataPoints) {
            series.add(dataPoint.getLoss(), dataPoint.getAccuracy());
        }
        dataset.addSeries(series);
        
        return dataset;
    }
    
    private static JFreeChart createLossOverAcc(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createScatterPlot(
        "Training Results", // chart title
        "Loss", // x axis label
        "Accuracy", // y axis label
        dataset, // data
        PlotOrientation.VERTICAL,
        true, // include legend
        true, // tooltips
        false // urls
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        return chart;
    }

    private static JFreeChart createAccOverEpochs(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createScatterPlot(
            "Accuracy over Epochs", // chart title
            "Epochs", // x axis label
            "Accuracy", // y axis label
            dataset, // data
            PlotOrientation.VERTICAL,
            true, // include legend
            true, // tooltips
            false // urls
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        return chart;
    }
    
    private static XYDataset createDatasetAccOverEpochs(List<DataPoint> dataPoints) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        XYSeries series = new XYSeries("Accuracy");
        for (DataPoint dataPoint : dataPoints) {
            series.add(dataPoint.getEpochs(), dataPoint.getAccuracy());
        }
        dataset.addSeries(series);
        
        return dataset;
    }
}

class DataPoint {
    private final double learningRate;
    private final int hiddenNeurons;
    private final int epochs;
    private final double accuracy;
    private final double loss;
    
    public DataPoint(double learningRate, int hiddenNeurons, int epochs, double accuracy, double loss) {
        this.learningRate = learningRate;
        this.hiddenNeurons = hiddenNeurons;
        this.epochs = epochs;
        this.accuracy = accuracy;
        this.loss = loss;
    }
    
    public double getLearningRate() {
        return learningRate;
    }
    
    public int getHiddenNeurons() {
        return hiddenNeurons;
    }
    
    public int getEpochs() {
        return epochs;
    }
    
    public double getAccuracy() {
        return accuracy;
    }
    
    public double getLoss() {
        return loss;
    }
}
