package testing;

import java.io.ByteArrayInputStream;
import java.io.File;

import javazoom.jl.player.Player;
import time.TimeFileLoader;
import time.TimeRetriever;

import java.io.FileInputStream;

/**
 * Created by aviam on 7/23/2017.
 */
public class Main{
    public static void main(String[] args) {
        String filetype = "mp3";
        TimeFileLoader tfl = new TimeFileLoader(new TimeRetriever(false), filetype);
        File file = tfl.getFile();
        System.out.println(file);

        try{
            FileInputStream fis = new FileInputStream(file);
            Player playMP3 = new Player(fis);

            playMP3.play();

        }catch(Exception e){System.out.println(e);}

    }

}
