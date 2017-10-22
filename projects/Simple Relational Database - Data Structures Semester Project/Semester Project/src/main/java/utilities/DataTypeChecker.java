package utilities;

import dataStructures.Value;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;

/**
 * Created by Avi on 12/28/2016.
 */
public class DataTypeChecker {
    public static boolean isInt(String value){
        try{
            Integer.parseInt(value);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean isDouble(String value){
        try{
            if(isInt(value)){
                return false;
            }
            Double.parseDouble(value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isBoolean(String value){
        if(value.startsWith("'") && value.endsWith("'")){
            value = value.substring(1, value.length() - 1);
        }
        if(value.equals("false") || value.equals("true")){
            return true;
        }
        return false;
    }

    public static boolean isNull(String value){
        if(value.toLowerCase().equals("null")){
            return true;
        }
        return false;
    }

    public static boolean isString(String value){
        if(value.startsWith("'") && value.endsWith("'")){
            value = value.substring(1, value.length() - 1);
        }
        if(isInt(value)){
            return false;
        }
        if(isDouble(value)){
            return false;
        }
        if(isBoolean(value)){
            return false;
        }
        if(isNull(value)){
            return false;
        }
        return true;
    }

    public static boolean isInt(Value value){
        return value.getDataType().equals(ColumnDescription.DataType.INT);
    }

    public static boolean isDouble(Value value){
        return value.getDataType().equals(ColumnDescription.DataType.DECIMAL);
    }

    public static boolean isBoolean(Value value){
        return value.getDataType().equals(ColumnDescription.DataType.BOOLEAN);
    }

    public static boolean isString(Value value){
        return value.getDataType().equals(ColumnDescription.DataType.VARCHAR);
    }
}
