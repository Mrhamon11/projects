package database;

import com.sun.org.apache.xpath.internal.operations.Bool;
import dataStructures.SchemaElement;
import dataStructures.Table;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import logging.Count;
import logging.Deserializer;
import logging.Logger;
import logging.Serializer;
import utilities.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the actual database. Only has one public method, execute, to execute the desired query. Other methods are
 * here for saving, and restoring database on startup, and they happen automatically when the program is either closed,
 * or needs to be saved.
 * Created by Avi on 12/25/2016.
 */
public class Database implements Serializable, CanExecute{
    private Map<String, Table> tableMap;
    private transient SQLParser parser;
    private Count count;
    private Serializer serializer;
    private transient Logger logger;

    /**
     * Constructs the database. If there is no save file, a new database will be made. Otherwise, it restores all
     * tables from saved files.
     */
    public Database(){
        List<Object> fields = remakeDatabase();

        if(fields == null) {
            fields = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                fields.add(null);
            }
        }

        if(fields.get(0) == null){
            this.tableMap = new HashMap<>();
        }
        else{
            this.tableMap = (Map<String, Table>) fields.get(0);
        }

        this.parser = new SQLParser();

        this.count = new Count();

        if(fields.get(1) == null){
            this.serializer = new Serializer(this);
        }
        else{
            this.serializer = (Serializer) fields.get(1);
        }

        //If there is any modifications on the logged that weren't serialized, execute them.
        List<String[]> queries = fileHasData();
        if(queries == null){
            this.logger = new Logger();
        }
        else{
            this.logger = new Logger();
            for(String[] query : queries){
                try {
                    if(checkTimestamp(query)){
                    execute(query[1]);
                }
                }catch (Exception e){

                }
            }
        }
    }

    /**
     * Deserializes the database file if it exists. Otherwise, starts from scratch.
     * @return All of the objects in the serialized database file, or null if this is a fresh start.
     */
    private static List<Object> remakeDatabase(){
        Deserializer deserializer = new Deserializer();
        Database database = deserializer.deserialize();
        if(database == null){
            return null;
        }
        List<Object> fields = new ArrayList<>();
        if(database.getTableMap() != null){
            fields.add(database.getTableMap());
        }
        else{
            fields.add(null);
        }
        if(database.getSerializer() != null){
            fields.add(database.getSerializer());
        }
        else{
            fields.add(null);
        }
        return fields;
    }

    /**
     * Public method that executes query. Any modifications to data will be logged, and serialized if it is the 5th
     * modifications.
     * @param sql The sql query to be executed.
     * @return A table containing true of false depending on whether or not the insert, create index, delete, and update
     *         queries succeeded, the newly created empty table if a create table query was executed, false if it failed,
     *         and the new table from a select query if query was legal, false if it wasn't.
     * @throws Exception
     */
    @Override
    public Table execute(String sql) throws Exception {
    	//JD: adding a system.out so I can see what query was submitted
    	System.out.println("Executing Query: " + sql); 
        SchemaElement se = new SchemaElement("False", ColumnDescription.DataType.VARCHAR, true,false,false,false,null,null, null,null, "False");
        List<SchemaElement> l = new ArrayList<>();
        l.add(se);
        SchemaElement se2 = new SchemaElement("True", ColumnDescription.DataType.VARCHAR, true,false,false,false,null,null, null,null, "True");
        List<SchemaElement> l2 = new ArrayList<>();
        l2.add(se2);
        Table falseTable = new Table("False", l);
        Table trueTable = new Table("True", l2);
        Table toReturn = falseTable;

        //JD: from a performance perspective it would be better to parse once, get an "SQLQuery" object back, and then use instanceof on the query object, rather than paying the price of parsing the same query 6 times 
        try {
            CreateTableQuery c = (CreateTableQuery) parser.parse(sql);
            CreateTable ct = new CreateTable(c, tableMap, this.count);
            this.logger.logQueryToDisk(sql);
            serialize();
            toReturn = ct.createdTable();
        } catch (Exception e) {

        }

        try {
            InsertQuery i = (InsertQuery) parser.parse(sql);
            InsertRow ir = new InsertRow(i, tableMap, this.count);
            this.logger.logQueryToDisk(sql);
            serialize();
            toReturn = trueTable;
        } catch (Exception e) {

        }

        try {
            SelectQuery s = (SelectQuery) parser.parse(sql);
            try{
                SelectWhere sw = new SelectWhere(s, tableMap);
                toReturn = sw.tableFromConditions();
            }
            catch(Exception e){
                e = new Exception("Improper Select Query");
            }
        } catch (Exception e) {

        }

        try {
            UpdateQuery u = (UpdateQuery) parser.parse(sql);
            UpdateWhere uw = new UpdateWhere(u, tableMap, this.count);
            this.logger.logQueryToDisk(sql);
            serialize();
            toReturn = trueTable;
        } catch (Exception e) {

        }

        try {
            DeleteQuery d = (DeleteQuery) parser.parse(sql);
            DeleteWhere dw = new DeleteWhere(d, tableMap, this.count);
            this.logger.logQueryToDisk(sql);
            serialize();
            toReturn = trueTable;
        } catch (Exception e) {

        }

        try {
            CreateIndexQuery c = (CreateIndexQuery) parser.parse(sql);
            CreateIndex ci = new CreateIndex(c, tableMap, this.count);
            this.logger.logQueryToDisk(sql);
            serialize();
            toReturn = trueTable;
        } catch (Exception e) {

        }
        return toReturn;
    }

    /**
     * Serializes the database if the modification count is 5 or more. Count is reset, and modification log is wiped.
     */
    private void serialize(){
        if(this.count.getNumOfModifications() >= 5){
            this.count.resetCount();
            this.serializer.serialize();
            this.logger.wipeLog();
        }
    }

    /**
     * Checks the timestamp on the modification query on the modification log to ensure that it is only executed if the
     * time it was logged was after the most recent database serialization.
     * @param str An array containing the timestamp in index 0, and the query in index 1.
     * @return True if the time of the modification was after the most recent database serialization, false otherwise.
     */
    private boolean checkTimestamp(String[] str){
        try{
            File file = new File("src/logging/database_last_serialization_time.txt");
            if(!file.exists()){
                return true;
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            long databaseTimeLogged = Long.parseLong(br.readLine());
            long modQueryTime = Long.parseLong(str[0]);
            if(databaseTimeLogged < modQueryTime){
                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            throw new IllegalArgumentException("file couldn't be read");
        }
    }

    /**
     * If the modification log has queries when starting up the database, load the timestamp and query into an array,
     * and load that array in a List.
     * @return The list of all modifications on the log with their timestamps.
     */
    private List<String[]> fileHasData(){
        try{
            String line;
            File file = new File("src/logging/fiveWrites.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            List<String[]> queries = new ArrayList<>();
            while((line = br.readLine()) != null){
                String[] timeAndQuery = new String[2];
                timeAndQuery[0] = line;
                line = br.readLine();
                timeAndQuery[1] = line;
                queries.add(timeAndQuery);
            }
            return queries;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Returns the map of tables that represents the database.
     * @return The map of tables that represents the database.
     */
    private Map<String, Table> getTableMap() {
        return tableMap;
    }

    /**
     * Returns the serializer of the database.
     * @return The serializer of the database.
     */
    private Serializer getSerializer() {
        return serializer;
    }
}
