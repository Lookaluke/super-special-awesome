
package pokeman;

import java.awt.Graphics2D;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private ArrayList<Pokemon> allPokemon = new ArrayList<Pokemon>();
    private static final int MAX_NUMBER_OF_POKEMON = 200;
    
    public Character(Window w){
        super("Walking","",(int)(Window.COLUMNS/2)*Window.TILE_WIDTH,(int)(Window.ROWS/2)*Window.TILE_HEIGHT,w);
        walking = new Animation("Walking");
        press = 0;
        jumping=0;
        addPokemon(new Pokemon("Meh",1));
        addPokemon(new Pokemon("Rattata",8));
        addPokemon(new Pokemon("Bulbasaur",7));
        addPokemon(new Pokemon("Friger",6));
        addPokemon(new Pokemon("Charmander",16));
        addPokemon(new Pokemon("Magikarp",16));

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
        
        if(moreHeight != 0)
        {
            g.setColor(Color.BLACK);
            g.fillOval(Window.TILE_WIDTH * 12, (int)(Window.TILE_HEIGHT * 10.6), Window.TILE_WIDTH,Window.TILE_HEIGHT / 2);
        }
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

         col = getCollision(Animation.NONE);
        
        if(direction!=Animation.NONE && !isMoving() && col!=null && col.getNumber(0)>=50 && col.getNumber(0)<150 && Math.random()<.1){
            try {
                ArrayList<String> s = new ArrayList<String>();
                Scanner input = new Scanner(new File("Areas\\" + col.getNumber(0)+".txt"));
                while (input.hasNextLine()) {
                    s.add(input.nextLine());
                }
                String actual = s.get((int)(Math.random()*s.size()));
                int firstIndex = actual.indexOf(":");
                String name = actual.substring(0,firstIndex);
                int level = Integer.parseInt(actual.substring(firstIndex+1));
                getWindow().startBattle(new Battle(this, new Pokemon(name, level), getWindow().getFrame()));
            } catch (FileNotFoundException ex) {
                System.out.println("area file doesn't exist");
            }
        }else
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
    
    public void switchPokemon(int index1,int index2){
        Pokemon hold = pkmn[index1];
        pkmn[index1] = pkmn[index2];
        pkmn[index2] = hold;
    }
    
    //takes pokemon in the index of current pkmn and adds to pc.
    public boolean leavePokemon(int index){
        if(index >= 0 && index <= 5 && allPokemon.size() < MAX_NUMBER_OF_POKEMON)
        {
            Pokemon transfer = pkmn[index];
            pkmn[index] = null;
            allPokemon.add(transfer);
            return true;
        }
        else
            return false;
    }
    
    //takes pokemon in the index of pc and adds to pokemon if there is space.
    public boolean takePokemon(int index){
        if(index < allPokemon.size() && index >= 0)
        {
            for(int i = 0; i < pkmn.length; i++)
            {
                if(pkmn[i] == null)
                {
                    Pokemon transfer = allPokemon.remove(index);
                    addPokemon(transfer);
                    return true;
                }
            }
            return false;
        }
        else
            return false;
    }
}
