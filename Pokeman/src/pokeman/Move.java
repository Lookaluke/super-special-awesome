/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

/**
 *
 * @author Kunal
 */
public class Move {
    private Element elem;
    private int power;
    private String name;
    private Animation anim;
    private int accuracy;
    private String description;
    private int totalPP, currentPP;
    private Status effect;
    private int statusPercentage;
    
	
    public Element getElement()
    {
        return elem;
    }
    
    public int power()
    {
        return power;
    }
	
    public void draw(){
        
    }
    
}