
package pokeman;


/**
 * catches pokemon, but as of now it does nothing
 * @author Kunal
 */
public class Pokeball extends Item<Pokemon> {
    
    private double multiplier;
    
    public Pokeball(){
        super("Pokeball", 200, 1);
    }
    
    @Override
    public boolean use(Pokemon other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}