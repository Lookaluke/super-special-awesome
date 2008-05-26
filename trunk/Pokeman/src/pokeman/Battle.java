/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

/**
 * The back-end to the battle system. Each battle is instantiated, with 
 * either 
 * @author Kunal
 */

public class Battle {
    private Pokemon yours;
    private Pokemon[] theirs;
    
    //should this be an int?
    private Pokemon theirCurrent;
    
    
    public Battle(Character you, Pokemon enemy) {
        yours = you.currentPokemon()[0];
        theirs = new Pokemon[1];
        theirs[0] = enemy;
        theirCurrent = enemy;
    }
    

    /*
    public Battle(Character you, Trainer enemy){
        theirs = enemy.getPokemonArray();
        theirCurrent = theirs[0];
    }
    */
    public void turn(Move yourMove){
        //first, you check the speeds to see who goes first
        //then you do the calculation on the first one
        //you check if dead. if not, then do the second move
        //check if dead again.
        
        
        
        int[] movesLeft = theirCurrent.getMovesLeft();
        
        int movenumber = (int)(Math.random() * movesLeft.length);
        int moveChoice = movesLeft[movenumber];
        Move theirMove = theirCurrent.getMoves()[moveChoice]; 
        
        if (yours.getSpeed() > theirCurrent.getSpeed())
        {
            //perform your move first
            //reduce pp only if its a human...
            
            //we need to modify takeDamage() to properly handle pokemon fainting
            if(yourMove.useMove())
            {
                theirCurrent.takeDamage(calculateDamage(yourMove, true));
            }
            else
            {
                //tell player that there is no pp left, use different move                
            }
           
            //now do their move
            yours.takeDamage(calculateDamage(theirMove, false));
        } 
        else 
        {
            yours.takeDamage(calculateDamage(theirMove, false));
            theirCurrent.takeDamage(calculateDamage(yourMove, true));
        }
    }
    
    /**
     * returns the damage done, -1 if it misses
     * @param m the move to calculate damage with
     * @param isPerson true if its the human controlled one attacking the comp
     * @return
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
        

        

        int accuracything = 0;//(int) (attacker.getAccuracy() * m.accuracy() * 0.0256);

            
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
            int randomNumber = (int)(Math.random() * 39) + 217;
            return (int)(((((Math.min(((((2*level/5 + 2)*attack*power)/Math.max(1, defense))/50), 997) + 2)*stab)*typeModifer)/10)*randomNumber)/255;
        } else {
            return -1;
        }
    }
}
