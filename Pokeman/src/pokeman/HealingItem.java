
package pokeman;


/**
 * Heals the pokemon specified in use()
 * @author Kunal
 */
public class HealingItem extends Item<Pokemon>{
    private int amountToHeal;
    
    /**
     * takes a string for hte file location
     * @param s
     */
    public HealingItem(String s){
        super("Potion", 200, 1);
        //parse out a file
    }
    
    public boolean use(Pokemon other){
        if(other.getCurrentHP() == other.getMaxHP()){
            return false;
        } else {
            other.heal(amountToHeal);
            return true;
        }
    }
    
}
