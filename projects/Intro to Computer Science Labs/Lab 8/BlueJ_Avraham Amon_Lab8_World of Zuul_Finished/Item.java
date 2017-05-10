
/**
 * This class holds the information regarding items that will be found in the rooms
 * in the game itself.
 * 
 * @author Avraham Amon 
 * @version 11/17/2014
 */
public class Item
{
    private String name;
    private String description;
    private double weight;
    
    /**
     * Constructs the objects that holds the information of the items.
     *
     * @param name The name of the item
     * @param description A description of what the item does
     * @param weight The weight of the item
     */
    public Item(String name, String description, double weight)
    {
        // initialise instance variables
        this.name = name;
        this.description = description;
        this.weight = weight;
    }
    
    /**
     * Returns the name of the item.
     *
     * @return The name of the item
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns the description of the item.
     * 
     * @return The item's description
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Returns the weight of the item.
     *
     * @return The item's weight
     */
    public double getWeight()
    {
        return weight;
    }
}
