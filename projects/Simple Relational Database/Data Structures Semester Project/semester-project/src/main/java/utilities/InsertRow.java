package utilities;

import dataStructures.Table;
import dataStructures.Value;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.InsertQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLQuery;
import logging.Count;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Avi on 12/25/2016.
 */
public class InsertRow {
    private InsertQuery insertQuery;
    private String tableName;
    private List<Value> values;
    private Table table;

    public InsertRow(SQLQuery sqlQuery, Map<String, Table> tableMap, Count count){
        this.insertQuery = (InsertQuery) sqlQuery;
        this.tableName = this.insertQuery.getTableName();
        this.table = tableMap.get(this.tableName);
        if(table != null){
            assignValues(this.insertQuery.getColumnValuePairs(), table);
            insertRowToTable(this.values);
        }
        else{
            throw new IllegalArgumentException("A \"" + this.tableName + "\" table doesn't exist!");
        }
        count.addOne();
    }

    private void assignValues(ColumnValuePair[] columnValuePairs, Table table){
        this.values = new ArrayList<>();
        populateList(this.table.getSchema().size());
        Map<String, Integer> columnIndices = table.getColumnListIndices();
        for(ColumnValuePair cvp : columnValuePairs){
            if(columnIndices.get(cvp.getColumnID().getColumnName()) != null){
                int index = columnIndices.get(cvp.getColumnID().getColumnName());
                Object o = findValueType(cvp.getValue());
                //JD: given what you do in findValueType and in the Value constructor, it seems unnecessary to even test the type here or use a type parameter when creating an instance of Value
                //why not just simply do: this.calues.set(index,o);
                //and if someone is using an unacceptable type they;d get an exception from your call to findValueType anyway
                if(o instanceof Integer){
                    int i = (Integer) o;
                    Value<Integer> value = new Value<>(i);
                    this.values.set(index, value);
                }
                else if(o instanceof Double){
                    double d = (Double) o;
                    Value<Double> value = new Value<>(d);
                    this.values.set(index, value);
                }
                else if(o instanceof Boolean){
                    boolean b = (Boolean) o;
                    Value<Boolean> value = new Value<>(b);
                    this.values.set(index, value);
                }
                else if(o instanceof String){
                    String s = (String) o;
                    Value<String> value = new Value<>(s);
                    this.values.set(index, value);
                }
                else{
                    throw new IllegalArgumentException("Only values of type int, varchar, boolean, or decimal allowed.");
                }
            }
            else {
                throw new IllegalArgumentException("Column \"" + cvp.getColumnID().getColumnName().toString() + "\" " +
                        "doesn't exist in table \"" + cvp.getColumnID().getTableName() + ".\"");
            }
        }
    }

    private Object findValueType(String value){
        try{
            if(value.startsWith("'") && value.endsWith("'")){
                return value.substring(1, value.length() - 1);
            }
        }catch (Exception e){

        }
        try{
            int i = Integer.parseInt(value);
            return i;
        }catch (Exception e){

        }
        try{
            double d = Double.parseDouble(value);
            return d;
        }catch(Exception e){

        }
        try{
            if(value.toLowerCase().equals("true") || value.toLowerCase().equals("false")){
                Boolean b = Boolean.parseBoolean(value);
                return b;
            }
        }catch (Exception e){

        }
        //If we got here, value is a something else, so throw error.
        throw new IllegalArgumentException("Only int, varchar, boolean and decimal types allowed");
    }

    private void insertRowToTable(List<Value> values){
        this.table.insertRow(values);
    }

    private void populateList(int size){
        for(int i = 0; i < size; i++){
            this.values.add(null);
        }
    }
}