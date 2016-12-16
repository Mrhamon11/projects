

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class Tester.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class Tester
{
    private Canvas canvas1;
    private BoxBall boxBall1;
    private BoxBall boxBall2;

    
    

    /**
     * Default constructor for test class Tester
     */
    public Tester()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        canvas1 = new Canvas("demo", 100, 100);
        boxBall1 = new BoxBall(1, 1, 20, java.awt.Color.BLUE, 100, 1, 100, 1, canvas1, 20);
        boxBall2 = new BoxBall(75, 0, 20, java.awt.Color.BLUE, 100, 0, 100, 0, canvas1, 20);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
}
