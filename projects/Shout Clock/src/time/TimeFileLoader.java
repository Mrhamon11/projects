package time;

import database.Database;

import java.io.File;

/**
 * Created by aviam on 7/23/2017.
 */
public class TimeFileLoader {
    private String fullTimeString;
    private String hour;
    private String minute;
    private boolean amPm;
    private Database db;

    public TimeFileLoader(TimeRetriever tr, Database db){
        this.fullTimeString = tr.getTimeString();
        this.amPm = tr.isAmPm();
        this.db = db;
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
        return this.db.getFileAtIndex(Integer.parseInt(this.hour), Integer.parseInt(this.minute));
    }

    @Override
    public String toString() {
        return this.hour + " " + this.minute;
    }
}

