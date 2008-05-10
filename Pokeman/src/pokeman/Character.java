/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Mark
 */
public class Character {
    private Animation walking;
    private int direction;
    private boolean press;

    public Character(){
        walking = new Animation("Walking");
        press = false;
        direction = Animation.DOWN;
    }
    
    public void draw(Graphics2D g){
        
        g.drawImage(walking.getFrame(),null,Window.WIDTH/2-walking.getFrame().getWidth()/2,Window.HEIGHT/2-walking.getFrame().getHeight()/2);
        g.draw(new Rectangle(Window.WIDTH/2-walking.getFrame().getWidth()/2,Window.HEIGHT/2-walking.getFrame().getHeight()/2,walking.getFrame().getWidth(),walking.getFrame().getHeight()));
        press = false;
    }
    
    public int getWidth(){
        return walking.getFrame().getWidth();
    }
    
    public int getHeight(){
        return walking.getFrame().getHeight();
    }
    
    public void direction(int dir){
        if(direction != dir)
        {
            direction = dir;
            walking.setFrame(dir,0);
        }
        walking.nextFrame(direction);
        press = true;
        
    }
}
