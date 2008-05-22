/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kunal
 */
public class Move {
    
    public static final int HP = 0,ATTACK=1,DEFENSE=2,SPECIAL=3,SPEED=4,ACCURACY=5,EVASION=6;
    
    private Element element;
    private int power;
    private String name;
    private Animation anim;
    private int accuracy;
    private int totalPP, currentPP;
    private Status effect;
    private int statusPercentage;
    private int attacksWhat;
    private boolean raises;
    
    public Move(String n,Element e,int p,int what,boolean r,Animation a,int ac,int tot,int cur,Status ef,int percent){
        element = e;
        power = p;
        attacksWhat = what;
        raises = r;
        name = n;
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
        try {
            
            name = f;
            f = "Moves\\"+f+"\\";
            anim = new Animation(f);
            f += "Info.txt";
            Scanner input = new Scanner(new File(f));

            String ele = getInfo(input.nextLine(),"Element:");
            
            Element[] elements = {Element.FIRE, Element.WATER, Element.GROUND, 
                Element.FLYING, Element.POISON, Element.NORMAL, Element.PSYCHIC, Element.GHOST, 
                Element.BUG, Element.ROCK, Element.GRASS, Element.FIGHTING, Element.ELECTRIC, 
                Element.ICE, Element.DRAGON, Element.NOTHING};
            
            for(Element e:elements){                
                if(e.name().equals(ele))
                    element = e;
            }
            power = Integer.parseInt(getInfo(input.nextLine(),"Power:"));
            accuracy = Integer.parseInt(getInfo(input.nextLine(),"Accuracy:"));
            totalPP = Integer.parseInt(getInfo(input.nextLine(),"PP:"));
            currentPP = totalPP;
            String line = input.nextLine();
            if(line.substring(0,line.indexOf(":")).equals("RAISES"))
                raises = true;
            else
                raises = false;
            line = getInfo(line,":");
            if(line.equals("HP"))
                attacksWhat = HP;
            if(line.equals("ATTACK"))
                attacksWhat = ATTACK;
            if(line.equals("DEFENSE"))
                attacksWhat = DEFENSE;
            if(line.equals("SPECIAL"))
                attacksWhat = SPECIAL;
            if(line.equals("SPEED"))
                attacksWhat = SPEED;
            if(line.equals("ACCURACY"))
                attacksWhat = ACCURACY;
            if(line.equals("EVASION"))
                attacksWhat = EVASION;
            line = getInfo(input.nextLine(),":");
            Status[] s = {Status.POISON, Status.NORMAL, Status.FROZEN, Status.PARALYZED, 
                Status.BURN, Status.CONFUSION, Status.SLEEP};
            for(Status stat:s){                
                if(stat.name().equals(line))
                    effect = stat;
            }
            statusPercentage = Integer.parseInt(getInfo(input.nextLine(),":"));
            input.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Move.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	    
    private String getInfo(String s,String after){
        int index = s.indexOf(after)+after.length()+1;
        int index2 = s.indexOf(",",index);
        if(index2==-1)
            return s.substring(index).trim();
        return s.substring(index,index2).trim();
    }
    
    
    public Element getElement()
    {
        return element;
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