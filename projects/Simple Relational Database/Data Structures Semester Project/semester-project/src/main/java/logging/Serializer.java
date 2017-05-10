package logging;

import database.Database;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by Avi on 1/1/2017.
 */
public class Serializer implements Serializable {
    private Database database;

    public Serializer(Database database) {
        this.database = database;
    }
    public void serialize(){
        try{
            File file = new File("src/logging/database.ser");
            File backup = new File("src/logging/database_backup.ser");
            backup.delete();
            file.renameTo(backup);
            FileOutputStream fileOut= new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.database);
            out.close();
            fileOut.close();

            ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
            out = new ObjectOutputStream(bos) ;
            out.writeObject(this.database);
            out.close();

            byte[] buf = bos.toByteArray();

            File time = new File("src/logging/database_last_serialization_time.txt");
            if(!time.exists()){
                time.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(time));
            bw.write(System.currentTimeMillis() + "\n");
            bw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
