
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
        int catchrate = other.getCatchRate();
        int max = other.getMaxHP();
        int curr = other.getCurrentHP();
        double status = other.getStatus().getCatchMultipler();
        int a = (int) ((((3 * max - 2 * curr) * catchrate * multiplier) / (3 * max)) * status);
        if (a >= 255){
            return true;
        } else{
            int b = (int) (1048560 / Math.sqrt(Math.sqrt(16711680.0 / a)));
            int ran1 = (int) (Math.random() * 65535);
            int ran2 = (int) (Math.random() * 65535);
            int ran3 = (int) (Math.random() * 65535);
            int ran4 = (int) (Math.random() * 65535);
            return (ran1 <= b && ran2 <= b && ran3 <= b && ran4 <= b);
        }
    }
    
}