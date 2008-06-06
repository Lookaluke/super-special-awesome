
package pokeman;

import java.awt.Graphics2D;
import javax.swing.JFrame;

/**
 * The back-end to the battle system. Each battle is instantiated, with 
 * either a pokemon and you, or a trainer and you.
 * @author Kunal
 */

public class Battle {
    private Pokemon yours;
    private Pokemon[] theirs;
    
    //should this be an int?
    private Pokemon theirCurrent;
    
    private BattleFrontEnd frontEnd;
    private Thread turnThread;
    private boolean isOver;

    
    
    public Battle(Character you, Pokemon enemy,JFrame frame) {
        frontEnd = new BattleFrontEnd(frame,you);
        yours = you.currentPokemon()[0];
        theirs = new Pokemon[1];
        theirs[0] = enemy;
        theirCurrent = enemy;
        frontEnd.setPokemon(theirCurrent, false);
        frontEnd.setPokemon(yours,true);
        frontEnd.setText("A "+enemy.getName()+" has appeared!");
        isOver = false;
    }
    
  
    
    /*
    public Battle(Character you, Trainer enemy){
        theirs = enemy.getPokemonArray();
        theirCurrent = theirs[0];
        here, we set the music to trainer music
    }
    */

    
    /**
     * tells the front end to draw the stuff.
     * @param g2
     */
    public void draw(Graphics2D g2){
        boolean over = true;
        for(int i=0;i<theirs.length;i++){
            if(theirs[i].getCurrentHP()!=0)
                over = false;
        }
        isOver = over && !frontEnd.waitingForHP();// && !frontEnd.waiting();
        

        
        frontEnd.draw(g2);
        
        if(!frontEnd.waiting() && (turnThread==null || !turnThread.isAlive()))
            frontEnd.makeMenu(BattleFrontEnd.MAIN);
        
        Object result = frontEnd.getResult();
        if(result!=null && result instanceof Move){
            turnThread = new Thread(new Turn((Move)result,theirCurrent,yours,frontEnd));
            turnThread.start();
        }
        
        if(isOver){
            turnThread.interrupt();
            turnThread = null;
        }
        
    }
    
    public boolean isOver(){
        return isOver;
    }
    
}
