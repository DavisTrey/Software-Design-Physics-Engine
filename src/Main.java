import springies.Springies;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;


/**
 * Creates window that can be moved, resized, and closed by the user.
 * 
 * @author Robert C. Duvall
 */
public class Main
{
    // constants
    public static final Dimension SIZE = new Dimension(800, 600);
    public static final String TITLE = "Springies!";

    /**
     * main --- where the program starts
     * 
     * @param args
     */
    public static void main (String args[])
    {
        // view of user's content
        final Springies sp = new Springies();
        JButton jb = new JButton("Make new Ball");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent arg0) {
                sp.addBall();
            }
        });
        // container that will work with user's OS
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // add our user interface components
        frame.getContentPane().add(sp, BorderLayout.CENTER);
        frame.getContentPane().add(jb, BorderLayout.SOUTH);
        // display them
        frame.pack();
        frame.setVisible(true);
    }
}
