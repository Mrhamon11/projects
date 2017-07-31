package record;

import javax.sound.sampled.*;
import java.io.File;

/**
 * Created by aviam on 7/30/2017.
 */
public class Recorder {
    private AudioFormat format;
    private String packageName;
    private String hour;
    private String minute;

    public Recorder(String packageName , String hour, String minute){
        this.format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        this.packageName = packageName;
        try{
            checkMinute(Integer.parseInt(minute));
            this.minute = minute;
            checkHour(Integer.parseInt(hour));
            this.hour = hour;
        } catch (IllegalArgumentException iae) {
            System.out.println(iae);
        }
    }

    public File record(){
        try{
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, this.format);
            if(!AudioSystem.isLineSupported(info)){
                System.err.println("Line not supported");
            }

            final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open();

            System.out.println("Started Recording...");
            targetLine.start();

            Record record = new Record(this.packageName, this.hour, this.minute, targetLine);
            Thread thread = new Thread(record, "recorder");

            thread.start();

            Thread.sleep(5000);

            record.stop();

            targetLine.stop();
            targetLine.close();

            System.out.println("Ended Sound Test");

            return record.getAudioFile();
        } catch (Exception e){
            return null;
        }
    }

    private void checkHour(int hour){
        if(!(hour >= 1 && hour <= 12)){
            throw new IllegalArgumentException("hour var must be 1 <= hour <= 12");
        }
    }

    private void checkMinute(int minute){
        if(!(minute >= 0 && minute <= 59)){
            throw new IllegalArgumentException("minute var must be 1 <= hour <= 12");
        }
    }

    private class Record implements Runnable{
        private volatile boolean exit = false;

        private String packageName;
        private String hour;
        private String minute;
        private TargetDataLine targetLine;
        private String rootDir;

        private File audioFile;

        public Record(String packageName, String hour, String minute, TargetDataLine targetLine){
            this.packageName = packageName;
            this.hour = hour;
            this.minute = minute;
            this.targetLine = targetLine;
            this.rootDir = "src/time_files/custom/";
            makeDirIfNotExist();
        }

        @Override
        public void run() {
            while(!exit) {
                AudioInputStream audioInputStream = new AudioInputStream(this.targetLine);
                File af = new File(this.rootDir + this.packageName + "/" + this.hour
                        + "/" + this.minute + ".wav");
                try {
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, af);
                } catch (Exception e) {

                }
                this.audioFile = af;
            }
            System.out.println("Stopped Recording");
        }

        public void stop(){
            this.exit = true;
        }

        private void makeDirIfNotExist(){
            //Make package dir
            File p = new File(this.rootDir + this.packageName);
            if(!p.exists()){
                p.mkdir();
            }

            //Make hour dir
            File h = new File(p.toString() + "/" + this.hour);
            if(!h.exists()){
                h.mkdir();
            }
        }

        public File getAudioFile(){
            return this.audioFile;
        }
    }
}
