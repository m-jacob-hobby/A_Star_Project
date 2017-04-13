/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star;

import java.util.ArrayList;

/**
 *
 * @author MJHobby
 */
public class Path {
    private ArrayList<Integer> coordinates = new ArrayList<Integer>();
    private int position, x, y;
    private boolean end;
    
    public Path(){
        position = 0;
        
    }
    
public void addCoordinates(int x, int y){
    this.coordinates.add(x);
    this.coordinates.add(y);
}

public void close(){
    this.position = this.coordinates.size()-1;
}

public void getNext(){

    if (this.position >= 1){
        this.y = coordinates.get(position);
        this.x = coordinates.get(position - 1);
    
        this.position -= 2;
    } else {
        this.position = 0;
        this.end = true;
    }
    
}

public boolean isEnd(){
    return this.end;
}

public int getX(){
    return this.x;
}

public int getY(){
    return this.y;
}
    
    
}
