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
    ArrayList<ArrayList<Double>> qTable;
    int epochs;
    double lerningRate;
    int maxSteps = 2000;
    
    World world;
    
    double gamma = 0.95;
    double epsilon = 1.0;
    double maxEpsilon = 1;
    double minEpsilon = 0.01;
    double decayRate = 0.05;
    
    ArrayList<Integer> rewards;
    
    public Q(int stateSize, int actionSize, int epochs, double learningRate, World w) throws InterruptedException {
        this.actionSize = actionSize;
        this.stateSize = stateSize;
        this.epochs = epochs;
        this.lerningRate = learningRate;
        this.world = w;
        
        this.qTable = new ArrayList<ArrayList<Double>>();        
        this.rewards = new ArrayList<>();
        
        
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "QTables";
        File file = new File(path + System.getProperty("file.separator"), "qtable.ser");

        if (file.exists()) {
            System.out.println("Reading existing table");
            Thread.sleep(1000);
            this.qTable = MyAgent.readTable();
        } else {
            System.out.println("Creating new qTable");
            Thread.sleep(1000);
            initTable();
        }
    }
    
    private void initTable() {
        for (int i = 0; i < stateSize; i++) {
            this.qTable.add(new ArrayList());
            this.qTable.set(i, new ArrayList(Collections.nCopies(this.actionSize, 0.0)));
        }
        
        for (int i = 0; i < this.qTable.size(); i++) {
            System.out.print("row: " + i + "    ");
            for (int j = 0; j < this.qTable.get(i).size(); j++) {
                System.out.print(this.qTable.get(i).get(j) + "  ");
            }
            System.out.print("\n");
        }
    }
    
    public static int getStateIndex(World w) {
        int magic;
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        boolean stench = w.hasStench(x, y);
        boolean breeze = w.hasBreeze(x, y);
        
        if (breeze && stench) {
            magic = 0;
        } else if (stench) {
            magic = 1;
        } else if (breeze) {
            magic = 2;
        } else {
            magic = 3;
        }
        
        return 4 * (4 * (x - 1) + (y - 1)) + magic;
    }
    
    public void train() throws IOException {
        for (int i = 0; i < this.epochs; i++) {
            World world = this.world.cloneWorld();
            System.out.println("epoch : " + i);    
            
            for (int k = 0; k < this.maxSteps; k++) {
                boolean wumpusRewardTaken = false;
                int stateIndex = this.getStateIndex(world);
                Random r = new Random();
                double expTradeoff = r.nextDouble();
                
                int action = 10;
                
                
                if (expTradeoff > this.epsilon) {
                    double actionValue = Collections.max(this.qTable.get(stateIndex));
                    action = this.qTable.get(stateIndex).indexOf(actionValue);
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
                    case 4:
                        newWorld.turnDown();
                        newWorld.shootForward();
                        break;
                    case 5:
                        newWorld.turnRight();
                        newWorld.shootForward();
                        break;
                    case 6:
                        newWorld.turnUp();
                        newWorld.shootForward();
                        break;
                    case 7:
                        newWorld.turnLeft();
                        newWorld.shootForward();
                        break;
                    default:
                        System.out.println("action != 1 - 7");
                        System.exit(1);
                }

                if (newWorld.hasPit(newWorld.getPlayerX(), newWorld.getPlayerY())) {
                    newWorld.doAction(newWorld.A_CLIMB);
                }
                
                int score2 = newWorld.getScore();
                double reward = score2 - score1;
                
                if (!newWorld.wumpusAlive() && !wumpusRewardTaken) {
                    reward += 1000;
                    wumpusRewardTaken = true;
                }
                
                if (newWorld.hasGlitter(newWorld.getPlayerX(), newWorld.getPlayerY())) {
                    newWorld.doAction(newWorld.A_GRAB);
                    reward += 10000;
                }
                
                int newStateIndex = this.getStateIndex(newWorld);
                double prevValue = this.qTable.get(stateIndex).get(action);
                double max = Collections.max(this.qTable.get(newStateIndex));
                
                this.qTable.get(stateIndex).set(action, prevValue + this.lerningRate * (reward + this.gamma * max - prevValue));
                
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
        
//        int mapNr = Integer.parseInt(GUI.getSelectedLevel());

//        System.out.println("Writing msp nr " + mapNr);
                        
        try {
            String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "QTables";
            new File(path).mkdir();
//            File file = new File(path + System.getProperty("file.separator"), "qtable" + mapNr + ".ser");
            File file = new File(path + System.getProperty("file.separator"), "qtable.ser");

            if (file.exists()) {
                file.delete();
            }
            
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
            int randomAction;
            
            if (w.hasArrow()) {
                randomAction = r.nextInt(8);
            } else {
                randomAction = r.nextInt(4);
            }
            
            if (randomAction < 4) {
                if (this.checkValidAction(randomAction, w)) {
                    return randomAction;
                }
            } else {
                if (this.checkValidAction(randomAction - 4, w)) {
                    return randomAction;
                }
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
