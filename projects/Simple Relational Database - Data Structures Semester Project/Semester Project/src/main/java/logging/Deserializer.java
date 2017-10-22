package logging;

import database.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Created by Avi on 1/1/2017.
 */
public class Deserializer {

    public Deserializer(){

    }

    public Database deserialize(){
        Database database = null;
        try {
            File dir = new File ("src/logging");
            if(!dir.exists()){
                dir.mkdir();
            }
            File file = new File("src/logging/database.ser");
            if(!file.exists()){
                file.createNewFile();
            }else{
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                database = (Database) in.readObject();
                in.close();
                fileIn.close();
                return database;
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }
}
