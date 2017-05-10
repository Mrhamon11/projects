import java.util.Random;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Write a description of class Theif here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Theif extends Player
{
    private Random random;

    /**
     * Constructs all fields from Player class, creates a new Random 
     * object that will be used for moving around, stealing, and dropping
     * items.
     */
    public Theif(Room initialRoom)
    {
        super(initialRoom);
        random = new Random();
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
     * The theif will either steal an item from it's current room,
     * drop a random item in it's current room, or move to a different
     * room all based on a dice roll.
     *
     */
    public void act()
    {
        if(getRoom().allRoomItems() != null) {
            if(throwDice() == 5 || throwDice() == 6) {
                ArrayList<Item> temp;
                temp = new ArrayList<Item>(getRoom().allRoomItems());
                int index = random.nextInt(temp.size());
                Item item = temp.get(index);
                addToBag(item);
                getRoom().removeItem(item);
            }
            if(throwDice() == 1) {
                if(getBagItems() != null) {
                    ArrayList<Item> temp = new ArrayList<Item>(getBagItems());
                    int index = random.nextInt(temp.size());
                    Item item = temp.get(index);
                    getBagItems().remove(item);
                    getRoom().addItem(item);
                }
            }
            if(throwDice() == 3) {
                ArrayList<String> temp = getRoom().getExitList();
                int index = random.nextInt(temp.size());
                String direction = temp.get(index);
                goRoom(direction);
            }
        }
    }
}
