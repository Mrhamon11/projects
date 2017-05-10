import java.util.List;

/**
 * Class Player - a player in an adventure game.
 * 
 * This class is part of the "World of Zuul" game which is a 
 * text based adventure game.
 * 
 * A "Player" represents a specific character in the game. It is directly
 * connected to the current room and will move when the room in the game changes.
 * Each character also carries a bag that will allow a character to pick up 
 * items and drop them.
 * 
 * @author Avraham Amon
 * @version 11/23/14
 */
public class Player extends Carrier
{
    
    /**
     * Creates a player and place the new character in the current room. The 
     * player will also have a bag that will allow the player to carry items 
     * found in rooms throughout the game, and drop them when desired. The bag
     * has a weight limit to how much can be carried.
     *
     * @param initialRoom The Player's initial room
     */
    public Player(Room initialRoom)
    {
        super(initialRoom);
    }

    /**
     * This method will return the long description of the
     * room that the player is currently in. It will also 
     * tell the player if any other actors are in the room.
     *
     * @return The string will all of the information
     */
    public String look()
    {
        String temp = getRoom().getLongDescription();
        List<Actor> actorList = getActors();
        for(Actor actor : actorList) {
            if(getRoom() == actor.getRoom() && this != actor) {
                if(actor.getClass().getSimpleName().equals("Professor")) {
                    temp += "\n" + actor.toString();
                }
                else {
                    temp += "\n" + "A " + actor.getClass().getSimpleName() + " is in the room.";
                }
            }
        }
        return temp;
    }
}
