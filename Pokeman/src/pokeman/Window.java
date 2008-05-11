/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author Mark
 */
public class Window extends JComponent{
    
    private ArrayList<BufferedImage> img = new ArrayList<BufferedImage>(20); 
    private ArrayList<String> imgNames = new ArrayList<String>(20); 
        
    public static final int COLUMNS = 25,ROWS=18,WIDTH = 800,HEIGHT = 576,TILE_WIDTH = WIDTH/COLUMNS,TILE_HEIGHT = HEIGHT/ROWS,
            BACKGROUND = 0,STATIC = 1,DYNAMIC = 2;
    private static final int numberOfCounts = 2;
    private BufferedImage[][] background = new BufferedImage[3][3];
    private String levelName;
    private int levelX,levelY;
    private int x,y;
    private ArrayList<Collideable> collision = new ArrayList<Collideable>();
    
    private boolean upPressed,downPressed,leftPressed,rightPressed;
    private boolean stopUp,stopDown,stopRight,stopLeft;
    private int pressBuffer;
    
    private Character player = new Character();
    
    private int timerCounter;
        
    public Window(JFrame frame){
        
        frame.add(this);
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        frame.pack();
        frame.addKeyListener(new KeyListen());
        
        //loadImgs("Images\\Dynamic");
        levelX = 0; 
        levelY = 0;
        levelName = "Levels\\level";
        loadLevel(levelName,levelX,levelY);
        background[0][0] = background[1][1];
        background[0][1] = background[1][1];
        background[0][2] = background[1][1];
        //loadLevel(levelName,levelX-1,levelY);
        repaint();
        frame.pack();  
        repaint();
        
        Timer t = new Timer(80, new Action());
        t.start();
        
        timerCounter = 0;
        pressBuffer = Animation.NONE;
        
    }
    
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(background[i][j]!=null){
                    int xPos = (levelX-(i-1))*WIDTH+x;
                    int yPos = (levelY-(j-1))*HEIGHT+y;
                    BufferedImage hold = background[i][j];
                    if(!(xPos>=hold.getWidth() || yPos>=hold.getHeight())){
                        if(xPos<0){
                            hold = hold.getSubimage(-xPos,0,hold.getWidth()+xPos,hold.getHeight());
                            xPos = 0;
                        }
                        if(yPos<0){
                            hold = hold.getSubimage(0,-yPos,hold.getWidth(),hold.getHeight()+yPos);
                            yPos = 0;
                        }
                        if(xPos>0)
                            hold = hold.getSubimage(0,0,hold.getWidth()-xPos,hold.getHeight());
                        if(yPos>0)
                            hold = hold.getSubimage(0,0,hold.getWidth(),hold.getHeight()-yPos);
                        g2.drawImage(hold,null,xPos,yPos);
                    }
                }
            }
        }
        player.draw(g2);
    }
    
     /**
     * This method loads the current level. Link will interact with this level.
     * This will actualy load the background image as well as the locations of
     * the images that go on top so provided the root name of the level without
     * any extensions.
     * 
     * @param name This is the name of the level to load
     */
    public void loadLevel(String name,int xCoord,int yCoord)
    {
        String temp = name+xCoord+","+yCoord;
        load(temp,xCoord,yCoord);
        
        try {
            background[1+levelX-xCoord][1+levelY-yCoord] = ImageIO.read(new File(temp+".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * This allows you to load all of the images in a folder into the program.
     * This must be done for images that will be used in a map prior to the map
     * being loaded.
     * 
     * @param folder The floder with the images.
     */
    private void loadImgs(String folder){
        File f = new File(folder);
        
        File[] files = f.listFiles();
        String[] list = null;
        if(files!=null){
            list = new String[files.length];
            
            for(int i=0;i<files.length;i++)
            {
                list[i] = files[i].getName();
                imgNames.add(files[i].getName());
                try{
                    img.add(ImageIO.read(files[i]));
                }catch(java.io.IOException e){
                    System.out.println("File not found");
                }                
            }            
        }       
    }
    
    private void load(String name,int xCoord,int yCoord){
        try {
            Scanner input = new Scanner(new File(name + "collision.txt"));
            String str = "";
            while (input.hasNextLine()) {
                str += input.nextLine();
            }
            
            for (int y1 = 0; y1 < ROWS; y1++) {
                for (int x1 = 0; x1 < COLUMNS; x1++) {
                    collision.add(new Collideable(x1+xCoord*WIDTH,y1+yCoord*HEIGHT,Integer.parseInt(str.substring(0,str.indexOf(",")))));
                    str = str.substring(str.indexOf(",")+1);
                }
            }
            
            Collections.sort(collision);
            
        } catch (FileNotFoundException ex) {
            System.out.println("File" + name + "not found");
        }
    }
    
     /**
     * The class that implments the keyListener
     */
    public class KeyListen implements KeyListener
    {
        
        public void keyTyped(KeyEvent keyEvent) {        
        }

        public void keyPressed(KeyEvent keyEvent) {   

            
            if(true){
                if(keyEvent.getKeyCode()==KeyEvent.VK_UP)
                    pressBuffer = Animation.UP;
                if(keyEvent.getKeyCode()==KeyEvent.VK_DOWN)
                    pressBuffer = Animation.DOWN;
                if(keyEvent.getKeyCode()==KeyEvent.VK_RIGHT)
                    pressBuffer = Animation.RIGHT;
                if(keyEvent.getKeyCode()==KeyEvent.VK_LEFT)
                    pressBuffer = Animation.LEFT;
            }

        }
        
        public void keyReleased(KeyEvent keyEvent){
                if(keyEvent.getKeyCode()==KeyEvent.VK_UP)
                    stopUp = true;
                if(keyEvent.getKeyCode()==KeyEvent.VK_DOWN)
                    stopDown = true;
                if(keyEvent.getKeyCode()==KeyEvent.VK_RIGHT)
                    stopRight = true;
                if(keyEvent.getKeyCode()==KeyEvent.VK_LEFT)
                    stopLeft = true;
        }
    }
    
     /**
     * This regulates the rate at which the game moves
     */
    public class Action implements ActionListener{
        public void actionPerformed(ActionEvent event){  
                        
            int col = (WIDTH/2-player.getWidth()/2-x)/TILE_WIDTH;
            int row = (HEIGHT/2-player.getHeight()/2-y)/TILE_HEIGHT+1;

            boolean collisionRight = false,collisionLeft = false,collisionUp = false,collisionDown = false;
            int c;
            c = Collections.binarySearch(collision,new Collideable(col,row-1,0));
            if(c<0 || collision.get(c).getNumber()!=1)
                collisionUp = true;
            c = Collections.binarySearch(collision,new Collideable(col,row+1,0));
            if(c<0 || collision.get(c).getNumber()!=1)
                collisionDown = true;
            c = Collections.binarySearch(collision,new Collideable(col+1,row,0));
            if(c<0 || collision.get(c).getNumber()!=1)
                collisionRight = true;
            c = Collections.binarySearch(collision,new Collideable(col-1,row,0));
            if(c<0 || collision.get(c).getNumber()!=1)
                collisionLeft = true;
            
            if(timerCounter==0 && !(upPressed || downPressed || rightPressed || leftPressed))
            {
       

                            
                upPressed = pressBuffer == Animation.UP && collisionUp;
                downPressed = pressBuffer == Animation.DOWN && collisionDown;
                rightPressed = pressBuffer == Animation.RIGHT && collisionRight;
                leftPressed = pressBuffer == Animation.LEFT && collisionLeft;
                

            }
            
            if(timerCounter==0)
            {
                if(upPressed)
                    player.direction(Animation.UP);   

                if(downPressed)
                    player.direction(Animation.DOWN); 

                if(rightPressed)
                    player.direction(Animation.RIGHT);   

                if(leftPressed)
                    player.direction(Animation.LEFT); 

            }
            

            
            if(upPressed)
                y+=HEIGHT/ROWS/numberOfCounts; 

            if(downPressed)
                y-=HEIGHT/ROWS/numberOfCounts;

            if(rightPressed)
                x-=WIDTH/COLUMNS/numberOfCounts;   

            if(leftPressed)
                x+=WIDTH/COLUMNS/numberOfCounts;   

            
            repaint();
            if(upPressed || downPressed || rightPressed || leftPressed)
                timerCounter++;
            else
                timerCounter = 0;

            
            if(timerCounter==numberOfCounts){
                timerCounter=0;
                if((stopUp && upPressed) || !collisionUp){
                    upPressed = false;
                    stopUp = false;
                    if(pressBuffer == Animation.UP)
                        pressBuffer = Animation.NONE;
                }                
                c = Collections.binarySearch(collision,new Collideable(col,row+2,0));                
                if((stopDown && downPressed) || (c<0 || collision.get(c).getNumber()==1)){
                    downPressed = false;
                    stopDown = false;
                    if(pressBuffer == Animation.DOWN)
                        pressBuffer = Animation.NONE;
                    
                }
                c = Collections.binarySearch(collision,new Collideable(col+2,row,0));
                if((stopRight && rightPressed) || (c<0 || collision.get(c).getNumber()==1)){
                    rightPressed = false;
                    stopRight = false;
                    if(pressBuffer == Animation.RIGHT)
                        pressBuffer = Animation.NONE;
                }
                if((stopLeft && leftPressed) || !collisionLeft){
                    leftPressed = false;
                    stopLeft = false;
                    if(pressBuffer == Animation.LEFT)
                        pressBuffer = Animation.NONE;
                }      
            }
        }
    }
}
