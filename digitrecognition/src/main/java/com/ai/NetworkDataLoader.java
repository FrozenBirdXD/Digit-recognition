package com.ai;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class NetworkDataLoader {

    private static final String TRAIN_IMAGES_FILE = "digitrecognition\\src\\train-images-idx3-ubyte.gz";
    private static final String TRAIN_LABELS_FILE = "digitrecognition\\src\\train-labels-idx1-ubyte.gz";
    private static final String TEST_IMAGES_FILE = "digitrecognition\\src\\t10k-images-idx3-ubyte.gz";
    private static final String TEST_LABELS_FILE = "digitrecognition\\src\\t10k-labels-idx1-ubyte.gz";
    private static final int IMAGE_MAGIC_NUMBER = 2051;
    private static final int LABEL_MAGIC_NUMBER = 2049;

    public static void main(String[] args) throws IOException {
        // Load training data
        double[][] trainingImages = loadData(TRAIN_IMAGES_FILE);
        int[] trainingLabels = loadLabels(TRAIN_LABELS_FILE);

        // Load test data
        double[][] testImages = loadData(TEST_IMAGES_FILE);
        int[] testLabels = loadLabels(TEST_LABELS_FILE);

        // print statistics about the data
        System.out.println("Training images: " + trainingImages.length);
        System.out.println("Test images: " + testImages.length);
        System.out.println("Image size: " + trainingImages[0].length);
        System.out.println("Number of classes(different label values): " + countClasses(trainingLabels));

        // SimpleNetwork network = new SimpleNetwork(784, 200, 10, 0.2);
        // // train neural network with the training data
        // network.fit(trainingImages, oneHotEncode(trainingLabels), 20000);

        // // network.readParams();
        // // network.saveParams();

        // // predicts the first 50 images and prints them
        // for (int i = 0; i < 50; i++) {
        //     List<Double> result = network.predict(testImages[i]);
        //     System.out.println(result.toString());
        //     System.out.println(testLabels[i]);
        // }

        // // overall evaluation
        // int numCorrect = 0;
        // int numTested = 0;
        // for (int i = 0; i < testLabels.length; i++) {
        //     if (network.getPredictionInt(network.predict(testImages[i])) == testLabels[i]) {
        //         numCorrect++;
        //         numTested++;
        //     } else {
        //         numTested++;
        //     }
        // }

        // System.out.println("Amount of correctly identified digits: " + numCorrect);
        // System.out.println("Amount of tested digits: " + numTested);
        // System.out.println("Percentage correct: " + numCorrect / (double) numTested);
        // System.out.println();
        // System.out.println(network.getPredictionInt(network.predict(testImages[0])));
        // System.out.println(testLabels[0]);

        // find the optimal hyperparameters
        // list with each parameter to test

        double[] learningRates = {0.01, 0.02, 0.05, 0.1, 0.15, 0.2};
        int[] hiddenNeurons = {16, 64, 128, 256};
        int[] epochs = {5000, 10000, 15000, 20000};

        // double[] learningRates = {0.01, 0.02};
        // int[] hiddenNeurons = {16, 100};
        // int[] epochs = {5000};

        List<Double> accuracyList = new ArrayList<>();
        List<Double> lossList = new ArrayList<>();
        List<double[]> precisionList = new ArrayList<>();

        double biggestAccuracy = 0;
        double[] bestParams = new double[3];

        for (double learningRate : learningRates) {
            for (int hiddenNeuron : hiddenNeurons) {
                for (int epoch : epochs) {
                    // initializes and train your neural network with the current hyperparameters
                    SimpleNetwork network = new SimpleNetwork(784, hiddenNeuron, 10, learningRate);
                    network.fit(trainingImages, oneHotEncode(trainingLabels), epoch);
        
                    // records the metrics for the current hyperparameters
                    double accuracy = network.calculateAccuracy(testImages, testLabels);
                    double loss = network.calculateLoss(testImages, oneHotEncode(testLabels));
                    double[] precision = network.calculatePrecision(testImages, testLabels);
        
                    // adds the metrics to the lists
                    accuracyList.add(accuracy);
                    lossList.add(loss);
                    precisionList.add(precision);

                    if (accuracy > biggestAccuracy) {
                        // network.saveParams();
                        biggestAccuracy = accuracy;
                        bestParams[0] = learningRate;
                        bestParams[1] = hiddenNeuron;
                        bestParams[2] = epoch;
                    }

                    // prints the result of each hyperparameter
                    System.out.println("Learning rate: " + learningRate + ", Hidden neurons: " + hiddenNeuron + ", Epochs: " + epoch + ", Accuracy: " + accuracy + ", Loss: " + loss);
                    System.out.println("Precision: " + Arrays.toString(precision));
                }
            }
        }
        System.out.println(biggestAccuracy);
        System.out.println("Best params: " + Arrays.toString(bestParams));
        System.out.println(accuracyList);
        System.out.println(lossList);
        System.out.println(precisionList);
    }

    // load the training data from the file
    private static double[][] loadData(String fileName) throws IOException {
        try (DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))) {
            int magicNumber = in.readInt();
            // checks if magic number is correct
            if (magicNumber != IMAGE_MAGIC_NUMBER) {
                System.err.println("Invalid magic number for image file: " + magicNumber);
            }

            // read 'metadata' from file
            int numImages = in.readInt();
            int numRows = in.readInt();
            int numCols = in.readInt();

            // store imagedata in 2d array
            double[][] images = new double[numImages][numRows * numCols];
            for (int i = 0; i < numImages; i++) {
                for (int j = 0; j < numRows * numCols; j++) {
                    images[i][j] = in.readUnsignedByte() / 255.0; // normalize pixel values to [0, 1]
                }
            }

            return images;
        }
    }

    // read the training data labels from the file
    private static int[] loadLabels(String fileName) throws IOException {
        try (DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))) {
            int magicNumber = in.readInt();
            // checks if magic number is correct
            if (magicNumber != LABEL_MAGIC_NUMBER) {
                System.err.println("Invalid magic number for label file: " + magicNumber);
            }

            // read 'metadata' from file
            int numLabels = in.readInt();

            // store labels in array 
            int[] labels = new int[numLabels];
            for (int i = 0; i < numLabels; i++) {
                labels[i] = in.readUnsignedByte();
            }

            return labels;
        }
    }

    // count how many different labels there are
    private static int countClasses(int[] labels) {
        int max = -1;
        for (int label : labels) {
            if (label > max) {
                max = label;
            }
        }
        return max + 1;
    }

    // make label data usable for the neural network
    private static double[][] oneHotEncode(int[] labels) {
        int numLabels = labels.length;
        int numClasses = countClasses(labels);
        double[][] encodedLabels = new double[numLabels][numClasses];
        for (int i = 0; i < numLabels; i++) {
            encodedLabels[i][labels[i]] = 1.0;
        }
        return encodedLabels;
    }
}
