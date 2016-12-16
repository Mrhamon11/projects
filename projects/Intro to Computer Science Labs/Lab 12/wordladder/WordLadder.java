import java.util.ArrayList;
/**
 * Write a description of class WordLadder here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WordLadder
{
    // instance variables - replace the example below with your own
    private Lexicon lex;
    private ArrayList<String> ladder;
    private InputReader reader;

    public static final int ERR_NOERROR = 0;
    public static final int ERR_DUPLICATE = -1;
    public static final int ERR_INVALID_WORD = -2;
    public static final int ERR_TOO_MANY_CHANGES = -3;
    public static final int ERR_WRONG_LENGTH = -4;

    private int lastErrorCode;

    /**
     * Constructor for objects of class WordLadder
     */
    public WordLadder()
    {
        lex = new Lexicon();
        reader = new InputReader();
        ladder = new ArrayList<String>();
    }
    
    /**
     * Alternate Constructor using a pre-existing Lexicon object
     * This is only used in unit tests to speed things up.
     */
    public WordLadder(Lexicon lex)
    {
        this.lex = lex;
        reader = new InputReader();
        ladder = new ArrayList<String>();
    }

    /**
     * get numeric value of last error code
     */
    public int getLastErrorCode() {
        return lastErrorCode;
    }
    
    /** 
     * get the most recently entered word in the word ladder
     */
    private String getPreviousWord()
    {
        return ladder.get(ladder.size() - 1);
    }

    /**
     * Try to add a word to the current ladder.
     * If all rules are satisfied, add word and return true.
     * Otherwise, print a helpful error message and return false.
     */
    public boolean tryAdd(String newWord)
    {
        newWord = newWord.trim().toUpperCase();

        if (isOkToAdd (newWord)) {
            ladder.add(newWord);
            return true;
        }
        else {
            printErrorMessage();
            return false;
        }
    }

    /**
     * Evaluate all the rules for inclusion of a word in the current ladder
     * Return true if all rules are satisfied, otherwise false.
     */
    private boolean isOkToAdd(String word)
    {
        lastErrorCode = ERR_NOERROR;
        word = word.trim().toUpperCase();

        if (!lex.isValidEnglishWord(word)) {
            lastErrorCode = ERR_INVALID_WORD;
            return false;
        }

        if (ladder.size() > 0) {
            String previousWord = getPreviousWord();

            if (isDuplicate(word)) {
                lastErrorCode = ERR_DUPLICATE;
                return false;
            }

            if (!isLengthOK(word, previousWord)) {
                lastErrorCode = ERR_WRONG_LENGTH;
                return false;
            }

            if (countDifferences(word, previousWord) != 1) {
                lastErrorCode = ERR_TOO_MANY_CHANGES;
                return false;
            }
        }
        return true;
    }

    /**
     * Return true if word is already in the word ladder, otherwise false
     * JUST A STUB. COMPLETE THIS AS PART OF THE LAB
     */
    private boolean isDuplicate(String word)
    {
        return false;
    }

    /**
     * Return true if both words have the same length, otherwise false
     * JUST A STUB. COMPLETE THIS AS PART OF THE LAB
     */
    private boolean isLengthOK(String word, String previousWord)
    {
        return true;
    }

    /**
     * Return the number of character positions at which word1 and word2 differ
     * This code may assume that they are non-empty and both of the same length.
     * JUST A STUB. COMPLETE THIS AS PART OF THE LAB
     */
    private int countDifferences (String word1, String word2)
    {
        return 1;
    }

    /**
     * Print a helpful error message based on value of lastErrorCode
     * JUST A STUB. COMPLETE THIS AS PART OF THE LAB
     */
    private void printErrorMessage()
    {
        System.out.println("You have made an error.");
    }

    /**
     * Return a string of a given length.
     * The last character of the string is pipe ("|")
     * All other characters are spaces.
     * JUST A STUB. COMPLETE THIS AS PART OF THE LAB
     */
    private static String ladderConnector(int length)
    {
        String temp = "";
        return temp;
    }

     /**
     * Find first character position at with word1 and word2 differ
     * @param word1 first word
     * @param word2 second word
     * @return position at which word1 and word2 first differ, or -1 if identical
     */

    private static int findFirstDifference (String word1, String word2)
    {
        for (int i = 0; i < word2.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                return i;
            }
        }
        return -1;
    }

     /**
     * Print a nicely formatted word ladder
     */
    public void printLadder()
    {
        int size = ladder.size();
        if (size > 0) {
            System.out.println(ladder.get(0));
            for (int i = 1; i < size; i++) {
                String prev = ladder.get(i - 1);
                String current = ladder.get(i);
                int diffPoint = findFirstDifference(current, prev);
                System.out.println(ladderConnector(diffPoint));
                System.out.println(current);
            }
        }
        else {
            System.out.println("Empty word ladder");
        }

    }

    /**
     * Play an interactive word ladder game using the BlueJ terminal
     * COMPLETE THIS AS PART OF THE LAB
     */
    public void play()
    {
        System.out.println("Welcome to Word Ladder!");
        System.out.println("Enter words one per line to form a word ladder.");
        System.out.println("Enter a blank line to end game.");
        String word = reader.getInput();  // get first word.
        // Complete the method: lab code starts here
        
        
        // Lab code ends here
        System.out.println("Bye!");
    }
}