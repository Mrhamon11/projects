import client.Client;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import server.Server;

/**
 * Unit test for JavaSocketClientServer project. When run, the database save file is pulled from
 * System.getProperty("file") and saved to instantiate the Server and Client. The path value comes
 * from the -Dfile="path/to/the/file.json" which must be entered after mvn test in order for the tests
 * to pass.
 * Created by Avi on 9/28/2017.
 */
public class ClientServerTest {
    private static String file = "";
    private static Client client = null;


    /**
     * Sets the path of the file.
     */
    @BeforeClass
    public static void setFile(){
        file = System.getProperty("file");
    }

    /**
     * Instantiates the Server in it's own thread, and Client on Main thread. Client reference can
     * be accessed from any unit test.
     */
    @BeforeClass
    public static void instantiate(){
        Thread serverThread = new Thread(() -> {
            new Server(file).startServer();
        });
        serverThread.start();
        client = new Client();
    }

    /**
     * Test to show that proper GET requests work, and their results are printed to the console.
     * Shows that get requests are case insensitive.
     */
    @Test
    public void goodGetTest(){
        System.out.println("Now testing good GET requests...\n");

        Assert.assertTrue(client.submitRequest("GET COM"));
        Assert.assertTrue(client.submitRequest("get com"));
        Assert.assertTrue(client.submitRequest("Get Com"));
        Assert.assertTrue(client.submitRequest("GeT MaT"));
        Assert.assertTrue(client.submitRequest("get bio"));

        System.out.println("\nEnd of test...\n");
    }

    /**
     * Test to show that improper GET requests fail.
     * Shows that major codes not in database will not work, and error message will be printed.
     * Also shows that GET command must be first.
     */
    @Test
    public void badGetTest(){
        System.out.println("Now testing bad GET requests...\n");

        Assert.assertFalse(client.submitRequest("Get AVI"));
        Assert.assertFalse(client.submitRequest("GET COMP"));
        Assert.assertFalse(client.submitRequest("COM GET"));
        Assert.assertTrue(client.submitRequest("Get com"));

        System.out.println("\nEnd of test...\n");
    }

    /**
     * Test to show that correct ADD requests are properly handled and added to
     * database. In order to allow this test to run multiple times, a delete
     * request will occur on all new classes, removing them from the database.
     */
    @Test
    public void goodAddTest(){
        System.out.println("Now testing good ADD requests...\n ");

        //Add new course called Avi's Course under new AVI dept:
        Assert.assertTrue(client.submitRequest("ADD AVI 12345 Avi's Course"));
        //Add another crn for existing Avi's Course (crn can be of any length):
        Assert.assertTrue(client.submitRequest("Add Avi 0021 Avi's Course"));
        //Add new course called AI under existing COM dept:
        Assert.assertTrue(client.submitRequest("add com 94981 AI"));

        //Print all courses in AVI and COM departments with crns to show that they were added
        Assert.assertTrue(client.submitRequest("get avi"));
        Assert.assertTrue(client.submitRequest("get com"));

        /*Delete the new classes so test can be rerun and pass as adding a course with
          existing crn doesn't work*/
        Assert.assertTrue(client.submitRequest("delete 12345"));
        Assert.assertTrue(client.submitRequest("delete 0021"));
        Assert.assertTrue(client.submitRequest("delete 94981"));

        /*Print out all courses in AVI and COM department with crns to show that they were
          deleted.*/
        Assert.assertTrue(client.submitRequest("get avi"));
        Assert.assertTrue(client.submitRequest("get com"));

        System.out.println("\nEnd of test...\n");
    }

