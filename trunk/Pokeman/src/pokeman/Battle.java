
package pokeman;

import java.awt.Graphics2D;
import javax.swing.JFrame;

/**
 * The back-end to the battle system. Each battle is instantiated, with 
 * either a pokemon and 
 * @author Kunal
 */

public class Battle {
    private Pokemon yours;
    private Pokemon[] theirs;
    
    //should this be an int?
    private Pokemon theirCurrent;
    
    private BattleFrontEnd frontEnd;


    
    
    public Battle(Character you, Pokemon enemy,JFrame frame) {
        frontEnd = new BattleFrontEnd(frame);
        yours = you.currentPokemon()[0];
        theirs = new Pokemon[1];
        theirs[0] = enemy;
        theirCurrent = enemy;
        frontEnd.setPokemon(theirCurrent, false);
        frontEnd.setPokemon(yours,true);
        frontEnd.setText("A "+enemy.getName()+" has appeared!");
    }
    
  
    



    /*
    public Battle(Character you, Trainer enemy){
        theirs = enemy.getPokemonArray();
        theirCurrent = theirs[0];
        here, we set the music to trainer music
    }
    */
    public void turn(Move yourMove){
        //first, you check the speeds to see who goes first
        //then you do the calculation on the first one
        //you check if dead. if not, then do the second move
        //check if dead again.
        
        //subtracts pp, and if the move has no PP, it breaks out
        //we may modify the return to give more info to the frontend
        if(!yourMove.useMove())
            return;
        
        
        
        Move theirMove=null;
        while(theirMove==null){
            int movenumber = (int)(Math.random() * 4);        
             theirMove = theirCurrent.getMoves()[movenumber]; 
        }
               
        if (yours.getSpeed() > theirCurrent.getSpeed())
        {
            //perform your move first
            
            //we need to modify takeDamage() to properly handle pokemon fainting
            theirCurrent.takeDamage(calculateDamage(yourMove, true));
            
           
            //now do their move
            yours.takeDamage(calculateDamage(theirMove, false));
        } 
        else 
        {
            yours.takeDamage(calculateDamage(theirMove, false));
            theirCurrent.takeDamage(calculateDamage(yourMove, true));
        }
        frontEnd.setText("It's super effective");
    }
    
    public void draw(Graphics2D g2){
        frontEnd.draw(g2);
        
        Object result = frontEnd.getResult();
        if(result!=null && result instanceof Move){
            turn((Move)result);
        }
        
        if(!frontEnd.waiting())
            frontEnd.makeMenu(BattleFrontEnd.MAIN);
        
        
    }
    
    /**
     * returns the damage done, -1 if it misses
     * @param m the move to calculate damage with
     * @param isPerson true if its the human controlled one attacking the comp
     * @return the damage, -1 if it misses.
     */
    private int calculateDamage(Move m, boolean isPerson){
        
        
        Pokemon attacker;
        Pokemon defender;
        if (isPerson){
            attacker = yours;
            defender = theirCurrent;
        } else {
            attacker = theirCurrent;
            defender = yours;
        }
        

        //int accuracything = 0;//(int) (attacker.getAccuracy() * m.accuracy() * 0.0256);
        int accuracything = (int) ( m.accuracy() * 2.56);
            
        if ((int)(Math.random() * 256) < accuracything)
        {
            //move hit, now calculate damage
            int level = attacker.getLevel();
            int attack = attacker.getAttack();
            int power = m.power();
            int defense = defender.getDefense();
            double stab;
            if (m.element() == attacker.getElement1() || m.element() == attacker.getElement2())
            {
                stab = 1.5;
            }
            else 
            {
                stab = 1;
            }
            double typeModifer = m.element().multiplerAgainst(defender.getElement1()) * m.element().multiplerAgainst(defender.getElement2()) * 10;
            //tell front end its super/not very effective?
            int randomNumber = (int)(Math.random() * 39) + 217;
            System.out.println((int)(((((Math.min(((((2*level/5 + 2)*attack*power)/Math.max(1, defense))/50), 997) + 2)*stab)*typeModifer)/10)*randomNumber)/255);
            return (int)(((((Math.min(((((2*level/5 + 2)*attack*power)/Math.max(1, defense))/50), 997) + 2)*stab)*typeModifer)/10)*randomNumber)/255;
        } else {
            return -1;
        }
    }
    
}
