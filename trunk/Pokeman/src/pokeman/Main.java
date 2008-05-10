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
        frame.setTitle("Pokemon Horn");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        new Window(frame);
    }

}
