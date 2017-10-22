package logging;

import com.google.gson.Gson;
import database.Course;
import database.Database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Serializer class to be used in the JavaSocketClientServer project. Converts Course objects into JSON
 * strings using GSON, and saves the JSON representation in a file of the same name as the one passed in the
 * src/main/resources folder. A save occurs anytime the database is changed in any way. Every save creates
 * new save file with the supplied name, and the old file is renamed to include a timestamp. These backups
 * are stored in the same folder as the main file.
 * Created by Avi on 9/29/2017.
 */
public class Serializer {
    private Gson gson;
    private File dbFile;
    private File dbFileDir;

    /**
     * Creates a new Serializer.
     * @param path the path of the database file
     */
    public Serializer(String path){
        this.gson = new Gson();
        this.dbFile = new File(path);
        this.dbFileDir = this.dbFile.getParentFile();
    }

    /**
     * Saves the contents of the supplied database into a JSON file of the same name as the dbFile.
     * The old file is renamed to include the timestamp when the new file was saved.
     * @param db the database whose contents will be saved
     */
    public void serialze(Database db){
        List<Course> allCourses = db.getAllCourses();
        String json = this.gson.toJson(allCourses);
        long currentTime = System.currentTimeMillis();
        String[] split = this.dbFile.getName().split("\\.");
        String name = this.dbFileDir + "/" + split[0] +  "_" + currentTime + "." + split[1];
        File backup = new File(name);
        this.dbFile.renameTo(backup);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.dbFile));
            bw.write(json);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        };
    }
}
