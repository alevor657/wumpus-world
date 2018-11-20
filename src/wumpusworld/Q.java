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
import java.util.List;
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
    int maxSteps = 100000;
    
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
            this.initTable();
        }
    }
    
    private void initTable() {
        for (int i = 0; i < stateSize; i++) {
            this.qTable.add(new ArrayList());
            this.qTable.set(i, new ArrayList(Collections.nCopies(this.actionSize, 0.0)));
        }
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

    public static int getStateIndex(World w, int x, int y, int direction) {        
        int down = 0;
        int right = 0;
        int up = 0;
        int left = 0;
        int tile = 0;
        
        if (direction == w.DIR_DOWN) {
            down = getMagicValue(w, x, y-2);
            right = getMagicValue(w, x+1, y-1);
            up = getMagicValue(w, x, y);
            left = getMagicValue(w, x-1, y-1);
            tile = getTileValue(w, x, y-1);
            
            return 2*(6*(6*(6*(left) + right) + down) + up) + tile;
        }else if(direction == w.DIR_RIGHT){
            down = getMagicValue(w, x+1, y-1);
            right = getMagicValue(w, x+2, y);
            up = getMagicValue(w, x+1, y+1);
            left = getMagicValue(w, x, y);
            tile = getTileValue(w, x+1, y);
            
            return 2*(6*(6*(6*(up) + down) + right) + left) + tile;
        }else if(direction == w.DIR_UP){
            down = getMagicValue(w, x, y);
            right = getMagicValue(w, x+1, y+1);
            up = getMagicValue(w, x, y+2);
            left = getMagicValue(w, x-1, y+1);
            tile = getTileValue(w, x, y+1);
            
            return 2*(6*(6*(6*(right) + left) + up) + down) + tile;
        }else if(direction == w.DIR_LEFT){
            down = getMagicValue(w, x-1, y-1);
            right = getMagicValue(w, x, y);
            up = getMagicValue(w, x-1, y+1);
            left = getMagicValue(w, x-2, y);
            tile = getTileValue(w, x-1, y);
            
            return 2*(6*(6*(6*(down) + up) + left) + right) + tile;
        }
        
        return -1;
    }
    
    public void train() throws IOException {
        for (int i = 0; i < this.epochs; i++) {
            World world = this.world.cloneWorld();
            int[] looper = new int[3];
            Arrays.fill(looper, -1);
//            int grandma= -1;
//            int mom = -2;
//            int child = -3;
            System.out.println("epoch : " + i);    
            boolean loop1=false;
            boolean loop2=false;
            
            for (int k = 0; k < this.maxSteps; k++) {
                boolean wumpusRewardTaken = false;
                int x = world.getPlayerX();
                int y = world.getPlayerY();
                
//                grandma = mom;
//                mom = child;
//                child = 4*(x-1) + y;

//                if(loop1){
//                    if(grandma == child){
//                        loop2 = true;
//                    }
//                }
//
//                if(grandma == child){
//                    loop1 = true;
////                    System.out.println("looping");
//                }
                
                int stateIndex = this.getStateIndex(world, x, y, world.getDirection());
                Random r = new Random();
                double expTradeoff = r.nextDouble();
                
                boolean exploreReward1 = false;
                boolean exploreReward2 = false;
                
                int action = -10;
                int direction = -1;
                double maxVal = 0.0;
                double maxy = -100000.0;
                int tempStateIndex;
                
                if (expTradeoff > this.epsilon) {
                    if(loop1 && loop2){
                        direction = this.returnValidDirection(world);
                        action = this.getValidRandomAction(world);
//                        System.out.println("Taking random step");
                        loop1=false;
                        loop2=false;
                        
                    } else {
    //                    double actionValue = Collections.max(this.qTable.get(stateIndex));
    //                    action = this.qTable.get(stateIndex).indexOf(actionValue);
                        int bestDirection = -1;
                        int[] directs = {0,1,2,3};
                        for (int direct: directs){
                            if(direct==0 && !world.isValidPosition(x, y+1)){
                                continue;
                            }else if(direct==1 && !world.isValidPosition(x+1, y)){
                                continue;
                            }
                            else if(direct==2 && !world.isValidPosition(x, y-1)){
                                continue;
                            }
                            else if(direct==3 && !world.isValidPosition(x-1, y)){
                                continue;
                            }
                            tempStateIndex = this.getStateIndex(world, x, y, direct);
                            maxVal = Collections.max(this.qTable.get(tempStateIndex));

                            if(maxVal > maxy){
                                stateIndex = tempStateIndex;
                                maxy = maxVal;
                                bestDirection = direct;
                            }
                        }
                        direction = bestDirection;
                        action = this.qTable.get(stateIndex).indexOf(maxy);
                    }
                } else {
//                    System.out.println("Exploring");
                    direction = this.returnValidDirection(world);
//                    if(world.hasStench(x, y)){
                        action = this.getValidRandomAction(world);
//                    }else{
//                        action = 0;
//                    }
                    
                }
                
                World newWorld = world.cloneWorld();
                int score1 = newWorld.getScore();
                
                if (direction == 0) {
                    newWorld.turnUp();
//                    System.out.println("GOING up");
                    if(!newWorld.isVisited(x, y+1)){
                        exploreReward1 = true;
                    }
                } else if(direction == 1) {
                    newWorld.turnRight();
    //                    System.out.println("GOING right");
                    if(!newWorld.isVisited(x+1, y)){
                        exploreReward1 = true;
                    }
                } else if(direction == 2) {
                    newWorld.turnDown();
//                    System.out.println("GOING down");
                    if(!newWorld.isVisited(x, y-1)){
                        exploreReward1 = true;
                    }
                } else if(direction == 3) {
                    newWorld.turnLeft();
//                    System.out.println("GOING left");
                    if(!newWorld.isVisited(x-1, y)){
                        exploreReward1 = true;
                    }
                } else{
                    System.out.println("Direction is wrong!!");
                    System.exit(0);
                }
                
                switch (action) {
                    case 0:
//                        newWorld.turnDown();
                        newWorld.moveForward();
                        exploreReward2 = true;
                        break;
                    case 1:
//                        newWorld.turnRight();
                        newWorld.shootForward();
                        newWorld.hasArrow = true;
                        break;
                    
                    default:
                        System.out.println("action != 1 - 7");
                        System.out.println(action);
                        System.exit(1);
                }

                if (newWorld.hasPit(newWorld.getPlayerX(), newWorld.getPlayerY())) {
                    newWorld.doAction(newWorld.A_CLIMB);
//                    score1 += 1000;
                }
                
                
                int score2 = newWorld.getScore();
                double reward = score2 - score1;
                
                if (!newWorld.wumpusAlive() && !wumpusRewardTaken) {
                    reward += 25;
                    wumpusRewardTaken = true;
                }
                
                if (newWorld.hasGlitter(newWorld.getPlayerX(), newWorld.getPlayerY())) {
                    newWorld.doAction(newWorld.A_GRAB);
//                    reward -= 990;
                }
                
                
                if(exploreReward1 && exploreReward2){
                    reward += 7;
                }
                exploreReward1 = false;
                exploreReward2 = false;
                
                int newStateIndex=-1;
                double prevValue = this.qTable.get(stateIndex).get(action);
                double maxy1 = -10000000.0;
                int[] directs1 = {0,1,2,3};
                for (int direct: directs1){
                    // if valid 
                    tempStateIndex = this.getStateIndex(newWorld, x, y, direct);
                    maxVal = this.qTable.get(stateIndex).get(0);
                    if(maxVal > maxy1){
                        newStateIndex = tempStateIndex;
                        maxy1 = maxVal;
                        
                    }
                }
//                double max = Collections.max(this.qTable.get(newStateIndex));
                
                this.qTable.get(stateIndex).set(action, prevValue + this.lerningRate * (reward + this.gamma * maxy1 - prevValue));
                
                world = newWorld;
                
                if (newWorld.gameOver()) {
                    System.out.println("Steps: "+k);
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
    
//    private boolean checkValidAction(int action, World w) {
//        int pX = w.getPlayerX();
//        int pY = w.getPlayerY();
//        
//        ArrayList<int[]> directions = new ArrayList<>();
//        int down[] = { pX, pY - 1 };
//        int right[] = { pX + 1, pY };
//        int up[] = { pX, pY + 1 };
//        int left[] = { pX -1, pY };
//
//        directions.add(down);
//        directions.add(right);
//        directions.add(up);
//        directions.add(left);
//        
//        int xPos = directions.get(action)[0];
//        int yPos = directions.get(action)[1];
//
//        return w.isValidPosition(xPos, yPos);
//    }
    private int returnValidDirection(World w) {
        int pX = w.getPlayerX();
        int pY = w.getPlayerY();
        
        ArrayList<int[]> directions = new ArrayList<>();
//        int[] validDirections = {};
        ArrayList validDirections = new ArrayList();
        int down[] = { pX, pY - 1 };
        int right[] = { pX + 1, pY };
        int up[] = { pX, pY + 1 };
        int left[] = { pX -1, pY };

        directions.add(up);
        directions.add(right);
        directions.add(down);      
        directions.add(left);
        for(int i=0;i<4;i++){
            int xPos = directions.get(i)[0];
            int yPos = directions.get(i)[1];
            
            if(w.isValidPosition(xPos, yPos)){
                validDirections.add(i);
            }
        }
        Random r = new Random();
        int randomVal = r.nextInt(validDirections.size());
        int randDir = (int) validDirections.get(randomVal);
        

        return randDir;
    }
    
    private int getValidRandomAction(World w) {
        Random r = new Random();
        int randomAction;
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        
        boolean stench = w.hasStench(x, y);
        if(stench){
            randomAction = r.nextInt(2);
        }else{
            randomAction = 0;
        }
        return randomAction;
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
