package logging;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import database.Course;
import database.Database;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Deserializer class to be used in the JavaSocketClientServer project. Parses the supplied JSON file, and
 * using GSON, creates Course objects which are inserted into the database.
 * Created by Avi on 9/29/2017.
 */
public class Deserializer {
    private Gson gson;
    private File dbFile;
    private File dbFileDir;

    /**
     * Creates new Deserializer.
     * @param dbDir the JSON file that is to be parsed
     */
    public Deserializer(String dbDir){
        this.gson = new Gson();
        this.dbFile = new File(dbDir);
        this.dbFileDir = this.dbFile.getParentFile();
    }

    /**
     * Deserializes the JSON file, and inserts parsed Course objects into the database.
     * Database is then returned.
     * @return the database with all of the parsed courses
     */
    public Database deserialize(){
        Database database = new Database();
        File[] allBackups = this.dbFileDir.listFiles();
        if(allBackups.length == 1){
            firstTimeDeserialize(database);
        }
        else{
            deserializeFromNewestBackup(database);
        }
        return database;
    }

    /**
     * If there are no other JSON files in the directory, GSON hasn't touched the data yet, and thus,
     * very specific parsing needs to be done.
     * @param db the database where Course objects will be saved to
     */
    private void firstTimeDeserialize(Database db){
        JsonParser parser = new JsonParser();

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(this.dbFile);
            Reader reader = new InputStreamReader(inputStream);
            JsonObject section = parser.parse(reader).getAsJsonObject();
            JsonArray sections = section.getAsJsonArray("section");
            for(JsonElement jsonElement : sections){
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String dept = jsonObject.keySet().toArray()[0].toString();
                JsonArray courses = jsonObject.getAsJsonArray(dept);
                for(JsonElement element : courses){
                    Course course = this.gson.fromJson(element, Course.class);
                    db.addCourse(course);
                }
            }
            inputStream.close();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * If there are backups in the directory supplied, then GSON created the format for the
     * backups. GSON can now convert the JSON into Course objects and then this method saves them
     * to the supplied database
     * @param db the database where Course objects will be saved to
     */
    private void deserializeFromNewestBackup(Database db){
        String json = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader
                    (this.dbFile));
            String line;
            while((line = br.readLine()) != null){
                json += line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Type type = new TypeToken<List<Course>>(){}.getType();
        List<Course> courses = this.gson.fromJson(json, type);
        for(Course course : courses){
            db.addCourse(course);
        }
    }

//    public static void main(String[] args) {
//        Database db = new Deserializer("src/main/resources/courses.json").deserialize();
//        new Serializer("src/main/resources/courses.json").serialze(db);
//    }
}