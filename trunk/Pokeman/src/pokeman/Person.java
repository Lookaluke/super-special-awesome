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
    private Collideable lastEdition1,lastEdition2,newEdition1,newEdition2;
    

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
            lastEdition1 = new Collideable(x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT,1,0,0);
            lastEdition2 = new Collideable(x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT+1,1,0,0);
            w.addToCollision(lastEdition1);
            w.addToCollision(lastEdition2);
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
        
        if(counter%Window.numberOfCounts==0 && moving){            
            walk.nextFrame(direction);
            if(direction == Animation.RIGHT)
                x += Window.TILE_WIDTH/Window.numberOfCounts;
            if(direction == Animation.LEFT)
                x -= Window.TILE_WIDTH/Window.numberOfCounts;
            if(direction == Animation.UP)
                y -= Window.TILE_WIDTH/Window.numberOfCounts;
            if(direction == Animation.DOWN)
                y += Window.TILE_WIDTH/Window.numberOfCounts;
            if(counter>=Window.numberOfCounts*(Window.numberOfCounts-1))
            {
                counter = 0;
                moving = false;


                w.removeFromCollision(lastEdition1);
                w.removeFromCollision(lastEdition2);
                lastEdition1 = newEdition1;
                lastEdition2 = newEdition2;
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

        if(canMove(dir)){

            moving = true;
            direction = dir;
            newEdition1 = new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+yChange,1,0,0);
            newEdition2 = new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+1+yChange,1,0,0);
            w.addToCollision(newEdition1);
            w.addToCollision(newEdition2);
        }
        counter = 0;
    }
    
    /**
     * Override this method to determine when a person can move
     * @param dir The direction to move
     * @return true if they can move
     */
    protected boolean canMove(int dir){
        return (1!=w.inCollision(new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+yChange,0,0,0)) || dir==Animation.DOWN) && (1!=w.inCollision(new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+1+yChange,0,0,0)) || dir==Animation.UP);
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
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