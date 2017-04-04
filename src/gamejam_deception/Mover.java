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
public class Mover {
    
    private int x, y;
    
    public Mover(int startX, int startY){
        this.x = startX;
        this.y = startY;
    }
    
    public void move(int destX, int destY){
        this.x = destX;
        this.y = destY;
    }
    
}
