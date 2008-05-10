/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Graphics2D;

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
        if(!press)
            walking.standingFrame();
        g.drawImage(walking.getFrame(),null,Window.WIDTH/2-walking.getFrame().getWidth()/2,Window.HEIGHT/2-walking.getFrame().getHeight()/2);
        press = false;
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
