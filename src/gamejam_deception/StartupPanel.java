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
import java.util.PriorityQueue;

/**
 *
 * @author MJHobby
 */
public class StartupPanel {
    
    // for heuristic cost calculations
    private static final int DIAGONAL_COST = 14;
    private static final int VERT_HORIZ_COST = 10;
    
    private int height, width, wallCount;
    private int startX, startY, goalX, goalY;
    private boolean setStart = false;
    private boolean setGoal = false;
    private boolean finished = false;
    private boolean wait = false;
    private int[] wallCoords;
    private String actionMessage;
    private ArrayList<Integer> walls = new ArrayList<Integer>();
    //private JLabel currentAction = new JLabel();
    //private JLabel wallCounter = new JLabel();
    
    //private JButton nextButton;   // If game running, pauses; inactive when paused
    private static JLabel[][] board; // Our game board where we display comp or obstacles
    // Initialize JLabels
    private JLabel currentAction = new JLabel();
    private JLabel wallCounter = new JLabel();
    
    static PriorityQueue<Node> open;
    static boolean closed[][];
    public static Node[][] grid;

    StartupPanel(int h, int w){
        this.height = h;
        this.width = w;
        this.wallCount = h + w + (h+w)/2;
        this.wallCoords = new int[h + w];
        this.grid = new Node[w][h];
    }

    
    public void setFrame(){

        // Set up the main JFrame
        JFrame pane = new JFrame();
        //pane.dispatchEvent(New WindowEvent(pane, WindowEvent.WINDOW_CLOSING));
        pane.setDefaultCloseOperation(pane.EXIT_ON_CLOSE);
        pane.getContentPane();
        pane.setLayout(new GridLayout(2, 2));
        
        // Initialize JPanel
        JPanel grid = new JPanel(new GridLayout(height, width));
        grid.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        JPanel updates = new JPanel(new GridLayout(3, 1));
        JPanel title = new JPanel(new GridLayout(1,1));
        JPanel buttons = new JPanel(new GridLayout(1, 3));
        
        // Initialize the JButtons
        JButton nextButton = new JButton("NEXT");
        JButton buildButton = new JButton("BUILD");
        buildButton.setEnabled(false);
        buildButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //setCourse();
            }
        });

        // Set up wallCounter JLabel
        wallCounter.setText("Walls left to build: " + wallCount);
        wallCounter.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Set up currentAction JLabel
        currentAction.setText(actionMessage);
        currentAction.setFont(new Font("Serif", Font.BOLD, 16));
        currentAction.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add components to the updates JPanel
        updates.add(wallCounter);
        updates.add(currentAction);
        
        // Add buttons to buttons jpanel
        buttons.add(nextButton);
        buttons.add(buildButton);
        
        // Build the grid buttons for wall setting
        for(int i = 0; i < height*width ; i++){
            JButton wallsButton = new JButton(Integer.toString(i%10) + ", " + Integer.toString(i/10));
            wallsButton.setBackground(null);
            wallsButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    setWalls(wallsButton, buildButton);
                }
            });
            grid.add(wallsButton);
        }

        pane.add(updates);
        pane.add(grid);
        pane.add(buttons);
        pane.pack();
        pane.setVisible(true);
        
        this.wait = true;
    }
    
    public void setCourse(){
        
    }
    
    // Check if the input is within the game board boundaries
    public boolean inBounds(int inputNum){
        boolean result = false;
        if((inputNum < this.height || inputNum < this.width) && inputNum > 0) result = true;
        return result; 
    }
    
    /*
        @params:
            current: current node being tested
            t: the next node of the path
            cost: the cost of total travel to current
        
    */
    public void findAndSetCosts(Node current, Node t, int cost){
        // Add to closed if null node
        if(t == null || closed[t.getX()][t.getY()])return;
        
        // Get cost and find if node in open set
        int tFinalCost = t.heuristicCost + cost;
        boolean inOpen = open.contains(t);
        
        if(!inOpen || tFinalCost < t.finalCost){
            t.finalCost = tFinalCost;
            t.parent = current;
            if(!inOpen)open.add(t);
        }
    }
    
    public void setOpen(int moverX, int moverY){
        open.add(grid[moverX][moverY]);
        Node current;
        
        while(true){ 
            current = open.poll();
            if(current == null)break;
            closed[current.getX()][current.getY()]=true; 

            if(current.equals(grid[goalX][goalY])){
                return; 
            } 

            Node n;  
            if(current.getX() - 1 >= 0){
                n = grid[current.getX() - 1][current.getY()];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST); 

                if(current.getY() - 1 >= 0){ //current.y-1 >= 0){                      
                    n = grid[current.getX() - 1][current.getY() - 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST); 
                }

                if(current.getY() + 1 < width){ //current.y+1 < grid[0].length){
                    n = grid[current.getX() - 1][current.getY() + 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST); 
                }
            } 

            if(current.getY() - 1 >= 0){//current.y-1 >= 0){
                n = grid[current.getX()][current.getY() - 1];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST); 
            }

            if(current.getY() + 1 < width){//current.y+1 < grid[0].length){
                n = grid[current.getX()][current.getY() + 1];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST); 
            }

            if(current.getX() + 1 < width){ //current.x+1 < grid.length){
                n = grid[current.getX() + 1][current.getY()];
                findAndSetCosts(current, n, current.finalCost+VERT_HORIZ_COST); 

                if(current.getY() - 1 >= 0){//current.y-1 >= 0){
                    n = grid[current.getX() + 1][current.getY() - 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST); 
                }
                
                if(current.getY() + 1 < width){ //current.y+1 < grid[0].length){
                   n = grid[current.getX() + 1][current.getY() + 1];
                    findAndSetCosts(current, n, current.finalCost+DIAGONAL_COST); 
                }  
            }
        } 
    }
    
    /*
        params:
            @width = width of board
            @height = height of board
            @walls = array of x and y coordinates of walls to be placed on board
        Output debug map of grid
    */
    public void run(int moverX, int moverY, Path navigation){ // int[] walls
            //Reset
           grid = new Node[this.width][this.height];
           closed = new boolean[this.width][this.height];
           open = new PriorityQueue<>((Object o1, Object o2) -> {
                Node c1 = (Node)o1;
                Node c2 = (Node)o2;

                return c1.finalCost < c2.finalCost?-1:
                        c1.finalCost > c2.finalCost?1:0;
            }); 
           
           for(int x=0 ; x < this.width ; ++x){
              for(int y=0 ; y < this.height ; ++y){
                  grid[x][y] = new Node(x, y, false);
                  grid[x][y].setHeuristic(goalX, goalY);
              }
           }
           grid[moverX][moverY].finalCost = 0;
           
           /*
             Set walls cells. Simply set the cell values to null
             for walls cells.
           */
           for(int i=0; i < walls.size() ; i+=2){
               int x = walls.get(i);                                            //walls[i];
               int y = walls.get(i + 1);                                        //walls[i+1];
               grid[x][y] = null;
           }
           
           setOpen(moverX, moverY);
           
           // For DEBUGGING: view map with values, start, end, and walls in console
           System.out.println("Key: ");
           System.out.println(" O  ... Computer start position");
           System.out.println(" X  ... Goal position");
           System.out.println("/// ... Wall");
           System.out.println(" #  ... Node cost");
           //Display initial map
           System.out.println("\nGrid: ");
            for(int x=0; x < this.width ;++x){
                for(int y=0; y < this.height ;++y){
                   if(x == moverX && y == moverY)System.out.print(" O  ");        //Start point
                   else if(x == goalX && y == goalY)System.out.print(" X  ");     //Goal
                   else if(grid[x][y]!=null)System.out.printf("%-3d ", grid[x][y].finalCost);
                   else System.out.print("/// "); 
                }
                System.out.println();
            } 
            System.out.println();
            
            // For DEBUGGING: Outputs the path from end to start that is the shortest traversable path
            int pathCounter = 1;
            if(closed[goalX][goalY]){
               //Trace back the path 
                System.out.println("Computer's Path: ");
                Node current = grid[goalX][goalY];
                //path.push(current);
                navigation.addCoordinates(goalX, goalY);
                System.out.println("1. " + current);
                while(current.parent!=null){
                    pathCounter++;
                    System.out.println(pathCounter + ". " +current.parent);
                    current = current.parent;
                    //path.push(current);
                    navigation.addCoordinates(current.getX(), current.getY());
                } 
                navigation.close();
                System.out.println();
            }else System.out.println("No possible path!");
           
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
