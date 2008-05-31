/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
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
    
    public static final int NONE = -1,MAIN = 0,MOVE = 1;
    
    private TextBox txt;
    private Menu menu,moveMenu;
    private int menuType;
    private JFrame frame;
    private BufferedImage background,circle;
    private Move lastMove;
    private Pokemon theirs,yours;
    
    //for testing purposes only, remove in final
    private int a = 100;


    
    private int xBorder = 150,yBorder = 30,yStart=475;
    
    public BattleFrontEnd(JFrame frame){
        try {
            this.frame = frame;
            
            menuType = NONE;

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


        if(txt!=null){
            txt.draw(g2);
            if(txt.isOver())
            {
                txt = null;                
            }
        }

        if(menu!=null){
            menu.draw(g2);
            if(menu.result()!=null && menu.result().equals("Attack"))
                makeMenu(MOVE);
        }


        
        g2.drawImage(yours.getBack(),null,xBorder,yStart-yBorder-yours.getBack().getHeight());
        g2.drawImage(theirs.getFront(),null,Window.WIDTH-xBorder-theirs.getBack().getWidth(),yBorder);
        
        drawInterface(g2, yours, 435, 320);
        drawInterface(g2, theirs, 10, 30);        
        
    }
    
    public void setPokemon(Pokemon p,boolean isYours){
        if(isYours){
            yours = p;
        }else{
            theirs = p;
        }
    }
    
    public void setText(String text){
        txt = new TextBox(frame,text,0,475,800,101,true);
    }
    
    public void makeMenu(int type){
        menuType = type;
        
        if(type == MAIN){
            String[] str = {"Attack","Pokemon","Item","Run"};
            menu = new Menu(frame,str,0,yStart,600,576-yStart);
        }
        
        if(type == MOVE){
            Move[] moves = yours.getMoves();
            String[] str = {moves[0]==null?null:moves[0].name(),moves[1]==null?null:moves[1].name(),moves[2]==null?null:moves[2].name(),moves[3]==null?null:moves[3].name()};
            menu = new Menu(frame,str,0,yStart,600,576-yStart);
        }
    }
    
    public Object getResult(){
        if(menu == null)
            return null;
        String result = menu.result();
        if(result == null)
            return null;
        
        Object ret = null;
        
        if(menuType == MOVE){
            ret = yours.getMoves()[menu.getSelected()];
        }
        
        if(result!=null){
            menu = null;
            menuType = NONE;
        }
        return ret;
    }
    
    public boolean waiting(){
        return txt!=null || menu!=null || moveMenu!=null;
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
        
        //DRAW INSIDE WHITE ROUNDRECT
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
        
        //draw "HP"
        Rectangle2D.Double square = new Rectangle2D.Double(x+6, y+2, 4, 4);
        cone = new Color(248, 208, 80);
        ctwo = new Color(248, 176, 64);
        
        g2.setColor(cone);
        g2.fill(square);
        square.x = x+12;
        g2.fill(square);
        square.x = x+18;
        g2.fill(square);
        square.x = x+22;
        square.height = 2;
        square.width = 6;
        g2.fill(square);
        square.width = 2;
        square.height = 2;
        square.x = x+26;
        square.y = y+4;
        g2.fill(square);
        g2.setColor(ctwo);
        square.width = 4;
        square.height = 4;
        square.x = x+6;
        square.y = y+8;
        g2.fill(square);
        square.x = x+12;
        g2.fill(square);
        square.x = x+18;
        g2.fill(square);
        square.x = x+6;
        square.y = y+6;
        square.width = 10;
        square.height = 2;
        g2.fill(square);
        square.x = x+18;
        g2.fill(square);
        
    }
    
    public void drawInterface(Graphics2D g2, Pokemon p,int x, int y){
        int WIDTH = 250,HEIGHT = 75,ARC = 20;
        int HP_XSHIFT = 55,HP_YSHIFT = 35;
        int NAME_XSHIFT = 35,NAME_YSHIFT = 0;
        int HP2_XSHIFT = 100,HP2_YSHIFT = 38;
        
        Font f = new Font("Pokemon RS part B",Font.BOLD,20);
        
           
        g2.setFont(f);
        
        g2.setColor(new Color(77,104,99));
        g2.fill(new RoundRectangle2D.Double(x,y,WIDTH,HEIGHT,ARC,ARC));
        g2.setColor(new Color(32,56,0));
        g2.fill(new RoundRectangle2D.Double(x+3,y+3,WIDTH-6,HEIGHT-6,ARC,ARC));
        g2.setColor(new Color(252,249,216));        
        g2.fill(new RoundRectangle2D.Double(x+5,y+5,WIDTH-10,HEIGHT-10,ARC,ARC));
        drawHpBar(g2, x+HP_XSHIFT, y+HP_YSHIFT, p.getMaxHP(), p.getCurrentHP());
        
        g2.setColor(new Color(62,59,68));     
        //System.out.println((f.getStringBounds("", new FontRenderContext(null,true,true)).getHeight()));
        //g2.draw(new Rectangle2D.Double(x+NAME_XSHIFT,y+NAME_YSHIFT,f.getStringBounds("Hello", new FontRenderContext(null,true,true)).getWidth(),f.getStringBounds("Hello", new FontRenderContext(null,true,true)).getHeight()));
        String name = p.getName() + "  Lv: "+p.getLevel();
        g2.drawString(name, x+NAME_XSHIFT,y+NAME_YSHIFT+ (int)(f.getStringBounds(name, new FontRenderContext(null,true,true)).getHeight()));
        String hp = p.getCurrentHP() + "/" + p.getMaxHP();
        g2.drawString(hp, x+HP2_XSHIFT,y+HP2_YSHIFT+ (int)(f.getStringBounds(hp, new FontRenderContext(null,true,true)).getHeight()));
    }
}