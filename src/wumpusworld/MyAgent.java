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
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;   
    }
    
    public void printTable(Double[][] t) {
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                System.out.print(t[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
   
            
    /**
     * Asks your solver agent to execute an action.
     */

    public void doAction()
    {
        
//        Q q = new Q(16, 4, 10000, 0.1, this.w);
//        try {
//            q.train();
//        } catch (IOException ex) {
//            Logger.getLogger(MyAgent.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("Trained!");
//        Double[][] table = q.qTable;
//        
//        int stateIndex = (this.w.getPlayerX() - 1) * 4 + this.w.getPlayerY();
//        double max = Collections.max(Arrays.asList(table[stateIndex]));
//        int action = Arrays.asList(table[stateIndex]).indexOf(max);
//        System.out.println(Double.MIN_VALUE + "===========");
//        this.printTable(table);
//        switch (action) {
//                    case 0:
//                        this.w.goDown();
//                        break;
//                    case 1:
//                        this.w.goRight();
//                        break;
//                    case 2:
//                        this.w.goUp();
//                        break;
//                    case 3:
//                        this.w.goLeft();
//                        break;
//                }
        Double[][] qtable = this.readTable();
        
        int stateIndex = (this.w.getPlayerX() - 1) * 4 + this.w.getPlayerY();
        double max = Collections.max(Arrays.asList(qtable[stateIndex]));
        int action = Arrays.asList(qtable[stateIndex]).indexOf(max);
        System.out.println(Double.MIN_VALUE + "===========");
        this.printTable(qtable);
        switch (action) {
                    case 0:
                        this.w.goDown();
                        break;
                    case 1:
                        this.w.goRight();
                        break;
                    case 2:
                        this.w.goUp();
                        break;
                    case 3:
                        this.w.goLeft();
                        break;
                }
    }
    
    private Double[][] readTable() {
        ObjectInputStream objectinputstream = null;
        
        Double[][] qTable = null;
        
        try {
            File in = new File(System.getProperty("user.dir") + "qTable.ser");
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
    
     /**
     * Genertes a random instruction for the Agent.
     */
    public int decideRandomMove()
    {
      return (int)(Math.random() * 4);
    }
    
    
}

