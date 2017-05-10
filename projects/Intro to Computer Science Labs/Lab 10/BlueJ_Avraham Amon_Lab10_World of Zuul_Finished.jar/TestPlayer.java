

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestPlayer.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TestPlayer
{
    private Room room1;
    private Room room2;
    private Item item1;
    private Item item2;
    private Player player1;

    
    
    
    
    

    /**
     * Default constructor for test class TestPlayer
     */
    public TestPlayer()
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
        room1 = new Room("in room 1");
        room2 = new Room("in room 2");
        room1.setExit("north", room2);
        room2.setExit("south", room1);
        room1.getExit("north");
        item1 = new Item("Gold", "A sack of Gold", 5.3);
        item2 = new Item("Potato", "A sack of Potatoes", 6.3);
        room1.addItem(item1);
        room2.addItem(item2);
        player1 = new Player(room1);
        player1.addToBag(item1);
        player1.addToBag(item2);
        player1.getInventory();
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
