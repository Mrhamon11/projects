package testing;

import java.io.File;
import java.util.Scanner;

import database.Database;
import player.PlayAudio;
import record.Recorder;
import time.TimeFileLoader;
import time.TimeRetriever;
import time_file_ds.TimeFile;
import time_file_ds.TimeFileTreeNode;

/**
 * Created by aviam on 7/23/2017.
 */
public class Main{
    public static void main(String[] args) {

        Database db = new Database();

        Scanner scanner = new Scanner(System.in);

        System.out.println(">>> Commands: time, record, file, stop");
        System.out.print(">>> ");
        while (scanner.hasNextLine()) {
            System.out.print(">>> ");
            String input = scanner.nextLine();
            if (input.toLowerCase().equals("time")) {
                TimeFileLoader tfl = new TimeFileLoader(new TimeRetriever(false), db);
                TimeFile timeFile = tfl.getTimeFile();

                new PlayAudio(timeFile).play();
            } else if (input.equals("record")) {
                recordHandler(scanner, db);
                System.out.print(">>> ");
            } else if (input.startsWith("file")){
                fileHandler(scanner, db, input);
            } else if (input.equals("stop")) {
                break;
            } else {
                System.out.println("I don't understand...");
                System.out.print(">>> ");
            }
        }
    }

    private static void recordHandler(Scanner scanner, Database db){
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

    private static void fileHandler(Scanner scanner, Database db, String input){
        if(input.trim().equals("file")){
            System.out.print(">>> file commands: file view, file go *folder/file name*, file select *file name*," +
                    " file back \n>>> ");
            fileHandler(scanner, db, scanner.nextLine());
            return;
        }
        input = input.substring(5);
        if(input.equals("view")){
            viewHandler(db);
        }
        else if(input.startsWith("go")){
            input = input.substring(3);
            goHandler(input, db);
        }
        else if(input.startsWith("select")){
            input = input.substring(7);
            selectHandler(scanner, input, db);
        }
        else if(input.equals("back")){
            if(!db.backDir()){
                System.out.print(">>> You are on the root directory. \n>>> ");
            }
            viewHandler(db);
        }
        else{
            System.out.print(">>> That is not a valid go command. \n>>> ");
        }
    }

    private static void viewHandler(Database db){
        for(TimeFileTreeNode tf : db.viewCurrent()){
            System.out.println(">>>>> " + tf);
        }
        System.out.print(">>> ");
    }

    private static void goHandler(String input, Database db){
        TimeFileTreeNode node = db.containsNameInCurrentView(input);
        if(node != null) {
            if (node.getData().getFile().isDirectory()) {
                db.listChildren(node);
                viewHandler(db);
            } else {
                System.out.print(">>> You can only go into directories, not files. \n>>> ");
            }
        }
    }

    private static void selectHandler(Scanner scanner, String input, Database db){
        System.out.print(">>> Which hour would you like this recording to replace?: \n>>> ");
        String hour = scanner.nextLine().trim();
        System.out.print(">>> Which minute would you like this recording to replace?: \n>>> ");
        String minute = scanner.nextLine().trim();
        System.out.print(">>> ");

        TimeFileTreeNode node = db.containsNameInCurrentView(input);
        if(node != null){
            TimeFile file = node.getData();
            if(!file.getFile().isDirectory()){
                db.changeTimeFile(file, Integer.parseInt(hour), Integer.parseInt(minute));
            }
        } else {
            System.out.print(">>> Select only works with files, not directories. \n>>> ");
        }
    }
}
