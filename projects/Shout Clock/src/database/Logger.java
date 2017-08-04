package database;

import time_file_ds.TimeFile;
import util.Utilities;

import java.io.*;

/**
 * Created by aviam on 7/31/2017.
 */
public class Logger {
    private TimeFile[][] db;
    private BufferedWriter bw;

    public Logger(TimeFile[][] db){
        this.db = db;
        try {
            this.bw = new BufferedWriter(new FileWriter(DatabaseProperties.getDbFile()));
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setAllToDefaultFiles(){
        try{
            this.bw = new BufferedWriter(this.bw);
            wipeDBFile();
            for(int i = 0; i < DatabaseProperties.getRows(); i++){
                for(int j = 0; j < DatabaseProperties.getColumns(); j++){
                    String hour = Utilities.convertIntToFolderString(i + 1); //i + 1 because hours are from 1 to 12, not 0 to 11
                    String minute = Utilities.convertIntToFolderString(j);
                    this.db[i][j] = new TimeFile(DatabaseProperties.getDefaultPath() + hour + "/" + minute
                            + DatabaseProperties.getFileType());
                    writeFileToDB(this.bw, this.db[i][j]);
                }
            }
            this.bw.close();
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void save(){
        try{
            this.bw = new BufferedWriter(this.bw);
            wipeDBFile();
            for(int i = 0; i < DatabaseProperties.getRows(); i++){
                for(int j = 0; j < DatabaseProperties.getColumns(); j++){
                    writeFileToDB(this.bw, this.db[i][j]);
                }
            }
            this.bw.close();
        } catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFileToDB(Writer writer, TimeFile fileToWrite){
        try{
            writer.write(fileToWrite.getFile().toString() + "\n");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void wipeDBFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(DatabaseProperties.getDbFile());
        writer.print("");
        writer.close();
    }
}
