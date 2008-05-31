/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JFrame;

/**
 *
 * @author Mark
 */
public class Menu {
    private static final int widthFactor = 20, heightFactor = 5, curveFactor = 40, ySpaceFactor = 5, xSpaceFactor = 20;
    private static final Font f = new Font("Pokemon RS",Font.PLAIN,30);
    
    private String[] items;
    private int x,y,width,height,textHeight;
    private JFrame frame;
    
    private int lines;
    private int selected;
    private String result;
    
    public Menu(JFrame fr,String[] menuItems,int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        items = menuItems;
        
        fr.addKeyListener(new Key());
        frame = fr;
        
        textHeight = (int)f.getStringBounds(items[0], new FontRenderContext(null,true,true)).getHeight();
        lines = (height-heightFactor*2-ySpaceFactor)/(textHeight);
        result = null;
        selected = 0;
    }
    
    public void draw(Graphics2D g2){
        
        g2.setColor(new Color(86,218,228));
        g2.fill(new RoundRectangle2D.Double(x,y,width,height,curveFactor,curveFactor));
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(x+widthFactor/2,y+heightFactor/2,width-widthFactor,height-heightFactor,curveFactor,curveFactor));

        g2.setColor(Color.BLACK);
        
        g2.setFont(f);
        if(result==null){
            int perLine = items.length/lines;
            int xSpace = (width-2*widthFactor-xSpaceFactor)/perLine;
            for(int j=0;j<lines;j++){
                for(int i=0;i<perLine;i++){
                    String str = items[j*perLine+i];
                    if(items[j*perLine+i]==null)
                        str = "---";
                    g2.drawString(str,x+widthFactor+xSpaceFactor*(i+1)+xSpace*i,y+heightFactor+ySpaceFactor*(j)+textHeight*(j+1));
                    if(selected == j*perLine+i){
                        int x1 = x+widthFactor+xSpaceFactor*(i+1)+xSpace*i;
                        int y1 = y+heightFactor+ySpaceFactor*(j)+textHeight*(j+1);
                        int[] xCoords = {x1-15,x1-15,x1-5};
                        int[] yCoords = {y1-5,y1-19,y1-12}; 
                        g2.fill(new Polygon(xCoords,yCoords,3));
                    }
                }
            }
        }
        

        
    }
    
    public String result(){
        return result;
    }
    
    public int getSelected(){
        return selected;
    }
    
    public class Key implements KeyListener{

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_UP && selected-items.length/lines>=0 && (items[selected-items.length/lines]!=null))
                selected-=items.length/lines;
            if(e.getKeyCode()==KeyEvent.VK_DOWN && selected+items.length/lines<=items.length-1 && (items[selected+items.length/lines]!=null))
                selected+=items.length/lines;
            if(e.getKeyCode()==KeyEvent.VK_RIGHT && selected<items.length-1 && (items[selected+1]!=null))
                selected++;
            if(e.getKeyCode()==KeyEvent.VK_LEFT && selected>0 && (items[selected-1]!=null))
                selected--;
            if(e.getKeyCode()==KeyEvent.VK_Z)
                result = items[selected];

        }

        public void keyReleased(KeyEvent e) {
        }

    }
}
