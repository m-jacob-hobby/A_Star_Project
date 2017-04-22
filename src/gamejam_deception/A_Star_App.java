/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a_star;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javafx.scene.media.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.PriorityQueue;
import java.util.ArrayList;

/**
 *
 * @author MJHobby
 */
public class A_Star_App extends JFrame{

    /*
    // for heuristic cost calculations
    private static final int DIAGONAL_COST = 14;
    private static final int VERT_HORIZ_COST = 10;
    
    // open and closed list to track checked and unchecked nodes
    static PriorityQueue<Node> open;
    static boolean closed[][];
    public static Node[][] grid = new Node[5][5];
    
    // Navigation path and computer mover that navigates board
    private static Path navigation = new Path();
    private  Mover computer;
    
    // Game board parameters
    private JButton nextButton;   // If game running, pauses; inactive when paused
    private static JLabel[][] board; // Our game board where we display comp or obstacles
    
    // TODO: these will be set in initialization panel
    public static int width = 10;
    public static int height = 10; // Game board width and height
    public static int compX = 0;
    public static int compY = 0; // Computer start position x and y coordinates
    private static int endX, endY;
    */
    
    /*
    public A_Star_App(){

        // Panels for the board grid, and panel for buttons
        JPanel boardPanel = new JPanel(new GridLayout(height, width));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        JPanel initialPanel = new JPanel();
        boardPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        initialPanel.setBorder(BorderFactory.createEmptyBorder());
        
        board = new JLabel[height][width];
        
        // default computer start position
        computer = new Mover(compX, compY);
        
        // loop through y coord of board to initialize board spaces
        for(int x =0 ; x < width ; x++){
            for(int y = 0; y < height ; y++){
                board[x][y] = new JLabel(" ", JLabel.CENTER);   // 'blank' space filled in for each piece of the board
                board[x][y].setOpaque(true);
                boardPanel.add(board[x][y]);                    // add the new empty space to our boardPanel
            }
        }

        // initialize the start & stop buttons and place on the buttonPanel
        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                move();
            }
        });
        nextButton.setEnabled(true);
        buttonPanel.add(nextButton);

        
        // Add contents to the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);
        mainPanel.add(initialPanel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set the app to be visible
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawBoard();

    }
    */
     
    /*
    // Check if the input is within the game board boundaries
    public static boolean inBounds(int inputNum){
        boolean result = false;
        if((inputNum < height || inputNum < width) && inputNum > 0) result = true;
        return result; 

    }
    */
    
    /*
        @params:
            current: current node being tested
            t: the next node of the path
            cost: the cost of total travel to current
        
    */
    /*
    static void findAndSetCosts(Node current, Node t, int cost){
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
    */
    
    /*
    public static void setOpen(){
        open.add(grid[compX][compY]);
        Node current;
        
        while(true){ 
            current = open.poll();
            if(current == null)break;
            closed[current.getX()][current.getY()]=true; 

            if(current.equals(grid[endX][endY])){
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
    */
    
    // Display player, walls, and other pieces
    /*
    public void drawBoard(){

        for (int x = 0 ; x < width ; x++){
            for (int y = 0 ; y < height ; y++){
                board[x][y].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if (x == compX && y == compY){
                    board[x][y].setText("START");
                    board[x][y].setBackground(Color.YELLOW);
                }
                else if(grid[x][y] == null) board[x][y].setBackground(Color.BLACK); //.setText("#");
                else if(x == endX && y == endY ) {
                    board[x][y].setText("GOAL");
                    board[x][y].setBackground(Color.RED);
                }else if (computer.getX() == x && computer.getY() == y) {
                    board[x][y].setBackground(Color.GREEN);
                    board[x][y].setText("COMP");
                }
                else {
                    board[x][y].setBackground(Color.BLUE);
                    board[x][y].setText("");
                }
            }
        }
   
    }
    */
    
    /*
        params:
            @width = width of board
            @height = height of board
            @walls = array of x and y coordinates of walls to be placed on board
        Output debug map of grid
    */
    /*
    public static void run(int width, int height, ArrayList<Integer> walls){ // int[] walls
            //Reset
           grid = new Node[width][height];
           closed = new boolean[width][height];
           open = new PriorityQueue<>((Object o1, Object o2) -> {
                Node c1 = (Node)o1;
                Node c2 = (Node)o2;

                return c1.finalCost < c2.finalCost?-1:
                        c1.finalCost > c2.finalCost?1:0;
            }); 
           
           for(int x=0 ; x < width ; ++x){
              for(int y=0 ; y < height ; ++y){
                  grid[x][y] = new Node(x, y, false);
                  grid[x][y].setHeuristic(endX, endY);
              }
           }
           grid[compX][compY].finalCost = 0;
           
             //Set walls cells. Simply set the cell values to null
             //for walls cells.
           for(int i=0; i < walls.size() ; i+=2){
               int x = walls.get(i);                                            //walls[i];
               int y = walls.get(i + 1);                                        //walls[i+1];
               grid[x][y] = null;
           }
           
           setOpen();
           
           // For DEBUGGING: view map with values, start, end, and walls in console
           System.out.println("Key: ");
           System.out.println(" O  ... Computer start position");
           System.out.println(" X  ... Goal position");
           System.out.println("/// ... Wall");
           System.out.println(" #  ... Node cost");
           //Display initial map
           System.out.println("\nGrid: ");
            for(int x=0; x < width ;++x){
                for(int y=0; y < height ;++y){
                   if(x == compX && y == compY)System.out.print(" O  ");        //Start point
                   else if(x == endX && y == endY)System.out.print(" X  ");     //Goal
                   else if(grid[x][y]!=null)System.out.printf("%-3d ", grid[x][y].finalCost);
                   else System.out.print("/// "); 
                }
                System.out.println();
            } 
            System.out.println();
            
            // For DEBUGGING: Outputs the path from end to start that is the shortest traversable path
            int pathCounter = 1;
            if(closed[endX][endY]){
               //Trace back the path 
                System.out.println("Computer's Path: ");
                Node current = grid[endX][endY];
                //path.push(current);
                navigation.addCoordinates(endX, endY);
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
    */
    
    /*
    public void move(){
        if(!navigation.isEnd()){
            navigation.getNext();
            computer.move(navigation.getX(), navigation.getY());
        } else {
            nextButton.setEnabled(false);
        }
        drawBoard();
    }
    */

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Path navigation = new Path();
        Mover computer;

        int width = 15;
        int height = 15;
        
        StartupPanel initialize = new StartupPanel(height, width);              // Builds the initial panel. This panel will prompt
                                                                                // the user to determine start, goal and all wall blocks        
        while(!initialize.getIsFinished()){                                     // that will be utilized. Runs until finished and won't        
            if (!initialize.getWait())                                          // setFrame until after user selects next block.
                initialize.setFrame();
        }
        
        //ArrayList<Integer> wallList = initialize.getWalls();
        //compX = initialize.getStartX();                                         // Start and end coordinates from the initial panel
        //compY = initialize.getStartY();                                         // and selected by the users
        //endX = initialize.getGoalX();
        //endY = initialize.getGoalY();
        
        computer = new Mover(initialize.getStartX(), initialize.getStartY());
        
        initialize.run(computer.getX(), computer.getY(), navigation);
        
        A_Star_App d = new A_Star_App();
        
    }
    
}
