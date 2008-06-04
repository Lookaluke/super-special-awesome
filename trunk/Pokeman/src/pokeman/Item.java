
package pokeman;

import java.io.Serializable;

/**
 * Defines an interface that all items abide by. An item
 * has an effect on another object, of type T. For example, a
 * potion will heal a pokemon, making it implement Item for
 * Pokemon. Other items will implement for Move and for 
 * 
 * @author Kunal
 */
public abstract class Item<T> implements Serializable {
    private int price;
    private String name;
    private int quantity;
    
    public Item(String name, int price, int quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    
    /**
     * uses the item on the specified object. It returns true if it worked,
     * false if it didn't for whatever reason. If the item has only one use,
     * this method should reduce the quantity.
     * @param other
     * @return true if it was able to complete the action, false if not
     */
    public abstract boolean use(T other);
    
    /**
     * gets the price of the current item. The selling price will be half
     * of the price returned here.
     * @return the buying price of the current item
     */
    public int getPrice(){
        return price;
    }
    
    /**
     * gets the name of the current item. For example, a super potion
     * will return "Super Potion".
     * @return the name of the item
     */
    public String getName(){
        return name;
    }
    
    /**
     * returns the current quantity of the item.
     * @return
     */
    public int getQuantity(){
        return quantity;
    }
    
    public boolean stockUp(int extra){
        int tempstock = quantity + extra;
        if (tempstock > 99){
            return false;
        } else {
            quantity = tempstock;
            return true;
        }
    }
    
    public boolean equals(Object other){
        return ((Item)other).getName().equals(name);
    }
}
