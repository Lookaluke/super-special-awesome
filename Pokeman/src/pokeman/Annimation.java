/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Mark Benjamin
 */
public class Annimation {
    
    public static final int  NONE = -1,UP = 0,DOWN = 1,RIGHT = 2,LEFT = 3;
    private BufferedImage[][] annimations;
    private int currentFrame,currentDirection;
    private int[] numberOfFrames = new int[4];
    
    
    /** Creates a new instance of Annimation */
    public Annimation(String name) {
        
        for(int i=0;i<4;i++)
        {
            numberOfFrames[i] = 0;
        }
        
        annimations = new BufferedImage[10][10];
        
        currentDirection = DOWN;
        
        File f = new File("Annimations\\"+name);
        File[] files = f.listFiles();
        
        for(int i=0;i<files.length;i++)
        {
            
            String nameOfFile = files[i].getName();
            
            if(nameOfFile.contains(name) && (nameOfFile.contains(".PNG") ||  nameOfFile.contains(".png")))
            {             
                String newName = nameOfFile.replace(name,"").replace(".PNG","").replace(".png","");
                char firstLetter = newName.charAt(0);
                newName = newName.substring(1,newName.length());
                
                try{
                    int number=-1;
                    switch(firstLetter)
                    {
                        case 'U':
                            number = UP;                            
                            break;
                        case 'D':
                            number = DOWN;
                            break;
                        case 'R':
                            number = RIGHT;
                            break;
                        case 'L':
                            number = LEFT;
                            break;                                    
                    }
                    numberOfFrames[number]++;
                    annimations[number][Integer.parseInt(newName)-1] = ImageIO.read(files[i]);
                }catch(Exception e){System.out.println("Cannot load annimation image");}
            }
        }        
    }   
    
    public BufferedImage getFrame(){        
        if(currentDirection>=0)
        {
            currentFrame %= numberOfFrames[currentDirection];
            return annimations[currentDirection][currentFrame];
        }
        else
            return null;
    }
    
    public void nextFrame(int direction){
        if(currentDirection!=direction)
            currentFrame = 0;
        currentDirection = direction;
        currentFrame++;
        currentFrame %= numberOfFrames[direction];
    } 
    
    public void setFrame(int direction,int frameNum){
        currentDirection = direction;
        currentFrame = frameNum;
    } 
    

    
    public void standingFrame(){
        currentFrame = 0;
    } 
    
    public int getFrameNumber(){
        return currentFrame;
    }
}

