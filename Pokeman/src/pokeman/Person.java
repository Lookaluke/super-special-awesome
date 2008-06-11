package pokeman;

import java.awt.Graphics2D;
import java.io.Serializable;


public class Person implements Comparable<Person>,Serializable{
    private String name,speech;
    private int x,y,direction;
    private int xChange,yChange;
    private Animation walk;
    private boolean moving,allowedToUpdate;
    private int screenX,screenY;
    private int counter;
    private Window w;
    private Collideable lastEdition,newEdition;
    private TextBox txt;
    private boolean question;
    private boolean yes;
    private Menu menu;
    private boolean stationary;
    private Character player;
    

    /**
     * Constructor for objects of class Person
     */
    public Person(String n,String s,int x,int y,Window w)
    {
        
        this.w = w;
        speech = s;
        name = n;
        direction = Animation.DOWN;
        screenX = x/Window.WIDTH;
        screenY = y/Window.HEIGHT;
        this.x = x;
        this.y = y;
        
        allowedToUpdate = true;
        
        if(w!=null){
            lastEdition = new Collideable(this,x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT+1,1,0,0);
            w.addToCollision(lastEdition);
            walk = new Animation(name);
        }
        

    }

    public String getName()
    {
        return name;
    }
    
    public void draw(Graphics2D g,int currentX,int currentY){
        counter++;


        
        if(allowedToUpdate)
            update();
        if(!moving)
            walk.standingFrame();
        g.drawImage(walk.getFrame(),null,x+currentX,y+currentY);
        
        if(counter%4==0 || walk.getDirection()!=direction){
            walk.nextFrame(direction);
        }
        
        if(moving && !stationary){   
            if(direction == Animation.RIGHT)
                x += Window.TILE_WIDTH/Window.numberOfCounts;
            if(direction == Animation.LEFT)
                x -= Window.TILE_WIDTH/Window.numberOfCounts;
            if(direction == Animation.UP)
                y -= Window.TILE_WIDTH/Window.numberOfCounts;
            if(direction == Animation.DOWN)
                y += Window.TILE_WIDTH/Window.numberOfCounts;


            if(counter>=(Window.numberOfCounts-1))
            {
                counter = 0;
                moving = false;
                w.removeFromCollision(lastEdition);
                lastEdition = newEdition;
            }
        }
    }
    
    public void drawTextAndMenus(Graphics2D g){
        if(txt!=null){
            txt.draw(g);
            if(txt.isOver()){
                if(!question){
                    System.out.println("here");
                    this.allowUpdate(true);
                    this.getPlayerTalkingTo().allowUpdate(true);
                    txt = null;
                }else{
                    if(menu==null){
                        String[] str = {"yes","no"};
                        menu = new Menu(getWindow().getFrame(),str,50,400,100,100,Style.STANDARD_TEXT);
                        txt = new TextBox(getWindow().getFrame(),"",0,475,800,101,true,false,Style.STANDARD_TEXT); 
                        txt.removeKeyListener();
                        question = false;
                    }
                    
                }                
            }
        }
        
        if(menu!=null){
            menu.draw(g);
            String ret = menu.result();
            if(ret!=null){
                String str = "";
                if(ret.equals("yes")){
                    str = getSpeech().substring(getSpeech().indexOf("yes:")+"yes:".length(),getSpeech().indexOf("no:"));
                    yes = true;
                }else{
                    str = getSpeech().substring(getSpeech().indexOf("no:")+"no:".length());
                    yes = false;
                }
                txt = new TextBox(getWindow().getFrame(),str,0,475,800,101,true,false,Style.STANDARD_TEXT);
                menu = null;
                
            }
        }
    }
    
    /**
     * Override this method to make the person do what you want. This person 
     * just wanders around
     */
    protected void update(){

        if(counter>=50 && Math.random()<.1)
        {
            int dir = (int)(Math.random()*4);
            makeMove(dir);            
            
        }
    }
    
