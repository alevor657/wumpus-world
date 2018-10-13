/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author user
 */
public class Q {
    private int actionSize;
    private int stateSize;
    Double[][] qTable;
    int epochs;
    double lerningRate;
    int maxSteps = 99;
    
    World world;
    
    double gamma = 0.95;
    double epsilon = 1.0;
    double maxEpsilon = 1;
    double minEpsilon = 0.01;
    double decayRate = 0.005;
    
    ArrayList<Integer> rewards;
    
    public Q(int actionSize, int stateSize, int epochs, double learningRate, World w) {
        this.actionSize = actionSize;
        this.stateSize = stateSize;
        this.epochs = epochs;
        this.lerningRate = learningRate;
        this.world = w;
        
        this.qTable = new Double[this.actionSize][this.stateSize];
        
        for (int j = 0; j < this.qTable.length; j++) {
            Arrays.fill(this.qTable[j], (double)Integer.MIN_VALUE);
        }
        
        this.rewards = new ArrayList<>();
    }
    
    public void train() {
        for (int i = 0; i < this.epochs; i++) {
            World world = this.world.cloneWorld();
            System.out.println("epoch : " + i);    
            for (int k = 0; k < this.maxSteps; k++) {
                int stateIndex = (world.getPlayerX() - 1) * 4 + world.getPlayerY() - 1;
                Random r = new Random();
                double expTradeoff = r.nextDouble();
                
                int action;
                
                if (expTradeoff > this.epsilon) {
                    double actionValue = Collections.max(Arrays.asList(this.qTable[stateIndex]));
                    action = Arrays.asList(this.qTable[stateIndex]).indexOf(actionValue);
                } else {
                    // Update new world
                    action = this.getValidRandomMove(world);
                }
                
                World newWorld = world.cloneWorld();
                int score1 = newWorld.getScore();
                
                switch (action) {
                    case 0:
                        newWorld.goDown();
                        break;
                    case 1:
                        newWorld.goRight();
                        break;
                    case 2:
                        newWorld.goUp();
                        break;
                    case 3:
                        newWorld.goLeft();
                        break;
                }
                
                int score2 = newWorld.getScore();
                double reward = score2 - score1;
                
                int newStateIndex = (newWorld.getPlayerX() - 1) * 4 + newWorld.getPlayerY() - 1 ;
                double prevValue = this.qTable[stateIndex][action];
                double max = Collections.max(Arrays.asList(this.qTable[newStateIndex]));
                
                this.qTable[stateIndex][action] = prevValue + this.lerningRate * (reward + this.gamma * max - prevValue);
                
                world = newWorld;
                
                if (newWorld.gameOver()) {
                    break;
                }
            }
            
            this.epsilon = this.minEpsilon + (this.maxEpsilon - this.minEpsilon) * Math.exp(-1 * this.decayRate * i);
        }
    }
    
    private int getValidRandomMove(World w) {
        int pX = w.getPlayerX();
        int pY = w.getPlayerY();
        
        ArrayList<int[]> directions = new ArrayList<>();
        int down[] = { pX, pY - 1 };
        int right[] = { pX + 1, pY };
        int up[] = { pX, pY + 1 };
        int left[] = { pX -1, pY };

        directions.add(down);
        directions.add(right);
        directions.add(up);
        directions.add(left);
        
        Random r = new Random();
        
        while (true) {
            int randomMove = r.nextInt(4);
            int xPos = directions.get(randomMove)[0];
            int yPos = directions.get(randomMove)[1];
            
            if (w.isValidPosition(xPos, yPos)) {
                return randomMove;
            }
        }
    }
    
    
    
    private int getNrPossibleMoves(World w) {
        int pX = w.getPlayerX();
        int pY = w.getPlayerY();

        int nrPossibleMoves = 4;

        if (pX == 1 || pX == 4) {
            nrPossibleMoves --;
        }

        if (pY == 1 || pY == 4) {
            nrPossibleMoves --;
        }
        
        return nrPossibleMoves;
    }
}
