package com.ai;

import java.util.List;

public class SimpleNetwork {
    // matricies with weights and biases: weightsInputHidden = weights of the connections between the neurons of the Input layer and the Hidden layer ...
    private Matrix weightsInputHidden, weightsHiddenOutput, biasesHidden, biasesOutput;
    private double learningRateEta;
    
    /**
     * Creates a neural network with the given amount of neurons
     * @param inputNeurons amount of neurons for input layer
     * @param hiddenNeurons amount of neurons for hidden layer
     * @param outputNeurons amount of neurons for output layer
     * @param learningRate the learning rate eta for how fast the network learns
     */
    public SimpleNetwork(int inputNeurons, int hiddenNeurons, int outputNeurons, double learningRate) {
        this.learningRateEta = learningRate;

        // initialize matricies with random values 
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
        Matrix hidden_T = Matrix.transpose(hidden);
        Matrix who_delta =  Matrix.multiply(gradient, hidden_T);
        
        // update the weights and biases between the hidden and output layers
        this.weightsHiddenOutput.add(who_delta);
        this.biasesOutput.add(gradient);
        
        // calculate the hidden layer error by multiplying the output layer error with the weights
        Matrix who_T = Matrix.transpose(this.weightsHiddenOutput);
        Matrix hidden_errors = Matrix.multiply(who_T, error);
        
        // calculate the gradient of the hidden layer using the sigmoid derivative of the hidden layer values
        Matrix h_gradient = hidden.sigmoidDeriv();
        h_gradient.multiply(hidden_errors);
        h_gradient.multiply(this.learningRateEta);
        
        // calculate the delta for the weights between the input and hidden layers
        Matrix i_T = Matrix.transpose(input);
        Matrix wih_delta = Matrix.multiply(h_gradient, i_T);
        
        // update the weights and biases between the input and hidden layers
        this.weightsInputHidden.add(wih_delta);
        this.biasesHidden.add(h_gradient);
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
    
}


