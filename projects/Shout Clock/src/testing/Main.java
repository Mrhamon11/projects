package testing;

import java.io.File;
import java.util.Scanner;

import database.Database;
import player.PlayAudio;
import record.Recorder;
import time.TimeFileLoader;
import time.TimeRetriever;

/**
 * Created by aviam on 7/23/2017.
 */
public class Main{
    public static void main(String[] args) {

        File[][] files = getDatabase();

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.print(">>> ");
        while(scanner.hasNextLine()){
            System.out.print(">>> ");
            String input = scanner.nextLine();
            if(input.toLowerCase().equals("what time is it?")){
                String fileType = "wav";
                TimeFileLoader tfl = new TimeFileLoader(new TimeRetriever(false), fileType, "AmonTest");
                File file = tfl.getFile();

                new PlayAudio(file).play();
            }
            else if(input.equals("stop")){
                break;
            }
            else{
                System.out.println("I don't understand...");
                System.out.print(">>> ");
            }
        }



//        String fileType = "wav";
//        TimeFileLoader tfl = new TimeFileLoader(new TimeRetriever(false), fileType, "AmonTest");
//        File file = tfl.getFile();
//        System.out.println(file);
//
//        new PlayAudio(file).play();
//
//        try{
//            FileInputStream fis = new FileInputStream(file);
//            Player playMP3 = new Player(fis);
//
//            playMP3.play();
//
//        }catch(Exception e){System.out.println(e);}

//        File file = new Recorder("Amon", "04", "00").record();
//        new PlayAudio(file).play();
//        System.out.println(file.toString());
    }

    public static File[][] getDatabase(){
        Database db = new Database();
        return db.getSetFiles();
    }
}
