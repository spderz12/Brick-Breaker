import java.awt.*;

/**
 * Write a description of class Paddle here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Paddle extends Brick
{
    private int speed;
    private boolean isOnUp;
    private boolean isOnDown;
    private boolean lastup;
    private boolean lastdown;
    private static boolean rMove = false;
    public Paddle(int x, int y, int width, int height, Color colour, int sp)
    {
        super(x,y,width,height);
        speed = sp;//how fast paddle moves up/down
    }

    public int getSpeed(){return speed;}

    public void setSpeed(int s){speed = s;}

    public boolean getIsOnUp(){return isOnUp;}

    public boolean getIsOnDown(){return isOnDown;}

    public void setIsOnUp(boolean b){isOnUp = b;}

    public void setIsOnDown(boolean b){isOnDown = b;}

    public void move()
    {
        if(isOnUp)
        {
            setY(getY()-speed);
            lastup = true;
            lastdown = false;
        }
        else if (isOnDown)
        {
            setY(getY()+speed);
            lastup = false;
            lastdown = true;
        }

    }

    public boolean movingUp(){return lastup;}

    public boolean movingDown(){return lastdown;}

    public static boolean getrMove(){return rMove;}

    public void ballMoveL(Ball b)
    {

        b.setX(120);
        b.setY(super.getY()+ 50);

    }

    public void ballMoveR(Ball b)
    {

        b.setX(Main.getScreenWidth()-135);
        b.setY(super.getY()+ 50);

    }

    public void draw (Graphics g)
    {
        g.setColor(Color.green);
        g.fillRect(super.getX(),super.getY(), super.getWidth(), super.getHeight());
    }

    public void hit()
    {
        setVisible(true); //overides Brick's hit method
        //determines which paddle last hit ball --> which way power ups should fall
        if(super.getX() < Main.getScreenWidth()/2)
            rMove = false;
        else 
            rMove = true;
    }
}
