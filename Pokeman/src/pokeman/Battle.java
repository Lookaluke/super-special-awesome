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
    
    
    public Battle(Character you, Pokemon enemy)
    {
    
    }
    
    public Battle(Character you, Trainer enemy){
        theirs = enemy.getPokemonArray();
    }
}