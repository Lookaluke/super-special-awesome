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
public class Character extends Person{
    private Animation walking;
    private int direction;
    private int press,jumping;
    private Pokemon[] pkmn = new Pokemon[6];
    
    public Character(Window w){
        super("Walking","",(int)(Window.COLUMNS/2)*Window.TILE_WIDTH,(int)(Window.ROWS/2)*Window.TILE_HEIGHT,w);
        walking = new Animation("Walking");
        press = 0;
        jumping=0;
        
    }
    
    public void draw(Graphics2D g,int currentX,int currentY){

        int moreHeight = 0;
        if(jumping<Window.numberOfCounts+1){
            if(jumping>=Window.numberOfCounts/2)
                moreHeight = (jumping-Window.numberOfCounts)*4;
            if(jumping<Window.numberOfCounts/2)
                moreHeight = (jumping)*-4;
        }
        if(jumping>0){            
            jumping--;
            if(jumping==1)
                direction(getDirection());

        }
        super.draw(g,currentX,currentY+moreHeight);
    }
    
    /*public void draw(Graphics2D g){
        if(press>4)
            walking.standingFrame();
        press++;
        int moreHeight = 0;
        if(jumping>=maxJump/2)
            moreHeight = (jumping-maxJump)*4;
        if(jumping<maxJump/2)
            moreHeight = (jumping)*-4;
        if(jumping>0)
            jumping--;
        g.drawImage(walking.getFrame(),null,Window.WIDTH/2-walking.getFrame().getWidth()/2,Window.HEIGHT/2-walking.getFrame().getHeight()/2+moreHeight);
        //g.draw(new Rectangle(Window.WIDTH/2-walking.getFrame().getWidth()/2,Window.HEIGHT/2-walking.getFrame().getHeight()/2,walking.getFrame().getWidth(),walking.getFrame().getHeight()));
    }*/
    
    
    @Override
    protected void update(){

        Collideable[] col = getCollision(direction);
        for(int i=0;i<=1;i++){
            if(col[i]!=null && ((col[i].getNumber(0)==2 && direction == Animation.DOWN) || (col[i].getNumber(0)==3 && direction == Animation.RIGHT) || (col[i].getNumber(0)==4 && direction == Animation.LEFT))){
                jump();
                break;
            }
        }   

        
        for(int k=0;k<=1;k++){
            if(col[k]!=null && col[k].getNumber(0)>=-4 && col[k].getNumber(0)<=-1){
                
                direction = -(col[k].getNumber(0)+1);
                
                int oldX = getX()/Window.WIDTH;
                int oldY = -getY()/Window.HEIGHT;
                
                if(getX()<0)
                    oldX--;
                if(getY()<0)
                    oldY++;
                
                for(int i=-1;i<=1;i++)
                    for(int j=-1;j<=1;j++)
                        getWindow().unload(oldX+i,oldY+j);
                int levelX = col[k].getNumber(1);
                int levelY = col[k].getNumber(2);

                setX(levelX*Window.WIDTH+(int)(Window.COLUMNS/2)*Window.TILE_WIDTH);
                setY((-levelY)*Window.HEIGHT+(int)(Window.ROWS/2)*Window.TILE_HEIGHT);

                
                
                for(int i=-1;i<=1;i++)
                    for(int j=-1;j<=1;j++)
                        getWindow().loadLevel(levelX+i,levelY+j,i,j);
                
                for(Collideable door:getWindow().getCollision()){
                    if(door.getNumber(0)>=-4 && door.getNumber(0)<=-1 && Math.abs(door.getNumber(1)-oldX)<=0 && Math.abs(door.getNumber(2)-oldY)<=0){
                        
                        System.out.println("IN");
                        
                        if(direction == Animation.UP)                      
                            setY((door.getY()-1)*Window.TILE_HEIGHT);
                        else
                            setY((door.getY()+1)*Window.TILE_HEIGHT);
                        setX(door.getX()*Window.TILE_WIDTH);                
                    }
                }
                
                getWindow().setLevel(levelX,levelY);
                

            }
        }


        makeMove(direction);
        direction = Animation.NONE;
        
        
    }
    
    public int getWidth(){
        return walking.getFrame().getWidth();
    }
    
    public int getHeight(){
        return walking.getFrame().getHeight();
    }
    
    public void direction(int dir){
        direction = dir;
    }
    
    @Override
    protected boolean canMove(int dir){
        Collideable[] col = getCollision(dir);
        boolean upper = (col[0]==null || ((col[0].getNumber(0)<1 || col[0].getNumber(0)>4) || (col[0].getNumber(0)==2 && dir == Animation.DOWN) || (col[0].getNumber(0)==3 && dir == Animation.RIGHT) || (col[0].getNumber(0)==4 && dir == Animation.LEFT) || dir == Animation.DOWN));
        boolean lower = (col[1]==null || ((col[1].getNumber(0)<1 || col[1].getNumber(0)>4) || (col[1].getNumber(0)==2 && dir == Animation.DOWN) || (col[1].getNumber(0)==3 && dir == Animation.RIGHT) || (col[1].getNumber(0)==4 && dir == Animation.LEFT) || dir == Animation.UP));
        return (upper && lower);      
    }
    
    public void jump(){
        jumping = Window.numberOfCounts+1;
    }
    
    public void addPokemon(Pokemon p){
        for(int i=0;i<pkmn.length;i++)
            if(pkmn[i] == null){
                pkmn[i] = p;
                break;
            }
    }
    
    public Pokemon[] currentPokemon(){
        return pkmn;
    }
    
}
