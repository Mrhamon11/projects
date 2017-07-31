package database;

import util.Utilities;

import java.io.*;

/**
 * Created by aviam on 7/30/2017.
 */
public class Database {
    private static final int rows = 12;
    private static final int columns = 60;
    private static final String defaultPath = "src/time_files/default/";
    private static final String fileType = ".wav";
    private File[][] setFiles;

    public Database(){
        populateFiles();
    }

    private void populateFiles(){
        try{
            File db = new File("src/database/db.txt");
            BufferedReader br = new BufferedReader(new FileReader(db));
            if(br.readLine() == null){
                this.setFiles = new File[rows][columns];
                setAllToDefaultFiles(db);
            }
            else{
                readFromFile(br);
            }
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setAllToDefaultFiles(File db){
        BufferedWriter bw;
        try{
            bw = new BufferedWriter(new FileWriter(db));

            for(int i = 0; i < rows; i++){
                for(int j = 0; j < columns; j++){
                    String hour = Utilities.convertIntToFolderString(i + 1); //i + 1 because hours are from 1 to 12, not 0 to 11
                    String minute = Utilities.convertIntToFolderString(j);
                    this.setFiles[i][j] = new File(defaultPath + hour + "/" + minute + fileType);
                    writeFileToDB(bw, this.setFiles[i][j]);
                }
            }
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void writeFileToDB(Writer writer, File fileToWrite){
        try{
            writer.write(fileToWrite.toString() + "\n");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readFromFile(BufferedReader br){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                try{
                    String dir = br.readLine();
                    if(dir != null){
                        this.setFiles[i][j] = new File(dir);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                    //todo have it erase db.txt and set defaults.
//                    PrintWriter writer = new PrintWriter(file);
//                    writer.print("");
//                    writer.close();
                }
            }
        }
    }

    public void addRecord(){

    }

    public File[][] getSetFiles() {
        return setFiles;
    }
}
