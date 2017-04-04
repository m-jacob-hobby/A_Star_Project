/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamejam_deception;

/**
 *
 * @author MJHobby
 */
public class Node {
 
    int heuristicCost = 0; //Heuristic cost
    int finalCost = 0; //G+H
    int x, y;
    boolean isGoal;
    Node parent; 
        
    Node(int x, int y, boolean goal){
        this.x = x;
        this.y = y;
        this.isGoal = goal;
    }
        
    @Override
    public String toString(){
        return "["+this.x+", "+this.y+"]";
    }
    
    public static void setBlocked(Node n){
        n = null;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public boolean isGoal(){
        return this.isGoal;
    }
}