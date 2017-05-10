

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
    private Game game1;
    private CommandWords commandW1;
    private Item item1;
    private Item item2;
    private Item item3;
    private Room room1;

    
    
    
    
    

    
    

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
        game1 = new Game();
        commandW1 = new CommandWords();
        commandW1.isCommand("look");
        item1 = new Item("Book", "Holds spells", 2.0);
        item1.getDescription();
        item1.getName();
        item1.getWeight();
        item2 = new Item("Potion", "Heals health", 1.2);
        item2.getDescription();
        item2.getName();
        item2.getWeight();
        item3 = new Item("Flight Boots", "Wearer gains the ability to fly", 3.4);
        item3.getDescription();
        item3.getName();
        item3.getWeight();
        room1 = new Room("Classroom");
        room1.addItem(item1);
        room1.addItem(item3);
        room1.addItem(item2);
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
