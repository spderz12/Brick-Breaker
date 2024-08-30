
/**
 * Write a description of class Main here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javax.swing.*;
import java.awt.*;

public class Main
{
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static void main (String[] args)
    {
        JFrame frame = new JFrame();

        frame.setMinimumSize(new Dimension(getScreenWidth(),getScreenHeight()));

        frame.setTitle("Brick Breaker Pong - Jack");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BrickBreaker game = new BrickBreaker();
        game.setPreferredSize(new Dimension(getScreenWidth(),getScreenHeight()));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.add(game);
        frame.pack();

        frame.setVisible(true);
    
    }
    //these two methods help with resizing
    public static int getScreenWidth()
    {
        return (int) screenSize.getWidth();
    }

    public static int getScreenHeight()
    {
        return (int) screenSize.getHeight();
    }
}
