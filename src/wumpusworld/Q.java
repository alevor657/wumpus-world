/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * this.qTable is being mutated?
 */

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
    int maxSteps = 2000;
    
    World world;
    
    double gamma = 0.95;
    double epsilon = 1.0;
    double maxEpsilon = 1;
    double minEpsilon = 0.01;
    double decayRate = 0.005;
    
    ArrayList<Integer> rewards;
    
    public Q(int stateSize, int actionSize, int epochs, double learningRate, World w) {
        this.actionSize = actionSize;
        this.stateSize = stateSize;
        this.epochs = epochs;
        this.lerningRate = learningRate;
        this.world = w;
        
        this.qTable = new Double[this.stateSize][this.actionSize];
        
        for (int j = 0; j < this.qTable.length; j++) {
            Arrays.fill(this.qTable[j], -100000.0);
        }
        
//        this.qTable[0][0] = 7.0;
        
        for (int i = 0; i < this.qTable.length; i++) {
            System.out.print("row: " + i + "    ");
            for (int j = 0; j < this.qTable[i].length; j++) {
                System.out.print(this.qTable[i][j] + "  ");
            }
            System.out.print("\n");
        }
        
        this.rewards = new ArrayList<>();
    }
    
    public void train() throws IOException {
        for (int i = 0; i < this.epochs; i++) {
            World world = this.world.cloneWorld();
            System.out.println("epoch : " + i);    
            
            for (int k = 0; k < this.maxSteps; k++) {
                int stateIndex = (world.getPlayerX() - 1) * 4 + world.getPlayerY() - 1;
                Random r = new Random();
                double expTradeoff = r.nextDouble();
                
                int action = 10;
                
                
                if (expTradeoff > this.epsilon) {
//                    double actionValue = Collections.max(Arrays.asList(this.qTable[stateIndex]));
//                    action = Arrays.asList(this.qTable[stateIndex]).indexOf(actionValue);
                    
                    int x, y, temp1;
                    Double temp;
                    boolean swaped;

                    Double[] actions = this.qTable[stateIndex];
                    int[] indexes = { 0, 1, 2, 3 };
                    
//                     for (Double b : actions) {
//                        System.out.print(b + "  ");
//                    }
                     
                    for (x = 0; x < actions.length - 1; x++) {
                        swaped = false;

                        for (y = 0; y < actions.length - x - 1; y++) {
                            if (actions[y] < actions[y + 1]) {
                                temp = actions[y];
                                actions[y] = actions[y + 1];
                                actions[y + 1] = temp;

                                temp1 = indexes[y];
                                indexes[y] = indexes[y + 1];
                                indexes[y + 1] = temp1;
                                swaped = true;
                            }
                        }

                        if (!swaped) {
                            break;
                        }
                    }
                    
                   
                    
//                    System.out.println("\n");
                    
                    for (int z = 0; z < indexes.length; z++) {
//                        System.out.print(indexes[z]);
                        
                        if (this.checkValidAction(z, world)) {
                            action = z;
                            break;
                        }
                    }
//                    System.exit(1);
                } else {
                    // Update new world
                    action = this.getValidRandomMove(world);
                }
                
                World newWorld = world.cloneWorld();
                int score1 = newWorld.getScore();
                
                switch (action) {
                    case 0:
                        newWorld.turnDown();
                        newWorld.moveForward();
                        break;
                    case 1:
                        newWorld.turnRight();
                        newWorld.moveForward();
                        break;
                    case 2:
                        newWorld.turnUp();
                        newWorld.moveForward();
                        break;
                    case 3:
                        newWorld.turnLeft();
                        newWorld.moveForward();
                        break;
                }
                
//                if (newWorld.hasGlitter(newWorld.getPlayerX(), newWorld.getPlayerY())) {
//                    newWorld.doAction(newWorld.A_GRAB);
//                }
                
                int score2 = newWorld.getScore();
                double reward = score2 - score1;
                
                int newStateIndex = (newWorld.getPlayerX() - 1) * 4 + newWorld.getPlayerY() - 1 ;
                double prevValue = this.qTable[stateIndex][action];
                double max = Collections.max(Arrays.asList(this.qTable[newStateIndex]));
                
                if (stateIndex == 0 && action == 0) {
                    System.out.println("FUCK!");
                    System.exit(0);
                }
                
                this.qTable[stateIndex][action] = prevValue + this.lerningRate * (reward + this.gamma * max - prevValue);
                
                world = newWorld;
                
                if (newWorld.gameOver()) {
                    break;
                }
            }
            
            this.epsilon = this.minEpsilon + (this.maxEpsilon - this.minEpsilon) * Math.exp(-1 * this.decayRate * i);
        }
        
        this.serializeQtable();
    }
    
    private void serializeQtable() throws IOException {
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
        
        try {
            String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "QTables";
            new File(path).mkdir();
            File file = new File(path + System.getProperty("file.separator"), "qtable1.ser");
            fout = new FileOutputStream(file, true);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this.qTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(oos != null){
                oos.close();
            } 
        }
    }
    
    private boolean checkValidAction(int action, World w) {
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
        
        int xPos = directions.get(action)[0];
        int yPos = directions.get(action)[1];

        return w.isValidPosition(xPos, yPos);
    }
    
    private int getValidRandomMove(World w) {
        Random r = new Random();
        
        while (true) {
            int randomAction = r.nextInt(4);
            
            if (this.checkValidAction(randomAction, w)) {
                return randomAction;
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
