package com.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;

public class Network {
    private int numLayers;
    private List<Integer> sizes;
    private List<SimpleMatrix> biases;
    private List<SimpleMatrix> weights;

    public Network(List<Integer> sizes) {
        this.numLayers = sizes.size();
        this.sizes = sizes;
        this.biases = initBiases(sizes);
        this.weights = initWeights(sizes);
    }

    // sets all of the biases for every neuron except for the input layer
    private List<SimpleMatrix> initBiases(List<Integer> sizes) {
        // creates an array of the type SimpleMatrix (a two dimentional matrix)
        // biasesArray stores all the biases of the neurons
        SimpleMatrix[] biasesArray = new SimpleMatrix[sizes.size() - 1];
        Random rand = new Random();

        // interates over the layers of the network excluding the input layer
        for (int i = 1; i < sizes.size(); i++) {
            // creates a two dimensional SimpleMatrix object with 'sizes.get(i)' rows and 1 column
            SimpleMatrix bias = new SimpleMatrix(sizes.get(i), 1);
            // interates over the rows of the matrix == the neurons
            for (int j = 0; j < bias.numRows(); j++) {
                bias.set(j, 0, rand.nextGaussian());
            }
            biasesArray[i - 1] = bias;
        }
        return Arrays.asList(biasesArray);
    }

    // sets all of the weights of the connections between the neurons
    private List<SimpleMatrix> initWeights(List<Integer> sizes) {
        // creates an array of the type SimpleMatrix (a two dimentional matrix)
        // biasesArray stores all of the weights
        SimpleMatrix[] weightsArray = new SimpleMatrix[sizes.size() - 1];
        Random random = new Random();

        // interates over the layers of the network
        for (int i = 0; i < sizes.size() - 1; i++) {
            // creates a two dimensional SimpleMatrix object with 'sizes.get(i + 1)' rows and 'sizes.get(i)' columns
            SimpleMatrix weights = new SimpleMatrix(sizes.get(i + 1), sizes.get(i));
            // interates over the rows of the matrix
            for (int j = 0; j < weights.numRows(); j++) {
                // interates over the columns of the matrix
                for (int k = 0; k < weights.numCols(); k++) {
                    weights.set(j, k, random.nextGaussian());
                }
            }
            weightsArray[i] = weights;
        }
        return Arrays.asList(weightsArray);
    }

    // applies the sigmoid function to every element in the matrix
    private SimpleMatrix sigmoid(SimpleMatrix input) {
        // create Matrix with same dimensions as input
        SimpleMatrix output = new SimpleMatrix(input.numRows(), input.numCols());
        // iterates over all of the rows and colums and applies the sigmoid function to every element in the matrix
        for (int i = 0; i < input.numRows(); i++) {
            for (int j = 0; j < input.numCols(); j++) {
                output.set(i, j, 1 / (1 + Math.exp(-input.get(i, j))));     // sigmoid function
            }
        }
        return output;
    }

    // calculates the output of the neural network for a given training data
    private SimpleMatrix feedForward(SimpleMatrix trainingData) {
        // repeats for each layer of the network
        for (int i = 0; i < numLayers - 1; i++) {
            // gets the current biases and weights
            SimpleMatrix biases = this.biases.get(i);
            SimpleMatrix weights = this.weights.get(i);
            // calculates the outputs of all the neurons of a specific layer
            // the output of a single neuron is:
            // sigmoid(Σj(weightsj * inputsj - bias))
            // here the outputs are calculated with vectors
            trainingData = sigmoid(weights.mult(trainingData).plus(biases));
        }
        return trainingData;
    }

    // tests the feedForward method
    public List<SimpleMatrix> calculateOutput(SimpleMatrix input) {
        SimpleMatrix output = feedForward(input);
        return Arrays.asList(output);
    }

    
    public void SGD(List<SimpleMatrix> trainingData, int epochs, int miniBatchSize, double learningRate, List<SimpleMatrix> testData) {
        /* 
        int nTest = 0;
        if (testData != null) {
            nTest = testData.size();
        }
        */
        int n = trainingData.size();

        // iterates over each epoch
        for (int j = 0; j < epochs; j++) {
            // shuffle the training Data
            Collections.shuffle(trainingData);

            // creates all mini Batches
            List<List<SimpleMatrix>> miniBatches = new ArrayList<>();
            for (int k = 0; k < n; k += miniBatchSize) {
                // add parts of the complete training data to a mini batch
                miniBatches.add(trainingData.subList(k, Math.min(k + miniBatchSize, n)));
            }

            // updates the weights and biases according to each mini Batch
            for (List<SimpleMatrix> miniBatch : miniBatches) {
                updateMiniBatch(miniBatch, learningRate);
            }
            
            /* 
            if (testData != null) {
                System.out.println("Epoch " + j + ": " + evaluate(testData) + " / " + nTest);
            } else {
                System.out.println("Epoch " + j + " complete");
            }
            */
        }
    }

    // update weights and biases per mini batch using backprop and stochastic gradient descent
    private void updateMiniBatch(List<SimpleMatrix> miniBatch, double learningRate) {

    }

    // derivative sigmoid function

    // derivative cost function

    // backpropagation

    // evaluate if the output of the network is correct

    public List<Integer> getSizes() {
        return sizes;
    }

    public List<SimpleMatrix> getBiases() {
        return biases;
    }

    public List<SimpleMatrix> getWeights() {
        return weights;
    }
}
