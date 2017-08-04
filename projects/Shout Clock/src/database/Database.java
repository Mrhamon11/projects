package database;

import time_file_ds.TimeFile;

import java.io.*;

/**
 * Created by aviam on 7/30/2017.
 */
public class Database {
    private TimeFile[][] db;

    public Database(){
        Retriever retriever = new Retriever(this.db);
        this.db = retriever.populateDB();
    }

    public void changeTimeFile(TimeFile newTimeFile, int hour, int minute){
        this.db[hour - 1][minute] = newTimeFile;
        new Logger(this.db).save();
    }

    public TimeFile getFileAtIndex(int hour, int minute){
        return this.db[hour - 1][minute]; //hour - 1 because hours start at 1 not 0
    }
}
