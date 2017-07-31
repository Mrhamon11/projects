package player;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

/**
 * Created by aviam on 7/30/2017.
 */
public class PlayAudio {
    private Mixer mixer;
    private Clip clip;
    private File file;

    public PlayAudio(File file){
        this.file = file;
        Mixer.Info[] mixInfo = AudioSystem.getMixerInfo();
        this.mixer = AudioSystem.getMixer(mixInfo[0]);

        DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);

        try{
            this.clip = (Clip) this.mixer.getLine(dataInfo);
        } catch(Exception e){
            e.printStackTrace();
        }

        this.file = file;
    }

    public void play(){
        try{
            URL soundURL = this.file.toURI().toURL();
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
