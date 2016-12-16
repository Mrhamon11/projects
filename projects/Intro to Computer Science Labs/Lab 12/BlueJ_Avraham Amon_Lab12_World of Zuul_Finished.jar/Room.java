import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashSet<Item> items;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        items = new HashSet<Item>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
         return "You are " + description + ".\n" + getExitString() + "\n" + getItemString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north, west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    /**
     * Adds new item to the room.
     *
     * @param newItem The new item
     */
    public void addItem(Item newItem)
    {
        items.add(newItem);
    }

    /**
     * Returns all items that are currently in a specific room.
     *
     * @return Every item in the room
     */
    public String getItemString()
    {
        String returnString = "Items: \n";
        int i = 0;
        if (items.isEmpty()) {
            return "There are no items here";
        }
        for(Item item : items) {
            returnString += item.getName() + ": " + item.getDescription() + ".";
            if (i != items.size() - 1) {
                returnString += "\n";
            }
            i++;
        }
        return returnString;
    }

    /**
     * Removes a specific item from the current room.
     *
     * @param item The item to be removed
     * @return The item that was removed, or null if the item was not found
     */
    public Item removeItem(Item item)
    {
        if(items.contains(item)) {
            items.remove(item);
            return item;
        }
        else {
            return null;
        }
    }

    /**
     * Searches the room for the desired item and returns it if the item is
     * found in the room.
     *
     * @param itemName The item we are searching for
     * @return The item if found, and null if not
     */
    public Item searchItems(String itemName)
    {
        for(Item item : items) {
            if(item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Creates an Arraylist of strings for all of the availiable
     * exits found in any particular room.
     *
     * @return The ArrayList of all of the exits
     */
    public ArrayList<String> getExitList()
    {
        ArrayList<String> exitList = new ArrayList<String>();
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            exitList.add(exit);
        }
        return exitList;
    }
    
    /**
     * Creates a HashSet of all of the items in a particular 
     * room.
     *
     * @return All of the items in the room
     */
    protected HashSet<Item> allRoomItems()
    {
        if(!(items.isEmpty())) {
            return items;
        }
        return null;
    }
}

