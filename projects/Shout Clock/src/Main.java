import java.io.File;
import time.TimeFileLoader;
import time.TimeRetriever;

/**
 * Created by aviam on 7/23/2017.
 */
public class Main {
    public static void main(String[] args) {
        TimeFileLoader tfl = new TimeFileLoader(new TimeRetriever(true));
        File file = tfl.getFile();

    }
}
