/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.util.ArrayList;

/**
 *
 * @author Mark
 */
public class Cinematic {
    
    private Person p;
    private String string;
    private int index=0;
    private boolean over = false;
    
    public Cinematic(Person p,String s){
        this.p = p;
        this.string = s;
        string = string.replaceAll("$",p.getWindow().getPerson().getName());
    }
    
    public void update(){
        if(index<string.length() && index != -1){
            if(!p.getWindow().talking() && !p.getWindow().battling()){
                
                int newIndex = string.indexOf(":",index);
                if(newIndex<0)
                    newIndex = string.length();

                String hold = string.substring(index,newIndex);
                
                p.getWindow().getPerson().allowUpdate(false);
                p.allowUpdate(false);

                if(hold.length()==1){
                    int x = p.getX()/Window.TILE_WIDTH;
                    int y = p.getY()/Window.TILE_HEIGHT+1;
                    switch(hold.charAt(0)){
                        case '4':
                            string = string.substring(0,newIndex+1)+pathFind(p.getWindow().getPerson(),p.getWindow().getCollision())+string.substring(newIndex+1);
                            break;
                        case '5':
                            string = string.substring(0,newIndex+1)+pathFind(x,y+Window.ROWS/2+2,p.getWindow().getCollision())+string.substring(newIndex+1);
                            break;
                        default:
                            p.makeMove(Integer.parseInt(hold));
                            break;
                    }
                    
                }else{
                    if(hold.contains("BATTLE"))
                    {
                        //hold = hold.substring(hold.indexOf("BATTLE")+"BATTLE".length());
                        p.getWindow().startBattle(new Battle(p.getWindow().getPerson(),new Pokemon("Bulbasaur",5),p.getWindow().getFrame()));
                    }else{
                        p.getWindow().startTextBox(new TextBox(p.getWindow().getFrame(),hold,0,475,800,101,true,false,Style.STANDARD_TEXT));
                    }
                }
                
                index = string.indexOf(":",index)!=-1?string.indexOf(":",index)+1:-1;
                
            }
        }else{
            if(!p.getWindow().talking() && !p.getWindow().battling()){
                p.allowUpdate(true);
                p.getWindow().getPerson().allowUpdate(true);
                over = true;
            }
        }
    }
    
    public String pathFind(Character c,ArrayList<Collideable> cols){
        return pathFind(c.getX()/Window.TILE_WIDTH,c.getY()/Window.TILE_HEIGHT+1,cols);
    }
    
    public String pathFind(int finalX,int finalY,ArrayList<Collideable> cols){
        int smallestX=Integer.MAX_VALUE;
        int smallestY=Integer.MAX_VALUE;
        int largestX=Integer.MIN_VALUE;
        int largestY=Integer.MIN_VALUE;
               
        for(Collideable col:cols){
            if(col.getX()<smallestX)
                smallestX = col.getX();
            if(col.getX()>smallestX)
                largestX = col.getX();
            if(col.getY()<smallestY)
                smallestY = col.getY();
            if(col.getY()>smallestY)
                largestY = col.getY();
        }
        boolean[][] grid = new boolean[largestX-smallestX+1][largestY-smallestY+1];
        
        for(Collideable col:cols){
            if(col.getNumber(0)==1)
                grid[col.getX()-smallestX][col.getY()-smallestY] = true;
        }
        
        String ret = "";
        
        int x = p.getX()/Window.TILE_WIDTH-smallestX;
        int y = p.getY()/Window.TILE_HEIGHT+1-smallestY;
        finalX = finalX-smallestX;
        finalY = finalY-smallestY;
        

        int yChange = y<finalY?1:-1;
        int xChange = x<finalX?1:-1;
        
        int detourDirection = xChange;
        
        System.out.println(y+" "+finalY+" "+yChange);
        
        while(x!=finalX || y!=finalY){
            yChange = y<finalY?1:-1;
            xChange = x<finalX?1:-1;
            while(0!=Math.abs(finalY-y)){
                while(grid[x][y+yChange])
                {
                    if(grid[x+detourDirection][y])
                        detourDirection = -detourDirection;
                    ret+=(detourDirection==1?Animation.RIGHT:Animation.LEFT)+":";
                    x+=detourDirection;
                    
                }    
                ret+=(yChange==1?Animation.DOWN:Animation.UP)+":";
                y+=yChange;
                
            }
            System.out.println(ret);
            detourDirection = yChange;
            yChange = y<finalY?1:-1;
            xChange = x<finalX?1:-1;
            while(0!=Math.abs(finalX-x)){
                while(grid[x+xChange][y])
                {
                    if(grid[x+detourDirection][y])
                        detourDirection = -detourDirection;
                    ret+=(detourDirection==1?Animation.UP:Animation.DOWN)+":";
                    y+=detourDirection;
                }
                ret+=(xChange==1?Animation.RIGHT:Animation.LEFT)+":";
                x+=xChange;
            }
        }

        
        return ret.substring(0,ret.length()-2);      
    }
    
    
    public boolean over(){
        return over;
    }
    
}
