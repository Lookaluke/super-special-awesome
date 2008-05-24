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
    private Pokemon theirCurrent;
    
    
    public Battle(Character you, Pokemon enemy)
    {
        
    }
    
    public Battle(Character you, Trainer enemy){
        theirs = enemy.getPokemonArray();
        theirCurrent = theirs[0];
    }
    
    public void turn(Move yourMove, Move theirMove){
        //first, you check the speeds to see who goes first
        //then you do the calculation on the first one
        //you check if dead. if not, then do the second move
        //check if dead again.
    }
}
