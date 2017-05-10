import java.util.Random;

/**
 * This class can implement many methods to generate random
 * numbers in many different situations.
 * 
 * It generates a new random object from the constructor, and 
 * the methods will use this object to create random numbers 
 * depending on the situation
 * 
 * @author Avraham Amon
 * @version 1.0 (2014.11.2)
 */
public class RandomTester
{
    private Random random;
        
    /**
     * Constructor for objects of class RandomTester
     */
    public RandomTester()
    {
        random = new Random();
              
    }

    /**
     * Prints out a random number using the Random class
     */
    public void printOneRandom()
    {
        int index = random.nextInt();
        System.out.println(index);
        String[] responses = {"yes", "no", "maybe"};
    }
    
    /**
     * Prints out multiple random numbers using the random class
     * @param howMany The number of random numbers desired
     */
    public void printMultiRandom(int howMany)
    {
        for (int i = 0; i < howMany; i++) {
            int index = random.nextInt();
            System.out.println(index);
        }
    }
    
    /**
     * Simulates a dice roll
     *
     * @return The return value of the dice roll
     */
    public int throwDice()
    {
        int index = random.nextInt(6) + 1;
        return index;
    }
    
    /**
     * Will randomly return  either "yes", "no",
     * or "maybe"
     * @return Either "yes", "no", or "maybe"
     */
    public String getResponse()
    {
        int index = random.nextInt(3);
        if (index == 0) {
            return "yes";
        }
        else if (index == 1) {
            return "no";
        }
        else if (index == 2) {
            return "maybe";
        }
        else {
            return "Error";
        }
    }        
    
    /**
     * Generates a random number based on the length of 
     * of a String array by using the parameter's values
     * and returns the value of the string that is associated
     * with the index of that randomly generated number.
     *
     * @param responses Values for the String Array.
     * @return The random value from the String Array.
     */
    public String getResponse2(String[] responses)
    {
        int index = random.nextInt(responses.length);
        return responses[index];
    }
    
    /**
     * Generates a random number between 1 and max.
     *
     * @param max The limit to where the random number can
     * generate.
     * @return The random number generated.
     */
    public int nextIntOne(int max)
    {
        int index = random.nextInt(max) + 1;
        return index;
    }
    
    /**
     * Generates a random number between two values
     * inclusively.
     *
     * @param max The upper limit that can be generated.
     * @param min The lower limit that can be generated.
     * @return The randomly generated number within the limit
     */
    public int nextFromRange(int max, int min)
    {
        int index = max - min + 1;
        int i = random.nextInt(index);
        index = i + min;
        return index;
    }
}
