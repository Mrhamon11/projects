import java.io.*;
import java.util.HashSet;

/**
 * Write a description of class Lexicon here.
 * 
 * @author Van Kelly 
 * @version 0.1
 */
public class Lexicon
{
    // instance variables - replace the example below with your own
    private HashSet<String> wordlist;

    /**
     * Constructor for objects of class Lexicon
     */
    public Lexicon()
    {
        // initialise instance variables
        wordlist = new HashSet<String>();
        readWords();

    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    private void readWords()
    {
        try { 
            BufferedReader in = new BufferedReader(new FileReader("wordlist.txt")); 
            String str; 
            while ((str = in.readLine()) != null) { 
                if (str.length() > 2 && str.length() < 8) {
                    wordlist.add(str.trim().toUpperCase()); 
                }
            } 
            in.close();
        } 
        catch (IOException e) { 
            // begin lab exception handling code here

            // end lab exception handling code here
        } 
        catch (Exception e) { 
            // begin lab exception handling code here

            // end lab exception handling code here
        } 

    }

    public boolean isValidEnglishWord(String word)
    {
        return wordlist.contains(word.toUpperCase());
    }
}
