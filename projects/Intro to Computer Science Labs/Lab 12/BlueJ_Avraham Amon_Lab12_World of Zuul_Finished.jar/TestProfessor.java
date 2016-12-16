

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestProfessor.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TestProfessor
{
    private Room room1;
    private Room room2;
    private Professor professo1;

    /**
     * Default constructor for test class TestProfessor
     */
    public TestProfessor()
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
        room1 = new Room("room 1");
        room2 = new Room("room 2");
        professo1 = new Professor(room1);
        room1.setExit("north", room2);
        room1.setExit("south", room2);
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
