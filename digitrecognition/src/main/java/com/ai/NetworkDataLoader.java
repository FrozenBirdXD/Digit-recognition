package com.ai;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
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

        SimpleNetwork network = new SimpleNetwork(784, 100, 10, 0.01);
        // train neural network with the training data
        network.fit(trainingImages, oneHotEncode(trainingLabels), 15000);

        network.saveParams();

        // predicts the first 30 images
        for (int i = 0; i < 30; i++) {
            List<Double> result = network.predict(trainingImages[i]);
            System.out.println(result.toString());
            System.out.println(trainingLabels[i]);
        }

    }

    // load the training data from the file
    private static double[][] loadData(String fileName) throws IOException {
        try (DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))) {
            int magicNumber = in.readInt();
            // checks if magic number is correct
            if (magicNumber != IMAGE_MAGIC_NUMBER) {
                throw new IOException("Invalid magic number for image file: " + magicNumber);
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
                throw new IOException("Invalid magic number for label file: " + magicNumber);
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
