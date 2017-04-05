/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamejam_deception;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javafx.scene.media.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author MJHobby
 */
public class GameJam_Deception extends JFrame implements ActionListener {
    
    private JLabel[][] board; // Our game board where we display comp or obstacles
    
    
    // DeceptionDungeon Content //
    // -------------------------------------------------------------------------
    public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;
    // open and closed list to track checked and unchecked nodes
    static PriorityQueue<Node> open;
    static boolean closed[][];
    //Blocked nodes are just null Node values in grid
    public static Node[][] grid = new Node[5][5];
    
    //private static int startX, startY;
    private static int endX, endY;
    
    /*
        params:
            @current: current node being tested
            @t: the next node of the path
            @cost: the cost of total travel to current
        
    */
    static void findAndSetCosts(Node current, Node t, int cost){
        // Add to closed if null node
        if(t == null || closed[t.x][t.y])return;
        
        // Get cost and find if node in open set
        int tFinalCost = t.heuristicCost+cost;
        boolean inOpen = open.contains(t);
        
        if(!inOpen || tFinalCost < t.finalCost){
            t.finalCost = tFinalCost;
            t.parent = current;
            if(!inOpen)open.add(t);
        }
    }
    // -------------------------------------------------------------------------
    
    private Path navigation = new Path();
    private  Mover computer;
    
    private JButton startButton,  // If game has been paused, selecting will resume (inactive when running)
                    stopButton;   // If game running, pauses; inactive when paused
    
    public static int width = 10;
    public static int height = 10; // Game board width and height
    public static int compX = 0;
    public static int compY = 0; // Computer start position x and y coordinates
    private boolean play ,pause;
    
    public GameJam_Deception(){

        
        // Panels for the board grid, and panel for buttons
        JPanel boardPanel = new JPanel(new GridLayout(height, width));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        
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
        startButton = new JButton("PLAY");
        stopButton = new JButton("PAUSE");
        startButton.addActionListener(this);
        stopButton.addActionListener(this);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        
        // Add contents to the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set the app to be visible
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawBoard();
        
        //Timer timer = new Timer();
        //timer.schedule(drawBoard(), 0, 5000);
    }
    
    public static boolean inBounds(int inputNum){
        if((inputNum < height || inputNum < width) && inputNum > 0) return true;
        else return false; 

    }
    
    public static void setOpen(){
        open.add(grid[compX][compY]);
        Node current;
        
        while(true){ 
            current = open.poll();
            if(current==null)break;
            closed[current.getX()][current.getY()]=true; 

            if(current.equals(grid[endX][endY])){
                return; 
            } 

            Node t;  
            if(current.getX() - 1 >= 0){
                t = grid[current.getX()-1][current.getY()];
                findAndSetCosts(current, t, current.finalCost+V_H_COST); 

                if(current.getY() - 1 >= 0){ //current.y-1 >= 0){                      
                    t = grid[current.getX() - 1][current.getY() - 1];
                    findAndSetCosts(current, t, current.finalCost+DIAGONAL_COST); 
                }

                if(current.getY() + 1 < width){ //current.y+1 < grid[0].length){
                    t = grid[current.getX() - 1][current.getY() + 1];
                    findAndSetCosts(current, t, current.finalCost+DIAGONAL_COST); 
                }
            } 

            if(current.getY() - 1 >= 0){//current.y-1 >= 0){
                t = grid[current.getX()][current.getY() - 1];
                findAndSetCosts(current, t, current.finalCost+V_H_COST); 
            }

            if(current.getY() + 1 < width){//current.y+1 < grid[0].length){
                t = grid[current.getX()][current.getY() + 1];
                findAndSetCosts(current, t, current.finalCost+V_H_COST); 
            }

            if(current.getX() + 1 < width){ //current.x+1 < grid.length){
                t = grid[current.getX() + 1][current.getY()];
                findAndSetCosts(current, t, current.finalCost+V_H_COST); 

                if(current.getY() - 1 >= 0){//current.y-1 >= 0){
                    t = grid[current.getX() + 1][current.getY() - 1];
                    findAndSetCosts(current, t, current.finalCost+DIAGONAL_COST); 
                }
                
                if(current.getY() + 1 < width){ //current.y+1 < grid[0].length){
                   t = grid[current.getX() + 1][current.getY() + 1];
                    findAndSetCosts(current, t, current.finalCost+DIAGONAL_COST); 
                }  
            }
        } 
    }
    
    // Display player, walls, and other pieces
    public void drawBoard(){

        for (int x = 0 ; x < width ; x++){
            for (int y = 0 ; y < height ; y++){
                board[x][y].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if (compX == x && compY == y) {
                    board[x][y].setBackground(Color.GREEN);
                    board[x][y].setText("START");
                }
                else if(grid[x][y] == null) board[x][y].setBackground(Color.BLACK); //.setText("#");
                else if(x == endX && y == endY ) {
                    board[x][y].setText("GOAL");
                    board[x][y].setBackground(Color.RED);
                } //.setBackground(Color.BLUE) ;//          //nodes[x][y].isGoal()
                else {
                    board[x][y].setBackground(Color.BLUE);
                    // For debugging node heuristic values:
                    //board[x][y].setText(nodes[x][y].heuristicDisplay());
                }
            }
        }
        
        if(closed[endX][endY]){
               //Trace back the path 
                Node current = grid[endX][endY];
                while(current.parent!=null){
                    board[current.getX()][current.getY()].setBackground(Color.GRAY);
                    current = current.parent;
                } 
           }else System.out.println("No possible path");
        
    }
    
    
    // TODO: implement sound effects
    public void playSound(String soundFilePath, boolean isMP3) {
        
    // .wav files
    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("D:/MusicPlayer/fml.mp3").getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch(Exception ex) {
        System.out.println("Error with playing sound.");
        ex.printStackTrace();
    }
    
    // mp3 files
    /*
    String bip = "bip.mp3";
    Media hit = new Media(new File(bip).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(hit);
    mediaPlayer.play();
    */
    
}
    
