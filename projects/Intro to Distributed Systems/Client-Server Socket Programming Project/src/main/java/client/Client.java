package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class to be used in the JavaSocketClientServer project.
 * Client connects to server in this project and submits requests to the server
 * for handling. Objects of this class will then receive input from the server and
 * display the results to the user. Clients can only send ADD, GET and DELETE requests
 * to the server, and the requests must pertain to information regarding courses offered
 * in specific university departments. New courses and departments can be added with the
 * ADD request.
 *
 * Formats for requests:
 *   GET: (GET 3-Letter-Major-Code) -> Gets all courses in the department corresponding to 3 letter code.
 *   ADD: (ADD 3-Letter-Major-Code Unique-CRN-Course-Code Course-Name) -> Add Course-Name with unique CRN to
 *        department with 3 letter code. Courses in db can have the same name, even in the same department
 *        as long as the supplied CRN is different. 3-Letter-Major-Code must only contain letters.
 *        Unique-CRN-Course-Code must only contain numbers.
 *   DELETE: (DELETE Unique-CRN-Course-Code) -> Deletes the course with the supplied CRN. If a course has more
 *           than one CRN, the course will not be deleted, only the reference to the CRN.
 *           Unique-CRN-Course-Code must only contain numbers.
 *   EXIT: (EXIT) -> Shuts down the server, no more requests can be made.
 *
 * All requests are case insensitive. Requests must be in the correct order.
 * Created by Avi on 9/28/2017.
 */
public class Client {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    /**
     * Constructs a new Client. Client connects to Server through port 9182 on the localhost.
     */
    public Client(){
        try {
            this.socket = new Socket(InetAddress.getLocalHost(), 9195);
            this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
            this.inputStream = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * Starts the Client user interface. Allows clients to submit requests to the Server.
     */
    public void startClient(){
        Scanner keyboard = new Scanner(System.in);
        boolean done = false;
        System.out.print(">>> Welcome to the client. Please enter a request for the server.\n" +
                "Requests must start with either ADD, GET, or DELETE. Type EXIT to quit.\n" +
                "Commands are case insensitive.\n>>> ");
        while(!done && keyboard.hasNextLine()){
            String line = keyboard.nextLine();
            submitRequest(line);
            if(line.trim().toLowerCase().equals("exit")){
                done = true;
                System.out.println("Goodbye.");
            }
            else{
                System.out.print(">>> ");
            }
        }
    }

    /**
     * Submits a request to the server for processing. The Server will respond and this method
     * will print a message to the console indicating whether the request was completed or not.
     * Incomplete requests will contain an error message.
     * @param request The request to be processed.
     * @return true if the request was completed, false otherwise.
     */
    public boolean submitRequest(String request){
        try {
            //If null, inform user and return false, don't bother passing to server.
            if(request == null) {
                System.out.println("Request cannot be null\n");
                return false;
            }

            this.outputStream.writeObject(request);

            //If the request starts with "get" read in a String from the server.
            //If result is null, major code didn't exist. Otherwise, print out all
            //courses and return true.
            if(request.trim().toLowerCase().startsWith("get")) {
                String allCourses = (String) this.inputStream.readObject();
                if(allCourses == null){
                    System.out.println("The supplied major code doesn't exist!\n");
                    return false;
                }
                System.out.println(allCourses);
                return true;
            }
            //If the request is an ADD request, read in boolean from server
            //determining if ADD was successful. Then read in corresponding message.
            //Print message, and return success.
            else if(request.trim().toLowerCase().startsWith("add")){
                boolean success = (Boolean) this.inputStream.readObject();
                String message = (String) this.inputStream.readObject();
                System.out.println(message);
                return success;
            }
            //If the request is an DELETE request, read in boolean from server
            //determining if DELETE was successful. Then read in corresponding message.
            //Print message, and return success.
            else if(request.trim().toLowerCase().startsWith("delete")){
                boolean success = (Boolean) this.inputStream.readObject();
                String message = (String) this.inputStream.readObject();
                System.out.println(message);
                return success;
            }
            //If the request is EXIT, close down server, return true.
            else if(request.trim().toLowerCase().equals("exit")){
                System.out.println("Exiting...\n");
                return true;
            }
            //Otherwise, print error message and return false.
            else{
                System.out.println("Requests must start with either get, add, or delete!");
                return false;
            }
        } catch (IOException e) {
            //If we get here, connection with server socket was lost, so close down
            //socket and streams.
            System.out.println("Connection to the server has been lost.");
            try {
                this.socket.close();
                this.outputStream.close();
                this.inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
