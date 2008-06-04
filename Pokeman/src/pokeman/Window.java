
package pokeman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
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

import java.util.logging.Level;
import java.util.logging.Logger;
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
    private ArrayList<Person> people = new ArrayList<Person>();
        
    public static final int COLUMNS = 25,ROWS=18,WIDTH = 800,HEIGHT = 576,TILE_WIDTH = WIDTH/COLUMNS,TILE_HEIGHT = HEIGHT/ROWS,
            BACKGROUND = 0,STATIC = 1,DYNAMIC = 2;
    public static final int numberOfCounts = 4;
    private static final String levelName = "Levels\\level";
    
    private BufferedImage[][] background = new BufferedImage[3][3];
    
    private int levelX,levelY;
    private int x,y;
    private ArrayList<Collideable> collision = new ArrayList<Collideable>();
    
    private boolean upPressed,downPressed,leftPressed,rightPressed;
    private boolean stopUp,stopDown,stopRight,stopLeft;
    private int pressBuffer;
    private int jump;
    private JFrame frame;
    
    private Character player = new Character(this);
    
    private int timerCounter;
    private Collideable special;
    
    private int control;
    private TextBox txt;
    private Menu menu;
    private Battle battle;
    
    private ArrayList<String> trainerSayings = new ArrayList<String>();
        
    /**
     * Makes a new window that draws all the specified stuff on
     * @param frame The frame that this window is in
     */
    public Window(JFrame frame){
        
        
        File file = new File("Images\\Pokeball.gif");
        BufferedImage p = null;
        try
        {
            p = ImageIO.read(file);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        Toolkit tools = Toolkit.getDefaultToolkit();
        setCursor(tools.createCustomCursor(p, new Point(0,0),"Pokeball"));

        
        //txt = new TextBox(frame,"Hello welcome to pokemon razmatazz. How are you doing today? I am fine thanks for asking. My name is Mark, I'm the only person working on this project",0,475,800,101);
        //String[] str = {"Attack","Pokemon","Item","Run"};
        //menu = new Menu(frame,str,0,475,600,101);
        //player.allowUpdate(false);
        
        battle = new Battle(player,new Pokemon("Friger",10),frame);
        
        this.frame = frame;
        
        //Pokemon p = new Pokemon("Meh",5);
        frame.add(this);
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        frame.pack();
        frame.addKeyListener(new KeyListen());
                
        loadTrainerSayings(trainerSayings,"Trainers\\Trainers.txt");
                
        levelX = 0; 
        levelY = 0;
        for(int i=-1;i<=1;i++)
            for(int j=-1;j<=1;j++)
                loadLevel(levelX+i,levelY+j,i,j);

        

        
        repaint();
        
        frame.pack();  
        repaint();
        
        frame.pack();  
        
        Timer t = new Timer(160/numberOfCounts, new Action());
        t.start();
        
        timerCounter = 0;
        pressBuffer = Animation.NONE;
        
        control = 0;
        people.add(player);
        
        
        
    }
    
    /**
     * This method paints the world and everything in it
     * @param g Dont worry about this
     */
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        if(control==0){
            g2.setColor(Color.BLACK);
            g2.fill(new Rectangle(0,0,WIDTH,HEIGHT));

            //System.out.println("X "+levelX+" Y: "+levelY);

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
            Collections.sort(people);
            for(int i = 0;i<people.size();i++){
                //people.get(i).update();
                people.get(i).draw(g2, x, y);
            }
            
            /*g2.setColor(Color.RED);

            for(Collideable c:collision){
                if(c.getNumber(0)!=0)
                    g2.draw(new Rectangle(c.getX()*TILE_WIDTH+x, c.getY()*TILE_HEIGHT+y, TILE_WIDTH, TILE_HEIGHT));
            }*/



            if(txt!=null){
                txt.draw(g2);
                if(txt.isOver())
                {
                    txt = null;
                    player.unRead();
                }
            }
            
            if(menu!=null){
                menu.draw(g2);
                if(menu.result()!=null)
                {
                    menu = null;
                    player.allowUpdate(true);
                }
            }
            
            
        }
        
        if(control==1 && battle!=null){
            battle.draw(g2);
            if(battle.isOver())
                control = 0;
        }
        

        

        
    }
    
     /**
     * This method loads the current level. 
     * 
     * @param xCoord The x coordinate of the level
     * @param yCoord The y coordinate of the level
     */
    public void loadLevel(int xCoord,int yCoord,int offsetX,int offsetY)
    {
        String temp = levelName+xCoord+","+yCoord;
        //System.out.println(temp);
        
        try {
            //System.out.println("XCoord: "+xCoord+" YCoord: "+yCoord);
            
            background[-offsetX+1][offsetY+1] = ImageIO.read(new File(temp+".png"));
            load(xCoord,yCoord);
        } catch (IOException ex) {
            background[-offsetX+1][offsetY+1] = null;
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
                    
                    if(values[0]!=0 || values[1]!=0 || values[2]!=0)
                    {
                        addToCollision(new Collideable(this,x1+xCoord*COLUMNS,y1+yCoord*-ROWS,values[0],values[1],values[2]));
                    }
                    str = str.substring(str.indexOf(",")+1);
                }
            }

            input = new Scanner(new File(name + ".2.txt"));
            str = "";
            while (input.hasNextLine()) {
                str += input.nextLine();
            }
            
            for (int y1 = 0; y1 < ROWS; y1++) {
                for (int x1 = 0; x1 < COLUMNS; x1++) {
                    String smallString = str.substring(0,str.indexOf(","));
                    int value = 0;
                    String personName="";
                    if(smallString.indexOf(":")!=-1){
                        personName = smallString.substring(0,smallString.indexOf(":"));
                        smallString = smallString.substring(smallString.indexOf(":")+1);
                        
                        value = Integer.parseInt(smallString.substring(0));
                    }else
                        personName = smallString;
                    personName = personName.replaceAll(".png", "").replaceAll(".PNG","");
                    if(personName.length()>1){
                        people.add(new Person(personName,trainerSayings.get(value),x1*TILE_WIDTH+xCoord*WIDTH,y1*TILE_HEIGHT+yCoord*-HEIGHT,this));
                    }
                        
                    
                    str = str.substring(str.indexOf(",")+1);
                    
                }
               
            }
            
            Collections.sort(people);
            
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
    public void unload(int xCoord,int yCoord){

            
        for (int y1 = 0; y1 < ROWS; y1++) {
            for (int x1 = 0; x1 < COLUMNS; x1++) {
                int c = Collections.binarySearch(collision,new Collideable(this,x1+xCoord*COLUMNS,y1+yCoord*-ROWS,0,0,0));
                if(c>=0)
                    collision.remove(collision.get(c));
                int p = Collections.binarySearch(people,new Person("","",x1*TILE_WIDTH+xCoord*WIDTH,y1*TILE_HEIGHT+yCoord*HEIGHT,null));
                if(p>=0 && player!=people.get(p))
                    people.remove(people.get(p));

                    
            }
        }
      
    }
    
    public void loadTrainerSayings(ArrayList<String> strs,String file){
        try {
            File f = new File(file);
            Scanner s = new Scanner(f);
            while(s.hasNextLine()){
                s.next();
                strs.add(s.nextLine());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void addToCollision(Collideable c){
        int pos = Collections.binarySearch(collision, c);

        if(pos<0)
            pos = -pos - 1;
        
        collision.add(pos,c);
    }
    
    public boolean removeFromCollision(Collideable c){
        int pos = collision.indexOf(c);
        if(pos>=0)
        {
            collision.remove(pos);
            return true;
        }
        else
            return false;
    }
    
    public ArrayList<Collideable> getCollision(){
        return collision;
    }
    
    public Collideable inCollision(Collideable c){
        int pos = Collections.binarySearch(collision, c);
        if(pos>0)
            return collision.get(pos);
        else 
            return null;
    }
    
    public void setLevel(int x,int y){
        levelX = x;
        levelY = y;
    }
    
     /**
     * The class that implments the keyListener
     * This gets all the keyboard events
     */
    public class KeyListen implements KeyListener
    {
        
        public void keyTyped(KeyEvent keyEvent) {    
            if(timerCounter==0 && keyEvent.getKeyChar()=='z' && (txt==null || txt.isOver()))
            {
                String str = player.read();
                if(str!=null)
                    txt = new TextBox(frame,str,0,475,800,101,true,Style.STANDARD_TEXT);
            }
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
                        

            
            if(timerCounter==0 && !(upPressed || downPressed || rightPressed || leftPressed))
            {
                upPressed = pressBuffer == Animation.UP;// && collisionUp;
                downPressed = pressBuffer == Animation.DOWN;// && collisionDown;
                rightPressed = pressBuffer == Animation.RIGHT;// && collisionRight;
                leftPressed = pressBuffer == Animation.LEFT;// && collisionLeft;
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
                

            }
            if(upPressed || downPressed || rightPressed || leftPressed)
                timerCounter++;
            else
                timerCounter = 0;
            
            if(timerCounter==numberOfCounts)
            {
                timerCounter = 0;
                            
                if((stopUp && upPressed)){
                    upPressed = false;
                    stopUp = false;
                    if(pressBuffer == Animation.UP)
                        pressBuffer = Animation.NONE;
                }
                if((stopDown && downPressed)){
                    downPressed = false;
                    stopDown = false;
                    if(pressBuffer == Animation.DOWN)
                        pressBuffer = Animation.NONE;

                }
                
                if((stopRight && rightPressed)){
                    rightPressed = false;
                    stopRight = false;
                    if(pressBuffer == Animation.RIGHT)
                        pressBuffer = Animation.NONE;
                }
                
                if((stopLeft && leftPressed)){
                    leftPressed = false;
                    stopLeft = false;
                    if(pressBuffer == Animation.LEFT)
                        pressBuffer = Animation.NONE;
                }    
            }

            
            x = -(player.getX()-(int)(Window.COLUMNS/2)*Window.TILE_WIDTH);
            y = -(player.getY()-(int)(Window.ROWS/2)*Window.TILE_HEIGHT);
            
            int tempX = player.getX()/WIDTH;
            int tempY = -player.getY()/HEIGHT;
            
            if(player.getX()<0)
                tempX--;
            if(player.getY()<0)
                tempY++;
            
            loadNewLevels(tempX,tempY);
            
            levelX = tempX;
            levelY = tempY;

                   
            
            repaint();
        }      
    }
    

    /*
     * Determines what needs to be loaded or unloaded, moves the backgrounds
     * acordingly
     */    
    private void loadNewLevels(int newX,int newY){
        
        
        if(Math.abs(newX-levelX)==1){
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

            loadLevel(2*newX-levelX,levelY,-levelX+newX,0);
            loadLevel(2*newX-levelX,levelY+1,-levelX+newX,1);
            loadLevel(2*newX-levelX,levelY-1,-levelX+newX,-1);
            levelX = newX;

        }
        if(Math.abs(newY-levelY)==1){
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
            
            loadLevel(levelX,2*newY-levelY,0,-levelY+newY);
            loadLevel(levelX+1,2*newY-levelY,1,-levelY+newY);
            loadLevel(levelX-1,2*newY-levelY,-1,-levelY+newY);
            levelY = newY;
        }
    }   
}
