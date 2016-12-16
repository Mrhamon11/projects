import java.util.List;
import java.util.ArrayList;

/**
 * Class Actor - Superclass for all possible players in an adventure
 * game.
 * 
 * An "Actor" will have many characteristic that will be created from 
 * the subclasses. This class will keep track of all the information for
 * all character's current room, as well as keep track of every character
 * that has been created in an ArrayList.
 * 
 * @author Avraham Amon
 * @version 12/28/14
 */
public abstract class Actor
{
    private Room currentRoom;
    private static List<Actor> actors = new ArrayList<Actor>();
    
    /**
     * Actor Constructor
     *
     * @param initialRoom A parameter
     */
    protected Actor(Room initialRoom)
    {
        currentRoom = initialRoom;
        actors.add(this);
    }

     /**
     * Returns the current room that the actor is in.
     *
     * @return The current room
     */
    protected Room getRoom()
    {
        return currentRoom;
    }
    
    /**
     * This method will allow the user to choose the room that the 
     * actor appears in.
     *
     * @param newRoom The new room
     */
    protected void setRoom(Room newRoom)
    {
        currentRoom = newRoom;
    }
    
    /**
     * Allows the actor to move to a room by defining a specifc direction
     * in which to move. If there is no exit in the provided direction, this 
     * method will inform the player by returnin a string.
     *
     * @param direction The direction which the actor wants to move
     * @return A string that is returned telling the user that the specific 
     *         intended direction is not allowed
     */
    protected String goRoom(String direction)
    {
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) {
            return "There is no door!";
        }
        else {
            currentRoom = nextRoom;
            return currentRoom.getLongDescription();
        }
    }
    
    /**
     * A static method that returns all of the 
     * actors in the "actors" field.
     *
     * @return The list of actors that have been created
     */
    protected static List<Actor> getActors()
    {
        return actors;
    }
}
