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
        this.wallCount = h + w;
        this.wallCoords = new int[this.wallCount];
    }
    
    public void setFrame(){
        JFrame frame = new JFrame();
        JFrame pane = new JFrame();
        //pane.dispatchEvent(New WindowEvent(pane, WindowEvent.WINDOW_CLOSING));
        pane.setDefaultCloseOperation(pane.EXIT_ON_CLOSE);
        pane.getContentPane();
        pane.setLayout(new GridLayout(1, 2));
        
        JPanel grid = new JPanel(new GridLayout(height, width));
        JPanel updates = new JPanel(new GridLayout(2, 1));
        
        wallCounter.setText("Walls left to build: " + wallCount);
        currentAction.setText(actionMessage);
        
        updates.add(wallCounter);
        updates.add(currentAction);
        
        for(int i = 0; i < height*width ; i++){
            JButton button = new JButton(Integer.toString(i/10) + ", " + Integer.toString(i%10));
            button.setBackground(null);
            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    setWalls(button);
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
    
    public void setWalls(JButton button){
        actionMessage = "";
        
        if(wallCount > 0){
            actionMessage = "Build more walls!";
            if(!button.isBackgroundSet()){
                button.setBackground(Color.BLACK);
                wallCount--;
                if(wallCount == 0) {
                    setStart = true;
                    actionMessage = "Set the start position!";
                }
            } else {
                button.setBackground(null);
                wallCount++;
            }
        }else if(setStart){
            actionMessage = "Set the start position!";
            if(!button.isBackgroundSet()){
                button.setBackground(Color.GREEN);
                setStart = false;
                setGoal = true;
                actionMessage = "Set the goal position!";
            }
        }else if(setGoal){
            actionMessage = "Set the goal position!";
            if(!button.isBackgroundSet()){
                button.setBackground(Color.BLUE);
                setGoal = false;
                setIsFinished(true);
            }
        }
        wallCounter.setText("Walls left to build: " + wallCount);
        currentAction.setText(actionMessage);
        
        this.wait = false;
        
    }
}
