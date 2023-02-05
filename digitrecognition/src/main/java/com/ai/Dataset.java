package com.ai;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Dataset {
    private static final int LABEL_MAGIC_NUMBER = 2049;
    private static final int IMAGE_MAGIC_NUMBER = 2051;
    
    // reads the images from a file
    public static double[][] readImages(String fileName) throws IOException {
        try (DataInputStream input = new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))) {
            // if the magic number is correct
            int magic = input.readInt();
            if (magic != IMAGE_MAGIC_NUMBER) {
                throw new IOException("Invalid magic number in file");
            }

            int numImages = input.readInt();
            int numRows = input.readInt();
            int numColums = input.readInt();

            // creates a two dimensional array of Integers with the first index corresponding to each image and the second one to the data of that image
            int[][] images = new int[numImages][numRows * numColums];
            for (int i = 0; i < numImages; i++) {
                for (int j = 0; j < numRows * numColums; j++) {
                    images[i][j] = input.readUnsignedByte();
                }
            }
            input.close();
            // convert int[][] to double[][]
            double[][] imagesDouble = convertToDouble(images);
            return imagesDouble;
        }
    }

    // converts int[][] to double[][]
    private static double[][] convertToDouble(int[][] dataInt) {
        double[][] dataDouble = new double[dataInt.length][dataInt[0].length];
        for (int i = 0; i < dataInt.length; i++) {
            for (int j = 0; j < dataInt[i].length; j++) {
                dataDouble[i][j] = (double) dataInt[i][j];
            }
        }
        return dataDouble;
    }
    
    // converts int[] to double[]
    private static double[] convertToDouble(int[] dataInt) {
        double[] dataDouble = new double[dataInt.length];
        int counter = 0;
        for (int number : dataInt) {
            dataDouble[counter] = number;
            counter++;
        }
        return dataDouble;
    }
    
    // returns the data from a single image in an array
    public double[] getSingleImage(double[][] allImages, int index) {
        double[] singleImage = allImages[index];
        return singleImage;
    }

    // prints the image with the given data
    public void printImage(double[] imageDataSingle) {
        int counter = 0;
        for (double number : imageDataSingle) {
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
    }

    // returns the label for a given image
    public double getSingleLabel(double[] allLabels, int index) {
        double singleLabel = allLabels[index];
        return singleLabel;
    }
    
    // reads the label from a file
    public static double[] readLabels(String fileName) throws IOException {
        try (DataInputStream input = new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))) {
            // if the magic number is correct
            int magic = input.readInt();
            if (magic != LABEL_MAGIC_NUMBER) {
                throw new IOException("Invalid magic number in file!");
            }
            
            int numLabels = input.readInt();
            // creates an array with the index corresponding to the image
            int[] labels = new int[numLabels];
            for (int i = 0; i < numLabels; i++) {
                labels[i] = input.readByte();
            }
            input.close();
            // converts int[] to double[]
            double[] labelsDouble = convertToDouble(labels);
            return labelsDouble;
        }
    }
}
