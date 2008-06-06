/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Mark
 */
public class PokemonMenu {

    private Character character;
    
    private static final int X_SHIFT = 50,Y_SHIFT=150,WIDTH1=200,HEIGHT1=130;
    private static final Color SELECTED_OUT_LINE = new Color(247,144,47),NOT_SELECTED_OUT_LINE = new Color(31,94,150),SELECTED_UP = new Color(248,185,144),SELECTED_DOWN = new Color(253,208,203),
            NOT_SELECTED_UP = new Color(56,144,216), NOT_SELECTED_DOWN = new Color(133,191,215);
    
    public PokemonMenu(Character c){
        character = c;
    }
    
    public void draw(Graphics2D g2){

        g2.setColor(new Color(33,104,96));
        g2.fill(new Rectangle2D.Double(0,0,Window.WIDTH,Window.HEIGHT));
        
        bigPkm(false,g2,character.currentPokemon()[0]);
    }
    
    public void bigPkm(boolean selected, Graphics2D g2,Pokemon p){
        
        Font f = Window.FONT;
        f = f.deriveFont(Font.PLAIN, 20);
        g2.setFont(f);
        
        
        if(p==null)
            return;
        Color color1 = NOT_SELECTED_UP,color2 = NOT_SELECTED_DOWN,color3 = NOT_SELECTED_OUT_LINE;
        
        if(selected)
        {
            color1 = SELECTED_UP;
            color2 = SELECTED_DOWN;
            color3 = SELECTED_OUT_LINE;
        }
        
        g2.setColor(color3);
        g2.fill(new RoundRectangle2D.Double(X_SHIFT-5,Y_SHIFT-5,WIDTH1+10,HEIGHT1+10,5,5));
        
        g2.setColor(color1);
        g2.fill(new Rectangle2D.Double(X_SHIFT,Y_SHIFT,WIDTH1,HEIGHT1));
        g2.setColor(color2);
        g2.fill(new Rectangle2D.Double(X_SHIFT,Y_SHIFT+HEIGHT1*2/3,WIDTH1,HEIGHT1/3));
        
        BattleFrontEnd.drawHpBar(g2, X_SHIFT+40, Y_SHIFT+HEIGHT1*2/3, p.getCurrentHP(), p.getMaxHP(), p.getCurrentHP()/(double)p.getMaxHP());
        
        String hp = p.getCurrentHP() + "/" + p.getMaxHP();
        
        g2.setColor(Color.BLACK);
        g2.drawString(hp, X_SHIFT+100, (int)(Y_SHIFT+HEIGHT1*2/3+f.getStringBounds(hp, new FontRenderContext(null,true,true)).getHeight())+10);
        String name = p.getName() + " Lvl: "+p.getLevel();
        g2.drawString(name, (int)(X_SHIFT+WIDTH1/2-f.getStringBounds(name, new FontRenderContext(null,true,true)).getWidth()/2), (int)(Y_SHIFT+20+f.getStringBounds(name, new FontRenderContext(null,true,true)).getHeight()));
        g2.drawImage(p.getFront().getScaledInstance(40, 40, Image.SCALE_DEFAULT), X_SHIFT+5, Y_SHIFT+20,null);
    }
    
    public boolean isOver(){
        return false;
    }
    
}
