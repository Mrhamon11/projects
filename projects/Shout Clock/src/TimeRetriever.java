import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aviam on 7/23/2017.
 */
public class TimeRetriever {
    private static DateFormat dateFormat;
    private Date time;
    private boolean militaryTimeFormat;
    private String timeString;
    private boolean amPm;

    public TimeRetriever(boolean militaryTimeFormat){
        String hourFormat;
        String amPm;
        if(militaryTimeFormat){
            hourFormat = "HH";
            amPm = "";
            this.amPm = false;
        }
        else{
            hourFormat = "hh";
            amPm = " a"; //space before to put space before am/pm and time
            this.amPm = true;
        }
        this.dateFormat = new SimpleDateFormat(hourFormat + ":mm" + amPm);
        this.time = new Date();
        this.timeString = this.dateFormat.format(this.time);
    }

    public boolean isAmPm() {
        return this.amPm;
    }

    public String getTimeString() {
        return this.timeString;
    }
}
