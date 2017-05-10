import java.util.Random;
import java.util.ArrayList;

/**
 * The Professor class is a subclass of Player. This subclass is 
 * another player that travels throughout the game field, but 
 * of moving by the user's command, it will move in a random direction
 * and face a random direction based on a dice roll. 
 * 
 * @author Avraham Amon
 * @version 12/8/14
 */
public class Professor extends Player
{
    private Random random;
    private String pointingDirection;

    /**
     *  Constructs all fields from Player class, creates a new Random
     *  object that will be used for the professor to move around, and
     *  has the professor point in a random direction;
     */
    public Professor(Room initialRoom)
    {
        super(initialRoom);
        random = new Random();
        point();
    }
    
    /**
     * Simulates a dice roll
     *
     * @return The return value of the dice roll
     */
    private int throwDice()
    {
        int index = random.nextInt(6) + 1;
        return index;
    }
    
    /**
     * Will move the professsor into a random room, or not move 
     * at all, and point the professor in a random direction.
     *
     */
    public void act()
    {
        if (throwDice() == 6) {
            ArrayList<String> temp = getRoom().getExitList();
            int index = random.nextInt(temp.size());
            String direction = temp.get(index);
            goRoom(direction);
            point();
        }
    }
    
    /**
     * Points the professor in a random direction.
     *
     */
    private void point()
    {
        int index = random.nextInt(4);
        if (index == 0) {
            pointingDirection = "north";
        }
        else if (index == 1) {
            pointingDirection = "east";
        }
        else if (index == 2) {
            pointingDirection = "south";
        }
        else {
            pointingDirection = "west";
        }
    }
    
    /**
     * The message the player will see if he encounters the 
     * professor in the same room. Will also show the direction
     * the professor is facing.
     *
     * @return A string informing the player that the professor is 
     *         in the same room as the player
     */
    public String toString()
    {
        return "There is a professor here. The professor is pointing \n" +
               pointingDirection + ".";
    }
    
}
