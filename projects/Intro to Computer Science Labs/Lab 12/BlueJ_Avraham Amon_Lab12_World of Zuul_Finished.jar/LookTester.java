

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class LookTester.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class LookTester
{
    private Room room1;
    private Player player1;
    private Thief thief1;
    
    /**
     * Default constructor for test class LookTester
     */
    public LookTester()
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
        room1 = new Room("in a test room");
        player1 = new Player(room1);
        thief1 = new Thief(room1);
        
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
