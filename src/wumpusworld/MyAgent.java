package wumpusworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    Double[][] qtable = null;
    
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
        this.train();
        
//        if (this.qtable == null) {
//            this.qtable = this.readTable();
//        }
//        
//        this.doTurn();
    }
    
    public void doTurn() {
        Double[][] qtable = this.qtable;
        
        int stateIndex = (this.w.getPlayerX() - 1) * 4 + this.w.getPlayerY() - 1;
        double max = Collections.max(Arrays.asList(qtable[stateIndex]));
        int action = Arrays.asList(qtable[stateIndex]).indexOf(max);
        
//        qtable[stateIndex][action] -= 1.0;
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
        }
        
        System.out.println(max);
        
        for (int i = 0; i < qtable[stateIndex].length; i++) {
            System.out.print(qtable[stateIndex][i] + "  ");
        }
        
        if (this.w.hasGlitter(this.w.getPlayerX(), this.w.getPlayerY())) {
            this.w.doAction(this.w.A_GRAB);
        }
        
        if (this.w.hasPit(this.w.getPlayerX(), this.w.getPlayerY())) {
            this.w.doAction(this.w.A_CLIMB);
        }
    }
    
    private void train() {
        Q q = new Q(16, 4, 10000, 0.1, this.w.cloneWorld());
        try {
            q.train();
        } catch (IOException ex) {
            Logger.getLogger(MyAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Trained!");
    }
    
    private Double[][] readTable() {
        ObjectInputStream objectinputstream = null;
        
        Double[][] qTable = null;
        
        try {
            String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "QTables";
            File in = new File(path, "qtable1.ser");
            FileInputStream streamIn = new FileInputStream(in);
            objectinputstream = new ObjectInputStream(streamIn);
            qTable = (Double[][]) objectinputstream.readObject();
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
    
    public void printTable(Double[][] t) {
        for (int i = 0; i < t.length; i++) {
            System.out.print("row: " + i + "    ");
            for (int j = 0; j < t[i].length; j++) {
                System.out.print(t[i][j] + "  ");
            }
            System.out.print("\n");
        }
    }
}

