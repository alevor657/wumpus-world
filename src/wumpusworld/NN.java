/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import java.util.Random;

/**
 *
 * @author user
 */
public class NN {
    private Random random;
    public Double[][] weights = new Double[1][3];
    
    public NN(int nrWeights) {
        this.random = new Random();
        this.random.setSeed(1);
        
        this.randomizeWeights(nrWeights);
    }
    
    private void randomizeWeights(int nrWeights) {
        for (int i = 0; i < nrWeights; i++) {
            this.weights[0][i] = 2 * this.random.nextDouble() - 1;
        }
    }
    
    private double sigmoid(double x) {
            return 1 / ( 1 + Math.pow(Math.E,(-1*x)));
    }
    
    private double sigmoidDerivative(double x) {
        return x * (1 - x);
    }
}
