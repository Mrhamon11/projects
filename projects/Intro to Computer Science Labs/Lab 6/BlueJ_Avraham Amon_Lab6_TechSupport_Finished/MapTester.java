import java.util.HashMap;

/**
 * This class simulates a Phonebook and will allow one to 
 * create a new database that will store names, and their 
 * corresponding numbers.
 * 
 * @author Avraham Amon 
 * @version 1.0 (2014.11.2)
 */
public class MapTester
{
    private HashMap<String, String> phonebook;
    /**
     * Create a Phonebook
     */
    public MapTester()
    {
        phonebook = new HashMap<String, String>();
    }

    /**
     * Enters name and number into the phonebook.
     *
     * @param name The name of the person.
     * @param number The number of the person.
     */
    public void enterNumber(String name, String number)
    {
        phonebook.put(name, number);
    }
    
    /**
     * Looks up number of person entered.
     *
     * @param name Name of person.
     * @return Returns the person's number.
     */
    public String lookUpNumber(String name)
    {
        String number = phonebook.get(name);
        return number;
    }   
}
