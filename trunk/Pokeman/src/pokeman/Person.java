package pokeman;

import java.awt.Graphics2D;


public class Person
{
    private String name,speech;
    private int x,y,direction;
    private Animation walk;
    private int screenX,screenY;

    /**
     * Constructor for objects of class Person
     */
    public Person(String n,String s,int x,int y)
    {
        speech = s;
        name = n;
        direction = Animation.DOWN;
        screenX = x/Window.WIDTH;
        screenY = y/Window.HEIGHT;
        this.x = x;
        this.y = y;
        walk = new Animation(name);
    }

    public String getName()
    {
        return name;
    }
    
    public void draw(Graphics2D g){
        g.drawImage(walk.getFrame(),null,x,y);
        walk.nextFrame(direction);
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
}