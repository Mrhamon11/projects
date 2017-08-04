package testing;

import java.io.File;
import java.util.Scanner;

import database.Database;
import player.PlayAudio;
import record.Recorder;
import time.TimeFileLoader;
import time.TimeRetriever;
import time_file_ds.TimeFile;

/**
 * Created by aviam on 7/23/2017.
 */
public class Main{
    public static void main(String[] args) {

        Database db = new Database();

        Scanner scanner = new Scanner(System.in);

        System.out.print(">>> ");
        while (scanner.hasNextLine()) {
            System.out.print(">>> ");
            String input = scanner.nextLine();
            if (input.toLowerCase().equals("what time is it?")) {
                TimeFileLoader tfl = new TimeFileLoader(new TimeRetriever(false), db);
                TimeFile timeFile = tfl.getTimeFile();

                new PlayAudio(timeFile).play();
            } else if (input.equals("record")) {
                recordHandler(scanner, db);
                System.out.print(">>> ");
            } else if (input.equals("change file")){

            } else if (input.equals("stop")) {
                break;
            } else {
                System.out.println("I don't understand...");
                System.out.print(">>> ");
            }
        }
    }

    public static void recordHandler(Scanner scanner, Database db){
        System.out.print(">>> Please enter a folder name: \n>>> ");
        String folder = scanner.nextLine().trim();
        System.out.print(">>> Please enter the hour for new recording: \n>>> ");
        String hour = scanner.nextLine().trim();
        System.out.print(">>> Please enter the minute for the new recording: \n>>> ");
        String minute = scanner.nextLine().trim();

        System.out.println(">>> File will be played back after recording...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(">>> Now recording...");

        TimeFile newTimeFile = new Recorder(folder, hour, minute).record();

        System.out.println(">>> Playing back recording...");

        PlayAudio playAudio = new PlayAudio(newTimeFile);
        playAudio.play();

        boolean yes = yesNoVerifyer(">>> Would you like to keep this recording? Press \"y\" to keep this recording" +
                        "and \"n\" to make a new one. \n>>> ",
                scanner);

        if(!yes){
            recordHandler(scanner, db);
        }else {
            yes = yesNoVerifyer(">>> Would you like to set this file as the new file for this time? (y/n) \n>>> ", scanner);
            if(yes) {
                db.changeTimeFile(newTimeFile, Integer.parseInt(hour), Integer.parseInt(minute));
            }
        }
    }

    private static boolean yesNoVerifyer(String message, Scanner scanner){
        System.out.print(message);
        String yesNo = scanner.nextLine();
        if(yesNo.equals("y")){
            return true;
        }
        else if(yesNo.equals("n")){
            return false;
        }
        else{
            System.out.print("Please only enter either a \"y\" or a \"n\".");
            return yesNoVerifyer(message, scanner);
        }
    }
}
