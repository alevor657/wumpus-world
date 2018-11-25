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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * this.qTable is being mutated?
 */

/**
 *
 * @author user
 */
public class Q {
    Map<Integer, ArrayList<Double>> map = new HashMap<>();
    int epochs;
    double lerningRate;
    int maxSteps = 2000;
    
    World world;
    
    double gamma = 0.95;
    double epsilon = 1.0;
    double maxEpsilon = 1;
    double minEpsilon = 0.01;
    double decayRate = 0.0005;
        
    public Q(int epochs, double learningRate, World w) throws InterruptedException {
        this.epochs = epochs;
        this.lerningRate = learningRate;
        this.world = w;
                
        this.map = MyAgent.readTable();
    }
    
    public static int getTileValue(World w, int x, int y){
        if(!w.isVisited(x, y)){
            return 0;
        }else{
            return 1;
        }
    }
    
    public static int getMagicValue(World w, int x, int y) {
        boolean stench;
        boolean breeze;
        int magic = -1;
        
        stench = w.hasStench(x, y);
        breeze = w.hasBreeze(x, y);
            
        if (!w.isValidPosition(x, y)) {
            magic = 0;
        } else if (!w.isVisited(x, y)) {
            magic = 1;
        } else if (breeze && stench) {
            magic = 2;
        } else if (stench) {
            magic = 3;
        } else if (breeze) {
            magic = 4;
        } else {
           magic = 5;
        }
        if(magic != -1){
            return magic;
        } else {
            System.out.println("Something is wrong in magic value");
            System.exit(0);
            return 0;
        }
    }    
    
    public static int getStateIndexEff(World w) {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        
        int index=0;
        int temp1;
        int temp2;
        int temp3;
        int temp4;
        int temp5;
        int temp6;
        int temp7;
        int temp8;
        int temp9;
        int temptile1;
        int temptile2;
        int temptile3;
        int temptile4;
        
        
        temp1 = getMagicValue(w, x, y+2);
        
//        index = (temp*Math.pow(10, 12));
        
        temp2 = getMagicValue(w, x+1, y+1);
        
//        index += temp*Math.pow(10, 11);
        
        temp3 = getMagicValue(w, x+2, y);
        
//        index += temp*Math.pow(10, 10);
        
        temp4 = getMagicValue(w, x+1, y-1);
        
//        index += temp*Math.pow(10, 9);
        
        temp5 = getMagicValue(w, x, y-2);
        
//        index += temp*Math.pow(10, 8);
        
        temp6 = getMagicValue(w, x-1, y-1);
        
//        index += temp*Math.pow(10, 7);
        
        temp7 = getMagicValue(w, x-2, y);
        
//        index += temp*Math.pow(10, 6);
        
        temp8 = getMagicValue(w, x-1, y+1);
        
//        index += temp*Math.pow(10, 5);
        
        temptile1 = getTileValue(w, x, y+1);
        
//        index += temp*Math.pow(10, 4);
        
        temptile2 = getTileValue(w, x+1, y);
        
//        index += temp*Math.pow(10, 3);
        
        temptile3 = getTileValue(w, x, y-1);
        
//        index += temp*Math.pow(10, 2);
        
        temptile4 = getTileValue(w, x-1, y);
        
//        index += temp*Math.pow(10, 1);
        
        temp9 = getMagicValue(w, x, y);
        
//        index += temp;
        
        index = 2*(2*(2*(2*(6*(6*(6*(6*(6*(6*(6*(6*(temp8) + temp7) + temp6) + temp5) + temp4) + temp3) + temp2) + temp1) + temp9) + temptile1) + temptile2) +temptile3) + temptile4;

        return index;
    }
    
    public void train() throws IOException {
        Map<Integer, ArrayList<Double>> map = new HashMap<>();
        
        for (int epoch = 0; epoch < this.epochs; epoch++) {
            World world = this.world.cloneWorld();
            int turnNr = 0;
            System.out.println("Epoch : " + epoch);
            
            for (; turnNr < this.maxSteps; turnNr++) {
                if(world.gameOver()){
                    System.out.println("Game over!");
                    System.out.println("Turns: " + turnNr);
                    System.out.println("Has Gold: " + world.hasGold());
                    break;
                }
                
                if (world.hasGlitter(world.getPlayerX(), world.getPlayerY())) {
                    world.doAction(world.A_GRAB);
                } else {
                    boolean wumpusRewardTaken = false;
                    int x = world.getPlayerX();
                    int y = world.getPlayerY();             

                    int stateIndex = this.getStateIndexEff(world);
                   
                    Random r = new Random();
                    double expTradeoff = r.nextDouble();

                    boolean exploreReward1 = false;
                    boolean exploreReward2 = false;

                    int action = -10;

                    if (expTradeoff > this.epsilon) {
                        stateIndex = this.getStateIndexEff(world);

                        ArrayList<Double> state = map.get(stateIndex);
                        if(state == null){
                            state = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0));
                        }
                        double actionValue;
                        
                        actionValue = Collections.max(state);
                        action = state.indexOf(actionValue);

                    } else {
                        action = this.getValidRandomMove(world);    
                    }

                    World newWorld = world.cloneWorld();

                    int score1 = newWorld.getScore();

                    switch (action) {
                        case 0:
                            newWorld.turnUp();
                            newWorld.moveForward();
                            if(!newWorld.isVisited(x, y+1)){
                                exploreReward2 = true;
                            }
                            break;
                        case 1:
                            newWorld.turnRight();
                            newWorld.moveForward();
                            if(!newWorld.isVisited(x+1, y)){
                                exploreReward2 = true;
                            }
                            break;
                        case 2:
                            newWorld.turnDown();
                            newWorld.moveForward();
                            if(!newWorld.isVisited(x, y-1)){
                                exploreReward2 = true;
                            }
                            break;
                        case 3:
                            newWorld.turnLeft();
                            newWorld.moveForward();
                            if(!newWorld.isVisited(x-1, y)){
                                exploreReward2 = true;
                            }
                            break;
                        case 4:
                            newWorld.turnUp();
                            newWorld.shootForward();
                            break;
                        case 5:
                            newWorld.turnRight();
                            newWorld.shootForward();
                            break;
                        case 6:
                            newWorld.turnDown();
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
                        reward += 25;
                        wumpusRewardTaken = true;
                    }                

                    if(exploreReward2){
                        reward += 7;
                    }

                    exploreReward2 = false;

                    ArrayList<Double> state = map.get(stateIndex);
                    
                    if(state == null){
                        state = new ArrayList<>(Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0));
                    }
                    double actionValue;
                        
                    actionValue = Collections.max(state);
                    action = state.indexOf(actionValue);
                    
                    double prevValue = actionValue;
                    
                    int newStateIndex = this.getStateIndexEff(newWorld);
                    ArrayList<Double> state1 = map.get(newStateIndex);
                    
                    if(state1 == null){
                        state = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0));
                    }
                    
                    double max = Collections.max(state);

                    state.set(action, prevValue + this.lerningRate * (reward + this.gamma * max - prevValue));
                    map.put(stateIndex, state);
                    world = newWorld;
                }
            }

            this.epsilon = this.minEpsilon + (this.maxEpsilon - this.minEpsilon) * Math.exp(-1 * this.decayRate * epoch);
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
            oos.writeObject(this.map);
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
}
