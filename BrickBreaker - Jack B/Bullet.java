import java.awt.Graphics;
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Write a description of class Bullet here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Bullet
{
    private int x;
    private int y;
    private int dx;
    private boolean bulletOnLeft;

    Image tempR;
    Image tempL;
    Image bullet;
    public Bullet(boolean rSide, ArrayList<Bullet> bL, ArrayList<Bullet> bR)
    {
        try
        {
            tempR = ImageIO.read(new File("BulletR.png"));
            tempL = ImageIO.read(new File("BulletL.png"));
        }
        catch (IOException e)
        {
        }
        if(rSide && bL.size() <= 3)
        {
            //adds bullet to stored bullets on left;
            bullet = tempR; //tempR is the direction the bullet points
            x = -100;
            y =-100;
            dx = 0;
            bL.add(this);
        }
        else if (!rSide && bR.size() <= 3)
        {
            bullet = tempL;
            x = -200;
            y =-200;
            dx = 0;
            bR.add(this);
        }
        bulletOnLeft = rSide;
    }

    public void fire(Paddle p)
    {
        //makes bullet coords equal paddle
        if(bulletOnLeft)
        {
            x = p.getX()+15; 
            dx = 5;
        }
        else
        {
            x = p.getX() - 5;
            dx = -5;
        }

        y = p.getY() + 40;  

    }

    public void move(Graphics g,ArrayList<Bullet> bullets)
    {
        x += dx;
        //removes bullet once it reaches end of screen
        if(x < 0 && dx < 0)
            bullets.remove(bullets.indexOf(this));
        else if(x > Main.getScreenWidth() && dx >0)
            bullets.remove(bullets.indexOf(this));
        draw(g);
    }

    public void draw (Graphics g)
    {
        g.drawImage(bullet, x,y, null);
    } 

    public void collidesWith(Brick b, ArrayList<Bullet> bullets)
    {
        //bullets arraylist needs to remove element after intersection, 
        //so a unique method is needed
        Rectangle bullet = new Rectangle(x+dx,y, 15,20);
        Rectangle brick = new Rectangle(b.getX(), b.getY(),b.getWidth(), b.getHeight());
        if(bullet.intersects(brick))
        {
            bullets.remove(bullets.indexOf(this));
            b.hit(this);
        }
    }

}
