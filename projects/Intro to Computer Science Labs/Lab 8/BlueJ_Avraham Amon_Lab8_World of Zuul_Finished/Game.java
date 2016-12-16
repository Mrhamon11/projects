/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Game 
{
    private Parser parser;
    private Player player;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together, creates items
     * in rooms, and the player in the desired room.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, labyrinth;

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        labyrinth = new Room("in the University Labyrinth");

        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", labyrinth);

        theater.setExit("west", outside);

        pub.setExit("east", outside);
        pub.setExit("west", labyrinth);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        labyrinth.setExit("east", outside);
        labyrinth.setExit("south", pub);

        Item book = new Item("Book", "Contains magical spells", 75);
        Item flightBooks = new Item("Boots", "Wearer gains the ability to fly", 25);
        Item potion = new Item("Potion", "Heals player's health", 10);
        Item cookie = new Item("Cookie", "A magic cookie", 5);

        labyrinth.addItem(book);
        labyrinth.addItem(flightBooks);
        labyrinth.addItem(potion);
        labyrinth.addItem(cookie);

        player = new Player(outside);  // player will start outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getRoom().getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("look")) {
            System.out.println(player.look());
        }
        else if (commandWord.equals("take")) {
            takeItem(command);
        }
        else if (commandWord.equals("drop")) {
            dropItem(command);
        }
        else if (commandWord.equals("inventory")) {
            inventory(command);
        }
        else if (commandWord.equals("eat")) {
            eatItem(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to move in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word,
            // we don't know where to go...
            System.out.println("Go where?");
        }
        else {
            String direction = command.getSecondWord();
            System.out.println(player.goRoom(direction));
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Try to pick up an item. If there is an item, enter the desired
     * item, otherwise print an error message.
     */
    private void takeItem(Command command)
    {
        if(!command.hasSecondWord()) {
            //if there is no second word,
            //we don't know what to take...
            System.out.println("Take what?");
        }
        else {
            String item = command.getSecondWord();
            System.out.println(player.takeItem(item));
        }
    }

    /**
     * Try to drop an item. If there is an item in the player's bag,
     * enter the item, otherwise print an error message.
     */
    private void dropItem(Command command)
    {
        if(!command.hasSecondWord()) {
            //if there is no second word,
            //we don't know what to drop...
            System.out.println("Drop what?");
        }
        else {
            String item = command.getSecondWord();
            System.out.println(player.dropItem(item));
        }
    }

    /**
     * Displays all items that are currently in the bag, otherwise
     * print and error message.
     */
    private void inventory(Command command)
    {
        if(command.hasSecondWord()) {
            //if there is a second word...
            System.out.println("Huh?");
        }
        else {
            String item = command.getCommandWord();
            System.out.println(player.inventory());
        }
    }
    
    private void eatItem(Command command)
    {
        if(!command.hasSecondWord()) {
            //if there is no second word,
            //we don't know what to eat...
            System.out.println("Eat what?");
        }
        else {
            String item = command.getSecondWord();
            System.out.println(player.eatItem(item));
        }
    }
}
