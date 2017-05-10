import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class BallDemo - a short demonstration showing animation with the 
 * Canvas class that shows how balls react in multiple scenarios. Class
 * was updated by Avraham Amon in latest version.
 *
 * @author Michael Kölling and David J. Barnes
 * @author Avraham Amon
 * @version 2014.11.09
 */

public class BallDemo   
{
    private Canvas myCanvas;
    private Random random;

    /**
     * Create a BallDemo object. Creates a fresh canvas and makes it visible.
     */
    public BallDemo()
    {
        myCanvas = new Canvas("Ball Demo", 600, 500);
        random = new Random();
    }

    /**
     * Starts the simulation. Uses a parameter to determine how many balls are created
     * and stores them in an ArrayList. Then it will display the balls on the canvas
     * and start an animation using the constants and variables defined in other classes.
     * All balls will appear on the top row, together in a row.
     *
     * @param numBalls The number of balls that will be created
     */
    public void bounce(int numBalls)
    {
        ArrayList<BouncingBall> multiBalls = new ArrayList<BouncingBall>();

        int ground = 400;   // position of the ground line

        myCanvas.setVisible(true);

        // draw the ground
        myCanvas.drawLine(50, ground, 550, ground);
        
        // Adds number of balls to ArrayList by iterating through numballs
        for(int i = 0; i < numBalls; i++) {
            int spacing = random.nextInt(501) + 50;
            BouncingBall ball = new BouncingBall(spacing, 50, 16, Color.BLUE, ground, myCanvas);
            ball.draw();
            multiBalls.add(ball);
        }

        // make them bounce
        boolean finished =  false;
        while(!finished) {
            // small delay
                myCanvas.wait(50);
                finished = true;
            for (int i = 0; i < numBalls; i++) {
                BouncingBall ball = multiBalls.get(i);
                                    ball.move();
                // stop once ball has travelled a certain distance on x axis
                if(ball.getXPosition() <= 550) {
                    finished = false;
                }
            }
        }
    }
    
    /**
     * Starts the simulation by using a parameter to determine the number of balls created.
     * It will generate the balls randomly in the left quarter of the canvas.
     *
     * @param numBalls The number of balls created
     */
    public void randomBounce(int numBalls)
    {
        ArrayList<BouncingBall> multiBalls = new ArrayList<BouncingBall>();

        int ground = 400;   // position of the ground line

        myCanvas.setVisible(true);

        // draw the ground
        myCanvas.drawLine(50, ground, 550, ground);
        
                // crate and show the balls
        for(int i = 0; i < numBalls; i++) {
            int xSpacing = random.nextInt(226) + 50;
            int ySpacing = random.nextInt(200);
            BouncingBall ball = new BouncingBall(xSpacing, ySpacing, 16, Color.BLUE, ground, myCanvas);
            ball.draw();
            multiBalls.add(ball);
        }

        // make them bounce
        boolean finished =  false;
        while(!finished) {
            // small delay
                myCanvas.wait(50);
                finished = true;
            for (int i = 0; i < numBalls; i++) {
                BouncingBall ball = multiBalls.get(i);
                                ball.move();
                // stop once ball has travelled a certain distance on x axis
                if(ball.getXPosition() <= 550) {
                    finished = false;
                }
            }
        }
    }
    
    /**
     * Generates a random color from a list that is then assigned to a ball.
     *
     * @return The random color
     */
    private Color randomBallColor()
    {
        int i = random.nextInt(4);
        if(i == 0) {
            return Color.BLUE;
        }
        else if(i == 1) {
            return Color.RED;
        }
        else if(i == 2) {
            return Color.BLACK;
        }
        else {
            return Color.MAGENTA;
        }
    }
    
    /**
     * Generates a random color from a list that is then assigned to 
     * the background color.
     *
     * @return The random color
     */
    private Color randomBackgroundColor()
    {
        int i = random.nextInt(4);
        if(i == 0) {
            return Color.ORANGE;
        }
        else if(i == 1) {
            return Color.YELLOW;
        }
        else if(i == 2) {
            return Color.GREEN;
        }
        else {
            return Color.GRAY;
        }
    }
    
    /**
     * Starts the simulation by using a parameter to determine the number of balls generated. It 
     * generates a visible box of a random color, that acts as the boundaries for the balls to 
     * bounce in. The balls will also be generated in a random color. Balls can be generated anywhere
     * in the box.
     *
     * @param numBalls The number of balls generated
     */
    public void boxBounce(int numBalls)
    {
        ArrayList<BoxBall> multiBalls = new ArrayList<BoxBall>();
        
        myCanvas = new Canvas("Colored", 600, 600, randomBackgroundColor());

        int ground = 400;   // position of the ground line

        myCanvas.setVisible(true);

        // draw the ground
        myCanvas.drawLine(50, ground, 550, ground);
        // draw the left wall
        myCanvas.drawLine(50, 50, 50, 400);
        // draw the right wall
        myCanvas.drawLine(550, 50, 550, 400);
        // draw the ceiling 
        myCanvas.drawLine(50, 50, 550, 50);
        
                // crate and show the balls
        for(int i = 0; i < numBalls; i++) {
            int xSpacing = random.nextInt(501) + 50;
            int ySpacing = random.nextInt(351) + 50;
            int ranSpeed = random.nextInt(35) + 15;
            BoxBall ball = new BoxBall(xSpacing, ySpacing, 16, randomBallColor(), ground, 50, 550,
                           50, myCanvas, ranSpeed);
            ball.draw();
            multiBalls.add(ball);
        }

        // make them bounce
        boolean finished =  false;
        while(!finished) {
            // small delay
                myCanvas.wait(50);
            for (int i = 0; i < numBalls; i++) {
                BoxBall ball = multiBalls.get(i);
                                ball.move();
                // stop once ball has travelled a certain distance on x axis
                if(ball.getXPosition() >= 550) {
                    finished = true;
                }
            }
        }
    }
}
