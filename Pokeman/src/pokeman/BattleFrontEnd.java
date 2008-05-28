/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Graphics2D;
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
    
    private int xBorder = 75,yBorder = 30,yStart=475;
    
    public BattleFrontEnd(Battle b,JFrame frame){
        this.frame = frame;
        battle = b;
        txt = new TextBox(frame,battle.getText(),0,yStart,800,576-yStart,true);
    }
    
    public void draw(Graphics2D g2){
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
    }
}
