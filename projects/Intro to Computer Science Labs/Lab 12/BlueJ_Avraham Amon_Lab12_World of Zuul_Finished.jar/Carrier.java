import java.util.HashSet;

/**
 * A "Carrier" will have many characteristic that will be created from 
 * the subclasses. This class will keep track of all the information for
 * all character's items, and contains the methods for the game to implement
 * these items.
 *  
 * @author Avraham Amon
 * @version 12/28/14
 */
public abstract class Carrier extends Actor
{
    private HashSet<Item> bag;
    private int weight;
    private int maxWeight;
        
    /**
     * Constructs a character that has the ability to carry 
     * objects in a bag. The bag has restrictions on how
     * much weight it can hold.
     *
     * @param initialRoom The initial room the carrier is placed
     */
    protected Carrier(Room initialRoom)
    {
        super(initialRoom);
        bag = new HashSet<Item>();
        weight = 0;
        maxWeight = 100;
    }

    /**
     * Allows the user to add an item to the bag HashSet.
     *
     * @param item The desired item
     */
    protected void addToBag(Item item)
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
    protected Item removeFromBag(Item item)
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
    protected String getInventory()
    {
        String returnString = "You are carrying: \n";
        if (bag.isEmpty()) {
            return "You are not carrying anything";
        }
        for(Item item : bag) {
            returnString += item.getName() + ": " + item.getDescription() + ".\n";
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
    protected Item searchBag(String itemName)
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
    protected String takeItem(String itemName)
    {
        // Check if item is in room.
        Item item = getRoom().searchItems(itemName);
        if (item == null) {
            return "There is no " + itemName + " here!";
        }
        else {
            if(item.getWeight() + weight <= maxWeight) {
                bag.add(item);
                getRoom().removeItem(item);
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
    protected String dropItem(String itemName)
    {
        // Check if item is in bag.
        Item item = searchBag(itemName);
        if(item == null) {
            return "You are not carrying a " + itemName + ".";
        }
        else {
            bag.remove(item);
            getRoom().addItem(item);
            weight -= item.getWeight();
            return itemName + " dropped.";
        }
    }

    /**
     * Displays all items that are currently in the bag.
     *
     * @return All of the items in the bag, or a string if the bag is empty.
     */
    protected String inventory()
    {
        // Check if bag is empty.
        if(bag.isEmpty()) {
            return "You are not carrying anything!";
        }
        else {
            return getInventory();
        }
    }

    /**
     * Allows user to eat item in the parameter and gain an
     * upgrade to maximum weight allowed to be carried. This method
     * first checks if the item is in the bag, then makes sure it is a 
     * cookie, and then gives the user the special ability.
     *
     * @param itemName The item
     * @return Mulitple strings depending on which conditions are met.
     */
    protected String eatItem(String itemName)
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

    /**
     * Returns a HashSet of all items in the bag.
     *
     * @return All of the items currently in the bag
     */
    protected HashSet<Item> getBagItems()
    {
        if(!(bag.isEmpty())) {
            return bag;
        }
        return null;
    }
}
