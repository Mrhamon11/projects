import java.io.File;

/**
 * Example of execution of program. Program takes in a file directory as a command line argument. If the user doesn't
 * supply one, it will pull data from the two txt files in the directory and run them as examples. Those files are
 * example.txt and test.txt. Both files have different representations of cities and different sequences of events.
 * Created by aviam on 5/8/2017.
 */
public class Main {
    public static void main(String[] args) {
        if(args.length == 0){
            //First print output from example.txt
            System.out.println("Output from example.txt:\n");
            File file = new File("example.txt");
            EventHandler example = new EventHandler(file);
            example.run();

            System.out.println("\n");

            //Next print output from test.txt
            System.out.println("Output from test.txt:\n");
            file = new File("test.txt");
            EventHandler test = new EventHandler(file);
            test.run();
        }
        else{
            System.out.println("Output from supplied txt file:\n");
            File file = new File(args[0]);
            EventHandler supplied = new EventHandler(file);
            supplied.run();
        }
    }
}
