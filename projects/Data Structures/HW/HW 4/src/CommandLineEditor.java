import java.util.Scanner;

/**
 * Opens a command line editor where users can input any strings they want. All strings can then be
 * re-printed in order by calling the #print command, and deleted by calling the #delete command. Both
 * #print and #delete will also work if numbers representing the lines are appended to them. For example,
 * if the user calls "#print 3" the third line entered will be reprinted. "#delete 3" will delete the 3
 * line of text entered. If the line hasn't been entered yet, a message will be displayed for the user.
 * Created by Avi on 10/2/2016.
 */
public class CommandLineEditor {
    private ArrayStack<String> text;
    private Scanner keyboard;
    private boolean exit;

    /**
     * Constructs a new CommandLineEditor.
     */
    public CommandLineEditor(){
        this.text = new ArrayStack<>();
        this.keyboard = new Scanner(System.in);
        this.exit = false; //Exit variable. Program will finish if set to true.
    }

    //Main method.
    public static void main(String[] args) {
        CommandLineEditor c = new CommandLineEditor();
        c.textEditor();
    }

    /**
     * Program loop. For every loop, it prints a '>' character and waits for user input.
     *
     */
    public void textEditor(){
        System.out.print("> ");
        while(!this.exit && this.keyboard.hasNextLine()){
            String textLine = this.keyboard.nextLine();
            //User entered a potential command.
            if(textLine.startsWith("#")){
                commands(textLine);
            }
            else {
                this.text.push(textLine); //Add text to stack only if it isn't a command.
            }
            //We only want to print a '>' if program will still run on next loop.
            if(!this.exit) {
                System.out.print("> ");
            }
        }
    }

    /**
     * Handles user input for a command. Checks which command was entered and passes
     * string to helper methods.
     * @param command the command.
     */
    private void commands(String command){
        String newCommand = command.substring(1).toLowerCase().trim().replaceAll("\\s","");
        if(newCommand.startsWith("print")){
            printCommand(newCommand);
        }
        else if(newCommand.startsWith("delete")){
            deleteCommand(newCommand);
        }
        else if(newCommand.equals("exit")){
            this.exit = true;
        }
        else{
            System.out.println("That is not a valid command.");
        }
    }

    /**
     * Handles the print command. Checks to see if user wants to print all text, or a specific line.
     * @param printCommand the print command.
     */
    private void printCommand(String printCommand){
        int lineNumber;
        //if no line number is specified in print command:
        if(printCommand.length() == 5){
            printText(0); //We want everything on the stack.
        }
        else {
            try {
                lineNumber = Integer.parseInt(printCommand.substring(5));
            } catch (Exception e) {
                System.out.println("#print command must be entered with either no other characters following it," +
                        "or a single positive number following it.");
                lineNumber = -1;
            }

            if (lineNumber > 0 && lineNumber <= this.text.size()) {
                printText(lineNumber); //We only want the text at that line number.
            } else {
                System.out.println("#print command must be entered with either no other characters following it, z" +
                        "or a single positive number following it. There also must be at least that many lines of " +
                        "text entered when using this command.");
            }
        }
    }

    /**
     * Passes command to either print all text, or just a single line.
     * @param lineNumber the line number the user wants to print. If 0, print all text.
     */
    private void printText(int lineNumber){
        if (lineNumber == 0){ //Used by system to signal that all text should be printed.
            popStack();
        }
        else{
            popStackLine(lineNumber);
        }
    }

    /**
     * Pops all text of the main stack to print them. Text will be put back in main stack in
     * the correct order once all printing completes.
     */
    private void popStack(){
        if(this.text.peek() == null){
            System.out.println("");
        }
        else {
            ArrayStack<String> helper = new ArrayStack<>();
            while (this.text.peek() != null) {
                helper.push(this.text.pop());
            }
            while (helper.peek() != null) {
                String previousText = helper.pop();
                System.out.println(previousText);
                this.text.push(previousText);
            }
        }
    }

    /**
     * Prints only the line from the stack that the user requests.
     * @param lineNumber the line number that the user wants to print.
     */
    private void popStackLine(int lineNumber){
        ArrayStack<String> helper = new ArrayStack<>();
        int lineCounter = this.text.size();
        while(this.text.peek() != null && lineCounter >= lineNumber) {
            helper.push(this.text.pop());
            lineCounter--;
        }
        System.out.println(helper.peek());

        while(helper.peek() != null){
            this.text.push(helper.pop());
        }
    }

    /**
     * Handles the delete command. Checks to see if user wants to delete all text, or a specific line.
     * @param deleteCommand the delete command.
     */
    private void deleteCommand(String deleteCommand){
        int lineNumber;
        //if no line number is specified in print command:
        if(deleteCommand.length() == 6){
            deleteText(0); //We want to delete everything in the stack.
        }
        else {
            try {
                lineNumber = Integer.parseInt(deleteCommand.substring(6));
            } catch (Exception e) {
                System.out.println("#delete command must be entered with either no other characters following it," +
                        "or a single positive number following it.");
                lineNumber = -1;
            }

            if (lineNumber > 0 && lineNumber <= this.text.size()) {
                deleteText(lineNumber); //We only want to delete the text at that line number.
            } else {
                System.out.println("#delete command must be entered with either no other characters following it, " +
                        "or a single positive number following it. There also must be at least that many lines of " +
                        "text entered when using this command.");
            }
        }
    }

    /**
     * Passes command to either delete all text, or just a single line.
     * @param lineNumber the line the user wants to delete. If it is 0, delete all text.
     */
    private void deleteText(int lineNumber){
        if (lineNumber == 0){ //Used by system to signal that all text should be deleted.
            popDeleteStack();
        }
        else{
            popDeleteStackLine(lineNumber);
        }
    }

    /**
     * Deletes all text in the stack.
     */
    private void popDeleteStack(){
        while(this.text.peek() != null) {
            text.pop();
        }
    }

    /**
     * Deletes only a specific line number in the stack.
     * @param lineNumber the line number the user wants to delete.
     */
    private void popDeleteStackLine(int lineNumber){
        ArrayStack<String> helper = new ArrayStack<>();
        int lineCounter = this.text.size();
        while(this.text.peek() != null && lineCounter >= lineNumber) {
            helper.push(this.text.pop());
            lineCounter--;
        }
        helper.pop();

        while(helper.peek() != null){
            this.text.push(helper.pop());
        }
    }
}
