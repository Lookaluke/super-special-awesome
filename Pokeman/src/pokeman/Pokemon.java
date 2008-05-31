
package pokeman;


/**
* This class represents a pokemon , with all the information needed to
* display, battle, and grow....
*
* Base Stats still need to be implemented......
*/

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Pokemon
{
   public static  final int FAST = 153, MEDIUM = 123, SLOW = 297, FADING = 3000;  
   private String name;
   private int totalHP, currentHP, level, experience, levelGrowth, attack,
           defense, special, speed;
   
   //determine how these will be parsed.
   private int baseHP, baseAttack, baseDefense, baseSpecial, baseSpeed;
   
   BufferedImage front,back;
   Move[] moves = new Move[4];
   
   //the next two are parallel...this may result in problems and Mr. Horn
   //told us not to do this.
   ArrayList<Move> availableMoves = new ArrayList<Move>();
   ArrayList<Integer> moveLevels = new ArrayList<Integer>();
   
   Element element1, element2;
   Status status;
   
   /**
    * Takes a file and the pokemon's level. The pokemon
    * file defines any pokemon.
    *
    * @param f The string for the folder for the pokemon
    * @param l The level of that pokemon
    */
   public Pokemon(String f,int l){
       try {
           name = f;
           
           f = "Pokemon\\" + f + "\\";
           front = ImageIO.read(new File(f + "front.png"));
           back = ImageIO.read(new File(f + "back.png"));
                     
           status = Status.NORMAL;
           experience = 0;
           level = l;
           
           Scanner input = new Scanner(new File(f + "info.txt"));
           
           String lg = getInfo(input.nextLine(),"Speed:");
           if(lg.equals("Fast"))
               levelGrowth = FAST;
           if(lg.equals("Medium"))
               levelGrowth = MEDIUM;
           if(lg.equals("Slow"))
               levelGrowth = SLOW;
           if(lg.equals("Fading"))
               levelGrowth = FADING;
           String elementLine = input.nextLine();
           String ele1 = getInfo(elementLine,"Element1:").toUpperCase();
           String ele2 = getInfo(elementLine,"Element2:").toUpperCase();
           Element[] elements = {Element.FIRE, Element.WATER, Element.GROUND,
               Element.FLYING, Element.POISON, Element.NORMAL, Element.PSYCHIC, Element.GHOST,
               Element.BUG, Element.ROCK, Element.GRASS, Element.FIGHTING, Element.ELECTRIC,
               Element.ICE, Element.DRAGON, Element.NOTHING};
           
           for(Element e:elements){                
               if(e.name().equals(ele1))
                   element1 = e;
               if(e.name().equals(ele2))
                   element2 = e;
           }
           
           String stat = input.nextLine();
           special = Integer.parseInt(getInfo(stat,"Special:"));
           attack = Integer.parseInt(getInfo(stat,"Attack:"));
           defense = Integer.parseInt(getInfo(stat,"Defense:"));
           speed = Integer.parseInt(getInfo(stat,"Speed:"));
           totalHP = Integer.parseInt(getInfo(stat,"HP:"));
           currentHP = getMaxHP();
           input.nextLine();

           while(input.hasNextLine()){
               String move = input.nextLine();
               availableMoves.add(new Move(move.substring(0,move.indexOf(":"))));
               moveLevels.add(Integer.parseInt(getInfo(move,":")));
           }
           int index = 0;
           for(int i=availableMoves.size()-1;i>=0;i--){
               if(moveLevels.get(i)<=level){
                   moves[index] = availableMoves.get(i);
                   index++;
                   if(index>moves.length)
                       break;
               }
           }
           

       } catch (IOException ex) {
           System.out.println("Pokemon file doesn't exist");
       }
   }
   
   private String getInfo(String s,String after){
       int index = s.indexOf(after)+after.length()+1;
       int index2 = s.indexOf(",",index);
       if(index2==-1)
           return s.substring(index).trim();
       return s.substring(index,index2).trim();
   }
   
   /**
    *
    * @param n name
    * @param t total HP
    * @param h current HP
    * @param l level
    * @param l levelGrowth
    * @param e exp
    * @param a attack power
    * @param d defense
    * @param s special
    * @param sp peed
    * @param ag attack power growth
    * @param dg defense growth
    * @param sg special growth
    * @param spg peed growth
    * @param hg health growth
    * @param f front image
    * @param b back image
    * @param m moves
    * @param e1 element one
    * @param e2 element two
    * @param stat status
    */
   public Pokemon(String n,int t, int h, int l, int lg, int e, int a, int d, int s,
           int sp, BufferedImage f, BufferedImage b, Move[] m, Element e1,
           Element e2, Status stat)
   {
       name = n;
       totalHP = t;
       currentHP = h;
       level = l;
       levelGrowth = lg;
       experience = e;
       attack = a;
       defense = d;
       special = s;
       speed = sp;
       front = f;
       back = b;
       moves = m;
       element1 = e1;
       element2 = e2;
       status = stat;
   }
   
   /**
    * Returns the Name
    */
   
   public String getName(){
       return name;
   }
   
   /**
    * Returns the level
    */
   
   public int getLevel(){
       return level;
   }
   
   /**
    * Returns the experience
    */
   
   public int getExperience(){
       return experience;
   }
   
   /**
    * Returns the attack
    */
   public int getAttack(){
       return (2 * attack * level/100) + 5;
   }
   
   /**
    * Returns the defense
    */
   public int getDefense(){
       return (2 * defense * level/100) + 5;
   }
   
   /**
    * Returns the special
    */
   public int getSpecial(){
       return (2 * special * level/100) + 5;
   }
   
   /**
    * Returns the speed
    */
   public int getSpeed(){
       return (2 * speed * level/100) + 5;
   }
   
   /**
    * Returns max HP
    */
   public int getMaxHP(){
       return (2 * totalHP * level/100) + 10 + level;
   }
   
   /**
    * Returns current HP
    */
   public int getCurrentHP(){
       return currentHP;
   }
   
   /**
    * Returns the front image
    */
   public BufferedImage getFront(){
       return front;
   } 

    /**
    * Returns the back image
     */
   public BufferedImage getBack(){
       return back;
   }

   
   /**
    * Returns the moves
    */
   public Move[] getMoves(){
       return moves;
   }
   
   /**
    * Returns the element1
    */
   public Element getElement1(){
       return element1;
   }
   
   /**
    * Returns the element2
    */
   public Element getElement2(){
       return element2;
   }
   
   
   /**
    * this method accumulates experience until you level up
    */
   public void addExperience(int exp){
       experience+=exp;
       if(levelUp(experience, level)){
           level++;
           //figure out how much stat increases
           attack++;
           defense++;
           speed++;
           special++;
           //(2 * Base * Level/100) + 10 + Level (for HP)
           //(2 * Base * Level/100) + 5 (for everything else)
       }
   }
   
   /**
    * allows you to level up
    */
   private boolean levelUp(int exp, int lvl){
       //code to tell if level up
       return false;
   }
 
   /**
    * allows damage to be done
    */
   public void takeDamage(int amt){
       currentHP -= amt;
       if(currentHP<0)
           currentHP = 0;
       if(currentHP < 1) die();// you ARE DEAD
   }
   
   /**
    * implementation for dead pkmn
    */
   private void die(){
       status = Status.FAINTED;
   }
   
   /**
    * Heals the pokemon by the specified amount. If the amount will result
    * in 
    * @param amt the amount to heal
    */
   public void heal(int amt){
       currentHP += amt;
       if(currentHP > totalHP)
           currentHP = totalHP;// fill only to max
   }
   
   /**
    * When the user selects fight from the menu, if this method returns
    * false, then it automatically uses struggle.
    * @return true if there are moves left.
    */
   public boolean hasMovesLeft(){
       for(Move m : moves) 
           if(m!=null && m.getPP() != 0)
               return true;
       return false;
   }  
}
