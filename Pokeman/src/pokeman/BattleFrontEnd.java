/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Mark
 */
public class BattleFrontEnd {
    
    private Battle battle;
    private TextBox txt;
    private Menu menu,moveMenu;
    private JFrame frame;
    private BufferedImage background,circle;
    
    
    //for testing purposes only, remove in final
    private int a = 100;


    
    private int xBorder = 150,yBorder = 30,yStart=475;
    
    public BattleFrontEnd(Battle b,JFrame frame){
        try {
            this.frame = frame;
            battle = b;
            txt = new TextBox(frame, battle.getText(), 0, yStart, 800, 576 - yStart, true);
            background = ImageIO.read(new File("Images\\BattleBackground.png"));
            circle = ImageIO.read(new File("Images\\circle.png"));
        } catch (IOException ex) {
            Logger.getLogger(BattleFrontEnd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void draw(Graphics2D g2){
        
        g2.drawImage(background,null,0,0);
        g2.drawImage(circle,null,0,yStart-yBorder-circle.getHeight()+75);
        g2.drawImage(circle,null,Window.WIDTH-circle.getWidth(),yBorder+75);
        
        String battleText = battle.getText();
        if(battleText!=null && txt==null){
            txt = new TextBox(frame,battleText,0,yStart,800,576-yStart,true);
        }
        if(txt!=null){
            txt.draw(g2);
            if(txt.isOver())
            {
                txt = null;
                if(menu==null){
                    String[] str = {"Attack","Pokemon","Item","Run"};
                    menu = new Menu(frame,str,0,yStart,600,576-yStart);
                }
            }
        }
        
        if(menu!=null){
            menu.draw(g2);
            if(menu.result()!=null){
                if(menu.result().equals("Attack")){
                    Move[] moves = battle.getYourPokemon().getMoves();
                    String[] str = {moves[0]==null?"---":moves[0].name(),moves[1]==null?"---":moves[1].name(),moves[2]==null?"---":moves[2].name(),moves[3]==null?"---":moves[3].name()};
                    moveMenu = new Menu(frame,str,0,yStart,600,576-yStart);
                }
                    
                menu=null;                
                
            }
        }
        
        if(moveMenu!=null){
            moveMenu.draw(g2);
            if(moveMenu.result()!=null){
                battle.turn(battle.getYourPokemon().getMoves()[moveMenu.getSelected()]);
                moveMenu = null;
            }
        }
        
        g2.drawImage(battle.getYourPokemon().getBack(),null,xBorder,yStart-yBorder-battle.getYourPokemon().getBack().getHeight());
        g2.drawImage(battle.getTheirPokemon().getFront(),null,Window.WIDTH-xBorder-battle.getYourPokemon().getBack().getWidth(),yBorder);
        
        //for testing only, remove in final
        drawHpBar(g2, 435, 320, 100, a);
        
        //for testing only, remove in final
        if(a > 1) a--;
        
        
    }
    
    public void drawHpBar(Graphics2D g2, int x, int y, int totalHP, int currentHP){

        //sizes for the large dark green round rectangle
        int MAINWIDTH = 132, MAINHEIGHT = 14, MAINARCS = 10;

        
        //size, offset for the white round rectangle
        int FirstXOffset = 30, FirstYOffset = 2, WhiteWidth = 100, WhiteHeight = 10,
                WhiteArcs = 10;
        
        //size, offset for the colored strip
        int XRectOffset = 35, YRectOffset = 4, RectMaxWidth = 93, SmallHeight = 2;
        
        //DRAW OUTSIDE BORDER
        g2.setColor(new Color(80, 104, 88));//dark green, for border
        RoundRectangle2D r = new RoundRectangle2D.Double(x, y, MAINWIDTH, MAINHEIGHT,
                MAINARCS, MAINARCS);
        
        g2.fill(r);
        
        //DRAW INSIDE WHITE RRECT
        RoundRectangle2D insiderect = new RoundRectangle2D.Double(x+FirstXOffset,
                y+FirstYOffset, WhiteWidth, WhiteHeight, WhiteArcs, WhiteArcs);
        g2.setColor(Color.WHITE);
        g2.fill(insiderect);
        
        //DRAW COLORED HEALTH BAR
        Color cone;
        Color ctwo;
        if(currentHP > ((int)(2.25 * totalHP/4 + 0.75))){
            cone = new Color(88, 208, 128);
            ctwo = new Color(112, 248, 168);
        } else if (currentHP > (265*(int)(0.2*totalHP + 0.2)/256 + 2)) {
            cone = new Color(200, 168, 8);
            ctwo = new Color(248, 224, 56);
        } else {
            cone = new Color(168, 64, 72);
            ctwo = new Color(248, 88, 56);
        }
        
        int RectActualWidth =(int)(RectMaxWidth * (currentHP/(double)totalHP));
        
        Rectangle2D.Double rect = new Rectangle2D.Double(x+XRectOffset, y+YRectOffset,
                RectActualWidth, SmallHeight);
        g2.setColor(cone);
        g2.fill(rect);
        rect = new Rectangle2D.Double(x+XRectOffset, y+YRectOffset+SmallHeight,
                RectActualWidth, SmallHeight*2);
        g2.setColor(ctwo);
        g2.fill(rect);
        
        rect = new Rectangle2D.Double(x + XRectOffset + RectActualWidth, y + YRectOffset,
                RectMaxWidth - RectActualWidth, SmallHeight);
        g2.setColor(new Color(72, 64, 88));//dark purpleish
        g2.fill(rect);
        
        rect = new Rectangle2D.Double(x+XRectOffset + RectActualWidth,
                y+YRectOffset+SmallHeight, RectMaxWidth -RectActualWidth,
                SmallHeight*2);
        g2.setColor(new Color(80, 104, 88));//dark green, same as border
        g2.fill(rect);
    }
}