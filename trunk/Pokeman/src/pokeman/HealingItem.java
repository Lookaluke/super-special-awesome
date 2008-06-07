
package pokeman;


/**
 * Heals the pokemon specified in use()
 * @author Kunal
 */
public class HealingItem extends Item<Pokemon>{
    private int amountToHeal;
    
    /**
     * takes a string for the name of the item
     * @param s
     */
    public HealingItem(String s){
        super(s, 1);
        if(s.equals("Potion")){
            amountToHeal = 20;
            setPrice(300);
        } else if(s.equals("Super Potion")) {
            amountToHeal = 50;
            setPrice(700);
        } else if(s.equals("Hyper Potion")) {
            amountToHeal = 200;
            setPrice(1200);
        } else if(s.equals("Max Potion")){
            amountToHeal = -1;
            setPrice(2500);
        }
    }
    
    public boolean use(Pokemon other){
        if(other.getCurrentHP() == other.getMaxHP()){
            return false;
        } else if (amountToHeal == -1)  {
            other.heal(other.getMaxHP());
            return true;
        } else {
            other.heal(amountToHeal);
            return true;
        }
    }
    
}
