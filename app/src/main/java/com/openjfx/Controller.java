package com.openjfx;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    private Canvas canvas;
    @FXML
    private Canvas canvas2;
    @FXML
    private Button clearButton;
    @FXML
    private BarChart chart;

    private WritableImage snapshot;

    private void updateChart(List<Double> result) {
        chart.getData().clear();
        XYChart.Series sr = new XYChart.Series();
        for (int i = 0; i < 10; i++) {
            sr.getData().add(new XYChart.Data(String.valueOf(i), result.get(i) * 100));
        }
        chart.getData().add(sr);
    }

    public void drawDigit() {
        // when the mouse it pressed on the canvas a circle is drawn
        this.canvas.setOnMousePressed(event -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.beginPath();
            gc.moveTo(event.getX(), event.getY());
            gc.setFill(Color.BLACK);
            gc.fillOval(event.getX(), event.getY(), 15, 15);
        });
        
        // when the mouse is dragged over the canvas a line is drawn
        this.canvas.setOnMouseDragged(event -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.lineTo(event.getX(), event.getY());
            gc.fillOval(event.getX(), event.getY(), 15, 15);

        });

        // when the mouse is released from the canvas a snapshot is taken
        // and feed to the neural network
        this.canvas.setOnMouseReleased(event -> {
            System.out.println("lijh");

            snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);

            snapshot = canvas.snapshot(params, null);

            ImageView imageView = new ImageView(snapshot);
            imageView.setPreserveRatio(true);

            snapshot = downsampleImage(imageView.snapshot(null, null));

            PixelReader reader = snapshot.getPixelReader();

            double[] pixels = new double[28 * 28];

            int index = 0;
            for (int y = 0; y < 28; y++) {
                for (int x = 0; x < 28; x++) {
                    Color color = reader.getColor(x, y);
                    double red = color.getRed() * 255;
                    double green = color.getGreen() * 255;
                    double blue = color.getBlue() * 255;
                    double grayValue = 0.21 * red + 0.72 * green + 0.07 * blue;
                    pixels[index++] = (int) Math.round(255 - grayValue);
                }
            }

            SimpleNetwork network = new SimpleNetwork(784, 256, 10);
            try {
                network.readParams();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Double> result = network.predict(pixels);
            System.out.println(result.toString());
            System.out.println(network.getPredictionInt(result));

            writeNum(network.getPredictionInt(result));

            int counter = 0;
            for (double number : pixels) {
                // start a new line every 28 pixels
                if (counter % 28 == 0) {
                    System.out.println("");
                }
                if (number == 0.0) {
                    System.out.print("  ");
                } else {
                    System.out.print("■■");
                }
                counter++;
            }
            System.out.println("");

            for (double n : pixels) {
                System.out.print(n + " ");
            }
            System.out.println("");

            updateChart(result);
        });      
    }

    private WritableImage downsampleImage(WritableImage image) {
        int width = 28;
        int height = 28;
        int blockWidth = 200 / width;
        int blockHeight = 200 / height;
    
        WritableImage downsampled = new WritableImage(width, height);
        PixelWriter writer = downsampled.getPixelWriter();
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double redSum = 0;
                double greenSum = 0;
                double blueSum = 0;
                double alphaSum = 0;
    
                // iterates over the corresponding 7x7 block in the original image
                for (int blockY = y * blockHeight; blockY < (y + 1) * blockHeight; blockY++) {
                    for (int blockX = x * blockWidth; blockX < (x + 1) * blockWidth; blockX++) {
                        Color color = image.getPixelReader().getColor(blockX, blockY);
                        redSum += color.getRed();
                        greenSum += color.getGreen();
                        blueSum += color.getBlue();
                        alphaSum += color.getOpacity();
                    }
                }
    
                // calculates the average color value of the 7x7 block
                double blockSize = blockWidth * blockHeight;
                double red = redSum / blockSize;
                double green = greenSum / blockSize;
                double blue = blueSum / blockSize;
                double alpha = alphaSum / blockSize;
    
                // sets the pixel in the downsampled image to the average color value
                writer.setColor(x, y, new Color(red, green, blue, alpha));
            }
        }
    
        return downsampled;
    }
    
    // test
    private void writeNum(int number) {
        GraphicsContext gc = canvas2.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font("Arial", 200));
        gc.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());  
        gc.fillText(String.valueOf(number), 50, 170);
    }

    @FXML
    private void onClearButtonClick() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());     
    }
}
