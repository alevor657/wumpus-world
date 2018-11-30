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
//        this.possibleMoves.add(5);
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
            danger=danger+2; 
        }
        if(w.hasBreeze(x+1, y)){
            danger=danger+2; 
        }
        if(w.hasBreeze(x, y-1)){
            danger=danger+2; 
        }
        if(w.hasBreeze(x-1, y)){
            danger=danger+2;  
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
            wumpusDanger++; 
            wumpusDanger++; 
        }
        if(w.hasStench(x+1, y)){
            wumpusDanger++;
            wumpusDanger++; 
            wumpusDanger++; 
        }
        if(w.hasStench(x, y-1)){
            wumpusDanger++;
            wumpusDanger++; 
            wumpusDanger++; 
        }
        if(w.hasStench(x-1, y)){
            wumpusDanger++; 
            wumpusDanger++; 
            wumpusDanger++; 
        }
        
        return wumpusDanger;
    }
    
    public void constructPath(int start, int destination){
        if(start-4 == destination || start+4 == destination||start+1 == destination||start-1 == destination){
            return;
        }
        
        int sX = start/4;
        int sY = start%4;
        
        int dX = destination/4;
        int dY = destination%4;
        int tempVal;
//        System.out.println("COnstructing");
//        System.out.println(start+":"+destination);
        if(sX == dX && sY == dY){
//            System.out.println(start+":"+destination);
//            this.Path.add(destination);
//            System.out.println(this.Path);
            return;
        }
        if(sX == dX){
            System.out.println(sY+"X==X"+dY);
            if(sY-dY <0){
                if(w.isVisited(sX, sY + 1) && this.Path.contains(4*sX+sY+1)==false){
                    tempVal = 4*sX+sY+1;
                    this.Path.add(tempVal);
                    System.out.println("adding: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
                    
                    System.out.println("1");
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX+1, sY) && this.Path.contains(4*(sX+1)+sY)==false){
                    tempVal = 4*(sX+1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("adding: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
                    System.out.println(this.Path);
                                        System.out.println("2");

                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY-1) && this.Path.contains(4*(sX)+sY-1)==false){
                    tempVal = 4*(sX)+sY-1;
                    this.Path.add(tempVal);
                    System.out.println("adding: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
                    System.out.println("3");
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX-1, sY) && this.Path.contains(4*(sX-1)+sY)==false){
                    tempVal = 4*(sX-1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("adding: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
                    System.out.println("4");
                    constructPath(tempVal, destination);
                    return;
                }
            }else if(sY-dY >0){
                if(w.isVisited(sX-1, sY) && this.Path.contains(4*(sX-1)+sY)==false){
                    tempVal = 4*(sX-1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("adding: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY-1) && this.Path.contains(4*(sX)+sY-1)==false){
                    tempVal = 4*(sX)+sY-1;
                    this.Path.add(tempVal);
                    System.out.println("adding: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX+1, sY) && this.Path.contains(4*(sX+1)+sY)==false){
                    tempVal = 4*(sX+1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("adding: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY + 1) && this.Path.contains(4*sX+sY+1)==false){
                    tempVal = 4*sX+sY+1; 
                    this.Path.add(tempVal);
                    System.out.println("adding: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }
                
            }
        }else if(sY == dY){
//            System.out.println("sX:"+sX+" | sY:"+sY+" | dX:"+dX+" | dY:"+dY);
            if(sX - dX < 0){
                if(w.isVisited(sX, sY + 1) && this.Path.contains(4*sX+sY+1)==false){
                    tempVal = 4*sX+sY+1;
                    this.Path.add(tempVal);
                    System.out.println("adding1: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX+1, sY) && this.Path.contains(4*(sX+1)+sY)==false){
                    tempVal = 4*(sX+1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("adding2: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX-1, sY) && this.Path.contains(4*(sX-1)+sY)==false){
                    tempVal = 4*(sX-1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("adding3: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY-1) && this.Path.contains(4*(sX)+sY-1)==false){
                    System.out.println("sX:"+sX+" | sY:"+sY+" | dX:"+dX+" | dY:"+dY);
                    tempVal = 4*(sX)+sY-1;
                    this.Path.add(tempVal);
                    System.out.println("adding4: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }

                    constructPath(tempVal, destination);
                    return;
                }
            }else if(sX - dX > 0){
                if(w.isVisited(sX, sY-1) && this.Path.contains(4*(sX)+sY-1)==false){
                    tempVal = 4*(sX)+sY-1;
                    this.Path.add(tempVal);
                    System.out.println("adding5: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX+1, sY) && this.Path.contains(4*(sX+1)+sY)==false){
                    tempVal = 4*(sX+1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("adding6: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX-1, sY) && this.Path.contains(4*(sX-1)+sY)==false){
                    tempVal = 4*(sX-1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("adding7: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY + 1) && this.Path.contains(4*sX+sY+1)==false){
                    tempVal = 4*sX+sY+1;
                    this.Path.add(tempVal);
                    System.out.println("adding8: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);
                    constructPath(tempVal, destination);
                    return;
                }
            }
        }else{
            if(w.isVisited(sX, sY-1) && this.Path.contains( 4*(sX)+sY-1)==false){
                    tempVal = 4*(sX)+sY-1;
                    this.Path.add(tempVal);
                    System.out.println("adding9: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
                    
                    System.out.println(this.Path);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX+1, sY) && this.Path.contains(4*(sX+1)+sY)==false){
                    tempVal = 4*(sX+1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("adding10: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);System.out.println(this.Path);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX-1, sY) && this.Path.contains(4*(sX-1)+sY)==false){
                    tempVal = 4*(sX-1)+sY;
                    this.Path.add(tempVal);
                    System.out.println("adding11: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);System.out.println(this.Path);
                    constructPath(tempVal, destination);
                    return;
                }else if(w.isVisited(sX, sY + 1) && this.Path.contains(4*sX+sY+1)==false){
                    tempVal = 4*sX+sY+1;
                    this.Path.add(tempVal);
                    System.out.println("adding12: "+tempVal);
                    if(tempVal-4 == destination || tempVal+4 == destination||tempVal+1 == destination||tempVal-1 == destination){
                        return;
                    }
//                    this.Path.add(tempVal);System.out.println(this.Path);
                    constructPath(tempVal, destination);
                    return;
                }
        }
        
    }
    
    public void addPossibleMoves(int value){
        int xx = value/4;
        int yy = value%4;
        
            if(w.isValidPosition(xx+1, yy) && !w.isVisited(xx+1, yy)){
                if(this.possibleMoves.indexOf(4*(xx+1)+yy)== -1){
                    this.possibleMoves.add(4*(xx+1)+yy);
//                    System.out.println("Added:"+(4*(xx+1)+yy));
                }
               
            }
            if(w.isValidPosition(xx-1, yy) && !w.isVisited(xx-1, yy)){
                if(this.possibleMoves.indexOf(4*(xx-1)+yy)== -1){
                    this.possibleMoves.add(4*(xx-1)+yy);
//                    System.out.println("Added:"+(4*(xx-1)+yy));
                }
                
            }
            if(w.isValidPosition(xx, yy+1) && !w.isVisited(xx, yy+1)){
                if(this.possibleMoves.indexOf(4*(xx)+yy+1)== -1){
                    this.possibleMoves.add(4*(xx)+yy+1);
//                    System.out.println("Added:"+(4*(xx)+yy+1));
                }
                
            }
            if(w.isValidPosition(xx, yy-1) && !w.isVisited(xx, yy-1)){
                if(this.possibleMoves.indexOf(4*(xx)+yy-1)== -1){
                    this.possibleMoves.add(4*(xx)+yy-1);
//                    System.out.println("Added:"+(4*(xx)+yy-1));
                }
                
            }
        
    }
    
    public void makeMove(){
        int firstVal = this.Path.get(0);
        int X = w.getPlayerX();
        int Y = w.getPlayerY();
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
        }else{
            System.out.println("X:"+X+" |Y:"+Y+" | xx:"+xx+" |yy:"+yy);
        }
        this.Path.remove(0);
        if(this.possibleMoves.indexOf(firstVal) !=-1){
            this.possibleMoves.remove(this.possibleMoves.indexOf(firstVal));
        }else{
            System.out.println(firstVal);
        }

        addPossibleMoves(firstVal);
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
            
        }
        if(this.stillMoving){
            makeMove();
//            System.out.println("I am moving");
//            int firstVal = this.Path.get(0);
//            int xx = firstVal/4;
//            int yy = firstVal%4;
//            if(X == xx){
//                if(Y - yy > 0){
//                    w.turnDown();
//                    w.moveForward();
//                }else if(Y - yy < 0){
//                    w.turnUp();
//                    w.moveForward();
//                }
//            }else if(Y == yy){
//                if(X - xx > 0){
//                    w.turnLeft();
//                    w.moveForward();
//                }else if(X - xx < 0){
//                    w.turnRight();
//                    w.moveForward();
//                }
//            }else{
//                System.out.println("X:"+X+" |Y:"+Y+" | xx:"+xx+" |yy:"+yy);
//            }
//            this.Path.remove(0);
//            if(this.possibleMoves.indexOf(firstVal) !=-1){
//                this.possibleMoves.remove(this.possibleMoves.indexOf(firstVal));
//            }else{
//                System.out.println(firstVal);
//            }
//            
//            addPossibleMoves(firstVal);
        }else{
            int safety =0;
            int tempSafety =1000;
            int bestMove = 0;
            int shootMove=0;
            int wumpusSafety=0;
            for (int i = 0; i < this.possibleMoves.size(); i++) {
                int val = this.possibleMoves.get(i);

                int x = val/4;
                int y = val%4;
                if(w.wumpusAlive()){
                    wumpusSafety = wumpusDanger(w, x, y);
                }else{
                    wumpusSafety = 0;
                }
                
                int pitSafety = pitDanger(w, x, y);
                if(wumpusSafety == 100){
                    shootMove = val;
                    break;
                }
                if(pitSafety+wumpusSafety<=tempSafety){
                    bestMove = val;
                    tempSafety = pitSafety+wumpusSafety;
                }
                System.out.println(x+","+y+":"+(pitSafety+wumpusSafety));
            }
            if(wumpusSafety == 100){
                int shootX = shootMove/4;
                int shootY = shootMove%4;

                if(X+1 == shootX && Y == shootY){
                    w.turnRight();
                    w.shootForward();
                    
                }else if(X-1 == shootX && Y == shootY){
                    w.turnLeft();
                    w.shootForward();
                    
                }else if(X == shootX && Y+1 == shootY){
                    w.turnUp();
                    w.shootForward();
       
                }else if(X == shootX && Y-1 == shootY){
                    w.turnDown();
                    w.shootForward();
                
                    
                }
            }else{
                System.out.println("Best move:"+bestMove);
    //            System.out.println("constructing the path");
                constructPath(4*X+Y, bestMove);
                this.Path.add(bestMove);
                System.out.println("Added:"+bestMove);
                
                int bestX = bestMove/4;
                int bestY = bestMove%4;
                int moveIndex = this.possibleMoves.indexOf(bestMove);
                int pathIndex = this.Path.indexOf(bestMove);
                if(X+1 == bestX && Y == bestY){
                    w.turnRight();
                    w.moveForward();
                    addPossibleMoves(4*(X+1)+Y);
                    this.possibleMoves.remove(moveIndex);
                    this.Path.remove(pathIndex);
                }else if(X-1 == bestX && Y == bestY){
                    w.turnLeft();
                    w.moveForward();
                    addPossibleMoves(4*(X-1)+Y);
                    this.possibleMoves.remove(moveIndex);
                    this.Path.remove(pathIndex);
                }else if(X == bestX && Y+1 == bestY){
                    w.turnUp();
                    w.moveForward();
                    addPossibleMoves(4*X+Y+1);
                    this.possibleMoves.remove(moveIndex);
                    this.Path.remove(pathIndex);
                }else if(X == bestX && Y-1 == bestY){
                    w.turnDown();
                    w.moveForward();
                    addPossibleMoves(4*X+Y-1);
                    this.possibleMoves.remove(moveIndex);
                    this.Path.remove(pathIndex);
                }else{
                    makeMove();
//                    int firstVal = this.Path.get(0);
//                    int xx = firstVal/4;
//                    int yy = firstVal%4;
//                    if(X == xx){
//                        if(Y - yy > 0){
//                            w.turnDown();
//                            w.moveForward();
//                        }else if(Y - yy < 0){
//                            w.turnUp();
//                            w.moveForward();
//                        }
//                    }else if(Y == yy){
//                        if(X - xx > 0){
//                            w.turnLeft();
//                            w.moveForward();
//                        }else if(X - xx < 0){
//                            w.turnRight();
//                            w.moveForward();
//                        }
//                    }else{
//                        System.out.println("X:"+X+" |Y:"+Y+" | xx:"+xx+" |yy:"+yy);
//                    }
//                    this.Path.remove(0);
//                    if(this.possibleMoves.indexOf(firstVal) !=-1){
//                        this.possibleMoves.remove(this.possibleMoves.indexOf(firstVal));
//                    }else{
//                        System.out.println(firstVal);
//                    }
//
//                    addPossibleMoves(firstVal);
                }
            }
////            moveTo(bestMove);

            
        }
        System.out.println("Path:"+this.Path);
        System.out.println("Possible Moves"+this.possibleMoves);
    }
}
