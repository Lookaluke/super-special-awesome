package pokeman;
/**
 * This class controls all music related tasks for: 
 *  ______     _                              
 *  | ___ \   | |                             
 *  | |_/ /__ | | _____ _ __ ___   ___  _ __  
 *  |  __/ _ \| |/ / _ \ '_ ` _ \ / _ \| '_ \ 
 *  | | | (_) |   <  __/ | | | | | (_) | | | |
 *  \_|  \___/|_|\_\___|_| |_| |_|\___/|_| |_|
 *  ________                                   _____                  
 *  ___  __ \_____ _________________ _________ __  /______ ___________
 *  __  /_/ /  __ `/__  /__  /_  __ `__ \  __ `/  __/  __ `/__  /__  /
 *  _  _, _// /_/ /__  /__  /_  / / / / / /_/ // /_ / /_/ /__  /__  /_
 *  /_/ |_| \__,_/ _____/____/_/ /_/ /_/\__,_/ \__/ \__,_/ _____/____/
 * 
 * coming to a HornAttacks center near you
 * 
 * @author Hurshal Patel
 */

import java.io.*;
import javax.sound.midi.*;

public class MusicSystem
{
    
    private File f = null;
    Sequence mySeq = null;
    Sequencer sequence = null;
            
       
    public void loadMusic(String filePath)
    {
        if(sequence != null)
            pause();
        f = new File(filePath);
        try
        {
            mySeq = MidiSystem.getSequence(f);
            sequence = MidiSystem.getSequencer();
            sequence.setSequence(mySeq);
            sequence.open();
        }
        catch(InvalidMidiDataException imde)
        {
            System.out.println("Error type: imde");
        }
        
        catch(IOException io)
        {
            System.out.println("Error type: io");
        }
        
        catch(MidiUnavailableException mue)
        {
            System.out.println("Error type: mue");
        }
    }
    
    public void play(boolean loop)
    {
        if(loop)
            sequence.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);   
        sequence.start();
    }
    
    public void pause()
    {
        sequence.stop();   
    }
    
    //time in milliseconds
    public void goTo(long pos)
    {
        sequence.setTickPosition(pos);   
    }
    
    public void close()
    {
        pause();
        sequence.close();
    }
}

