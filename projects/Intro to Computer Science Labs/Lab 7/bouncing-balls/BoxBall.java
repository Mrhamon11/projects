import java.awt.*;
import java.awt.geom.*;

/**
 * Class BoxBall - a graphical ball that observes the effect of gravity. The ball
 * has the ability to move. Details of movement are determined by the ball itself. It
 * will fall downwards, accelerating with time due to the effect of gravity, and bounce
 * upward again when hitting the ground. It will also bounce off of walls and the ceiling
 * like a ball would in real life.
 *
 * This movement can be initiated by repeated calls to the "move" method. The "move" method 
 * was updated by Avraham Amon to determine how the ball reacts when coming in contact with
 * the lines that define walls.
 * 
 * @author Michael Kölling (mik)
 * @author David J. Barnes
 * @author Bruce Quig
 * @author Avraham Amon
 *
 * @version 2014.11.09
 */

public class BoxBall
{
    private static final int GRAVITY = 3;  // effect of gravity

    private int ballDegradation = 2;
    private Ellipse2D.Double circle;
    private Color color;
    private int diameter;
    private int xPosition;
    private int yPosition;
    private final int groundPosition;      // y position of ground
    private final int leftWallPosition;
    private final int rightWallPosition;
    private final int ceilingPosition;
    private Canvas canvas;
    private int xSpeed = 2;    
    private int ySpeed = 1;                // initial downward speed

    /**
     * Constructor for objects of class BoxBall
     *
     * @param xPos  the horizontal coordinate of the ball
     * @param yPos  the vertical coordinate of the ball
     * @param ballDiameter  the diameter (in pixels) of the ball
     * @param ballColor  the color of the ball
     * @param groundPos  the position of the ground (where the wall will bounce)
     * @param drawingCanvas  the canvas to draw this ball on
     * @param xSpeed the initial speed in which the ball travels
     */
    public BoxBall(int xPos, int yPos, int ballDiameter, Color ballColor,
    int groundPos, int leftPos, int rightPos, int ceilingPos, Canvas drawingCanvas, int xSpeed)
    {
        xPosition = xPos;
        yPosition = yPos;
        color = ballColor;
        diameter = ballDiameter;
        groundPosition = groundPos;
        leftWallPosition = leftPos;
        rightWallPosition = rightPos;
        ceilingPosition = ceilingPos;
        canvas = drawingCanvas;
        this.xSpeed = xSpeed;
    }

    /**
     * Draw this ball at its current position onto the canvas.
     **/
    public void draw()
    {
        canvas.setForegroundColor(color);
        canvas.fillCircle(xPosition, yPosition, diameter);
    }

    /**
     * Erase this ball at its current position.
     **/
    public void erase()
    {
        canvas.eraseCircle(xPosition, yPosition, diameter);
    }    

    /**
     * Move this ball according to its position and speed and redraw.
     **/
    public void move()
    {
        // remove from canvas at the current position
        erase();
        // compute new position
        ySpeed += GRAVITY;
        yPosition += ySpeed;
        xPosition += xSpeed;

        // check if it has hit the ground
        if(yPosition >= (groundPosition - diameter) && ySpeed > 0) {
            yPosition = (int)(groundPosition - diameter);
            ySpeed = -ySpeed + ballDegradation; 
        }
        // check if it has hit the left wall
        if(xPosition <= (leftWallPosition) && xSpeed < 0) {
            xPosition = (int)(leftWallPosition + 1);
            xSpeed = xSpeed*(-1) - ballDegradation;
        }
        // check if it has hit the ceiling
        if(yPosition <= (ceilingPosition) && ySpeed < 0) {
            yPosition = (int)(ceilingPosition + 1);
            ySpeed = ySpeed*(-1) + ballDegradation;
        }
        // check if it has hit the right wall
        if(xPosition >= (rightWallPosition - diameter) && xSpeed > 0) {
            xPosition = (int)(rightWallPosition - diameter);
            xSpeed = -xSpeed + ballDegradation;
        }

        // draw again at new position
        draw();

    }

    /**
     * return the horizontal position of this ball
     */
    public int getXPosition()
    {
        return xPosition;
    }

    /**
     * return the vertical position of this ball
     */
    public int getYPosition()
    {
        return yPosition;
    }
}