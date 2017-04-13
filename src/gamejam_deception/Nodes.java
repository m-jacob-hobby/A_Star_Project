/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star;

//import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.text.DecimalFormat;

/**
 *
 * @author MJHobby
 */
public class Nodes {

    private boolean wall, trap, goal, fakeGoal, open;
    int x, y, destX, destY;
    double heuristic;
    
    public Nodes(int x, int y, int goalX, int goalY){
        this.x = x;
        this.y = y;
        this.destX = goalX;
        this.destY = goalY;
        this.open = true;
        this.wall = false;
        this.trap = false;
        this.goal = false;
        this.fakeGoal = false;
        
        // individual nodes heuristic value (straight line path to goal)
        int dx = destX - x;
        int dy = destY - y;
        this.heuristic = Math.sqrt((dx*dx)+(dy*dy));
    }
    
    // Euclidian calculation of nodes heuristic value
    public double calcHeuristic(){
        int dx = destX - x;
        int dy = destY - y;
        
        return Math.sqrt((dx*dx)+(dy*dy));
    }
    
    public String heuristicDisplay(){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(this.heuristic);
    }
    
    public double getHeuristicValue(){
        return this.heuristic;
    }
    
    public void assignOpen(){
        this.wall = false;
        this.trap = false;
        this.fakeGoal = false;
        this.goal = false;
        this.open = true;
    }
    
    public boolean isOpen(){
        return this.open;
    }
    
    public void assignGoal(){
        this.wall = false;
        this.trap = false;
        this.fakeGoal = false;
        this.open = false;
        this.goal = true;
    }

    public boolean isGoal(){
            return this.goal;
        }
    
    public void assignWall(){
        this.goal = false;
        this.trap = false;
        this.fakeGoal = false;
        this.open = false;
        this.wall = true;
    }
    
    public boolean isWall(){
            return this.wall;
        }
    
    public void assignTrap(){
        this.wall = false;
        this.goal = false;
        this.fakeGoal = false;
        this.open = false;
        this.trap = true;
    }
    
    public boolean isTrap(){
            return this.trap;
        }
    
    public void assignFakeGoal(){
        this.wall = false;
        this.trap = false;
        this.goal = false;
        this.open = false;
        this.fakeGoal = true;
    }
    
    public boolean isFakeGoal(){
            return this.fakeGoal;
        }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
   
}
