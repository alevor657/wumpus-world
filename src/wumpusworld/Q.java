/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author user
 */
public class Q {
    private int actionSize;
    private int stateSize;
    Integer[][] qTable;
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
        
        this.qTable = new Integer[this.actionSize][this.stateSize];
        this.rewards = new ArrayList<>();
    }
    
    public void train() {
        for (int i = 0; i < this.epochs; i++) {
            World newWorld = this.world.cloneWorld();
                        
            for (int k = 0; k < this.maxSteps; k++) {
                Random r = new Random();
                double expTradeoff = r.nextDouble();
                
                int action;
                
                if (expTradeoff > this.epsilon) {
                    action = 1;
                } else {
                    // Update new world
                    action = this.getValidRandomMove(newWorld);
                }
                
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
                int reward = score2 - score1;
                
                if (newWorld.gameOver()) {
                    break;
                }
                
                
                
//              
            }
            
            
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
