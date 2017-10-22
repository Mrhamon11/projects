package logging;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avi on 1/1/2017.
 */
public class Logger implements Serializable {
    private BufferedWriter bwMods;
    private BufferedReader brMods;
    private BufferedWriter bwTI;
    private String dir;
    private String tableIndexDir;

    public Logger(){
        try {
            this.dir = "src/logging/fiveWrites.txt";
            this.tableIndexDir = "src/logging/create_table_index.txt";
            this.bwMods = new BufferedWriter(new FileWriter(new File(this.dir)));
            this.bwTI = new BufferedWriter(new FileWriter(new File(this.tableIndexDir)));
            this.brMods = new BufferedReader(new FileReader(new File(this.dir)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void logQueryToDisk(String query){
        try {
            if(query.toLowerCase().startsWith("create")){
                this.bwTI = new BufferedWriter(new FileWriter(new File(this.tableIndexDir),true));
                this.bwTI.write(query + "\n");
                this.bwTI.close();
            }
            this.bwMods = new BufferedWriter(new FileWriter(new File(this.dir),true));
            this.bwMods.write(System.currentTimeMillis() + "\n");
            this.bwMods.write(query + "\n");
            this.bwMods.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String[]> getLogsFromDisk(){
        List<String[]> queries = new ArrayList<>();
        try {
            String line;
            while ((line = this.brMods.readLine()) != null) {
                String[] timeAndQuery = new String[2];
                timeAndQuery[0] = line;
                line = this.brMods.readLine();
                timeAndQuery[1] = line;
                queries.add(timeAndQuery);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return queries;
    }

    public void wipeLog(){
        try{
            PrintWriter pw = new PrintWriter(this.dir);;
            pw.print("");
            pw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
