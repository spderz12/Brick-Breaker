import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Write a description of class Brick here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Brick
{
    //instance vars
    private boolean visible;
    private Color colour;
    private int x;
    private int y;
    private int width;
    private int height;

    private Image brick;
    private Image Healthy;

    //constructor
    public Brick(int x, int y, int width, int height)
    {
        try
        {
            Healthy = ImageIO.read(new File("brick.png"));
        }
        catch (IOException e)
        {
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        //all bricks start as "healthy"
        brick = Healthy;
    }

    public int getX(){return x;}
    public int getY(){return y;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public boolean isVisible(){return visible;}
    
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setWidth(int w){width = w;}
    public void setHeight(int h){height = h;}
    public void setVisible(boolean v){visible = v;}
    public void setImage(Image i){brick = i;}
    public void draw (Graphics g)
    {
        g.drawImage(brick,x,y, 30, 70, null);
    }

    //hit method
    public void hit(Ball b, ArrayList<Ammo> ammo)
    {
        visible = false;
        int rand = (int) (10*Math.random()+1);
        //regular bricks have 10% chance of dropping a bullet
        if(rand <= 1)
            ammo.add(new Ammo(this));

    }

    public void hit(Bullet b)
    {
        visible = false;
    }

}
