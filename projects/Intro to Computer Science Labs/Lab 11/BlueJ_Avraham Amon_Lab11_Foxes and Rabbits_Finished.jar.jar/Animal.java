import java.util.Random;
import java.util.List;

/**
 * Superclass for all animals in the simulation. 
 * This class contains all of the common traits of each
 * animal, such as their age, and contains methods for returning 
 * the values of each animal's fields.
 * 
 * @author Avraham Amon
 * @version 2014.15.12
 */
public abstract class Animal
{
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).

    // The Animal's age.
    private int age;
    // Whether the Animal is alive or not.
    private boolean alive;
    // The Animal's position.
    private Location location;
    // The field occupied.
    private Field field;
    
    /**
     * Constructs an animal of any type, from any subclass.
     *
     * @param randomAge A random age for each animal when it is generated
     * @param field The field currently occupied
     * @param location The location within the field
     */
    public Animal(boolean randomAge, Field field, Location location)
    {
        age = 0;
        alive = true;
        this.field = field;
        setLocation(location);
        if(randomAge) {
            setAge(rand.nextInt(getMaxAge()));
        }
    }
    
    /**
     * This method shows the movement of Animals around the field.
     *
     * @param newAnimals A list of Animals
     */
    abstract protected void act(List<Animal> newAnimals);
    
    /**
     * Check whether the rabbit is alive or not.
     * @return true if the rabbit is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    
    /**
     * Indicate that the rabbit is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    /**
     * Return the rabbit's location.
     * @return The rabbit's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the rabbit at the new location in the given field.
     * @param newLocation The rabbit's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Returns current field of Animal.
     *
     * @return The field of the Animal
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Returns current Animal age.
     *
     * @return The age of the Animal
     */
    protected int getAge()
    {
        return age;
    }
    
    /**
     * Sets the age of the animal.
     *
     * @param newAge The new age desired
     */
    protected void setAge(int newAge)
    {
        age = newAge;
    }
    
    /**
     * Returns the max age of an animal.
     *
     * @return The max age of the animal
     */
    protected abstract int getMaxAge();
    
    /**
     * An animal can breed if it has reached the breeding age.
     * @return true if the animal can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return age >= getBreedingAge();
    }
    
    /**
     * Return the breeding age of this animal.
     * @return The breeding age of this animal.
     */
    abstract protected int getBreedingAge();
    
    /**
     * Returns the random object.
     *
     * @return The random object
     */
    protected Random getRand()
    {
        return rand;
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
    
    /**
     * Returns the breeding probablity of an animal.
     *
     * @return The breeding probablity of the animal
     */
    abstract protected double getBreedingProbability();
    
    /**
     * Returns the max litter size of an animal.
     *
     * @return The max litter size of the animal
     */
    abstract protected int getMaxLitterSize();
    
    /**
     * Creates a new born animal.
     *
     * @param field The field currently occupied
     * @param location The location within the field
     * @return The new baby animal
     */
    abstract protected Animal makeYoung(Field field, Location location);
    
    /**
     * Check whether or not this Animal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newAnimal A list to return newly born rabbits.
     */
    protected void giveBirth(List<Animal> newAnimals)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        List<Location> free = field.getFreeAdjacentLocations(location);
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Animal young = makeYoung(getField(), loc);
            newAnimals.add(young);
        }
    }
}
