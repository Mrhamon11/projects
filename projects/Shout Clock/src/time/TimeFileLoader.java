package time;

import java.io.File;

/**
 * Created by aviam on 7/23/2017.
 */
public class TimeFileLoader {
    private String fullTimeString;
    private String hour;
    private String minute;
    private boolean amPm;
    private String fileType;
    private boolean defaultFile;
    private String dir;

    public TimeFileLoader(TimeRetriever tr, String fileType, String packageName){
        this.fullTimeString = tr.getTimeString();
        this.amPm = tr.isAmPm();
        this.fileType = fileType;
        findHour();
        findMinute();
        getFolder(packageName);
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

    private void getFolder(String packageName){
        if(packageName == null || packageName.equals("")){
            this.dir = "src/time_files/default/";
        }
        else{
            this.dir = "src/time_files/custom/" + packageName + "/";
        }
    }

    public File getFile(){
        String filename = this.dir + this.hour + "/" + this.minute + "." + this.fileType;
        File timeFile = new File(filename);
        return timeFile;
    }

    @Override
    public String toString() {
        return this.hour + " " + this.minute;
    }
}

