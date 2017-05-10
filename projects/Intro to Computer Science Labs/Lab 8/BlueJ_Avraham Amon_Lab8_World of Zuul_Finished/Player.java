import java.util.HashSet;

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
public class Player
{
    private Room currentRoom;
    private HashSet<Item> bag;
    private int weight;
    private int maxWeight;

    /**
     * Creates a player and place the new character in the current room. The 
     * player will also have a bag that will allow the player to carry items 
     * found in rooms throughout the game, and drop them when desired. The bag
     * has a weight limit to how much can be carried.
     */
    public Player(Room initialRoom)
    {
        currentRoom = initialRoom;
        bag = new HashSet<Item>();
        weight = 0;
        maxWeight = 100;
    }

    /**
     * Returns the current room that the player is in.
     *
     * @return The current room
     */
    public Room getRoom()
    {
        return currentRoom;
    }

    /**
     * This method will allow the user to choose the room that the 
     * character appears in.
     *
     * @param newRoom The new room
     */
    public void setRoom(Room newRoom)
    {
        currentRoom = newRoom;
    }

    /**
     * This method will return the long description of the
     * room that the player is currently in.
     *
     */
    public String look()
    {
        return currentRoom.getLongDescription();
    }

    /**
     * Allows the player to move to a room by defining a specifc direction
     * in which to move. If there is no exit in the provided direction, this 
     * method will inform the player by returnin a string.
     *
     * @param direction The direction which the player wants to move
     * @return A string that is returned telling the user that the specific 
     *         intended direction is not allowed
     */
    public String goRoom(String direction)
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
     * Allows the user to add an item to the bag HashSet.
     *
     * @param item The desired item
     */
    public void addToBag(Item item)
    {
        bag.add(item);
    }

    /**
     * Allows the user to remove a specific item from the bag.
     *
     * @param item The item that the player wishes to discard
     * @return Either returns the item that was dropped, or null if the item was
     *         not found in the bag.
     */
    public Item removeFromBag(Item item)
    {
        if(bag.contains(item)) {
            bag.remove(item);
            return item;
        }
        else {
            return null;
        }
    }

    /**
     * Returns all items that are currently in the player's inventory.
     *
     * @return Every item in the bag
     */
    public String getInventory()
    {
        String returnString = "You are carrying: \n";
        int i = 0;
        if (bag.isEmpty()) {
            return "You are not carrying anything";
        }
        for(Item item : bag) {
            returnString += item.getName() + ": " + item.getDescription() + ".\n";
            i++;
        }
        returnString += "Total weight of all items in bag: " + weight;
        return returnString;
    }

    /**
     * Searches the bag for a specific item provided by a parameter.
     *
     * @param itemName The desired item
     * @return The item if it is in the bag, or null if the item was not found
     */
    public Item searchBag(String itemName)
    {
        for(Item item : bag) {
            if(item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Allows the user to pick up an item from a specific room, and put it in 
     * the player's bag assuming the item is not too heavy. The item will then 
     * be removed from the room that it was found in.
     *
     * @param itemName The desired item
     * @return A String saying that the item that was taken from the room, or
     *         a string if the item was not found.
     */
    public String takeItem(String itemName)
    {
        // Check if item is in room.
        Item item = currentRoom.searchItems(itemName);
        if (item == null) {
            return "There is no " + itemName + " here!";
        }
        else {
            if(item.getWeight() + weight <= maxWeight) {
                bag.add(item);
                currentRoom.removeItem(item);
                weight += item.getWeight();
                return itemName + " taken.";
            }
            else {
                return "You are trying to carry too much!";
            }
        }
    }

    /**
     * Allows the user to drop an item from the player's bag, and places in them
     * in the current room. This method will only drop the item if the item is 
     * actually in the bag.
     *
     * @param itemName The item that is to be discarded.
     * @return A string saying that the item was dropped, or a message saying 
     *         that the item was not found in the bag.
     */
    public String dropItem(String itemName)
    {
        // Check if item is in bag.
        Item item = searchBag(itemName);
        if(item == null) {
            return "You are not carrying a " + itemName + ".";
        }
        else {
            bag.remove(item);
            currentRoom.addItem(item);
            weight -= item.getWeight();
            return itemName + " dropped.";
        }
    }

    /**
     * Displays all items that are currently in the bag.
     *
     * @return All of the items in the bag, or a string if the bag is empty.
     */
    public String inventory()
    {
        // Check if bag is empty.
        if(bag.isEmpty()) {
            return "You are not carrying anything!";
        }
        else {
            return getInventory();
        }
    }
    
    public String eatItem(String itemName)
    {
        // Check if the cookie is in the bag.
        Item item = searchBag(itemName);
        if(item == null) {
            return "You are not carrying a " + itemName + "!";
        }
        else if(!(item.getName().equals("Cookie"))) {
            return "You can't eat that!";
        }
        else {
            bag.remove(item);
            weight -= item.getWeight();
            maxWeight += 50;
            return "You ate the " + itemName + " and can now your max weight\n" + 
                    "limit is " + maxWeight + "!";
        }
    }
}
