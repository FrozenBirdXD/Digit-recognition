package com.ai;

import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

public class App {
    public static void main(String[] args) {
        List<Integer> sizes = new ArrayList();
        sizes.add(5);
        sizes.add(3);
        sizes.add(10);
        Network network = new Network(sizes);
        System.out.println(network.getSizes());
        System.out.println(network.getBiases());
        System.out.println(network.getWeights());
        
        double[] input = {23.6, 235.1, 163.6, 134.6, 233.7};
        SimpleMatrix sampleInput = new SimpleMatrix(input);
        System.out.println(network.calculateOutput(sampleInput));
    }
}
