package com.openjfx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Matrix {
    private double[][] values;      // stores the values of the matrix  
    private int rows, columns;      // stores the number of rows and columns of the matrix

    public Matrix(int rows, int columns) {
        this.values = new double[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    // initializes the values randomly
    public void initRandomValues() {
        Random random = new Random();

        // loops through all of the rows and columns
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.values[i][j] = random.nextGaussian();
            }
        }
    }

    // applies the sigmoid function to every value in the matrix
    public void sigmoid() {
        // loops through all of the rows and columns
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.values[i][j] = 1 / (1 + Math.exp(-this.values[i][j]));     // sigmoid function
            }
        }
    }

    // return a matrix with the derivative of the sigmoid function applied to each element
    public Matrix sigmoidDeriv() {
        // creates the new matrix
        Matrix result = new Matrix(this.rows, this.columns);
        // iterates over all of the rows and colums and applies the derivative of the sigmoid function to every element in the matrix
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                result.values[i][j] = this.values[i][j] * (1 - this.values[i][j]);
            }
        }
        return result;
    }

    // scales the matrix by a value
    public void add(double value) {
        // loops through all of the rows and columns
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                // adds the value to everyone in the matrix, -> scaling it
                this.values[i][j] += value;
            }
        }
    }

    public void add(Matrix matrix) {
        if (this.columns != matrix.columns || this.rows != matrix.rows) {
            System.err.println("Matrices don't have the same dimensions!");
            System.err.println(columns + " and " + matrix.columns);
            System.err.println(rows + " and " + matrix.rows);
        } else {
            // loops through all of the rows and columns
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    // adds the values of each matrix together
                    this.values[i][j] += matrix.values[i][j];
                }
            }
        }
    }

    // multiplies every value in the matrix with the provided value, -> scaling it
    public void multiply(double value) {
        // loops through all of the rows and columns
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.values[i][j] *= value;
            }
        }
    }

    // element wise muliplication between matrix and current object
    public void multiply(Matrix matrix) {
        // loops through all of the rows and columns
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.columns; j++) {
                this.values[i][j] *= matrix.values[i][j];
            }
        }
    }

    // multiplies the values of each row of the first matrix by the corresponding values of each column of the second matrix
    // then sums the products to obtain the elements of the resulting matrix
    public static Matrix multiply(Matrix matrixOne, Matrix matrixTwo) {
        // creates the new matrix
        Matrix result = new Matrix(matrixOne.rows, matrixTwo.columns);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.columns; j++) {
                double sum = 0;
                for (int k = 0; k < matrixOne.columns; k++) {
                    sum += matrixOne.values[i][k] * matrixTwo.values[k][j];
                }
                result.values[i][j] = sum;
            }
        }
        return result;
    }

    // returns a matrix where each element is the difference between the corresponding elements of the two given matrices
    public static Matrix subtract(Matrix matrixOne, Matrix matrixTwo) {
        // creates the new matrix
        Matrix result = new Matrix(matrixOne.rows, matrixOne.columns);
        // loops through all of the rows and columns
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.columns; j++) {
                result.values[i][j] = matrixOne.values[i][j] - matrixTwo.values[i][j];
            }
        }
        return result;
    }

    // returns a new matrix with the number of rows and columns swapped
    public static Matrix transpose(Matrix matrix) {
        // creates the new matrix
        Matrix temp = new Matrix(matrix.columns, matrix.rows);
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.columns; j++) {
                temp.values[j][i] = matrix.values[i][j];
            }
        }
        return temp;
    }

    // converts an array to a matrix
    public static Matrix arrayToMatrix(double[] array) {
        // creates the new matrix
        Matrix result = new Matrix(array.length, 1);
        // loops through every element in the array
        for (int i = 0; i < array.length; i++) {
            result.values[i][0] = array[i];
        }
        return result;
    }

    // returns the current matrix object as an array
    public List<Double> matrixToArray() {
        // creates the new array
        List<Double> result = new ArrayList<Double>();
        // loops through all of the rows and columns
        for (int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.columns; j++) {
                result.add(this.values[i][j]);
            }
        }
        return result;
    }

    public double[][] getValues() {
        return this.values;
    }

    public void setValues(double[][] values) {
        this.values = values;
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }
}
