/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

/**
 *
 * @author Mark
 */
public class Eventers extends Person{

    String before,after;
    int event,result,number;
    boolean complete;
    
    public Eventers(String n,int number,String before,String after,int event,int result,int x,int y,Window w)
    {
        super(n,before,x,y,w);
        this.before = before;
        this.after = after;
        this.event = event;
        this.result = result;
        this.number = number;
        complete = getWindow().getPerson().isComplete(number);
        if(w.getPerson().isComplete(number))
            perform();
    }
    
    public void update(){
        if(complete){
            this.setSpeech(after);
        }else{
            hasCompleten(getWindow().getPerson());
        }
    }
    
    public void perform(){
        //switch(do1){
            
        //}
    }
    
    private boolean hasCompleten(Character c){
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
        if(result)
            c.complete(number);
        return result;
    }
    
}
