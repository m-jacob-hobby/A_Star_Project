/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamejam_deception;

import java.text.DecimalFormat;

/**
 *
 * @author MJHobby
 */
public class Path {
    private int x, y, curX, curY;
    private double curCost, totalTravel;
    
    public Path(){
        
    }
    
    public double getCost(Nodes n){
        return this.curCost + n.getHeuristicValue();
    }
    
    public void updateTotalCost(double n){
        this.curCost = n;
    }
    
    public String displayPathCost(){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(this.curCost);
    }
    
    public void updateTravel(Nodes n, int curX, int curY){
        double destX = n.getX();
        double destY = n.getY();
        
        double dx = destX - curX;
        double dy = destY - curY;
        
        this.totalTravel += Math.sqrt((dx*dx)+(dy*dy));
        
    }
    
    public String displayTravel(){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(this.totalTravel);
    }
    
    
}
