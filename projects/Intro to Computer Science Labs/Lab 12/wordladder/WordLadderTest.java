
/**
 * The test class WordLadderTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class WordLadderTest extends junit.framework.TestCase
{
    private Lexicon lex;
    private WordLadder wLadder;
    /**
     * Default constructor for test class WordLadderTest
     */
    public WordLadderTest()
    {
        lex = new Lexicon();  
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    protected void setUp()
    {
        wLadder = new WordLadder(lex);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    protected void tearDown()
    {
    }

    public void testDuplicateWord()
    {
        assertEquals(true, wLadder.tryAdd("cook"));
        assertEquals(false, wLadder.tryAdd("cook"));
        assertEquals(WordLadder.ERR_DUPLICATE, wLadder.getLastErrorCode());
    }

    public void testNotEnglish()
    {
        assertEquals(true, wLadder.tryAdd("cook"));
        assertEquals(false, wLadder.tryAdd("xyzz"));
        assertEquals(WordLadder.ERR_INVALID_WORD, wLadder.getLastErrorCode());
    }

    public void testWrongLength()
    {
        assertEquals(true, wLadder.tryAdd("cook"));
        assertEquals(false, wLadder.tryAdd("novel"));
        assertEquals(WordLadder.ERR_WRONG_LENGTH, wLadder.getLastErrorCode());
    }

    public void testTooManyChanges()
    {
        assertEquals(true, wLadder.tryAdd("cook"));
        assertEquals(false, wLadder.tryAdd("brag"));
        assertEquals(WordLadder.ERR_TOO_MANY_CHANGES, wLadder.getLastErrorCode());
    }
    
    public void testGoodLadder()
    {
        assertEquals(true, wLadder.tryAdd("test"));
        assertEquals(true, wLadder.tryAdd("best"));
        assertEquals(true, wLadder.tryAdd("beet"));
        assertEquals(true, wLadder.tryAdd("bees"));
        assertEquals(true, wLadder.tryAdd("byes"));
        assertEquals(true, wLadder.tryAdd("eyes"));
        assertEquals(true, wLadder.tryAdd("eves"));
        assertEquals(true, wLadder.tryAdd("ever"));
        assertEquals(true, wLadder.tryAdd("over"));
        assertEquals(0, wLadder.getLastErrorCode());
        wLadder.printLadder();
        
    }

}

