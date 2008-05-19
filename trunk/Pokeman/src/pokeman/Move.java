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
    
    public static final int HP = 0,ATTACK=1,DEFENSE=2,SPECIAL=3,SPEED=4,ACCURACY=5,EVASION=6;
    
    private Element elem;
    private int power;
    private String name;
    private Animation anim;
    private int accuracy;
    private int totalPP, currentPP;
    private Status effect;
    private int statusPercentage;
    private int attacksWhat;
    
    public Move(String n,Element e,int p,int what,Animation a,int ac,int tot,int cur,Status ef,int percent){
        elem = e;
        power = p;
        attacksWhat = what;
        n = name;
        anim = a;
        accuracy = ac;
        totalPP = tot;
        currentPP = cur;
        effect = ef;
        statusPercentage = percent;
    }
    
    /**
     * Makes a move from a file
     * @param f The name of the directory with the move
     */
    public Move(String f){
        
    }
	
    public Element getElement()
    {
        return elem;
    }
    
    public int power()
    {
        return power;
    }
    
    public String name(){
        return name;
    }
	
    public void draw(){
        
    }
    
}