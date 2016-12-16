/**
 * Read web server data and analyse
 * hourly access patterns.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version 2011.07.31
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    // Use a LogfileReader to access the data.
    private LogfileReader reader;

    /**
     * Create an object to analyze hourly web accesses.
     */
    public LogAnalyzer()
    { 
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // Create the reader to obtain the data.
        reader = new LogfileReader();
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }

    /**
     * @return the total number of accesses recorded in the 
     * log file
     */
    public int numberOfAccesses()
    {
        int total = 0;
        for (int i = 0; i < hourCounts.length; i++) {
            total += hourCounts[i];
        }
        return total;
    }

    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }

    public int busiestHour()
    {
        int max = hourCounts[0];
        int num = 0;
        for (int counter = 1; counter < hourCounts.length; counter++) {
            if (hourCounts[counter] > max) {
                max = hourCounts[counter];
                num = counter;
            }
        }
        return num;
    }

    public int quietestHour()
    {
        boolean isFirstElement = true;
        int min = 0;
        int i = 0;
        int num = 0;
        while (i < hourCounts.length) {
            int x = hourCounts[i];
            if(isFirstElement) {
                min = x;
                isFirstElement = false;
            }
            else if (min > x) {
                min = x;
                num = i;
            }
            i++;
        }
        return num;
    }

    public int busiestTwoHours()
    {
        int biggest1 = hourCounts[0];
        int biggest2 = 0;
        int num = 0;
        for (int i = 0; i < hourCounts.length; i++) {
            if (hourCounts[i] > biggest1) {
                biggest2 = biggest1;
                biggest1 = hourCounts[i];
                num = i;
            }
        }
        return num; 
    }
    
    /**
     * With the data for this specific array, there is no tie for busiest hour.
     * Because of this fact, this method with change the busiest hours to index 10
     * and 14 which now allows us to test our busiestTwoHours method. This method should 
     * only be used with the data set that is in the weblog.txt.
     */
    public void changeHighest()
    {
        hourCounts[18] = 100;
    }
}
