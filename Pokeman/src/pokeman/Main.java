/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pokeman;

import javax.swing.JFrame;
 
/**
 *
 * @author Mark
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Pokemon Razzmatazz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MusicSystem m = new MusicSystem();
        m.loadMusic("Music\\bluedabadee.mid");
        //m.play(true);

        frame.setVisible(true);
        new Window(frame);
    }

}
