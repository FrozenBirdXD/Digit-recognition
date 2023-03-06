package com.ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

public class App {

    private final static String TRAIN_IMAGES_URL = "digitrecognition\\src\\train-images-idx3-ubyte.gz";
    private final static String TRAIN_LABELS_URL = "digitrecognition\\src\\train-labels-idx1-ubyte.gz";
    public static void main(String[] args) {
        
        /* double[] input = {23.6, 235.1, 163.6, 134.6, 233.7};
        SimpleMatrix sampleInput = new SimpleMatrix(input);
        System.out.println(network.calculateOutput(sampleInput)); */
        List<Integer> sizes = new ArrayList<>();
        sizes.add(5);
        sizes.add(3);
        sizes.add(10);
        
        Dataset data = new Dataset();
        int index = 6;
        
        try {
            double[][] imageData = data.readImages(TRAIN_IMAGES_URL);
            double[] imageDataSingle = data.getSingleImage(imageData, index);
            double[] labelData = data.readLabels(TRAIN_LABELS_URL);
            double labelDataSingle = data.getSingleLabel(labelData, index);
            data.printImage(imageDataSingle);
            System.out.println("label: " + labelDataSingle);

            // SimpleMatrix sampleInput = new SimpleMatrix(imageDataSingle);
            // System.out.println(network.calculateOutput(sampleInput));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
