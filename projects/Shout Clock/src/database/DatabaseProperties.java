package database;

import java.io.File;
import java.io.IOException;

/**
 * Created by Avi on 7/31/2017.
 */
public class DatabaseProperties {
    private static final int rows = 12;
    private static final int columns = 60;
    private static final String defaultPath = "src/time_files/default/";
    private static final String fileType = ".wav";
    private static final File dbFile = new File("src/database/db.txt");

    public static int getRows() {
        return rows;
    }

    public static int getColumns() {
        return columns;
    }

    public static String getDefaultPath() {
        return defaultPath;
    }

    public static String getFileType() {
        return fileType;
    }

    public static File getDbFile() {
        if(!dbFile.exists()){
            try{
                dbFile.createNewFile();
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return dbFile;
    }
}
