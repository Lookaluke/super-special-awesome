
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Pokemon implements Serializable
{
   public static final int FAST = 153, MEDIUM = 123, SLOW = 297, FADING = 3000;  
   private String name;
   //these are base stats (except currentHP)
   private int baseHP, currentHP, level, experience, levelGrowth, attack,
           defense, special, speed;
   
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
           
           level = l;
           
           Scanner input = new Scanner(new File(f + "info.txt"));
           
           String lg = getInfo(input.nextLine(),"Speed:");
           if(lg.equals("Fast")){
               levelGrowth = FAST;
               experience = (int) (0.8 * Math.pow(level, 3));
           } else if(lg.equals("Medium")){
               levelGrowth = MEDIUM;
               experience = (int) (Math.pow(level, 3));
           } else if(lg.equals("Slow")){
               levelGrowth = SLOW;
               experience = (int) (1.25 * Math.pow(level, 3));
           } else if(lg.equals("Fading")){
               levelGrowth = FADING;
               experience = (int) (1.2 * Math.pow(level,
                       3) - 15 * Math.pow(level, 2) + 100 * level - 140);
           } else {
               throw new IllegalArgumentException("Invalid level up speed");
           }
           
           String elementLine = input.nextLine();
           String ele1 = getInfo(elementLine,"Element1:").toUpperCase();
           String ele2 = getInfo(elementLine,"Element2:").toUpperCase();
           
           
           for(Element e: Element.values()){                
               if(e.name().equals(ele1))
                   element1 = e;
               if(e.name().equals(ele2))
                   element2 = e;
           }
           
           //get base stats
           String stat = input.nextLine();
           special = Integer.parseInt(getInfo(stat,"Special:"));
           attack = Integer.parseInt(getInfo(stat,"Attack:"));
           defense = Integer.parseInt(getInfo(stat,"Defense:"));
           speed = Integer.parseInt(getInfo(stat,"Speed:"));
           baseHP = Integer.parseInt(getInfo(stat,"HP:"));
           currentHP = getMaxHP();
           input.nextLine();
           
           //set up possible moves
           while(input.hasNextLine()){
               String move = input.nextLine();
               availableMoves.add(new Move(move.substring(0,move.indexOf(":"))));
               moveLevels.add(Integer.parseInt(getInfo(move,":")));
           }
           
           //set up current moves by picking the latest 4 possible moves
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
    * @param t base HP
    * @param h current HP
    * @param l level
    * @param lg level growth
    * @param e experience
    * @param a base attack
    * @param d base defense
    * @param s base speed
    * @param sp base special
    * @param f front sprite
    * @param b back sprite
    * @param m move array
    * @param e1 element 1
    * @param e2 element 2
    * @param stat status
    * @deprecated
    */
   public Pokemon(String n,int t, int h, int l, int lg, int e, int a, int d, int s,
           int sp, BufferedImage f, BufferedImage b, Move[] m, Element e1,
           Element e2, Status stat)
   {
       name = n;
       baseHP = t;
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
       return (2 * baseHP * level/100) + 10 + level;
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
    * this method accumulates experience and handles single level-ups
    * currently, its probably broken for multiple levels
    */
   public void addExperience(int exp){
       experience+=exp;
       if(willLevelUp(experience)){
           level++;
       }
   }
   
   private boolean willLevelUp(int moreExp){
       int tempexp = experience + moreExp;
       int nextlevel = level+1;
       if(levelGrowth == FAST){
           return tempexp >= (int) (.8 * Math.pow(nextlevel, 3));
       } else if (levelGrowth == MEDIUM){
           return tempexp >= (int) (Math.pow(nextlevel, 3));
       } else if (levelGrowth == SLOW) {
           return tempexp >= (int) (1.25 * Math.pow(nextlevel, 3));
       } else if (levelGrowth == FADING){
           return tempexp >= (int) (1.2*Math.pow(nextlevel, 3) - 15 *
                   Math.pow(nextlevel, 2) + 100 * nextlevel - 140);
       } else {
           throw new IllegalStateException("Pokemon doesn't have valid growth rate");
       }
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
    * implementation for dead pkmn...we may add stuff later
    */
   private void die(){
       status = Status.FAINTED;
       reset();
   }
   
   /**
    * Heals the pokemon by the specified amount. If the amount goes over the max,
    * it automatically reduces to the max.
    * @param amt the amount to heal
    */
   public void heal(int amt){
       currentHP += amt;
       if(currentHP > getMaxHP())
           currentHP = getMaxHP();// fill only to max
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
   
   public void reset(){
       //get rid of temporary stat changes
   }
}
