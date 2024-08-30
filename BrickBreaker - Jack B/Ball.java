
/**
 * Write a description of class Ball here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Ball
{
    //instance vars
    private int x;
    private int y;
    private int size;
    private int dx;
    private int dy;
    private Color colour;

    //constructor
    public Ball(int x, int y, int size, int dx, int dy, Color colour)
    {
        this.x = x;
        this.y = y;
        this.size = size;
        this.dx = dx;
        this.dy = dy;
        this.colour = colour;

    }

    //getters and setters
    public int getX(){return x;}

    public int getY(){return y;}

    public void setX(int x) {this.x = x;}

    public void setDX(int x) {dx = x;}

    public int getDX(){return dx;}

    public void setY(int y) {this.y = y;}

    public void setDY(int y) {dy = y;}

    public int getDY(){return dy;}

    public int getSize(){return size;}

    public void move(Graphics g, ArrayList<Ammo> ammo)
    {
        x += dx;

        if(x < 5)
        {
            //hits left wall
            dy = 0;
            dx = 0;
            BrickBreaker.addPointR();

            BrickBreaker.setBallAttatched(true);
            if(BrickBreaker.getScoreR() == 7)
                BrickBreaker.setWin(true);

        }
        else if (x+size > Main.getScreenWidth())
        {
            //hits right wall
            dy = 0;
            dx = 0;
            BrickBreaker.addPointL();

            BrickBreaker.setBallAttatched(true);
            if(BrickBreaker.getScoreL() == 7)
                BrickBreaker.setWin(true);

        } 

        y += dy;

        //collides with top/bottom walls
        if(y < 5)
        {
            y = 5;
            dy = -dy;
        }
        else if (y+size > Main.getScreenHeight()-5)
        {
            y = Main.getScreenHeight()-5-size;
            dy = -dy;
        }

        draw(g);
    }

    public void draw (Graphics g)
    {
        g.setColor(colour);
        g.fillOval(x,y, size, size);
    } 

    public void collidesWith(Brick b, ArrayList<Ammo> ammo)
    {
        Rectangle ball = new Rectangle(x,y,size,size);
        Rectangle brick = new Rectangle(b.getX(), b.getY(),b.getWidth(), b.getHeight());
        if(ball.intersects(brick))
        {

            b.hit(this, ammo);

            if(x+size <= (b.getX() + dx) || x >= b.getX()+b.getWidth() + dx)
                dx = -dx;
            else
                dy=-dy;

            int x = getDX();
            int y = getDY();
            //slows down ball in x direction      
            if(Math.abs(getDX()) > 7)
            {   
                if(Math.abs(getDX()) > 12)
                {
                    if(x > 0)
                        setDX(getDX()-6);
                    else
                        setDX(getDX()+6);
                }
                else
                {
                    if(x > 0)
                        setDX(getDX()-2);
                    else
                        setDX(getDX()+2);
                }

            }
            //slows down ball in y direction
            if(Math.abs(getDY()) >  7)
            {
                if(Math.abs(getDY()) > 12)
                {
                    if(y > 0)
                        setDY(getDY()-6);
                    else
                        setDY(getDY()+6);
                }
                else
                {
                    if(y > 0)
                        setDY(getDY()-2);
                    else
                        setDY(getDY()+2);
                }

            }

        }
    }

    public void collidesWith(Paddle b)
    {
        Rectangle ball = new Rectangle(x,y,size,size);
        Rectangle paddle = new Rectangle(b.getX(), b.getY(),b.getWidth(), b.getHeight());

        if(ball.intersects(paddle))
        {
            b.hit();

            /*the further away the ball is from the middle of the paddle, 
            the faster it is reflected back*/
            double amp = (int) Math.abs(y - (b.getY()+(b.getHeight()/2)));

            if(amp < 11)
            {
                amp = 0;
                if(dx > 0)
                {
                    dx = dx+3;
                }
                else
                    dx = dx - 3;
            }
            else if (amp <= 25)
            {
                if(dy > 15)
                    amp = 0.3;
                else
                    amp = 0.8;
            }
            else if(amp <= 45)
                amp = 1;
            else if (amp< 55)
                amp = 2;
            else
                amp = 4;

            if(x+size <= b.getX() + dx || x >= b.getX()+b.getWidth() + dx)
            {
                dx = -dx;
                if(dy > 0){
                    //ball is moving downwards
                    if(y > (b.getY()+b.getHeight()/2))
                    {
                        //bottom paddle reflection
                        dy = (int)(amp*dy);

                    }
                    else if (y+size < (b.getY()+b.getHeight()/2))
                    {
                        //top paddle reflection
                        dy = (int)(amp*dy);
                        dy = -dy;

                    }
                }
                else if (dy < 0)
                {
                    //ball is coming up
                    if(y > (b.getY()+b.getHeight()/2))
                    {
                        //bottom paddle reflection
                        dy = (int)(amp*dy);
                        dy = -dy;

                    }
                    else if (y+size < (b.getY()+b.getHeight()/2))
                    {
                        //top paddle reflection
                        dy = (int)(amp*dy);

                    }
                }
                else
                {
                    //ball is straight
                    if(y > (b.getY()+b.getHeight()/2))
                    {
                        //bottom paddle
                        dy = (int) ((amp+1)*1);
                        dy = 2;

                    }
                    else if (y+size < (b.getY()+b.getHeight()/2))
                    {
                        //top paddle
                        dy = (int)((amp+1)*-1);

                    }
                }
            }
            //slows down ball when it hits paddle to avoid clipping
            if(dx >= 18)
                            dx = 12;
                            else if (dx<=-18)
                            dx=-12;
            if(dy >= 15)
                            dy = 9;
                            else if (dy<= -15)
                            dx=-9;  

        }
    }
}