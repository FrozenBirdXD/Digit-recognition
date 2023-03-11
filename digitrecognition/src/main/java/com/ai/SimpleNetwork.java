package com.ai;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class SimpleNetwork {
    // matricies with weights and biases: weightsInputHidden = weights of the connections between the neurons of the Input layer and the Hidden layer ...
    private Matrix weightsInputHidden, weightsHiddenOutput, biasesHidden, biasesOutput;
    private double learningRateEta;
    
    /**
     * Creates a neural network with the given amount of neurons and initializes them with random values
     * @param inputNeurons amount of neurons for input layer
     * @param hiddenNeurons amount of neurons for hidden layer
     * @param outputNeurons amount of neurons for output layer
     * @param learningRate the learning rate eta for how fast the network learns
     */
    public SimpleNetwork(int inputNeurons, int hiddenNeurons, int outputNeurons, double learningRate) {
        this.learningRateEta = learningRate;

        // initialize matricies with random values 
        this.weightsInputHidden = new Matrix(hiddenNeurons, inputNeurons);
        this.weightsInputHidden.initRandomValues();
        this.weightsHiddenOutput = new Matrix(outputNeurons, hiddenNeurons);
        this.weightsHiddenOutput.initRandomValues();

        this.biasesHidden = new Matrix(hiddenNeurons, 1);
        this.biasesHidden.initRandomValues();
        this.biasesOutput = new Matrix(outputNeurons, 1);
        this.biasesOutput.initRandomValues();
    }

    /**
     * Creates a neural network with the given amount of neurons without any random parameters
     * @param inputNeurons amount of neurons for input layer
     * @param hiddenNeurons amount of neurons for hidden layer
     * @param outputNeurons amount of neurons for output layer
     */
    public SimpleNetwork(int inputNeurons, int hiddenNeurons, int outputNeurons) {

        // creates matricies with correct size
        this.weightsInputHidden = new Matrix(hiddenNeurons, inputNeurons);
        this.weightsHiddenOutput = new Matrix(outputNeurons, hiddenNeurons);

        this.biasesHidden = new Matrix(hiddenNeurons, 1);
        this.biasesOutput = new Matrix(outputNeurons, 1);
    }
    
    /**
    * Utilizes backpropagation algorithm for training the neural network
    * @param inputs an array of input values
    * @param targets an array of target output values
    */
    public void train(double[] inputs, double[] targets) {
        // convert given data to matrix
        Matrix target = Matrix.arrayToMatrix(targets);
        Matrix input = Matrix.arrayToMatrix(inputs);

        // forward propagation to calculate the output of the hidden layer
        Matrix hidden = Matrix.multiply(this.weightsInputHidden, input);
        hidden.add(this.biasesHidden);
        hidden.sigmoid();
        
        // forward propagation to calculate the output of the output layer
        Matrix output = Matrix.multiply(this.weightsHiddenOutput,hidden);
        output.add(this.biasesOutput);
        output.sigmoid();
        
        // calculate the error in the output layer and propagate it back to the hidden layer
        // calculate the error between the predicted output and the target output
        Matrix error = Matrix.subtract(target, output);

        // calculate the gradient of the output layer using the sigmoid derivative of the output values
        Matrix gradient = output.sigmoidDeriv();
        gradient.multiply(error);
        gradient.multiply(this.learningRateEta);
        
        // calculate the delta for the weights between the hidden and output layers
        Matrix hiddenTranspose = Matrix.transpose(hidden);
        Matrix weightsHiddenOutputDelta =  Matrix.multiply(gradient, hiddenTranspose);
        
        // update the weights and biases between the hidden and output layers
        this.weightsHiddenOutput.add(weightsHiddenOutputDelta);
        this.biasesOutput.add(gradient);
        
        // calculate the hidden layer error by multiplying the output layer error with the weights
        Matrix weightsHiddenOutputTranspose = Matrix.transpose(this.weightsHiddenOutput);
        Matrix hiddenErrors = Matrix.multiply(weightsHiddenOutputTranspose, error);
        
        // calculate the gradient of the hidden layer using the sigmoid derivative of the hidden layer values
        Matrix hiddenGradient = hidden.sigmoidDeriv();
        hiddenGradient.multiply(hiddenErrors);
        hiddenGradient.multiply(this.learningRateEta);
        
        // calculate the delta for the weights between the input and hidden layers
        Matrix inputTranspose = Matrix.transpose(input);
        Matrix weightsInputHiddenDelta = Matrix.multiply(hiddenGradient, inputTranspose);
        
        // update the weights and biases between the input and hidden layers
        this.weightsInputHidden.add(weightsInputHiddenDelta);
        this.biasesHidden.add(hiddenGradient);
    }

    /**
    * Trains the neural network on the given dataset for a certain number of epochs
    * @param inputData a 2D array of input values
    * @param output a 2D array of target output values
    * @param epochs the number of times to iterate over the entire dataset
    */
    public void fit(double[][] inputData, double[][] output, int epochs) {
        for (int i = 0; i < epochs; i++) {    
            // select a random sample from the dataset and train the network on it
            int sampleN = (int) (Math.random() * inputData.length);
            this.train(inputData[sampleN], output[sampleN]);
        }
    }
    
    /**
     * Applies forward propagation with a single array as an input
     * @param inputs array with the data to analyse
     * @return return the prediction of the network as a list
     */
    public List<Double> predict(double[] inputs) {
        // convert input data to matrix
        Matrix inputMatrix = Matrix.arrayToMatrix(inputs);
        
        // calculate activations of hidden layer
        Matrix hiddenInputs = Matrix.multiply(this.weightsInputHidden, inputMatrix);
        hiddenInputs.add(this.biasesHidden);
        hiddenInputs.sigmoid();
        
        // calculate activations of output layer = result
        Matrix outputInputs = Matrix.multiply(this.weightsHiddenOutput, hiddenInputs);
        outputInputs.add(this.biasesOutput);
        outputInputs.sigmoid();
        
        // convert output matrix with activations to array
        return outputInputs.matrixToArray();
    }

    /**
     * Evaluates the biggest number in a list and returns the index = the number the network predicts
     * @param list A list of Doubles with the networks prediction
     * @return Returns the number the network predicted
     */
    public int getPredictionInt(List<Double> list) {
        int indexBiggest = 0;
        Double result = 0.0;
        // sorts out the biggest number
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > result) {
                indexBiggest = i;
                result = list.get(i);
            }
        }

        return indexBiggest;
    }
    
    /**
     * Saves the parameters of the network to a text file
     * @throws IOException
     */
    public void saveParams() throws IOException {
        // writes to file
        FileWriter writer = new FileWriter("weights-biases.txt");

        // writes weightsInputHidden params
        for (int i = 0; i < this.weightsInputHidden.getRows(); i++) {
            for (int j = 0; j < this.weightsInputHidden.getColumns(); j++) {
                writer.write(this.weightsInputHidden.getValues()[i][j] + " ");
            }
            writer.write("\n");
        }
        writer.write("\n");

        // writes weightsHiddenOutput params
        for (int i = 0; i < this.weightsHiddenOutput.getRows(); i++) {
            for (int j = 0; j < this.weightsHiddenOutput.getColumns(); j++) {
                writer.write(this.weightsHiddenOutput.getValues()[i][j] + " ");
            }
            writer.write("\n");
        }
        writer.write("\n");

        // writes biasesHidden params
        for (int i = 0; i < this.biasesHidden.getRows(); i++) {
            for (int j = 0; j < this.biasesHidden.getColumns(); j++) {
                writer.write(this.biasesHidden.getValues()[i][j] + " ");
            }
            writer.write("\n");
        }
        writer.write("\n");

        // writes biasesOutput params
        for (int i = 0; i < this.biasesOutput.getRows(); i++) {
            for (int j = 0; j < this.biasesOutput.getColumns(); j++) {
                writer.write(this.biasesOutput.getValues()[i][j] + " ");
            }
            writer.write("\n");
        }
        writer.write("\n");

        writer.close();
    }

    /**
     * Reads the network parameters from the file and applies them to the current network
     * @throws IOException
     */
    public void readParams() throws IOException {
        // reads file
        Scanner scanner = new Scanner(new File("weights-biases.txt"));

        // saves the values of the read parameters for each instance variable
        double[][] values = new double[this.weightsInputHidden.getRows()][this.weightsInputHidden.getColumns()];

        for (int i = 0; i < this.weightsInputHidden.getRows(); i++) {
            for (int j = 0; j < this.weightsInputHidden.getColumns(); j++) {
                values[i][j] = Double.parseDouble(scanner.next());
            }
        }
        // applies the read values to weightsInputHidden
        this.weightsInputHidden.setValues(values);

        values = new double[this.weightsHiddenOutput.getRows()][this.weightsHiddenOutput.getColumns()];

        for (int i = 0; i < this.weightsHiddenOutput.getRows(); i++) {
            for (int j = 0; j < this.weightsHiddenOutput.getColumns(); j++) {
                values[i][j] = Double.parseDouble(scanner.next());
            }
        }
        // applies the read values to weightsHiddenOutput
        this.weightsHiddenOutput.setValues(values);

        values = new double[biasesHidden.getRows()][this.biasesHidden.getColumns()];

        for (int i = 0; i < this.biasesHidden.getRows(); i++) {
            for (int j = 0; j < this.biasesHidden.getColumns(); j++) {
                values[i][j] = Double.parseDouble(scanner.next());
            }
        }
        // applies the read values to biasesHidden
        this.biasesHidden.setValues(values);

        values = new double[biasesOutput.getRows()][this.biasesOutput.getColumns()];

        for (int i = 0; i < this.biasesOutput.getRows(); i++) {
            for (int j = 0; j < this.biasesOutput.getColumns(); j++) {
                values[i][j] = Double.parseDouble(scanner.next());
            }
        }
        // applies the read values to biasesOutput
        this.biasesOutput.setValues(values);

        scanner.close();
    }
}


