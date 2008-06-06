/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

/**
 *
 * @author Kunal
 */
public class Elixier extends Item<Pokemon> {
    
    private int amountToHeal;
    
    public Elixier(){
        super("Elixier", 1000, 1);
        amountToHeal = 5;
    }
    
    
    @Override
    public boolean use(Pokemon other) {
        int counter = 0;
        for(Move m: other.getMoves()){
            if(amountToHeal >= 0){
                if(m.refilPP(amountToHeal))
                    counter++;
            } else {
                if(m.refilPP(m.getTotalPP())){
                    counter++;
                }
            }
        }
        
        return counter != 0;
    }

}
