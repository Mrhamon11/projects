

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestLabClass1.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TestLabClass1
{
    private Student student1;
    private LabClass labClass1;
    private Student SnowWhite;
    private Student LisaSimpson;
    private Student CharlieBrown;

    /**
     * Default constructor for test class TestLabClass1
     */
    public TestLabClass1()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        student1 = new Student("John Smith", "A123456");
        student1.changeName("John Q Smithfield");
        student1.getName();
        labClass1 = new LabClass(20);
        SnowWhite = new Student("SnowWhite", "100234");
        LisaSimpson = new Student("LisaSimpson", "122044");
        CharlieBrown = new Student("Charlie Brown", "12203P");
        LisaSimpson.getCredits();
        SnowWhite.addCredits(24);
        LisaSimpson.addCredits(56);
        CharlieBrown.addCredits(6);
        labClass1.enrollStudent(SnowWhite);
        labClass1.enrollStudent(LisaSimpson);
        labClass1.enrollStudent(CharlieBrown);
        labClass1.enrollStudent(student1);
        labClass1.numberOfStudents();
        labClass1.printList();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void testName()
    {
        student1.changeName("Barack Obama");
        assertEquals("Barack Obama", student1.getName());
    }
}

