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
public class NavigationPath {
    
    boolean isOpen; // Has it been navigated to yet
    int x, y;       // Its coordinates
    double travelCost; // cost to travel
    
    public NavigationPath(){
        
    }
    
}
