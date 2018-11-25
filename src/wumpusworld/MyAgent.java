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
            double stateIndex = Q.getStateIndexEff(this.w);
                        
            
        }
    }
    
    public void train() throws InterruptedException {
        Q q = new Q(170000000, 8, 200000, 0.01, this.w.cloneWorld()); //161243150
        
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

