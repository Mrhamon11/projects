package player;

import time_file_ds.TimeFile;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

/**
 * Created by aviam on 7/30/2017.
 */
public class PlayAudio {
    private Mixer mixer;
    private Clip clip;
    private TimeFile timeFile;

    public PlayAudio(TimeFile timeFile){
        this.timeFile = timeFile;
        Mixer.Info[] mixInfo = AudioSystem.getMixerInfo();
        this.mixer = AudioSystem.getMixer(mixInfo[0]);

        DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);

        try{
            this.clip = (Clip) this.mixer.getLine(dataInfo);
        } catch(Exception e){
            e.printStackTrace();
        }

        this.timeFile = timeFile;
    }

    public void play(){
        try{
            URL soundURL = this.timeFile.getFile().toURI().toURL();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
            this.clip.open(audioInputStream);
        } catch(Exception e){
            e.printStackTrace();
        }

        this.clip.start();

        do{
            try{
                Thread.sleep(50);
            } catch (Exception e){
                e.printStackTrace();
            }
        }while (this.clip.isActive());
    }
}
