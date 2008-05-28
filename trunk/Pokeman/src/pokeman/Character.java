
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
    private int justUsed;
    private Person currentlyReading;
    
    public Character(Window w){
        super("Walking","",(int)(Window.COLUMNS/2)*Window.TILE_WIDTH,(int)(Window.ROWS/2)*Window.TILE_HEIGHT,w);
        walking = new Animation("Walking");
        press = 0;
        jumping=0;
        addPokemon(new Pokemon("Meh",5));
        
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

    
    
    @Override
    protected void update(){

        
        
        Collideable col = getCollision(direction);

        if(col!=null && ((col.getNumber(0)==2 && direction == Animation.DOWN) || (col.getNumber(0)==3 && direction == Animation.RIGHT) || (col.getNumber(0)==4 && direction == Animation.LEFT))){
            jump();
        }


        boolean isDoor = false;
        

        if(justUsed==0 && col!=null && col.getNumber(0)>=-4 && col.getNumber(0)<=-1){

            direction = -(col.getNumber(0)+1);

            int oldX = getX()/Window.WIDTH;
            int oldY = -getY()/Window.HEIGHT;

            if(getX()<0)
                oldX--;
            if(getY()<0)
                oldY++;

            for(int i=-1;i<=1;i++)
                for(int j=-1;j<=1;j++)
                    getWindow().unload(oldX+i,oldY+j);
            int levelX = col.getNumber(1);
            int levelY = col.getNumber(2);

            setX(levelX*Window.WIDTH+(int)(Window.COLUMNS/2)*Window.TILE_WIDTH);
            setY((-levelY)*Window.HEIGHT+(int)(Window.ROWS/2)*Window.TILE_HEIGHT);



            for(int i=-1;i<=1;i++)
                for(int j=-1;j<=1;j++)
                    getWindow().loadLevel(levelX+i,levelY+j,i,j);

            for(Collideable door:getWindow().getCollision()){
                if(door.getNumber(0)>=-4 && door.getNumber(0)<=-1 && Math.abs(door.getNumber(1)-oldX)<=0 && Math.abs(door.getNumber(2)-oldY)<=0){


                                     
                    setY((door.getY()-1)*Window.TILE_HEIGHT);

                    setX(door.getX()*Window.TILE_WIDTH);                
                }
            }

            getWindow().setLevel(levelX,levelY);
            isDoor = true;

        }

        if(isDoor)
            justUsed = 4;
        else
            if(justUsed>0)
                justUsed--;

        makeMove(direction);
        
        direction = Animation.NONE;
        
        
    }
    
    public int getWidth(){
        return walking.getFrame().getWidth();
    }
    
    public String read(){
        Collideable c = getCollision(getDirection());
        if(c==null)
            return null;
        Object maker = c.getMaker();
        if(maker instanceof Person){
            Person p = (Person)maker;
            if(getDirection() == Animation.UP)
                p.setDirection(Animation.DOWN);
            if(getDirection() == Animation.DOWN)
                p.setDirection(Animation.UP);
            if(getDirection() == Animation.RIGHT)
                p.setDirection(Animation.LEFT);
            if(getDirection() == Animation.LEFT)
                p.setDirection(Animation.RIGHT);
            p.allowUpdate(false);
            this.allowUpdate(false);
            currentlyReading = p;
            return p.getSpeech();
        }
        return null;
    } 
    
    public void unRead(){
        if(currentlyReading!=null)
            currentlyReading.allowUpdate(true);
        this.allowUpdate(true);
    }
    
    public int getHeight(){
        return walking.getFrame().getHeight();
    }
    
    public void direction(int dir){
        direction = dir;
    }
    
    @Override
    protected boolean canMove(int dir){
        Collideable col = getCollision(dir);
        
        boolean lower = (col==null || ((col.getNumber(0)<1 || col.getNumber(0)>4) || (col.getNumber(0)==2 && dir == Animation.DOWN) || (col.getNumber(0)==3 && dir == Animation.RIGHT) || (col.getNumber(0)==4 && dir == Animation.LEFT)));
        return (lower);      
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
