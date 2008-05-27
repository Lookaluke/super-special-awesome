package pokeman;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;


public class Person implements Comparable<Person>
{
    private String name,speech;
    private int x,y,direction;
    int xChange,yChange;
    private Animation walk;
    private boolean moving;
    private int screenX,screenY;
    private int counter;
    private Window w;
    private Collideable lastEdition,newEdition;
    

    /**
     * Constructor for objects of class Person
     */
    public Person(String n,String s,int x,int y,Window w)
    {
        
        this.w = w;
        speech = s;
        name = n;
        direction = Animation.DOWN;
        screenX = x/Window.WIDTH;
        screenY = y/Window.HEIGHT;
        this.x = x;
        this.y = y;
        
        if(w!=null){
            lastEdition = new Collideable(x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT+1,1,0,0);
            w.addToCollision(lastEdition);
            walk = new Animation(name);
        }
        

    }

    public String getName()
    {
        return name;
    }
    
    public void draw(Graphics2D g,int currentX,int currentY){
        counter++;

        update();
        if(!moving)
            walk.standingFrame();
        g.drawImage(walk.getFrame(),null,x+currentX,y+currentY);
        
        if(counter%4==0 || walk.getDirection()!=direction){
            walk.nextFrame(direction);
        }
        
        if(moving){   

            if(direction == Animation.RIGHT)
                x += Window.TILE_WIDTH/Window.numberOfCounts;
            if(direction == Animation.LEFT)
                x -= Window.TILE_WIDTH/Window.numberOfCounts;
            if(direction == Animation.UP)
                y -= Window.TILE_WIDTH/Window.numberOfCounts;
            if(direction == Animation.DOWN)
                y += Window.TILE_WIDTH/Window.numberOfCounts;
            
            if(counter>=(Window.numberOfCounts-1))
            {
                counter = 0;
                moving = false;
                w.removeFromCollision(lastEdition);
                lastEdition = newEdition;
            }
        }
    }
    
    /**
     * Override this method to make the person do what you want. This person 
     * just wanders around
     */
    protected void update(){

        if(counter>=50)
        {
            int dir = (int)(Math.random()*4);
            makeMove(dir);            
            
        }
    }
    
    /**
     * Call this method in the direction you want to move, calls canMove to determine if it can move
     * so override that method instead
     * @param dir The direction to move
     */
    public void makeMove(int dir){
        
        if(!moving && dir!=Animation.NONE){
            
            xChange = 0;
            yChange = 0;

            switch(dir){
                case Animation.UP:
                    yChange = -1;
                    break;
                case Animation.DOWN:
                    yChange = 1;
                    break;
                case Animation.RIGHT:
                    xChange = 1;
                    break;
                case Animation.LEFT:
                    xChange = -1;
                    break;
            }
            
            direction = dir;
            
            if(canMove(dir)){

                moving = true;
                
                Collideable col = getCollision(dir);
                
                newEdition = new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+1+yChange,1,0,0);
                w.addToCollision(newEdition);
            }
            counter = 0;
        }
    }
    
    /**
     * Override this method to determine when a person can move
     * @param dir The direction to move
     * @return true if they can move
     */
    protected boolean canMove(int dir){
        Collideable col = getCollision(dir);
        return (col==null || col.getNumber(0)!=1); 
    }
    
    public int getX(){
        return x;
    }
    
    public void setX(int x){
        this.x = x;
    }
        
    public void setY(int y){
        this.y = y;
    }
    
    public int getY(){
        return y;
    }
    
    public Window getWindow(){
        return w;
    }
    
    public int getDirection(){
        return direction;
    }
    
    protected Collideable getCollision(int dir){
        Collideable col = null;
        if(dir==Animation.UP){
            col = w.inCollision(new Collideable(x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT-1+1,0,0,0));
        }
        if(dir==Animation.DOWN){
            col = w.inCollision(new Collideable(x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT+1+1,0,0,0));
        }
        if(dir==Animation.RIGHT){
            col = w.inCollision(new Collideable(x/Window.TILE_WIDTH+1,y/Window.TILE_HEIGHT+1,0,0,0));
        }
        if(dir==Animation.LEFT){
            col = w.inCollision(new Collideable(x/Window.TILE_WIDTH-1,y/Window.TILE_HEIGHT+1,0,0,0));
        }
        if(dir==Animation.NONE){
            col = w.inCollision(new Collideable(x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT+1,0,0,0));
        }
        
        return col;
    }

    public int compareTo(Person p) {
        if(this.getX()<p.getX())
            return -1;
        if(this.getX()==p.getX())
        {
            if(this.getY()<p.getY())
                return -1;
        }
        if(this.getX()==p.getX() && this.getY()==p.getY())
            return 0;
        
        return 1;
    }
}