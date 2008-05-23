/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
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
    private static final int numberOfCounts = 4;
    private static final String levelName = "Levels\\level";
    
    private BufferedImage[][] background = new BufferedImage[3][3];
    
    private int levelX,levelY;
    private int x,y;
    private ArrayList<Collideable> collision = new ArrayList<Collideable>();
    
    private boolean upPressed,downPressed,leftPressed,rightPressed;
    private boolean stopUp,stopDown,stopRight,stopLeft;
    private int pressBuffer;
    private int jump;
    
    private Character player = new Character();
    
    private int timerCounter;
        
    /**
     * Makes a new window that draws all the specified stuff on
     * @param frame The frame that this window is in
     */
    public Window(JFrame frame){
        Pokemon p = new Pokemon("Meh",5);
        frame.add(this);
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        frame.pack();
        frame.addKeyListener(new KeyListen());
        
        loadImgs("Images\\Dynamic");
        levelX = 0; 
        levelY = 0;
        for(int i=-1;i<=1;i++)
            for(int j=-1;j<=1;j++)
                loadLevel(levelX+i,levelY+j);

        
        repaint();
        frame.pack();  
        repaint();
        
        Timer t = new Timer(40, new Action());
        t.start();
        
        timerCounter = 0;
        pressBuffer = Animation.NONE;
        
    }
    
    /**
     * This method paints the world and everything in it
     * @param g Dont worry about this
     */
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.fill(new Rectangle(0,0,WIDTH,HEIGHT));
        

        
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(background[i][j]!=null){
                    int xPos = (levelX-(i-1))*WIDTH+x;
                    int yPos = (-levelY-(j-1))*HEIGHT+y;
                    BufferedImage hold = background[i][j];
                    if(!(Math.abs(xPos)>=hold.getWidth() || Math.abs(yPos)>=hold.getHeight())){
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
        //g2.setColor(Color.RED);
        
        //for(Collideable c:collision){
            //if(c.getNumber(0)!=0)
                //g2.draw(new Rectangle(c.getX()*TILE_WIDTH+x, c.getY()*TILE_HEIGHT+y, TILE_WIDTH, TILE_HEIGHT));
        //}
        
        //g2.fill(new RoundRectangle2D.Double(0,0,100,100,50,50));
    }
    
     /**
     * This method loads the current level. 
     * 
     * @param xCoord The x coordinate of the level
     * @param yCoord The y coordinate of the level
     */
    public void loadLevel(int xCoord,int yCoord)
    {
        String temp = levelName+xCoord+","+yCoord;
        
        
        try {
            //System.out.println("XCoord: "+xCoord+" YCoord: "+yCoord);
            background[1+levelX-xCoord][1+yCoord-levelY] = ImageIO.read(new File(temp+".png"));
            load(xCoord,yCoord);
        } catch (IOException ex) {
            background[1+levelX-xCoord][1+yCoord-levelY] = null;
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
                }                
            }            
        }       
    }
    
    /**
     * This method is called when you call load level it loads the collision data
     * and any dynamic images like walking characters
     * 
     * @param xCoord x coordinate of the level
     * @param yCoord y coordinate of the level
     */
    private void load(int xCoord,int yCoord){
        String name=levelName+xCoord+","+yCoord;
        try {
            Scanner input = new Scanner(new File(name + "collision.txt"));
            String str = "";
            while (input.hasNextLine()) {
                str += input.nextLine();
            }
            
            for (int y1 = 0; y1 < ROWS; y1++) {
                for (int x1 = 0; x1 < COLUMNS; x1++) {
                    String smallString = str.substring(0,str.indexOf(","));
                    int index = 0;
                    int i=0;
                    int[] values = new int[3];
                    while(i<smallString.length() && index<3){
                        if(smallString.charAt(i)==':')
                        {
                            values[index] = Integer.parseInt(smallString.substring(0,i));
                            smallString = smallString.substring(i+1);
                            index++;
                            i=0;
                        }else
                            i++;
                    }
                    if(index<3 && !smallString.equals(""))
                        values[index] = Integer.parseInt(smallString.substring(0));
                    collision.add(new Collideable(x1+xCoord*COLUMNS,y1+yCoord*-ROWS,values[0],values[1],values[2]));
                    str = str.substring(str.indexOf(",")+1);
                }
            }
            
            Collections.sort(collision);
            
        } catch (FileNotFoundException ex) {
            System.out.println("File" + name + "not found");
        }
    }
    
    /**
     * Unloads a level getting rid of the  collision data
     * 
     * @param xCoord x coordinate of the level
     * @param yCoord y coordinate of the level
     */
    private void unload(int xCoord,int yCoord){

            
        for (int y1 = 0; y1 < ROWS; y1++) {
            for (int x1 = 0; x1 < COLUMNS; x1++) {
                int c = Collections.binarySearch(collision,new Collideable(x1+xCoord*COLUMNS,y1+yCoord*-ROWS,0,0,0));
                if(c>=0)
                    collision.remove(collision.get(c));
            }
        }
      
    }
    
     /**
     * The class that implments the keyListener
     * This gets all the keyboard events
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

            System.out.println("Col: "+col+" Row: "+row);
            
            int c;
            
            c = Collections.binarySearch(collision,new Collideable(col,row,0,0,0));
            if(timerCounter == 0 && c>=0){
                int number = collision.get(c).getNumber(0);
                if(number>=-4 && number<=-1){
                    player.direction(-(number+1));
                    
                    int oldX = levelX;
                    int oldY = levelY;
                    levelX = collision.get(c).getNumber(1);
                    levelY = collision.get(c).getNumber(2);
                    
                    for(int i=-1;i<=1;i++)
                        for(int j=-1;j<=1;j++)
                            unload(oldX+i,oldY+j);
                   

                    x = -levelX*WIDTH;
                    y = levelY*HEIGHT;
                    
                    for(int i=-1;i<=1;i++)
                        for(int j=-1;j<=1;j++)
                            loadLevel(levelX+i,levelY+j);
                    System.out.println("OldX: "+oldX+" OldY: "+oldY);
                    for(Collideable door:collision){
                        if(door.getNumber(0)>=-4 && door.getNumber(0)<=-1 && Math.abs(door.getNumber(1)-oldX)<=1 && Math.abs(door.getNumber(2)-oldY)<=1){
                            x = (-door.getX()*TILE_WIDTH + WIDTH/2 - player.getWidth()/2);
                            y = -(door.getY()-1)*TILE_HEIGHT + HEIGHT/2 - player.getHeight()/2;

                            if(player.getDirection()==Animation.UP)
                                y+=TILE_HEIGHT; 

                            if(player.getDirection()==Animation.DOWN)
                                y-=TILE_HEIGHT;

                            if(player.getDirection()==Animation.RIGHT)
                                x-=TILE_WIDTH;   

                            if(player.getDirection()==Animation.LEFT)
                                x+=TILE_WIDTH;   
                        }
                    }
                    
                    System.out.println(x);
                    
                }
            }
            
            /*
             * This part determines in which directions the character can move 
             */
            boolean collisionRight = false,collisionLeft = false,collisionUp = false,collisionDown = false;
            boolean rightJump = false,leftJump = false, downJump = false;
            
            c = Collections.binarySearch(collision,new Collideable(col,row-1,0,0,0));
            if(c<0 || (collision.get(c).getNumber(0)<1 || collision.get(c).getNumber(0)>4))
                collisionUp = true;
            c = Collections.binarySearch(collision,new Collideable(col,row+1,0,0,0));
            if(c<0 || (collision.get(c).getNumber(0)<1 || collision.get(c).getNumber(0)>4 || collision.get(c).getNumber(0)==2))
            {
                collisionDown = true;
                if(c>0 && collision.get(c).getNumber(0)==2)
                    downJump = true;
            }
            c = Collections.binarySearch(collision,new Collideable(col+1,row,0,0,0));
            if(c<0 || (collision.get(c).getNumber(0)<1 || collision.get(c).getNumber(0)>4 || collision.get(c).getNumber(0)==3))
            {
                collisionRight = true;
                if(c>0 && collision.get(c).getNumber(0)==3)
                    rightJump = true;
            }
            c = Collections.binarySearch(collision,new Collideable(col-1,row,0,0,0));
            if(c<0 || (collision.get(c).getNumber(0)<1 || collision.get(c).getNumber(0)>4 || collision.get(c).getNumber(0)==4))
            {
                collisionLeft = true;
                if(c>0 && collision.get(c).getNumber(0)==4)
                    leftJump = true;
            }
            
            /*
             * Determines which direction if any he is moving in
             */ 
            if(timerCounter==0 && !(upPressed || downPressed || rightPressed || leftPressed))
            {
                upPressed = pressBuffer == Animation.UP && collisionUp;
                downPressed = pressBuffer == Animation.DOWN && collisionDown;
                rightPressed = pressBuffer == Animation.RIGHT && collisionRight;
                leftPressed = pressBuffer == Animation.LEFT && collisionLeft;
            }
            

            
            /*
             * Changes the animation 
             */
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
                
                if(jump>0)
                    jump--;
            }
            
            
            if((rightPressed && rightJump) || (leftPressed && leftJump) || (downPressed && downJump)){
                jump = 3;
                player.jump(numberOfCounts);
            }
            
            if(jump>0)
            {
                if(jump == 1){
                    upPressed = false;
                    downPressed = false;
                    rightPressed = false;
                    leftPressed = false;
                    pressBuffer = Animation.NONE;
                }
                if(jump == 2){
                    pressBuffer = player.getDirection();
                }                
            }
            

            /*
             * Moves the screen in the right direction, remember the character
             * doesn't actualy move
             */ 
            if(upPressed)
                y+=HEIGHT/ROWS/numberOfCounts; 

            if(downPressed)
                y-=HEIGHT/ROWS/numberOfCounts;

            if(rightPressed)
                x-=WIDTH/COLUMNS/numberOfCounts;   

            if(leftPressed)
                x+=WIDTH/COLUMNS/numberOfCounts;   

            
            repaint();
            
            /*
             * If he is moving it increases the timer if not it leaves it at 
             * 0 to await a new move
             */ 
            if(upPressed || downPressed || rightPressed || leftPressed)
                timerCounter++;
            else
                timerCounter = 0;

            /*
             * When this move is done is the button still being pressed then go 
             * ahead and continue moving if not stop
             */ 
            if(timerCounter==numberOfCounts){
                timerCounter=0;
                if(jump == 0){
                    // I have absolutely no idea why this works... but it does...
                    int r1=1;
                    int r2=1;
                    int c1=1;
                    int c2=1;
                    
                    if(col<0){
                        c2 = 2;
                    }else{
                        c1 = 2;
                    }
                    if(row<0){
                        r2 = 2;
                    }else{
                        r1 = 2;
                    }
                    
                    c = Collections.binarySearch(collision,new Collideable(col,row-r2,0,0,0)); 
                    if((stopUp && upPressed) || !(c<0 || (collision.get(c).getNumber(0)<1 || collision.get(c).getNumber(0)>4))){
                        upPressed = false;
                        stopUp = false;
                        if(pressBuffer == Animation.UP)
                            pressBuffer = Animation.NONE;
                    }                
                    c = Collections.binarySearch(collision,new Collideable(col,row+r1,0,0,0));                
                    if((stopDown && downPressed) || !(c<0 || (collision.get(c).getNumber(0)<1 || collision.get(c).getNumber(0)>4 || collision.get(c).getNumber(0)==2))){
                        downPressed = false;
                        stopDown = false;
                        if(pressBuffer == Animation.DOWN)
                            pressBuffer = Animation.NONE;

                    }
                    c = Collections.binarySearch(collision,new Collideable(col+c1,row,0,0,0));
                    if((stopRight && rightPressed) || !(c<0 || (collision.get(c).getNumber(0)<1 || collision.get(c).getNumber(0)>4 || collision.get(c).getNumber(0)==3))){
                        rightPressed = false;
                        stopRight = false;
                        if(pressBuffer == Animation.RIGHT)
                            pressBuffer = Animation.NONE;
                    }
                    c = Collections.binarySearch(collision,new Collideable(col-c2,row,0,0,0));
                    if((stopLeft && leftPressed) || !(c<0 || (collision.get(c).getNumber(0)<1 || collision.get(c).getNumber(0)>4 || collision.get(c).getNumber(0)==4))){
                        leftPressed = false;
                        stopLeft = false;
                        if(pressBuffer == Animation.LEFT)
                            pressBuffer = Animation.NONE;
                    }     
                }
            }
            
            /*
             * Determines what needs to be loaded or unloaded, moves the backgrounds
             * acordingly
             */ 
            int newX = -x/WIDTH;
            int newY = y/HEIGHT;
            if(newX!=levelX){
                unload(2*levelX-newX,levelY);
                unload(2*levelX-newX,levelY+1);
                unload(2*levelX-newX,levelY-1);
                
                if(newX<levelX){
                    for(int i=0;i<2;i++){
                        for(int j=0;j<3;j++){
                            background[i][j] = background[i+1][j];
                        }
                    }
                }else{
                    for(int i=1;i>=0;i--){
                        for(int j=0;j<3;j++){
                            background[i+1][j] = background[i][j];
                        }
                    } 
                }
                int temp = levelX;
                levelX = newX;
                loadLevel(2*newX-temp,levelY);
                loadLevel(2*newX-temp,levelY+1);
                loadLevel(2*newX-temp,levelY-1);
                
            }
            if(newY!=levelY){
                unload(levelX,2*levelY-newY);
                unload(levelX-1,2*levelY-newY);
                unload(levelX+1,2*levelY-newY);
                
                if(newY>levelY){
                    for(int i=0;i<2;i++){
                        for(int j=0;j<3;j++){
                            background[j][i] = background[j][i+1];
                        }
                    }
                }else{
                    for(int i=1;i>=0;i--){
                        for(int j=0;j<3;j++){
                            background[j][i+1] = background[j][i];
                        }
                    } 
                }              
                int temp = levelY;
                levelY = newY;
                loadLevel(levelX,2*newY-temp);
                loadLevel(levelX+1,2*newY-temp);
                loadLevel(levelX-1,2*newY-temp);
                
            }
            
        }
    }
}
