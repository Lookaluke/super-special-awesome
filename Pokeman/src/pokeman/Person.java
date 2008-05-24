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
            w.addToCollision(new Collideable(x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT,1,0,0));
            w.addToCollision(new Collideable(x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT+1,1,0,0));
            System.out.println(name); 
            walk = new Animation(name);
        }
        

    }

    public String getName()
    {
        return name;
    }
    
    public void draw(Graphics2D g,int currentX,int currentY){
        counter++;
        if(counter>=50)
        {
            int dir = (int)(Math.random()*4);
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
            System.out.println("x:"+(int)(x/Window.TILE_WIDTH+xChange)+", y:"+(int)(y/Window.TILE_HEIGHT+yChange));
            //w.removeFromCollision(new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+yChange,0,0,0));
            //w.removeFromCollision(new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+1+yChange,0,0,0));
            
            if(!w.isInCollision(new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+yChange,0,0,0)) && !w.isInCollision(new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+1+yChange,0,0,0))){
                moving = true;
                direction = dir;
                w.addToCollision(new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+yChange,1,0,0));
                w.addToCollision(new Collideable(x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+1+yChange,1,0,0));
            }
            
            counter = 0;
        }
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
            if(counter>=Window.numberOfCounts*3)
            {
                counter = 0;
                moving = false;


                System.out.println(w.removeFromCollision(new Collideable(x/Window.TILE_WIDTH-xChange,y/Window.TILE_HEIGHT-yChange,0,0,0)));
                System.out.println(w.removeFromCollision(new Collideable(x/Window.TILE_WIDTH-xChange,y/Window.TILE_HEIGHT+1-yChange,0,0,0)));
            }
        }
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