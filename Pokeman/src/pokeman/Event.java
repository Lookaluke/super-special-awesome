/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

/**
 *
 * @author Mark
 */
public class Event implements Comparable<Event>{
    private String before,after;
    private int event;
    private String doBefore,doAfter,doToCharacter;
    private boolean complete;
    private int number;
    

    public Event(int number,String before,String after,String doBefore,String doAfter,String doToCharacter,int event){
        this.before = before;
        this.after = after;
        this.event = event;
        this.doBefore = doBefore;
        this.doAfter = doAfter;
        this.doToCharacter = doToCharacter;
        this.number = number;
    }
    
    public void update(Character c,Dynamic d){
        
        if(!complete){
            d.setSpeech(before);
            act(d,doBefore);
            complete = hasCompleten(c,d);
            if(c.getCollision(Animation.NONE)!=null){
                if(c.getCollision(Animation.NONE).getMaker()==d){

                    System.out.println("here");
                    this.doToCharacter(c, d);
                }
            }
        }else{
            d.setSpeech(after);
            act(d,doAfter);
        }      
    }
    
    public void act(Dynamic d,String what){
        
        int[] ints = getInts(what);
        
        if(ints.length>0){
            switch(ints[0]){
                case 0:
                    break;
                case 1:
                    d.getWindow().removeFromDynamic(d);
                    break;
            }
        }
    }
   
    
    public void doToCharacter(Character c,Dynamic d){
        System.out.println("here");
        int[] ints = getInts(doToCharacter);

        if(ints.length>0){
            switch(ints[0]){
                case 0:                    
                    c.warp(ints[1], ints[2]);
                    break;
            }
        }
        d.talk(c);        
    }
    
    public int[] getInts(String str){
        int counter = 0;
        int index = 0;
        while(index!=-1){
            index = str.indexOf(":",index+1);
            counter++;
        }
        
        int[] ints = new int[counter];
        
        int lastIndex = -1;
        index = str.indexOf(":");
        counter = 0;
        while(index!=-1){
            ints[counter] = Integer.parseInt(str.substring(lastIndex+1,index).trim());
            lastIndex = index;
            counter++;
            index = str.indexOf(":",index+1);
        }

        ints[counter] = Integer.parseInt(str.substring(lastIndex+1).trim());
        
        return ints;
    }
    
    private boolean hasCompleten(Character c,Dynamic d){
        boolean result = false;
        switch(event){
            case 0:
                boolean hold = false;
                for(Pokemon p:c.currentPokemon())
                    if(p!=null)
                        hold = true;
                result = hold;
                break;
                
        }
        if(result){            
            c.complete(d.getNumber());
        }
        return result;
    }
    
    public int getNumber(){
        return number;
    }
    
    public int compareTo(Event e) {
        return this.getNumber()-e.getNumber();
    }
}
