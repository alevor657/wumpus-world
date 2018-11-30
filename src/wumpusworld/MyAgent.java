package wumpusworld;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Contans starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan Hagelbäck
 */
public class MyAgent implements Agent
{
    private World w;
    ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
    ArrayList<Integer> Path = new ArrayList<Integer>();
    boolean stillMoving = false;
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;
        this.possibleMoves.add(6);
        this.possibleMoves.add(9);
//        this.possibleMoves.add(14);
    }
    
    public int pitDanger(World w, int x, int y){
//        int danger = 0;
//        
//        if(w.hasBreeze(x, y+1)){
//            danger += 1; 
//        }
//        if(w.hasBreeze(x+1, y)){
//            danger += 1; 
//        }
//        if(w.hasBreeze(x, y-1)){
//            danger += 1; 
//        }
//        if(w.hasBreeze(x-1, y)){
//            danger += 1; 
//        }
//        
//        if(!w.isValidPosition(x, y+1) || !w.isVisited(x, y+1)){
//            if(w.hasBreeze(x+1, y)){
//                danger += 1; 
//            }
//            if(w.hasBreeze(x, y-1)){
//                danger += 1; 
//            }
//            if(w.hasBreeze(x-1, y)){
//                danger += 1; 
//            }
//        }
//        
//        if(!w.isValidPosition(x+1, y) || !w.isVisited(x+1, y)){
//            if(w.hasBreeze(x, y+1)){
//                danger += 1; 
//            }
//            
//            if(w.hasBreeze(x, y-1)){
//                danger += 1; 
//            }
//            if(w.hasBreeze(x-1, y)){
//                danger += 1; 
//            }
//        }
//        
//        if(!w.isValidPosition(x, y-1) || !w.isVisited(x, y-1)){
//            if(w.hasBreeze(x, y+1)){
//                danger += 1; 
//            }
//            if(w.hasBreeze(x+1, y)){
//                danger += 1; 
//            }
//
//            if(w.hasBreeze(x-1, y)){
//                danger += 1; 
//            }
//        }
//        
//        if(!w.isValidPosition(x-1, y) || !w.isVisited(x-1, y)){
//            if(w.hasBreeze(x, y+1)){
//                danger += 1; 
//            }
//            if(w.hasBreeze(x+1, y)){
//                danger += 1; 
//            }
//            if(w.hasBreeze(x, y-1)){
//                danger += 1; 
//            }
//        }
//        
//        return danger;

        int danger = 0;
        if(!w.isValidPosition(x, y+1) || !w.isVisited(x, y+1)){
            danger++;
        }
        if(!w.isValidPosition(x+1, y) || !w.isVisited(x+1, y)){
            danger++;
        }
        if(!w.isValidPosition(x, y-1) || !w.isVisited(x, y-1)){
            danger++;
        }
        if(!w.isValidPosition(x-1, y) || !w.isVisited(x-1, y)){
            danger++;
        }
        
        if(w.hasBreeze(x, y+1)){
            danger++; 
        }
        if(w.hasBreeze(x+1, y)){
            danger++;
        }
        if(w.hasBreeze(x, y-1)){
            danger++;
        }
        if(w.hasBreeze(x-1, y)){
            danger++; 
        }
        
        return danger;
    }
    
    public int wumpusDanger(World w, int x, int y){

        int wumpusDanger = 0;
        if(!w.isValidPosition(x, y+1) || !w.isVisited(x, y+1)){
            wumpusDanger++;
        }else if((w.hasStench(x, y+1) && w.hasStench(x, y-1)) ||
                (w.hasStench(x, y+1) && w.hasStench(x+1, y)) ||
                (w.hasStench(x, y+1) && w.hasStench(x-1, y))){
            
            wumpusDanger = 100;
            return wumpusDanger;

        }
        
        if(!w.isValidPosition(x+1, y) || !w.isVisited(x+1, y)){
            wumpusDanger++;
        }else if((w.hasStench(x+1, y) && w.hasStench(x-1, y)) ||
                (w.hasStench(x+1, y) && w.hasStench(x, y+1)) ||
                (w.hasStench(x+1, y) && w.hasStench(x, y-1))){
            
            wumpusDanger = 100;
            return wumpusDanger;

        }
        if(!w.isValidPosition(x, y-1) || !w.isVisited(x, y-1)){
            wumpusDanger++;
        }else if((w.hasStench(x, y-1) && w.hasStench(x, y+1)) ||
                (w.hasStench(x, y-1) && w.hasStench(x-1, y)) ||
                (w.hasStench(x, y-1) && w.hasStench(x+1, y))){
            
            wumpusDanger = 100;
            return wumpusDanger;

        }
        
        if(!w.isValidPosition(x-1, y) || !w.isVisited(x-1, y)){
            
            wumpusDanger++;
        }else if((w.hasStench(x-1, y) && w.hasStench(x+1, y)) ||
                (w.hasStench(x-1, y) && w.hasStench(x, y+1)) ||
                (w.hasStench(x-1, y) && w.hasStench(x, y-1))){
            
            wumpusDanger = 100;
            return wumpusDanger;

        }
        
        if(w.hasStench(x, y+1)){
            wumpusDanger++; 
        }
        if(w.hasStench(x+1, y)){
            wumpusDanger++;
        }
        if(w.hasStench(x, y-1)){
            wumpusDanger++;
        }
        if(w.hasStench(x-1, y)){
            wumpusDanger++; 
        }
        
        return wumpusDanger;
    }
    
    public void constructPath(int start, int destination){
        int sX = start/4;
        int sY = start%4;
        
        int dX = destination/4;
        int dY = destination%4;
        int tempVal;
        System.out.println("iam in ");
        if(sX == dX && sY == dY){
            System.out.println(start+":"+destination);
            this.Path.add(destination);
            return;
        }
        if(sX == dX){
            System.out.println(start+":"+destination);
            if(sY-dY <0){
                if(w.isVisited(sX, sY + 1)){
                    tempVal = 4*sX+sY+1;
                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX+1, sY)){
                    tempVal = 4*(sX+1)+sY;
                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY-1)){
                    tempVal = 4*(sX)+sY-1;
                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX-1, sY)){
                    tempVal = 4*(sX-1)+sY;
                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }
            }else if(sY-dY >0){
                System.out.println("in 2nd loop");
                if(w.isVisited(sX-1, sY)){
                    tempVal = 4*(sX-1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("in 3rd loop");
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY-1)){
                    tempVal = 4*(sX)+sY-1;
                    this.Path.add(tempVal);
                    System.out.println("in 4th loop");
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX+1, sY)){
                    tempVal = 4*(sX+1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("in 5th loop");
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY + 1)){
                    tempVal = 4*sX+sY+1;
                    this.Path.add(tempVal);
                    System.out.println("in 6th loop");
                    constructPath(tempVal, destination);
                    return;
                }
                
            }
        }else if(sY == dY){
            System.out.println("sX:"+sX+" | sY:"+sY+" | dX:"+dX+" | dY:"+dY);
            if(sX - dX < 0){
                if(w.isVisited(sX, sY + 1)){
                    tempVal = 4*sX+sY+1;
                    this.Path.add(tempVal);
                    System.out.println("iam in 12");
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX+1, sY)){
                    tempVal = 4*(sX+1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("iam in 13");
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX-1, sY)){
                    tempVal = 4*(sX-1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("iam in 14");
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY-1)){
                    tempVal = 4*(sX)+sY-1;
                    this.Path.add(tempVal);
                    System.out.println("iam in 15");
                    constructPath(tempVal, destination);
                    return;
                }
            }else if(sX - dX > 0){
                System.out.println("iam in 21");
                if(w.isVisited(sX, sY-1)){
                    tempVal = 4*(sX)+sY-1;
                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX+1, sY)){
                    tempVal = 4*(sX+1)+sY;
                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX-1, sY)){
                    tempVal = 4*(sX-1)+sY;
                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY + 1)){
                    tempVal = 4*sX+sY+1;
                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }
            }
        }
        return;
    }
    
    public void moveTo(int value){
//        int x = value/4;
//        int y = value%4;
        constructPath(w.getPlayerX()*4+w.getPlayerY(), value);
        
    }
    
    /**
     * Asks your solver agent to execute an action.
     */
    public void doAction()
    {
        //Location of the player
        int X = w.getPlayerX();
        int Y = w.getPlayerY();
        
        //Grab Gold if we can.
        if (w.hasGlitter(X, Y))
        {
            w.doAction(World.A_GRAB);
            return;
        }
        
        //We are in a pit. Climb up.
        if (w.isInPit())
        {
            w.doAction(World.A_CLIMB);
            return;
        }
        if(this.Path.size() == 0){
            this.stillMoving = false;
        }else{
            this.stillMoving = true;
            System.out.println("I am moving");
        }
        if(this.stillMoving){
            int firstVal = this.Path.get(0);
            int xx = firstVal/4;
            int yy = firstVal%4;
            if(X == xx){
                if(Y - yy > 0){
                    w.turnDown();
                    w.moveForward();
                }else if(Y - yy < 0){
                    w.turnUp();
                    w.moveForward();
                }
            }else if(Y == yy){
                if(X - xx > 0){
                    w.turnLeft();
                    w.moveForward();
                }else if(X - xx < 0){
                    w.turnRight();
                    w.moveForward();
                }
            }
            this.Path.remove(0);
            this.possibleMoves.remove(firstVal);
            if(w.isValidPosition(xx+1, yy) && !w.isVisited(xx+1, yy)){
                this.possibleMoves.add(4*(xx+1)+yy);
            }
            if(w.isValidPosition(xx-1, yy) && !w.isVisited(xx-1, yy)){
                this.possibleMoves.add(4*(xx-1)+yy);
            }
            if(w.isValidPosition(xx, yy+1) && !w.isVisited(xx, yy+1)){
                this.possibleMoves.add(4*(xx)+yy+1);
            }
            if(w.isValidPosition(xx, yy-1) && !w.isVisited(xx, yy-1)){
                this.possibleMoves.add(4*(xx)+yy-1);
            }
        }else{
            int safety =0;
            int tempSafety =1000;
            int bestMove = 0;

            for (int i = 0; i < this.possibleMoves.size(); i++) {
                int val = this.possibleMoves.get(i);

                int x = val/4;
                int y = val%4;
                int wumpusSafety = wumpusDanger(w, x, y);
                int pitSafety = pitDanger(w, x, y);
                if(safety<=tempSafety){
                    bestMove = val;
                }
                System.out.println(safety);
            }
            System.out.println("Best move:"+bestMove);
            System.out.println("constructing th path");
            constructPath(4*X+Y, bestMove);
////            moveTo(bestMove);
            System.out.println(4*X+Y);
                        System.out.println(bestMove);

            System.out.println(this.Path);
        }
        
    }
}
