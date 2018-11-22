package wumpusworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan Hagelbäck
 */
public class MyAgent implements Agent
{
    public World w;

    ArrayList<ArrayList<Double>> qtable = null;
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;   
    }
   
            
    /**
     * Asks your solver agent to execute an action.
     */
    public void doAction()
    {
//        if (this.qtable == null) {
//            this.qtable = MyAgent.readTable();
//        }
//
//        this.doTurn();
        
        try {
            this.train();
        } catch (InterruptedException ex) {
            Logger.getLogger(MyAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void doTurn() {
        if (this.w.hasGlitter(this.w.getPlayerX(), this.w.getPlayerY())) {
            this.w.doAction(this.w.A_GRAB);
        } else {
            if (this.w.hasPit(this.w.getPlayerX(), this.w.getPlayerY())) {
                this.w.doAction(this.w.A_CLIMB);
            }
            ArrayList<ArrayList<Double>> qtable = this.qtable;
            int x = this.w.getPlayerX();
            int y = this.w.getPlayerY();
            int stateIndex = Q.getStateIndex(this.w, x, y, this.w.getDirection());
                        
            int bestDirection = -1;
            int[] directs = {0, 1, 2, 3};
            int tempStateIndex = -1;
            double maxVal= Double.NEGATIVE_INFINITY;
            double maxy = Double.NEGATIVE_INFINITY;
            
            for (int direct : directs) {
                if(direct==0 && !this.w.isValidPosition(x, y+1)){
                    continue;
                } else if (direct==1 && !this.w.isValidPosition(x+1, y)){
                    continue;
                } else if (direct==2 && !this.w.isValidPosition(x, y-1)){
                    continue;
                } else if (direct==3 && !this.w.isValidPosition(x-1, y)){
                    continue;
                }
                
                tempStateIndex = Q.getStateIndex(this.w, x, y, direct);
                
//                if (!this.w.hasStench(x, y)){
//                    maxVal = qtable.get(tempStateIndex).get(0);
//                } else {
//                    maxVal = Collections.max(qtable.get(tempStateIndex)); 
//                }

                maxVal = Collections.max(qtable.get(tempStateIndex));
                System.out.println("Direction:"+direct+":"+qtable.get(tempStateIndex));
                
                if (maxVal > maxy) {
                    stateIndex = tempStateIndex;
                    maxy = maxVal;
                    bestDirection = direct;
                }
            }
            int direction = bestDirection;
            int action = qtable.get(stateIndex).indexOf(maxy);
            
            if(!this.w.hasStench(x, y)){
                action=0;
            }
    
            System.out.println("State index: " + qtable.get(stateIndex));

            switch (direction) {
                case 0:
                    System.out.println("GOING up");
                    this.w.turnUp();
                    break;
                case 1:
                    System.out.println("GOING RIGHT");
                    this.w.turnRight();
                    break;
                case 2:
                    System.out.println("GOING down");
                    this.w.turnDown();
                    break;
                case 3:
                    System.out.println("GOING Left");
                    this.w.turnLeft();
                    break;
            }
            
            if (!this.w.hasArrow()){
                action=0;
            }
            
            switch (action) {
                case 0:
                    System.out.println("moving");
                    this.w.moveForward();
                    break;
                case 1:
                    System.out.println("shooting");
                    this.w.shootForward();
                    break;
            }
        }
    }
    
    public void train() throws InterruptedException {
        Q q = new Q(20000, 2, 20000, 0.1, this.w.cloneWorld());
        
        try {
            q.train();
        } catch (IOException ex) {
            Logger.getLogger(MyAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Trained!");
    }
    
    public static ArrayList<ArrayList<Double>> readTable() {
        ObjectInputStream objectinputstream = null;
        
        ArrayList<ArrayList<Double>> qTable = null;
        
//        int mapNr = Integer.parseInt(GUI.getSelectedLevel());
        
        try {
            String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "QTables";
//            File in = new File(path, "qtable" + mapNr + ".ser");
            File in = new File(path, "qtable.ser");
            FileInputStream streamIn = new FileInputStream(in);
            objectinputstream = new ObjectInputStream(streamIn);
            qTable = (ArrayList<ArrayList<Double>>) objectinputstream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(objectinputstream != null){
                try {
                    objectinputstream .close();
                } catch (IOException ex) {
                    Logger.getLogger(MyAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        }
        
        return qTable;
    }    
    
    public void printTable(ArrayList<ArrayList<Double>> t) {
        for (int i = 0; i < t.size(); i++) {
            System.out.print("row: " + i + "    ");
            for (int j = 0; j < t.get(i).size(); j++) {
                System.out.print("index: " + j + " ");
                System.out.print(t.get(i).get(j) + "  ");
            }
            System.out.print("\n");
        }
    }
}

