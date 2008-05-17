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
    private int press,jumping;

    public Character(){
        walking = new Animation("Walking");
        press = 0;
        direction = Animation.DOWN;
    }
    
    public void draw(Graphics2D g){
        if(press>4)
            walking.standingFrame();
        press++;
        int moreHeight = 0;
        if(jumping>=4)
            moreHeight = (jumping-8)*4;
        if(jumping<4)
            moreHeight = (jumping)*4;
        if(jumping>0)
            jumping--;
        g.drawImage(walking.getFrame(),null,Window.WIDTH/2-walking.getFrame().getWidth()/2,Window.HEIGHT/2-walking.getFrame().getHeight()/2+moreHeight);
        //g.draw(new Rectangle(Window.WIDTH/2-walking.getFrame().getWidth()/2,Window.HEIGHT/2-walking.getFrame().getHeight()/2,walking.getFrame().getWidth(),walking.getFrame().getHeight()));
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
        press=0;
    }
    
    public void jump(){
        jumping=8;
    }
    
}
