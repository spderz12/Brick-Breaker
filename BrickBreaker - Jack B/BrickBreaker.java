
/**
 * Write a description of class BrickBreakerGame here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.imageio.*;
import java.io.*;
import java.awt.Graphics;

public class BrickBreaker extends JPanel implements MouseListener, KeyListener, ActionListener
{
    private Ball ball;
    private Timer timer;
    private boolean play;
    private Brick[][] bricks;
    private Paddle paddle;
    private Paddle paddle2;
    private static boolean ballAttatched;
    private static boolean leftStart;
    private static int scoreL;
    private static int scoreR;
    private Image zero;
    private Image one;
    private Image two;
    private Image three;
    private Image four;
    private Image five;
    private Image six;
    private Image seven;
    private Image mainScreen;
    private Image winOne;
    private Image winTwo;
    private ArrayList<Ammo> ammo = new ArrayList<Ammo>();
    private ArrayList<Bullet> storedBulletsR = new ArrayList<Bullet>();
    private ArrayList<Bullet> storedBulletsL = new ArrayList<Bullet>();
    private ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
    int screen = 1;
    private static boolean win = false;
    boolean loaded = false;
    private Image ammoIMG;
    private Image background;

    public BrickBreaker()
    {
        try
        {
            zero = ImageIO.read(new File("zero.png"));
            one = ImageIO.read(new File("one.png"));
            two = ImageIO.read(new File("two.png"));
            three = ImageIO.read(new File("three.png"));
            four = ImageIO.read(new File("four.png"));
            five = ImageIO.read(new File("five.png"));
            six = ImageIO.read(new File("six.png"));
            seven = ImageIO.read(new File("seven.png"));
            mainScreen = ImageIO.read(new File("main.png"));
            winOne = ImageIO.read(new File("win1.png"));
            winTwo = ImageIO.read(new File("win2.png"));
            ammoIMG = ImageIO.read(new File("Ammo.png"));
            background = ImageIO.read(new File("background.png"));
        }
        catch (IOException e)
        {
        }

        ball = new Ball(115,250, 20, 0, 0, Color.white);
        ballAttatched = true;
        //starts the game with the ball's y coord linked to the left paddle

        bricks = new Brick[5][9];
        scoreL = 0;
        scoreR = 0;
        leftStart = true;

        //fills brick array
        for(int i = 0; i < bricks.length; i++){
            for(int j = 0; j < bricks[i].length;j++)
            {
                int rand = (int) (100*Math.random()+1);
                //40% chance of a strong brick
                if (rand <= 40)
                    bricks[i][j] = new StrongBrick((Main.getScreenWidth()/2)-88 +35*i,  j*75 + 25, 30,70);
                else
                    bricks[i][j] = new Brick((Main.getScreenWidth()/2)-88 +35*i,  j*75 + 25, 30,70);
            }   
        }

        paddle = new Paddle(100, 300, 15, 120, Color.green, 8);
        paddle2 = new Paddle(Main.getScreenWidth()-105, 300, 15, 120, Color.green, 8);
        addKeyListener(this);
        addMouseListener(this);
        timer = new Timer(10, this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer.start();

        play = false;
    }

    public void reset()
    {
        /*when ever a new game is made, this code makes a new unique set of bricks and resets
        all other parameters */
        for(int i = 0; i < bricks.length; i++){
            for(int j = 0; j < bricks[i].length;j++)
            {
                int rand = (int) (100*Math.random()+1);
                if (rand <= 40)
                    bricks[i][j] = new StrongBrick((Main.getScreenWidth()/2)-85 +35*i,  j*75 + 25, 30,70);
                else
                    bricks[i][j] = new Brick((Main.getScreenWidth()/2)-85 +35*i,  j*75 + 25, 30,70);
            }   
        }
        scoreL = 0;
        scoreR = 0;
        leftStart = true;
        ballAttatched = true;
        play = false;
        win = false;
        paddle.setY(300);//returns paddles to middle of screen
        paddle2.setY(300);
        for(int i = 0; i < storedBulletsL.size(); i++)
        {
            storedBulletsL.remove(0);
        }
        for(int i = 0; i < storedBulletsR.size(); i++)
        {
            storedBulletsR.remove(0);
        }
    }

    public void paint (Graphics g)
    {
        if(screen == 1)
        {
            //main screen
            g.drawImage(mainScreen,0,0, Main.getScreenWidth(),Main.getScreenHeight(), null);
        }
        else if (screen == 2 && !win)
        {
            //game screen
            g.setColor(Color.yellow);
            g.fillRect(0,0,Main.getScreenWidth(),Main.getScreenHeight());

            g.drawImage(background,5,5,Main.getScreenWidth()-10,Main.getScreenHeight()-10, null);

            //draw score
            drawScore(g);

            //draw stored ammo
            drawStoredBullets(g);

            //draw paddles
            paddle.draw(g);
            paddle2.draw(g);

            //draw/move ball
            ball.move(g,ammo);
            ball.collidesWith(paddle);
            ball.collidesWith(paddle2);

            //moves any ammo objects on screen
            for(Ammo a: ammo)
            {
                a.move(g);
                a.collidesWith(paddle, ammo, storedBulletsR, storedBulletsL);
                a.collidesWith(paddle2, ammo, storedBulletsR, storedBulletsL);
            }

            //checks each players fired bullets, not stored ones from power ups
            for(Bullet b: firedBullets)
            {
                b.move(g, firedBullets);
            }

            //draw bricks
            for(int i = 0; i < bricks.length; i++)
            {
                for(int j = 0; j<bricks[i].length;j++){
                    if(bricks[i][j].isVisible())
                    {
                        bricks[i][j].draw(g);
                        ball.collidesWith(bricks[i][j], ammo);
                        for(Bullet b: firedBullets)
                            b.collidesWith(bricks[i][j], firedBullets);

                    }
                }
            }
        }
        else if (screen == 2 && win)
        {
            /*This additional boolean condition stops all motion in the game
            and indicates it has finished*/
            g.setColor(Color.yellow);
            g.fillRect(0,0,Main.getScreenWidth(),Main.getScreenHeight());

            g.setColor(Color.black);
            g.fillRect(5,5, Main.getScreenWidth()-10,Main.getScreenHeight()-10);

            drawScore(g);
            paddle.draw(g);
            paddle2.draw(g);
        }
        else if (screen == 3)
        {
            //win screens
            if (scoreL >= 7)
                g.drawImage(winOne,0,0, Main.getScreenWidth(),Main.getScreenHeight(), null);
            else
                g.drawImage(winTwo,0,0, Main.getScreenWidth(),Main.getScreenHeight(), null);
        }

    }

    public void actionPerformed(ActionEvent e)
    {
        /*this ensures players dont accidentally click on the play again/menu buttons before 
        they are visible*/
        if(screen == 3)
            loaded = true;

        //the win boolean also helps pause the game once one player reaches 7
        if(play && win == false)
        {
            repaint();

            paddle.move();
            paddle2.move();

            //stops paddle from leaving screen
            if(paddle.getY() <=0)
            {
                paddle.setY(5);
            }
            else if(paddle.getY()+paddle.getHeight() >= Main.getScreenHeight())
            {
                paddle.setY(Main.getScreenHeight()-paddle.getHeight()-5);
            }
            if(paddle2.getY() <=0)
            {
                paddle2.setY(5);
            }
            else if(paddle2.getY()+paddle2.getHeight() >= Main.getScreenHeight())
            {
                paddle2.setY(Main.getScreenHeight()-paddle.getHeight()-5);
            }

        }
        else
        {
            repaint();
        }
        if(ballAttatched)
        {
            /*when the point ends (i.e. ball is attatched to paddle again, 
            all ammo is erased from screen*/
            for(int i = 0; i < ammo.size(); i++)
            {
                ammo.remove(0);
            }
            //keeps the ball moving with paddle
            if(leftStart)
                paddle.ballMoveL(ball);
            else
                paddle2.ballMoveR(ball);
        }
    }

    public void keyPressed(KeyEvent e)
    {
        play = true;

        //paddle 1
        if(e.getKeyCode() == KeyEvent.VK_W)
        {
            paddle.setIsOnUp(true);
        }
        else if (e.getKeyCode() == KeyEvent.VK_S)
        {
            paddle.setIsOnDown(true);
        }

        //paddle 2
        if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            paddle2.setIsOnUp(true);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            paddle2.setIsOnDown(true);
        }

        //launches the ball at start
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            ballAttatched = false;
            if(leftStart)
            {
                ball.setDX(4);
                paddle.hit();
            }
            else
            {
                ball.setDX(-4);
                paddle2.hit();
            }
        }

    }

    public void keyTyped(KeyEvent e)
    {

    }

    public void keyReleased(KeyEvent e)
    {
        //this code helps smooth the paddle motion
        if(e.getKeyCode() == KeyEvent.VK_W)
        {
            paddle.setIsOnUp(false);
        }
        else if (e.getKeyCode() == KeyEvent.VK_S)
        {
            paddle.setIsOnDown(false);
        }
        if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            paddle2.setIsOnUp(false);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            paddle2.setIsOnDown(false);
        }
        //checks if player can fire bullet powerup
        if(e.getKeyCode() == KeyEvent.VK_D)
        {

            if(storedBulletsR.size()>=1)
            {
                System.out.println(storedBulletsR.size());

                Bullet b = storedBulletsR.get(0);
                storedBulletsR.remove(0);
                b.fire(paddle);
                firedBullets.add(b);
            }

        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {

            if(storedBulletsL.size()>=1)
            {
                System.out.println(storedBulletsL.size());

                Bullet b = storedBulletsL.get(0);
                storedBulletsL.remove(0);
                b.fire(paddle2);
                firedBullets.add(b);
            }

        }
    }

    public void mousePressed(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();
        System.out.print(x +","+y);
        //location of mouse on click
        if(screen == 1)
        {
            if(x >= 722 && y >= 453 && x <= 1135 && y <= 523)
            {
                screen = 2;
                reset();
            }
            else if (x>= 1145 && x>= 629)
                System.exit(0);
        }
        if(screen == 2 && win)
        {
            screen = 3;
            //prevents user from "skipping" screen 3
            try
            {
                timer.wait(50);
            }
            catch (InterruptedException ie)
            {
                ie.printStackTrace();
            }
        }
        if (screen == 3 && loaded)
        {
            if(x >= 200 && y >= 492 && x <= 529 && y <= 578)
            {
                screen = 2;
                reset();
            }
            else if(x>=783 && y>=493 && x<=1114 && y<=579)
                screen = 1;

        }
        repaint();
    }

    public void mouseClicked(MouseEvent e)
    {

    }

    public void mouseReleased (MouseEvent e){

    }

    public void mouseEntered(MouseEvent e){

    }

    public void mouseExited(MouseEvent e){

    }

    public void drawStoredBullets(Graphics g)
    {
        for(int i = 0; i < storedBulletsL.size(); i++)
        {
            g.drawImage(ammoIMG, 950 + 50*i, 600, null);
        }
        for(int i = 0; i < storedBulletsR.size(); i++)
        {
            g.drawImage(ammoIMG, 200 + 50*i, 600, null);
        }
    }

    public void drawScore(Graphics g)
    {
        if(scoreL == 0)
            g.drawImage(zero, 200,5, null);
        else if (scoreL == 1)
            g.drawImage(one, 200,5, null);
        else if (scoreL == 2)
            g.drawImage(two, 200,5, null);
        else if (scoreL == 3)
            g.drawImage(three, 200,5, null);
        else if (scoreL == 4)
            g.drawImage(four, 200,5, null);
        else if (scoreL == 5)
            g.drawImage(five, 200,5, null);
        else if (scoreL == 6)
            g.drawImage(six, 200,5, null);
        else if (scoreL == 7)
            g.drawImage(seven, 200,5, null);

        if(scoreR == 0)
            g.drawImage(zero, 950,5, null);
        else if (scoreR == 1)
            g.drawImage(one, 950,5, null);
        else if (scoreR == 2)
            g.drawImage(two, 950,5, null);
        else if (scoreR == 3)
            g.drawImage(three, 950,5, null);
        else if (scoreR == 4)
            g.drawImage(four, 950,5, null);
        else if (scoreR == 5)
            g.drawImage(five, 950,5, null);
        else if (scoreR == 6)
            g.drawImage(six, 950,5, null);
        else if (scoreR == 7)
            g.drawImage(seven, 950,5, null);
    }

    public static int getScoreL(){return scoreL;}

    public static int getScoreR(){return scoreR;}

    public static void setWin(boolean x){ win = x;}

    public static void addPointL()
    {
        scoreL++;
        leftStart =true;
    }

    public static void addPointR()
    {
        scoreR++;
        leftStart = false;
    }

    public static boolean ballAttatched(){return ballAttatched;}

    public static void setBallAttatched(boolean b){ballAttatched = b;}

}
