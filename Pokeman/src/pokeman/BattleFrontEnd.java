/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
    private static final int MAX_HEALTH_BAR_SIZE = 450;
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
        
        drawHpBar(g2, 300, 400, 100, a);
        if(a > 1)a--;

    }
    
    public void drawHpBar(Graphics2D g2, int x, int y, int totalHP, int currentHP){
        double percent = ((double)currentHP)/totalHP;
        if(percent <= 0.2)
            g2.setColor(new Color(220, 50, 0));//RED
        else if(percent <= 0.55)
            g2.setColor(new Color(220, 200, 75));//YELLOW
        else
            g2.setColor(new Color(60, 120, 45));//GREEN
        
        int totalPixels = (int)(percent* MAX_HEALTH_BAR_SIZE + 0.5);
        g2.fillRect(x, y, totalPixels, 35);
        g2.setColor(Color.BLACK);
        g2.drawString("" + currentHP + "/" + totalHP, x + 350, y + 70);

    }
    
}