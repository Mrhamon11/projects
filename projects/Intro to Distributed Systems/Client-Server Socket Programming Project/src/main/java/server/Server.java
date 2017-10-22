package server;

import database.Course;
import database.Database;
import logging.Deserializer;
import logging.Serializer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Server class to be used in the JavaSocketClientServer project.
 * Server connects to the Client object through a socket, and reads in input from the Client.
 * These input requests are then parsed by the Server object, and the proper actions to access
 * the database are taken based on the request. If the request fails to meet the guidelines
 * defined in the Client class, then the Server will pass null and false values back to the
 * Client to inform the user that something went wrong. Error and success messages will also
 * be passed to the Client to inform the user.
 * Created by Avi on 9/28/2017.
 */
public class Server {
    private boolean exit;
    private Database database;
    private Serializer serializer;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream inputScanner;
    private ObjectOutputStream outputStream;
    private final int MAX_MAJOR_CODE_SIZE = 3;


    /**
     * Creates a new Server.
     * @param path the path that the JSON database file is located.
     */
    public Server(String path){
        this.exit = false;
        this.database = new Deserializer(path).deserialize();
        this.serializer = new Serializer(path);
        try {
            this.serverSocket = new ServerSocket(9195);
            this.socket = this.serverSocket.accept();
            this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
            this.inputScanner = new ObjectInputStream(this.socket.getInputStream());
        } catch (ConnectException e){
            System.out.println("Couldn't connect server...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts Server. Server will now be able to take input from the client connected
     * to the same port. When the exit command is parsed, the Server shuts down.
     */
    public void startServer(){
        try {
            while (!this.exit) {
                acceptRequest();
            }
            this.serverSocket.close();
            this.socket.close();
            this.outputStream.close();
            this.inputScanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accepts the next requested passed by the Client object.
     */
    private void acceptRequest(){
        String request = null;
        String allCoursesInDept;
        try {
            //Read in request.
            request = (String) this.inputScanner.readObject();

            //If exit command, set this.exit to true to shut down Server object.
            if(request.trim().toLowerCase().equals("exit")){
                this.exit = true;
            }
            //If request starts with get, send to GET handler and send result to client.
            else if(request.trim().toLowerCase().startsWith("get")){
                allCoursesInDept = getHandler(request.substring(4));
                this.outputStream.writeObject(allCoursesInDept);
            }
            //If request starts with add, send to ADD handler and send results to client.
            else if(request.trim().toLowerCase().startsWith("add")){
                boolean success = addHandler(request.substring(4));
                //Save if request was good.
                if(success) {
                    this.serializer.serialze(this.database);
                }
                String message = getAddMessage(request.substring(4), success);
                this.outputStream.writeObject(success);
                this.outputStream.writeObject(message);
            }
            //If request starts with delete, send to DELETE handler and send results to client.
            else if(request.trim().toLowerCase().startsWith("delete")){
                boolean success = deleteHandler(request.substring(7));
                //Save if request was good.
                if(success){
                    this.serializer.serialze(this.database);
                }
                String message = getDeleteMessage(request.substring(7), success);
                this.outputStream.writeObject(success);
                this.outputStream.writeObject(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles GET request to see if request was good. If so, all courses in the supplied major code
     * department will be compiled into a string and returned for printing by the client.
     * @param majorCode the major code whose courses are requested
     * @return a string with all of the courses in the desired major code department
     */
    private String getHandler(String majorCode){
        majorCode = majorCode.trim().toUpperCase();
        if(!isCorrectLengthMajorCode(majorCode)){
            return null;
        }
        else if(!this.database.containsMajorCode(majorCode)){
            return null;
        }
        else{
            StringBuilder courses = new StringBuilder();
            List<Course> allCourses = this.database.getCoursesByDept(majorCode);
            for(Course course : allCourses){
                courses.append(course + "\n");
            }
            String allCoursesInDept = courses.toString();
            if(allCoursesInDept.isEmpty()){
                allCoursesInDept = "No courses in this department.\n";
            }
            return "All courses in " + majorCode + " dept: \n" + allCoursesInDept;
        }
    }

    /**
     * Handles ADD request to see if request was good. If so, course is added to the database and method
     * returns true. If anything is wrong with the request, the method returns false and doesn't add the
     * new course to the database.
     * @param request the request with the course to be added
     * @return true if course was added to database, false otherwise
     */
    private boolean addHandler(String request){
        //Get the index of the first section of the request which should be the major code.
        int indexOfSpace = request.indexOf(" ");
        String majorCode = request.substring(0, indexOfSpace);
        //If major code is not the correct length, or is composed of something other than just letters,
        //return false.
        if(!isCorrectLengthMajorCode(majorCode) || !isMajorCodeAlpha(majorCode)){
            return false;
        }
        String[] split = request.split(" ");
        //split.length >= 3. > 3 if course is more than one word. Can't be less than three or something is
        //missing.
        if(split.length < 3){
            return false;
        }

        //Compile end parts of string to build full course name.
        StringBuilder sb = new StringBuilder();
        for(int i = 2; i < split.length; i++){
            sb.append(split[i] + " ");
        }
        String courseTitle = sb.toString().trim();
        String crn = split[1];
        String dept = split[0].toUpperCase();

        //If the crn already exists in the database, don't add new course to database.
        if(this.database.crnExists(crn)){
            return false;
        }

        //If either the major code doesn't exist in db, or the course in dept doesn't exist in db
        //add new course to db
        if(!this.database.containsMajorCode(dept) || !this.database.courseInDeptExists(courseTitle, dept)){
            this.database.addCourse(courseTitle, crn, dept);
        }
        //Otherwise, a course in the dept exist, and we have to update crn
        else {
            this.database.addCRNToExistingCourseInDept(courseTitle, crn, dept);
        }
        return true;
    }

    /**
     * Create message to send to client depending on whether or not ADD request was successful.
     * @param request the request passed to Server
     * @param success whether or not the request successfully executed
     * @return the message to return to the client
     */
    private String getAddMessage(String request, boolean success){
        if(!success) {
            return "Error: course could not be added.\n";
        }

        StringBuilder sb = new StringBuilder();
        String[] split = request.split(" ");
        for(int i = 2; i < split.length; i++){
            sb.append(split[i] + " ");
        }
        String courseTitle = sb.toString().trim();
        String crn = split[1];
        String dept = split[0].toUpperCase();

        return courseTitle + " with CRN: " + crn
                + " was successfully added to the " + dept + " department.\n";
    }

    /**
     * Create message to send to client depending on whether or not DELETE request was successful.
     * @param crn the CRN code of the class in the request
     * @param success whether or not the request successfully executed
     * @return the message to return to the client
     */
    private String getDeleteMessage(String crn, boolean success){
        if(!success) {
            return "Error: course with CRN: " + crn + " could not be deleted.\n";
        }

        return "Course with CRN: " + crn + " was successfully deleted.\n";
    }

    /**
     * Handles the DELETE requests to see if the request was good. Checks if the supplied CRN in the
     * request is only composed of numbers, and actually exists in the database. If not, return false.
     * Otherwise, delete course from the database and return true.
     * @param request the request with the CRN code to be deleted
     * @return true if delete was successful, false otherwise
     */
    private boolean deleteHandler(String request){
        String crn = request.trim();
        if(!isInt(crn) || !this.database.crnExists(crn)){
            return false;
        }
        else{
            this.database.deleteCourse(crn);
            return true;
        }
    }

    /**
     * Checks to see if the major code is of the right length.
     * @param majorCode the major code
     * @return true if the major code is of the right length, false otherwise
     */
    private boolean isCorrectLengthMajorCode(String majorCode){
        majorCode = majorCode.trim().toUpperCase();
        return majorCode.length() == this.MAX_MAJOR_CODE_SIZE;
    }

    /**
     * Checks to see if the major code is only composed of letters.
     * @param majorCode the major code
     * @return true if the major code is only composed of letters false otherwise
     */
    private boolean isMajorCodeAlpha(String majorCode){
        return majorCode.matches("[a-zA-Z]+");
    }

    /**
     * Returns true if the supplied string is a number, false otherwise.
     * @param num the number to be checked
     * @return true if the supplied string is a number, false otherwise
     */
    private boolean isInt(String num){
        try{
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
}
