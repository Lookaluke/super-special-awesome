
package pokeman;


/**
 * This class represents a pokemon , with all the information needed to 
 * display, battle, and grow....
 * 
 */

import java.awt.image.*;
import java.io.File;

public class Pokemon
{
    private String name;
    private int totalHP, currentHP, level, experience, attack, defense,
            special, speed;
    BufferedImage image;
    Move[] moves = new Move[4];
    Element element1, element2;
    //Status status;
    
    /**
     * takes a file
     * @param f
     */
    public Pokemon(File f){
        
    }
    
    /**
     * 
     * @param n name
     * @param t total HP
     * @param h current HP
     * @param l level
     * @param e exp
     * @param a attack power
     * @param d defense
     * @param s special
     * @param sp peed
     * @param i image
     * @param m moves
     * @param e1 element one
     * @param e2 element two
     * @param stat status
     */
    public Pokemon(String n,int t, int h, int l, int e, int a, int d, int s,
            int sp, BufferedImage i, Move[] m, Element e1,
            Element e2, Status stat)
    {
        name = n;
        totalHP = t;
        currentHP = h;
        level = l;
        experience = e;
        attack = a;
        defense = d;
        special = s;
        speed = sp;
        image = i;
        moves = m;
        element1 = e1;
        element2 = e2;
        //status = stat;
    }
    
    /**
     * Returns the Name
     */
    
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns the level
     */
    
    public int getLevel()
    {
        return level;
    }
    
    /**
     * Returns the experience
     */
    
    public int getExperience()
    {
        return experience;
    }
    
    /**
     * Returns the attack 
     */
    
    public int getAttack()
    {
        return attack;
    }
    
    /**
     * Returns the defense 
     */
    
    public int getDefense()
    {
        return defense;
    }
    
    /**
     * Returns the special 
     */
    
    public int getSpecial()
    {
        return special;
    }
    
    /**
     * Returns the speed
     */
    
    public int getSpeed()
    {
        return speed;
    }
    
    /**
     * Returns the image
     */
    
    public BufferedImage getImage()
    {
        return image;
    }
    
    /**
     * Returns the moves
     */
    
    public Move[] getMoves()
    {
        return moves;
    }
    
    /**
     * Returns the element1
     */
    
    public Element getElement1()
    {
        return element1;
    }
    
    /**
     * Returns the element2 
     */
    
    public Element getElement2()
    {
        return element2;
    }
    
    
    /**
     * this method accumulates experience until you level up
     */
    public void addExperience(int exp)
    {
        experience+=exp;
        if(levelUp(experience, level))
        {
            level++;
            //figure out how much stat increases
            attack++;
            defense++;
            speed++;
            special++;
        }
    }
    
    /**
     * allows you to level up
     */
    private boolean levelUp(int exp, int lvl)
    {
        //code to tell if level up
        return false;
    }
   
    /**
     * allows damage to be done
     */
    public void takeDamage(int amt)
    {
        currentHP -= amt;
        if(currentHP < 1) die();// you ARE DEAD
    }
    
    /**
     * implementation for dead pkmn
     */
    private void die()
    {
        //KILL PKMN   
    }
    
    /**
     * allows healin
     */
    public void heal(int amt)
    {
        currentHP += amt;
        if(currentHP > totalHP)
            currentHP = totalHP;// fill only to max
    }

}
