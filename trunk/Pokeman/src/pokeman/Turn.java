/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

/**
 *
 * @author Mark
 */
public class Turn implements Runnable{
    
    private Move yourMove;
    private Pokemon theirCurrent,yours;
    private BattleFrontEnd frontEnd;
    
    public Turn(Move yourMove,Pokemon theirCurrent,Pokemon yours,BattleFrontEnd frontEnd){
        this.yourMove = yourMove;
        this.theirCurrent = theirCurrent;
        this.yours = yours;
        this.frontEnd = frontEnd;
    }
    
    @SuppressWarnings("empty-statement")
    public void run(){
        //first, you check the speeds to see who goes first
        //then you do the calculation on the first one
        //you check if dead. if not, then do the second move
        //check if dead again.
        
        //subtracts pp, and if the move has no PP, it breaks out
        //we may modify the return to give more info to the frontend
        if(!yourMove.useMove())
            return;
        
        
        
        Move theirMove=null;
        while(theirMove==null){
            int movenumber = (int)(Math.random() * 4);        
             theirMove = theirCurrent.getMoves()[movenumber]; 
        }
               
        if (yours.getSpeed() > theirCurrent.getSpeed())
        {
            //perform your move first
            
            frontEnd.setText(yours.getName()+" used "+yourMove.name());        
                    
            //we need to modify takeDamage() to properly handle pokemon fainting
            int max=calculateDamage(yourMove, true);
            for(int i=0;i<max;i++){
                try {
                    theirCurrent.takeDamage(i);
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    System.out.println("interupted");
                }
            }
            
            
           
            //now do their move
            max=calculateDamage(theirMove, false);
            for(int i=0;i<max;i++){
                try {
                    yours.takeDamage(i);
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    System.out.println("interupted");
                }
            }
        } 
        else 
        {
            frontEnd.setText(yours.getName()+" used "+yourMove.name());   
            while(frontEnd.waiting());
            
            int max=calculateDamage(theirMove, false);
            for(int i=0;i<max;i++){
                try {
                    yours.takeDamage(i);
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    System.out.println("interupted");
                }
            }
            
            
            max=calculateDamage(yourMove, true);
            for(int i=0;i<max;i++){
                try {
                    theirCurrent.takeDamage(i);
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    System.out.println("interupted");
                }
            }
        }
        //frontEnd.setText("It's super effective");
    }
    
    /**
     * returns the damage done, -1 if it misses
     * @param m the move to calculate damage with
     * @param isPerson true if its the human controlled one attacking the comp
     * @return the damage, -1 if it misses.
     */
    private int calculateDamage(Move m, boolean isPerson){
        
        
        Pokemon attacker;
        Pokemon defender;
        if (isPerson){
            attacker = yours;
            defender = theirCurrent;
        } else {
            attacker = theirCurrent;
            defender = yours;
        }
        

        //int accuracything = 0;//(int) (attacker.getAccuracy() * m.accuracy() * 0.0256);
        int accuracything = (int) ( m.accuracy() * 2.56);
            
        if ((int)(Math.random() * 256) < accuracything)
        {
            //move hit, now calculate damage
            int level = attacker.getLevel();
            int attack = attacker.getAttack();
            int power = m.power();
            int defense = defender.getDefense();
            double stab;
            if (m.element() == attacker.getElement1() || m.element() == attacker.getElement2())
            {
                stab = 1.5;
            }
            else 
            {
                stab = 1;
            }
            double typeModifer = m.element().multiplerAgainst(defender.getElement1()) * m.element().multiplerAgainst(defender.getElement2()) * 10;
            //tell front end its super/not very effective?
            int randomNumber = (int)(Math.random() * 39) + 217;
            System.out.println((int)(((((Math.min(((((2*level/5 + 2)*attack*power)/Math.max(1, defense))/50), 997) + 2)*stab)*typeModifer)/10)*randomNumber)/255);
            return (int)(((((Math.min(((((2*level/5 + 2)*attack*power)/Math.max(1, defense))/50), 997) + 2)*stab)*typeModifer)/10)*randomNumber)/255;
        } else {
            return -1;
        }
    }
}
