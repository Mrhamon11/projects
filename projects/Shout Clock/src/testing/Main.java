package testing;

import java.io.ByteArrayInputStream;
import java.io.File;

import javazoom.jl.player.Player;
import time.TimeFileLoader;
import time.TimeRetriever;

import java.io.FileInputStream;
import java.io.InputStream;

import javafx.scene.media.*;
import java.net.URL;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


/**
 * Created by aviam on 7/23/2017.
 */
public class Main{
    public static void main(String[] args) {
        TimeFileLoader tfl = new TimeFileLoader(new TimeRetriever(false));
        File file = tfl.getFile();
        System.out.println(file);

        try{
            FileInputStream fis = new FileInputStream(file);
            Player playMP3 = new Player(fis);

            playMP3.play();

        }catch(Exception e){System.out.println(e);}

    }

}
