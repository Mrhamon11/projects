package time;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aviam on 7/23/2017.
 */
public class TimeFileLoader {
    private String fullTimeString;
    private String hour;
    private String minute;
    private boolean amPm;

    public TimeFileLoader(TimeRetriever tr){
        this.fullTimeString = tr.getTimeString();
        this.amPm = tr.isAmPm();
        findHour();
        findMinute();
    }

    private void findHour(){
        int index = this.fullTimeString.indexOf(":");
        this.hour = this.fullTimeString.substring(0, index);
    }

    private void findMinute(){
        String[] split = this.fullTimeString.split(" ");
        int index = split[0].indexOf(":");
        this.minute = split[0].substring(index + 1);
    }

    public File getFile(){
        String filename = "src/time_files/" + this.hour + "/" + this.minute + ".mp3";
        File timeFile = new File(filename);
        return timeFile;
    }

    @Override
    public String toString() {
        return this.hour + " " + this.minute;
    }
}