    public void actionPerformed(ActionEvent e){
        // TODO: implement move call on computer character
        if (e.getSource() == startButton){
            play = true;
            pause = false;
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
        
        if (e.getSource() == stopButton){
            play = false;
            pause = true;
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
        // comp.move();
        /*
            int curX = comp.getX();
            int curY = comp.getY();
            int[] nextMove new int[](2)?; // size 2, the x & y coordinates of cheapest node
            // assign the current position node cost to lowestCost (it will be guaranteed to be larger than
            // neighboring nodes, unless current node is exit)
            int lowestCost = Nodes[curX][curY].cost(); 
        
            // Scan eight surrounding nodes for one with lowest value
            for(int x = curX - 1; x <= curX + 1 ; x++){
                for(int y = curY + 1 ; y <= curY - 1 ; y--){
                    int nodeCost = Nodes[x][y].cost();
                    if(nodeCost < lowestCost){
                        nextMove[0] = x;
                        nextMove[1] = y;
                    }
                }
            }
        
        */
        
        drawBoard();
        
    }
    
    /*
        params:
            @width = width of board
            @height = height of board
            @walls = array of x and y coordinates of walls to be placed on board
        Output debug map of grid
    */
    public static void run(int width, int height, int[] walls){ //, int startX, int startY, int goalX, int goalY
            //Reset
           grid = new Node[width][height];
           closed = new boolean[width][height];
           open = new PriorityQueue<>((Object o1, Object o2) -> {
                Node c1 = (Node)o1;
                Node c2 = (Node)o2;

                return c1.finalCost < c2.finalCost?-1:
                        c1.finalCost > c2.finalCost?1:0;
            }); 
           
           for(int i=0 ; i < width ; ++i){
              for(int j=0 ; j < height ; ++j){
                  grid[i][j] = new Node(i, j, false);
                  grid[i][j].setHeuristic(endX, endY);
              }
           }
           grid[compX][compY].finalCost = 0;
           
           /*
             Set walls cells. Simply set the cell values to null
             for walls cells.
           */
           for(int i=0; i < walls.length ;i+=2){
               int x = walls[i];
               int y = walls[i+1];
               grid[x][y] = null;
           }
           
           setOpen();
           System.out.println("Key: ");
           System.out.println(" O  ... Computer start position");
           System.out.println(" X  ... Goal position");
           System.out.println("/// ... Wall");
           System.out.println(" #  ... Node cost");
           //Display initial map
           System.out.println("\nGrid: ");
            for(int i=0; i < width ;++i){
                for(int j=0; j < height ;++j){
                   if(i == compX && j == compY)System.out.print(" O  "); //Start point
                   else if(i == endX && j == endY)System.out.print(" X  ");  //Goal
                   else if(grid[i][j]!=null)System.out.printf("%-3d ", grid[i][j].finalCost);
                   else System.out.print("/// "); 
                }
                System.out.println();
            } 
            System.out.println();
           
           int pathCounter = 1;
           if(closed[endX][endY]){
               //Trace back the path 
                System.out.println("Computer's Path: ");
                Node current = grid[endX][endY];
                System.out.println("1. " + current);
                while(current.parent!=null){
                    pathCounter++;
                    System.out.println(pathCounter + ". " +current.parent);
                    current = current.parent;
                } 
                System.out.println();
           }else System.out.println("No possible path!");
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Scanner reader = new Scanner(System.in);
        //System.out.println("Input the board width: ");
        width = 10;
        
        //System.out.println("Input the board height: ");
        height = 10; //reader.nextInt();
        
        //System.out.println("Input the start x position: ");
        compX = 0;//reader.nextInt();
        //while(compX > width - 1 || compX < 0){
        //    System.out.println("Invalid entry");
        //    compX = reader.nextInt();
        //}
        
        //System.out.println("Input the start y position: ");
        compY = 0;//reader.nextInt();
        //while(compY > height - 1 || compY < 0){
        //    System.out.println("Invalid entry");
        //    compY = reader.nextInt();
        //}
        
        //System.out.println("Input the goal x position: ");
        endX = 9;//reader.nextInt();
        //while(endX > width - 1 || endX < 0){
        //    System.out.println("Invalid entry");
        //    endX = reader.nextInt();
        //}
        
        //System.out.println("Input the goal y position: ");
        compY = 9;//reader.nextInt();
        //while(endY > height - 1 || endY < 0){
        //    System.out.println("Invalid entry");
        //    endY = reader.nextInt();
        //}
        
        int[] walls = new int[]{4,3,4,4,4,5,4,6,4,7,4,8,0,3,1,3,2,3,3,3,5,6,6,6,8,6,9,6,9,1,8,1,7,1,6,1,5,1,4,1};
        //int j = 0;
        //System.out.println("Enter the grid coordinates for the first of 4 wall blocks: ");
        //for(int i = 0 ; i < 10 ; i+=2){
        //    int x = 4;//reader.nextInt();
        //    int y = j+3;//reader.nextInt();
        //    walls[i] = x;
        //    walls[i+1] = y;
        //    j++;
        //}
        //walls[] ={4,3,4,4,4,5,4,6,0,3,0,4,0,5};
        
        run(width, height, walls); //, compX, compY, endX, endY
        
        GameJam_Deception d = new GameJam_Deception();
    }
    
}
