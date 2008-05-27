/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Scanner;

/**
 *
 * @author Mark
 */
public class Menus {

    private static final int widthFactor = 20, heightFactor = 10, curveFactor = 40, spaceFactor = 5;
    private static final Font f = new Font("Arial",Font.PLAIN,30);
    
    public static void textBox(Graphics2D g2, String s,int x,int y,int width,int height){
        g2.setColor(new Color(86,218,228));
        g2.fill(new RoundRectangle2D.Double(x,y,width,height,curveFactor,curveFactor));
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(x+widthFactor/2,y+heightFactor/2,width-widthFactor,height-heightFactor,curveFactor,curveFactor));
        
        g2.setColor(Color.BLACK);
        
        
        
        g2.setFont(f);
        int textHeight =  (int)f.getStringBounds(s, new FontRenderContext(null,true,true)).getHeight();
        int lines = (int)((height-heightFactor)/(textHeight+spaceFactor));
        
        int i=0;
        Scanner string = new Scanner(s);
        String words="";
        while(string.hasNext()){ 
            String last = words;
            String next = string.next()+" ";
            words+=next;
            if(f.getStringBounds(words, new FontRenderContext(null,true,true)).getWidth()>width-widthFactor){
                g2.drawString(last, x+widthFactor, y+heightFactor+textHeight*(i+1)+spaceFactor*(i-1));
                words = next;
                i++;
            }
        }
        g2.drawString(words, x+widthFactor, y+heightFactor+textHeight*(i+1)+spaceFactor*(i-1));
        
    }
    
}
