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
import java.util.Stack;
import java.util.TimerTask;
import java.util.Timer;

/**
 *
 * @author MJHobby
 */
public class A_Star_App extends JFrame implements ActionListener {

    // for heuristic cost calculations
    public static final int DIAGONAL_COST = 14;
    public static final int VERT_HORIZ_COST = 10;
    
    // open and closed list to track checked and unchecked nodes
    static PriorityQueue<Node> open;
    static boolean closed[][];
    public static Node[][] grid = new Node[5][5];
    
    // Navigation path and computer mover that navigates board
    private static Path navigation = new Path();
    private  Mover computer;
    
    // Game board parameters
    private JButton startButton,  // If game has been paused, selecting will resume (inactive when running)
                    stopButton,
                    nextButton;   // If game running, pauses; inactive when paused
    private static JLabel[][] board; // Our game board where we display comp or obstacles
    
    // TODO: these will be set in initialization panel
    public static int width = 10;
    public static int height = 10; // Game board width and height
    public static int compX = 0;
    public static int compY = 0; // Computer start position x and y coordinates
    private static int endX, endY;
    public static boolean play ,pause;
    
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
        
        // board and buttons not visible until initial panel filled out and completed
        //boardPanel.setVisible(false);
        //buttonPanel.setVisible(false);
        
        //Timer timer = new Timer();
        //timer.schedule(drawBoard(), 0, 5000);
    }
    
    public static boolean inBounds(int inputNum){
        if((inputNum < height || inputNum < width) && inputNum > 0) return true;
        else return false; 

    }
    
    public static void setInitialPanel(){
        
    }
    
    /*
        @params:
            current: current node being tested
            t: the next node of the path
            cost: the cost of total travel to current
        
    */
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

            Node n;  
            if(current.getX() - 1 >= 0){
                n = grid[current.getX()-1][current.getY()];
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
    
    // Display player, walls, and other pieces
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
        
        if (e.getSource() == nextButton){
            move();
            if (navigation.isEnd()){
                nextButton.setEnabled(false);
            }
        }
        
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
    
    public static void moveComputer(){
        Timer timer = new Timer();
        timer.schedule(new moveComputerTask(), 2000);
    }
    
    
    static class moveComputerTask extends TimerTask{
        public void run(){
            if(play){
                if(!navigation.isEnd()){
                    board[navigation.getX()][navigation.getY()].setBackground(Color.BLUE);
                    navigation.getNext();
                    board[navigation.getX()][navigation.getY()].setBackground(Color.GREEN);
                }
                
            }
        }
    }
    
    public void move(){
        if(!navigation.isEnd()){
            //board[navigation.getX()][navigation.getY()].setBackground(Color.BLUE);
            navigation.getNext();
            computer.move(navigation.getX(), navigation.getY());
            //board[navigation.getX()][navigation.getY()].setBackground(Color.GREEN);
        } else {
            nextButton.setEnabled(false);
        }
        drawBoard();
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        width = 10;
        height = 10;
        compX = 0;
        compY = 0;
        endX = 9;
        compY = 9;
        
        StartupPanel initialize = new StartupPanel(height, width);
        
        while(!initialize.getIsFinished()){
            if (!initialize.getWait())
                initialize.setFrame();
        }
        
        int[] walls = new int[]{4,3,4,4,4,5,4,6,4,7,4,8,0,3,1,3,2,3,3,3,5,6,6,6,8,6,9,6,9,1,8,1,7,1,6,1,5,1,4,1};
        
        run(width, height, walls); //, compX, compY, endX, endY
        
        A_Star_App d = new A_Star_App();
        //moveComputer();
        
    }
    
}
