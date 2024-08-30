import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StrongBrick extends Brick
{
    private int lives;
    private Image damage2;
    private Image damage1;
    public StrongBrick(int x, int y,int width, int height)
    {
        super(x,y,width,height);
        try
        {
            damage2 = ImageIO.read(new File("damage2.png"));
            damage1 = ImageIO.read(new File("damage1.png"));
        }
        catch (IOException e)
        {
        }
        lives = 3;
    }

    public boolean strong(){return true;}

    public void hit(Ball b, ArrayList<Ammo> ammo)
    {

        lives--;

        if(lives == 0)
        {
            super.setVisible(false);
            int rand = (int) (10*Math.random()+1);
            //30% chance for strong bricks
            if(rand <= 3)
                ammo.add(new Ammo(this));
        }

    }

    public void hit(Bullet b)
    {
        super.setVisible(false);
    }

    public void draw (Graphics g)
    {
        //subclass variable changes how damaged brick looks before calling parent draw
        if (lives == 2)
            super.setImage(damage2);
        else if(lives == 1)
            super.setImage(damage1);

        super.draw(g);
    }
}
