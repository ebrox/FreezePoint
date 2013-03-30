/**
 * Programmers: Chase McCowan & Ed Broxson 
 * Date: 03/22/2013 
 * Purpose: Driver for GaseousDiffusion
 */
package chemistry;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static chemistry.FreezePointDepression.addComponentsToPane;

public class FreezePointDepressionDriver {
    /**
     * Create the GUI and display it
     */
    private static void createAndShowGUI() {
        //create frame with title
        JFrame frame = new JFrame("Pellissippi State Community College");

        //set default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //set up content pane
        addComponentsToPane(frame.getContentPane());

        //set icon image for frame
        frame.setIconImage(new ImageIcon("src/layout/pscclogo.jpg").getImage());

        //set resize behavior to no resize
        frame.setResizable(false);

        //set window size
        frame.setSize(1000, 600);
        
        //center the window
        frame.setLocationRelativeTo(null);

        //display the window
        frame.setVisible(true);
    }
        /**
     * Schedule a job for the event-dispatching thread: creating and showing
     * this application's GUI.
     */
    public static void main(String[] args) {


        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });

    }
}
