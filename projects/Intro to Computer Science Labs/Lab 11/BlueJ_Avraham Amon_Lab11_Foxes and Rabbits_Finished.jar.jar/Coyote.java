import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a Coyote.
 * Coyotes age, move, eat foxes, and die.
 * 
 * @author Avraham Amon
 * @version 2014.15.12
 */
public class Coyote extends Animal
{
    // Characteristics shared by all coyotes (class variables).

    // The age at which a coyote can start to breed.
    private static final int BREEDING_AGE = 30;
    // The age to which a coyote can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a coyote breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single fox. In effect, this is the
    // number of steps a coyote can go before it has to eat again.
    private static final int FOX_FOOD_VALUE = 60;
    // The coyote's food level, which is increased by eating foxes.
    private int foodLevel;

    /**
     * Create a coyote. A coyote can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the coyote will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Coyote(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
        foodLevel = getRand().nextInt(FOX_FOOD_VALUE);
    }

    /**
     * This is what the coyote does most of the time: it hunts for
     * foxes. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newAnimal A list to return newly born animal.
     */
    public void act(List<Animal> newAnimals)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newAnimals);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Make this fox more hungry. This could result in the coyote's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for foxes adjacent to the current location.
     * Only the first live fox is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = getField().getObjectAt(where);
            if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    fox.setDead();
                    foodLevel = FOX_FOOD_VALUE;
                    // Remove the dead rabbit from the field.
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Returns the Max Age of coyote.
     * @return The coyote's max age
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * Returns the breeding age of the coyote.
     *
     * @return The coyote's breeding age
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * Returns the breeding probablity for coyotes.
     *
     * @return The coyote's breeding probablity
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * Returns the coyote's max litter size.
     *
     * @return The coyote's max litter size
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

    /**
     * Creates a new born animal which in this case is 
     * a coyote.
     *
     * @param field The field currently occupied
     * @param location The location within the field
     * @return The new baby coyote
     */
    public Animal makeYoung(Field field, Location location)
    {
        Animal young = new Coyote(false, field, location);
        return young;
    }
}