    /**
     * Test to show that bad ADD requests are rejected by the Server and not added into the database.
     * Tests include adding courses where the request was in the correct order, trying to add four letter
     * major codes for classes, adding a CRN code that already exists, and trying to add a course with
     * major code that contains numbers.
     */
    @Test
    public void badAddTest(){
        System.out.println("Now testing bad ADD requests...\n");

        //Attempting to add a class to a department with a major code longer than 3 letters:
        Assert.assertFalse(client.submitRequest("add FOUR 00001 Four Letter Major Course"));

        /*Attempting to add a class with the input out of order:
        Correct: (add, majorCode, crn, course name):
        Attempting: (add, crn, courseName, majorCode,*/
        Assert.assertFalse(client.submitRequest("add 12058 Out of Order Course TST"));
        //Same as above but attempting (add, crn, majorCode, course name)
        Assert.assertFalse(client.submitRequest("add 12058 TST Out of Order Course"));

        //Attempting to add a course with a crn that already exists in database:
        Assert.assertFalse(client.submitRequest("add COM 78790 Programming for non majors has this crn"));

        //Attempting to add a course with a majorCode that contains numbers:
        Assert.assertFalse(client.submitRequest("add AB1 28790 Numbered Major Code Dept"));
        Assert.assertFalse(client.submitRequest("add 123 28790 Numbered Major Code Dept"));

        //Print out courses in FOUR, TST, and COM to show that nothing was added:
        client.submitRequest("get four");
        client.submitRequest("get tst");
        client.submitRequest("get com");
    }

    /**
     * Test to show that properly formatted DELETE commands work. Unit test adds courses, prints them,
     * deletes them, and prints them again to show that courses were indeed being deleted. This is done to
     * make sure that database isn't affected for future unit tests.
     */
    @Test
    public void goodDeleteRequest(){
        System.out.println("Now testing good DELETE requests...\n");

        //Adding courses to be deleted:
        Assert.assertTrue(client.submitRequest("ADD AVI 12345 Avi's Course"));
        Assert.assertTrue(client.submitRequest("Add Avi 0021 Avi's Course"));
        Assert.assertTrue(client.submitRequest("add com 94981 AI"));

        //Print all courses in AVI and COM departments with crns to show that they were added
        Assert.assertTrue(client.submitRequest("get avi"));
        Assert.assertTrue(client.submitRequest("get com"));

        //Now deleting these courses:
        Assert.assertTrue(client.submitRequest("DELETE 12345"));
        Assert.assertTrue(client.submitRequest("Delete 0021"));
        Assert.assertTrue(client.submitRequest("delete 94981"));

        /*Print out all courses in AVI and COM department with crns to show that they were
          deleted.*/
        Assert.assertTrue(client.submitRequest("get avi"));
        Assert.assertTrue(client.submitRequest("get com"));
    }

    /**
     * Test to show that bad DELETE requests don't work. Shows that trying to delete a CRN code
     * that doesn't exist doesn't work.
     */
    @Test
    public void badDeleteTest(){
        System.out.println("Now testing bad DELETE requests...\n");

        //Attempting to delete a crn that doesn't exist:
        Assert.assertFalse(client.submitRequest("delete 00000"));
        Assert.assertFalse(client.submitRequest("delete 123"));

        //Attempting to delete something which isn't a crn:
        Assert.assertFalse(client.submitRequest("delete something"));

        System.out.println("\nEnd of test...\n");
    }

    /**
     * Test to show that only requests beginning with one of the three keywords, GET, ADD, DELETE, or
     * the request equaling EXIT, work.
     */
    @Test
    public void requestDoesNotStartWithRequestWordsTest(){
        System.out.println("Now testing to see if any requests work if they don't start with either get," +
                " add, delete. Exit does work however, to close the program.\n");

        //Attempting to send request that doesn't start with permitted keywords
        Assert.assertFalse(client.submitRequest("test Com 98765 New Course"));
        Assert.assertFalse(client.submitRequest("asdfsa 78790"));

        System.out.println("\nEnd of test...\n");
    }

    /**
     * Tests to show that if the Server stops and the socket is closed, the Client
     * can't access information from the Server's database. Test "interrupts" the server
     * thread by having Client close it normally with the EXIT command, and then have it perform
     * a legal GET command. Because the Client isn't connected, an error message is printed, and
     * Client closes all streams. Client and Server are re-instantiated at end of test in order to
     * complete other unit tests.
     */
    @Test
    public void serverInterruptedTest(){
        System.out.println("Now attempting to access server after it being interrupted...\n");

        client.submitRequest("exit");
        Assert.assertFalse(client.submitRequest("get com"));

        /*recall method so that server and client are up and running,
          and more tests can be run.*/
        instantiate();

        System.out.println("\nEnd of test...\n");
    }
}
