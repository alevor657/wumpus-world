package wumpusworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    Map<Integer, ArrayList<Double>> map;
    
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
//        if (this.map == null) {
//            System.out.println("Reading existing table");
//            this.map = MyAgent.readTable();
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
            
            Integer stateIndex = Q.getStateIndexEff(this.w);

            ArrayList<Double> state = this.map.get(stateIndex);
            System.out.println(Arrays.toString(this.map.keySet().toArray()));
            System.out.println(this.map.size());
            System.out.println(state);
            System.exit(0);

            double actionValue;

            actionValue = Collections.max(state);
            Integer action = state.indexOf(actionValue);
            
            switch (action) {
            case 0:
                this.w.turnUp();
                this.w.moveForward();
                break;
            case 1:
                this.w.turnRight();
                this.w.moveForward();
                break;
            case 2:
                this.w.turnDown();
                this.w.moveForward();
                break;
            case 3:
                this.w.turnLeft();
                this.w.moveForward();
                break;
            case 4:
                this.w.turnUp();
                this.w.shootForward();
                break;
            case 5:
                this.w.turnRight();
                this.w.shootForward();
                break;
            case 6:
                this.w.turnDown();
                this.w.shootForward();
                break;
            case 7:
                this.w.turnLeft();
                this.w.shootForward();
                break;
            default:
                System.out.println("action != 1 - 7");
                System.exit(1);
            }
        }
    }
    
    public void train() throws InterruptedException {
        Q q = new Q(150000, 0.01, this.w.cloneWorld()); //161243150
        
        try {
            q.train();
        } catch (IOException ex) {
            Logger.getLogger(MyAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Trained!");
    }
    
    public static HashMap<Integer, ArrayList<Double>>readTable() {
        ObjectInputStream objectinputstream = null;
        
        HashMap<Integer, ArrayList<Double>> qTable = null;
                
        try {
            String path = System.getProperty("user.dir") + System.getProperty("file.separator");
            File in = new File(path, "qtable.ser");
            
            if (!in.exists()) {
                System.out.println("File does not exists");
                return new HashMap<>();
            }
            
            FileInputStream streamIn = new FileInputStream(in);
            objectinputstream = new ObjectInputStream(streamIn);
            qTable = (HashMap<Integer, ArrayList<Double>>) objectinputstream.readObject();
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

