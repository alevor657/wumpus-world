package wumpusworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private World w;
    int rnd;
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
//        this.train();
        
        if (this.qtable == null) {
            this.qtable = this.readTable();
        }
        
        this.doTurn();
    }
    
    public void doTurn() {
        ArrayList<ArrayList<Double>> qtable = this.qtable;
        
        int stateIndex = Q.getStateIndex(this.w);
        double max = 0.0;
        
        if (this.w.hasArrow()){
            max = Collections.max(qtable.get(stateIndex));
        }else{
            max = Collections.max(qtable.get(stateIndex).subList(0, 4));
        }
        
        int action = qtable.get(stateIndex).indexOf(max);
        
        this.printTable(qtable);
        
        switch (action) {
            case 0:
                System.out.println("GOING DOWN");
                this.w.turnDown();
                this.w.moveForward();
                break;
            case 1:
                System.out.println("GOING RIGHT");
                this.w.turnRight();
                this.w.moveForward();
                break;
            case 2:
                System.out.println("GOING UP");
                this.w.turnUp();
                this.w.moveForward();
                break;
            case 3:
                System.out.println("GOING LEFT");
                this.w.turnLeft();
                this.w.moveForward();
                break;
            case 4:
                System.out.println("SHOTING DOWN");
                this.w.turnDown();
                this.w.shootForward();
                break;
            case 5:
                System.out.println("SHOTING RIGHT");
                this.w.turnRight();
                this.w.shootForward();
                break;
            case 6:
                System.out.println("SHOTING UP");
                this.w.turnUp();
                this.w.shootForward();
                break;
            case 7:
                System.out.println("SHOTING LEFT");
                this.w.turnLeft();
                this.w.shootForward();
                break;
        }
        
        System.out.println(max);
        
        if (this.w.hasGlitter(this.w.getPlayerX(), this.w.getPlayerY())) {
            this.w.doAction(this.w.A_GRAB);
        }
        
        if (this.w.hasPit(this.w.getPlayerX(), this.w.getPlayerY())) {
            this.w.doAction(this.w.A_CLIMB);
        }
    }
    
    private void train() {
        Q q = new Q(64, 8, 150000, 0.01, this.w.cloneWorld());
        try {
            q.train();
        } catch (IOException ex) {
            Logger.getLogger(MyAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Trained!");
    }
    
    private ArrayList<ArrayList<Double>> readTable() {
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

