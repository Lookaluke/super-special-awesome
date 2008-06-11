

package pokeman;

/**
 * Valid names for elixiers are "Max Elixier" and "Elixier"
 * other names will throw exceptions
 * @author Kunal
 */
public class Elixier extends Item<Pokemon>{
    
    private int amountToHeal;
    
    public Elixier(String name){
        super(name, 1);
        
        if(name.equals("Max Elixier")){
            setPrice(0);
            amountToHeal = -1;
        } else if (name.equals("Elixier")) {
            setPrice(1000);
            amountToHeal = 10;
        } else {
            throw new IllegalArgumentException("Invalid name");
        }
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
