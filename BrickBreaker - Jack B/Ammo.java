
/**
 * Write a description of class Bullet here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Ammo
{
    //instance vars
    private int x;
    private int y;

    private int dx;

    private Image ammoIMG;

    public Ammo(Brick b)
    {

        try
        {
            ammoIMG = ImageIO.read(new File("Ammo.png"));
        }
        catch (IOException e)
        {
        }
        this.x = b.getX();
        this.y = b.getY();

        if(!Paddle.getrMove())
            this.dx = -1;
        else
            this.dx = 1;
    }

    
    public void move(Graphics g)
    {
        x += dx;
        draw(g);
    }

    public void draw (Graphics g)
    {
        g.drawImage(ammoIMG, x,y, null);
    } 

    public void collidesWith(Paddle p, ArrayList<Ammo> ammo, ArrayList<Bullet> bR, ArrayList<Bullet> bL)
    {
        Rectangle item = new Rectangle(x,y,30,30);
        Rectangle paddle = new Rectangle(p.getX(), p.getY(),p.getWidth(), p.getHeight());
        if(item.intersects(paddle))
        {
            ammo.remove(ammo.indexOf(this));
            //add bullet to r or l side depending on who collects bullet
            if(p.getX() < Main.getScreenWidth()/2)
            {
                new Bullet(true, bR, bL);
            }
            else if (p.getX() > Main.getScreenWidth()/2)
            {
                new Bullet(false, bR, bL);
            }

        }
    }
}