    /**
     * Call this method in the direction you want to move, calls canMove to determine if it can move
     * so override that method instead
     * @param dir The direction to move
     */
    public void makeMove(int dir){
        
        if((!moving || stationary) && dir!=Animation.NONE){
            
            direction = dir;
            
            if(!stationary){

                xChange = 0;
                yChange = 0;

                switch(dir){
                    case Animation.UP:
                        yChange = -1;
                        break;
                    case Animation.DOWN:
                        yChange = 1;
                        break;
                    case Animation.RIGHT:
                        xChange = 1;
                        break;
                    case Animation.LEFT:
                        xChange = -1;
                        break;
                }



                if(canMove(dir)){

                    moving = true;

                    Collideable col = getCollision(dir);

                    newEdition = new Collideable(this,x/Window.TILE_WIDTH+xChange,y/Window.TILE_HEIGHT+1+yChange,1,0,0);
                    w.addToCollision(newEdition);
                }
            }else{
                moving = true;
            }
            counter = 0;
        }
    }
    
    /**
     * Override this method to determine when a person can move
     * @param dir The direction to move
     * @return true if they can move
     */
    protected boolean canMove(int dir){
        Collideable col = getCollision(dir);
        return (col==null || col.getNumber(0)==0); 
    }
    
    public int getX(){
        return x;
    }
    
    public void setX(int x){
        this.x = x;
    }
        
    public void setY(int y){
        this.y = y;
    }
    
    public int getY(){
        return y;
    }
    
    public void setStationary(boolean s){
        stationary = s;
    }
    
    public String getSpeech(){
        return speech;
    }
    
    public void allowUpdate(boolean allow){
        allowedToUpdate = allow;
    }
    
    public boolean allowedToUpdate(){
        return allowedToUpdate;
    }
    
    public void setDirection(int direction){
        this.direction = direction;
    }
    
    public Window getWindow(){
        return w;
    }
    
    public int getDirection(){
        return direction;
    }
    
    public boolean isMoving(){
        return moving;
    }
    
    public void removeKeyListener(){
        if(txt!=null)
            txt.removeKeyListener();
    }
    
    public void addKeyListener(){
        if(txt!=null)
            txt.addKeyListener();
    }
    
    public Character getPlayerTalkingTo(){
        return player;
    }
    
    public void setSpeech(String s){
        speech = s;
    }
    
    public void talk(Character player){
        
        if(!moving){
            this.player = player;
            this.allowUpdate(false);
            String str = getSpeech();
            if(getSpeech().indexOf("?")!=-1){
                question = true;
                str = getSpeech().substring(0,getSpeech().indexOf("?"));
            }
            txt = new TextBox(getWindow().getFrame(),str,0,475,800,101,true,false,Style.STANDARD_TEXT);
        }
    }
    
    public boolean saidYes(){
        boolean hold = yes;
        yes = false;
        return hold;
    }
    
    protected Collideable getCollision(int dir){
        Collideable col = null;
        if(dir==Animation.UP){
            col = w.inCollision(new Collideable(this,x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT-1+1,0,0,0));
        }
        if(dir==Animation.DOWN){
            col = w.inCollision(new Collideable(this,x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT+1+1,0,0,0));
        }
        if(dir==Animation.RIGHT){
            col = w.inCollision(new Collideable(this,x/Window.TILE_WIDTH+1,y/Window.TILE_HEIGHT+1,0,0,0));
        }
        if(dir==Animation.LEFT){
            col = w.inCollision(new Collideable(this,x/Window.TILE_WIDTH-1,y/Window.TILE_HEIGHT+1,0,0,0));
        }
        if(dir==Animation.NONE){
            col = w.inCollision(new Collideable(this,x/Window.TILE_WIDTH,y/Window.TILE_HEIGHT+1,0,0,0));
        }
        if(col!=null && col.getMaker()!=this)
            return col;
        else
            return null;
    }

    public int compareTo(Person p) {
        if(this.getX()<p.getX())
            return -1;
        if(this.getX()==p.getX())
        {
            if(this.getY()<p.getY())
                return -1;
        }
        if(this.getX()==p.getX() && this.getY()==p.getY())
            return 0;
        
        return 1;
    }
}