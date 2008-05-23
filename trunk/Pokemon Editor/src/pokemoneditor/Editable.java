/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemoneditor;


import java.awt.geom.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Editable extends JComponent implements MouseListener,MouseMotionListener{
 
    
    private boolean linesOn;
    public static final int COLUMNS = 25,ROWS=18,WIDTH = 800,HEIGHT = 576,BACKGROUND = 0,STATIC = 1,DYNAMIC = 2;
    private int xRate,yRate;
    private BufferedImage currentImage;
    private int mouseX,mouseY,currentLayer,oldX,oldY;
    
    private BufferedImage[][][] images = new BufferedImage[3][COLUMNS][ROWS];
    private String[][][] extraImageData = new String[3][COLUMNS][ROWS];
    
    private ArrayList<BufferedImage> img = new ArrayList<BufferedImage>(20); 
    private ArrayList<String> imgNames = new ArrayList<String>(20); 
    
    private String extraInfo;
    
    /** Creates a new instance of Editable */
    public Editable(ArrayList<BufferedImage> imgs ,ArrayList<String> strs) {
        xRate = (WIDTH/COLUMNS);
        yRate = (HEIGHT/ROWS);
        linesOn = true;
        addMouseListener(this);
        addMouseMotionListener(this);
        img = imgs;
        imgNames = strs;
        extraInfo = "";
    }
    
    public void paintComponent(Graphics g){

        Graphics2D g2 = (Graphics2D)g;


        int max = 3;
        if(!linesOn)
            max=2;
        for(int j=0;j<max;j++)
        {
            for(int y=0;y<ROWS;y++)
            {
                for(int x = 0;x<COLUMNS;x++)
                {
                    if(images[j][x][y]!=null)
                        g2.drawImage(images[j][x][y],null,x*xRate,y*yRate);
                }
            }
        }

        if(linesOn && false)
        {
            for(int x=0;x<=COLUMNS+1;x++){
                int tempX = xRate*x;
                g2.draw(new Line2D.Double(tempX,0,tempX,HEIGHT));
            }
            for(int y=0;y<=ROWS+1;y++){
                int tempY = yRate*y;
                g2.draw(new Line2D.Double(0,tempY,WIDTH,tempY));
            }
        }        
        
        if(currentImage!=null && linesOn)
            g2.drawImage(currentImage,null,mouseX,mouseY);
    }
    
    /**
     * Turns the grid on or off
     */
    public void setLines(boolean on)
    {
        linesOn = on;
    }
    
    /**
     * sets the current extra info
     * @param on
     */
    public void setExtraInfo(String info)
    {
        extraInfo = info;
    }
    
     /**
     * Sets the current Image
     */
    public void setImage(BufferedImage img,int layer)
    {
        currentLayer = layer;
        currentImage = img;
    }
    
    public void save(String name)
    {
        for(int layer=0;layer<3;layer++)
        {
            DataOutputStream d=null;
            try {
                d = new DataOutputStream(new FileOutputStream(name + "." + layer + ".txt"));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            for(int y=0;y<ROWS;y++)
            {
                String str="";
                for(int x = 0;x<COLUMNS;x++)
                {                
                    if(images[layer][x][y]!=null)
                        str+=imgNames.get(img.indexOf(images[layer][x][y]));
                    if(layer!=STATIC && extraImageData[layer][x][y]!=null && !extraImageData[layer][x][y].equals(""))
                        str+=":"+extraImageData[layer][x][y];
                    str+=",";
                }
                try {
                    d.writeBytes(str+"\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }  
        }
        try {
            PrintWriter writer = new PrintWriter(new File(name + "collision.txt"));
            int width = WIDTH/COLUMNS;
            int height = HEIGHT/ROWS;
            String arr[][] = new String[COLUMNS][ROWS];
            for(int y=0;y<ROWS;y++)
            {
                for(int x = 0;x<COLUMNS;x++)
                {                
                    if(images[STATIC][x][y]!=null){
                        String code = "1";
                        if(extraImageData[STATIC][x][y]!=null && !extraImageData[STATIC][x][y].equals(""))
                            code = extraImageData[STATIC][x][y];
                        for(int i=0;i<=(images[STATIC][x][y].getWidth()-.001)/width;i++){
                            for(int j=0;j<=(images[STATIC][x][y].getHeight()-.001)/height;j++){
                                if(x+i<arr.length && y+j<arr[0].length)
                                    arr[x+i][y+j] = code;
                                
                            }
                        }
                    }else{
                        if(arr[x][y]==null || arr[x][y].equals(""))
                            arr[x][y] = "0";
                    }
                }
            }
            
            for(int y=0;y<ROWS;y++)
            {
                for(int x = 0;x<COLUMNS;x++)
                {   
                    writer.write(arr[x][y]+",");
                }
            }
            writer.close();
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public void load(String name)
    {

        for (int layer = 0; layer < 3; layer++) {
            BufferedReader b = null;
            try {
                b = new BufferedReader(new FileReader(name + "." + layer + ".txt"));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            for (int y = 0; y < ROWS; y++) {
                String str = null;
                try {
                    str = b.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                int x = 0;
                String currentString = "";
                String extraData = "";
                boolean colon = false;
                for (int i = 0; i < str.length(); i++) {


                    if (str.charAt(i) == ',') {
                        if (imgNames.indexOf(currentString) != -1) {
                            images[layer][x][y] = img.get(imgNames.indexOf(currentString));
                        } else {
                            images[layer][x][y] = null;
                        }
                        extraImageData[layer][x][y] = extraData;
                        x++;
                        currentString = "";
                        extraData = "";
                        colon = false;
                    } else {
                        if (colon) {
                            extraData += str.charAt(i);
                        }
                        if (str.charAt(i) == ':') {
                            colon = true;
                        }
                        if (!colon) {
                            currentString += str.charAt(i);
                        }
                    }
                }
            }
        }
        
        try {
            Scanner input = new Scanner(new File(name + "collision.txt"));
            String line = "";
            while(input.hasNextLine()){
                 line += input.nextLine();
            }
            for (int y = 0; y < ROWS; y++) {
                for (int x = 0; x < COLUMNS; x++) {
                    extraImageData[STATIC][x][y] = line.substring(0,line.indexOf(","));
                    line = line.substring(line.indexOf(",")+1);
                    System.out.println(extraImageData[STATIC][x][y]);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Editable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void mouseClicked(MouseEvent e) 
    {    
    }
    
    public void mouseEntered(MouseEvent e) 
    {    
    }
    
    public void mouseExited(MouseEvent e) 
    {    
        mouseX = -10000;
        mouseY = -10000;     
        oldX = mouseX;
        oldY = mouseY;
        repaint();
    }
    
    public void mousePressed(MouseEvent e) 
    {    
    }
    
    public void mouseReleased(MouseEvent e) 
    {    
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            images[currentLayer][mouseX/xRate][mouseY/yRate] = currentImage;
            extraImageData[currentLayer][mouseX/xRate][mouseY/yRate] = extraInfo;
        }
        if(e.getButton()==MouseEvent.BUTTON3)
        {
            images[currentLayer][mouseX/xRate][mouseY/yRate] = null;
            extraImageData[currentLayer][mouseX/xRate][mouseY/yRate] = "";
        }
        if(e.getButton()==MouseEvent.BUTTON2)
        {
            for(int i=0;i<images[currentLayer].length;i++)
            {
                for(int j=0;j<images[currentLayer][0].length;j++)
                {
                    images[currentLayer][i][j] = currentImage;
                }
            }
        }
        
        repaint();
                    
    }
    
    public void mouseDragged(MouseEvent e) 
    {                   
        if(currentImage!=null)
        {
            mouseX = (int)(e.getX()/xRate)*xRate;
            mouseY = (int)(e.getY()/yRate)*yRate;
            int xDiff = Math.abs(mouseX - oldX);
            int yDiff = Math.abs(mouseY - oldY);
            repaint(mouseX-xDiff,mouseY-yDiff,currentImage.getWidth()+xDiff*2,currentImage.getHeight()+yDiff*2);
            oldX = mouseX;
            oldY = mouseY;
        }
    }
    
    public void mouseMoved(MouseEvent e) 
    {    
        if(currentImage!=null)
        {
            mouseX = (int)(e.getX()/xRate)*xRate;
            mouseY = (int)(e.getY()/yRate)*yRate;
            int xDiff = Math.abs(mouseX - oldX);
            int yDiff = Math.abs(mouseY - oldY);
            repaint(mouseX-xDiff,mouseY-yDiff,currentImage.getWidth()+xDiff*2,currentImage.getHeight()+yDiff*2);
            oldX = mouseX;
            oldY = mouseY;
        }
    }
    
    
}

