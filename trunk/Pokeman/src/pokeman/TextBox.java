/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.RoundRectangle2D;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 *
 * @author Mark
 */
public class TextBox{
    private static final int widthFactor = 20, heightFactor = 10, curveFactor = 40, spaceFactor = 5;
    private static final Font f = new Font("Arial",Font.PLAIN,30);
    
    private String[] str;
    private int x,y,width,height,textHeight;
    private int line,lines;
    private JFrame frame;
    private boolean over;
    private int index,finalIndex;
            
    public TextBox(JFrame fr,String s,int x,int y,int width,int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        
        fr.addKeyListener(new Key());
        frame = fr;
        
        
        
        textHeight =  (int)f.getStringBounds(s, new FontRenderContext(null,true,true)).getHeight();
        lines = (int)((height-heightFactor)/(textHeight+spaceFactor));
        
        str = new String[(int)f.getStringBounds(s, new FontRenderContext(null,true,true)).getWidth()/(width-widthFactor)+1];
        
        Scanner string = new Scanner(s);
        String words="";
        int i = 0;
        while(string.hasNext()){ 
            String last = words;
            String next = string.next()+" ";
            words+=next;
            if(f.getStringBounds(words, new FontRenderContext(null,true,true)).getWidth()>width-widthFactor){
                str[i] = last;
                words = next;
                i++;
            }
        }
        str[i] = words;
        
        index = 0;
        finalIndex = 0;
        for(int j=0;j<lines;j++)    
            if(j<str.length)
                finalIndex += str[lines].length();
        
    }
    
    public void draw(Graphics2D g2){
        if(!over){
            g2.setColor(new Color(86,218,228));
            g2.fill(new RoundRectangle2D.Double(x,y,width,height,curveFactor,curveFactor));
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Double(x+widthFactor/2,y+heightFactor/2,width-widthFactor,height-heightFactor,curveFactor,curveFactor));

            g2.setColor(Color.BLACK);



            g2.setFont(f);

            int i=-line;
            while(i<lines && i+line<str.length){ 

                if(i>=0){
                    if(index>str[i+line].length())
                        g2.drawString(str[i+line], x+widthFactor, y+heightFactor+textHeight*(i+1)+spaceFactor*(i-1));
                    else{
                        int tempIndex=0;
                        for(int j=0;j<i;j++)
                            tempIndex -= str[j+line].length();
                        g2.drawString(str[i+line].substring(index), x+widthFactor, y+heightFactor+textHeight*(i+1)+spaceFactor*(i-1));
                    }
                        
                }
                i++;

            }
        }

    }

    public boolean isOver(){
        return over;
    }
    
    public class Key implements KeyListener{

        public void keyTyped(KeyEvent e) {
            if(e.getKeyChar()=='x'){
                
            }
            if(e.getKeyChar()=='z' && index==finalIndex){
                line+=2;
                for(int j=line;j<line+2;j++)    
                    if(j<str.length)
                        finalIndex += str[j].length();
                if(line>str.length){
                    frame.removeKeyListener(this);
                    over = true;
                }
            }
        }

        public void keyPressed(KeyEvent e) { 
        }

        public void keyReleased(KeyEvent e) {
        }
    }
            
}
