
package pokeman;

/**
 * Ethers increase PP for one move.
 * @author Kunal
 */
public class Ether extends Item<Move> {
    
    private int amountToHeal;
    
    public Ether(){
        super("Ether", 500, 1);
        amountToHeal = 5;
    }
    
    @Override
    public boolean use(Move m) {
        if(amountToHeal >= 0)
            return m.refilPP(amountToHeal);
        else
            return m.refilPP(m.getTotalPP());
    }

}
