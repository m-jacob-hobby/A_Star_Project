/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 *
 * @author MJHobby
 */
public class StartupPanel {
    
    private int height, width, wallCount;
    private int startX, startY, goalX, goalY;
    private boolean setStart = false;
    private boolean setGoal = false;
    private boolean finished = false;
    private boolean wait = false;
    private int[] wallCoords;
    private String actionMessage;
    private ArrayList<Integer> walls = new ArrayList<Integer>();
    private JLabel currentAction = new JLabel();
    private JLabel wallCounter = new JLabel();

    StartupPanel(int h, int w){
        this.height = h;
        this.width = w;
        this.wallCount = h + w + (h+w)/2;
        this.wallCoords = new int[h + w];
    }
    
    public void setFrame(){

        JFrame pane = new JFrame();
        //pane.dispatchEvent(New WindowEvent(pane, WindowEvent.WINDOW_CLOSING));
        pane.setDefaultCloseOperation(pane.HIDE_ON_CLOSE);
        pane.getContentPane();
        pane.setLayout(new GridLayout(1, 2));
        
        JPanel grid = new JPanel(new GridLayout(height, width));
        JPanel updates = new JPanel(new GridLayout(3, 1));
        
        JButton build = new JButton("BUILD");
        build.setEnabled(false);
        build.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //pane.dispose();
            }
        });
        
        wallCounter.setText("Walls left to build: " + wallCount);
        wallCounter.setHorizontalAlignment(SwingConstants.CENTER);
        currentAction.setText(actionMessage);
        currentAction.setFont(new Font("Serif", Font.BOLD, 16));
        currentAction.setHorizontalAlignment(SwingConstants.CENTER);
        
        updates.add(wallCounter);
        updates.add(currentAction);
        updates.add(build);
        
        for(int i = 0; i < height*width ; i++){
            JButton button = new JButton(Integer.toString(i%10) + ", " + Integer.toString(i/10));
            button.setBackground(null);
            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    setWalls(button, build);
                }
            });
            grid.add(button);
        }

        pane.add(updates);
        pane.add(grid);
        pane.pack();
        pane.setVisible(true);
        
        this.wait = true;
    }
    
    public void setIsFinished(boolean value){
        this.finished = value;
    }
    
    public boolean getIsFinished(){
        return this.finished;
    }
    
    
    public boolean getWait(){
        return this.wait;
    }
    
    // Select button associated with grids that will set 
    // values to grid for traversing
    public void setWalls(JButton button, JButton build){
        actionMessage = "";                                                     // Informs the user what current block types they are selecting
        
        if(wallCount > 0){
            actionMessage = "Build more walls!";
            if(!button.isBackgroundSet()){                                      // User selected grid to be a new wall space
                button.setBackground(Color.BLACK);
                wallCount--;
                walls.add(getXCoordinate(button.getText()));                    // add next x coordinate
                walls.add(getYCoordinate(button.getText()));                    // add next y coordinate
                if(wallCount == 0) {
                    setStart = true;
                    actionMessage = "Set the start position!";
                }
            } else {                                                            // User wants to change wall back to traversable grid
                button.setBackground(null);
                walls.remove(walls.size() - 1);                                 // most recent y
                walls.remove(walls.size() - 2);                                 // most recent x
                wallCount++;
            }
        }else if(setStart){                                                     // User selects start position
            actionMessage = "Set the start position!";
            if(!button.isBackgroundSet()){
                button.setBackground(Color.GREEN);
                setStart = false;
                setGoal = true;
                actionMessage = "Set the goal position!";
                startX = getXCoordinate(button.getText());                      // Assign starting x coord
                startY = getYCoordinate(button.getText());                      // Assign starting y coord
            }
        }else if(setGoal){                                                      // User selects goal position
            actionMessage = "Set the goal position!";
            if(!button.isBackgroundSet()){
                button.setBackground(Color.BLUE);
                setGoal = false;
                goalX = getXCoordinate(button.getText());                       // Assign goal x coord
                goalY = getYCoordinate(button.getText());                       // Assign goal y coord
                build.setEnabled(true);
                setIsFinished(true);
            }
        }
        wallCounter.setText("Walls left to build: " + wallCount);
        currentAction.setText(actionMessage);
        
        this.wait = false;
        
    }
    
    public int getXCoordinate(String coordinates){
        String value = "" + coordinates.charAt(0);
        return Integer.parseInt(value);
    }
    
    public int getYCoordinate(String coordinates){
        String value = "" + coordinates.charAt(coordinates.length()-1);
        return Integer.parseInt(value);
    }
    
    public int getStartX(){
        return startX;
    }
    
    public int getStartY(){
        return startY;
    }
    
    public int getGoalX(){
        return goalX;
    }
    
    public int getGoalY(){
        return goalY;
    }
    
    public ArrayList<Integer> getWalls(){
        return walls;
    }
    
}
