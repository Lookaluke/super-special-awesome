/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

/**
 *
 * @author Kunal
 */
public enum Status {
    POISON, NORMAL, FROZEN, PARALYZED, BURN, FAINTED, SLEEP;
    
    public double getCatchMultipler(){
        switch(this){
            case POISON:
            case PARALYZED:
            case BURN:
                return 1.5;
            case SLEEP:
            case FROZEN:
                return 2;
            default:
                return 1;
        }
    }
}
