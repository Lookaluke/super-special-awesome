/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

/**
 * This class is rather unimportant it allows you to store a location where there
 * is a collideable object. Ex. grass or tree.
 * @author Mark
 */
public class Collideable implements Comparable<Collideable>{

    private int xTile,yTile,collisionNumber;
    
    public Collideable(int x,int y,int collisionNumber){
        this.collisionNumber = collisionNumber;
        xTile = x;
        yTile = y;
    }
        
    /**
     * Returns the xTile of this collideable thing
     * @return the xTile value
     */
    public int getX(){
        return xTile;
    }
    
    /**
     * Returns the yTile of this collideable thing
     * @return the yTile value
     */
    public int getY(){
        return yTile;
    }
    
    /**
    * Gets the collision number
    * @return the CollisionNumber
    */   
    public int getNumber(){
        return collisionNumber;
    }

    public int compareTo(Collideable c) {
        if(this.getX()<c.getX())
            return -1;
        if(this.getX()==c.getX())
        {
            if(this.getY()<c.getY())
                return -1;
        }
        if(this.getX()==c.getX() && this.getY()==c.getY())
            return 0;
        
        return 1;
        
        
    }
        
}
