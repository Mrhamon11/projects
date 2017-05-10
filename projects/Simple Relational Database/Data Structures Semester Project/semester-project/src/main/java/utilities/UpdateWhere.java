package utilities;

import dataStructures.*;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import logging.Count;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Avi on 12/25/2016.
 */
public class UpdateWhere {
    private UpdateQuery updateQuery;
    private String tableName;
    private Condition where;
    private Table table;
    private Count count;

    public UpdateWhere(SQLQuery sqlQuery, Map<String, Table> tableMap, Count count){
        this.updateQuery = (UpdateQuery) sqlQuery;
        this.tableName = this.updateQuery.getTableName();
        this.where = this.updateQuery.getWhereCondition();
        this.table = tableMap.get(this.tableName);
        this.count = count;
        updateRows();
    }

    private void updateRows(){
        if(where != null) {
            ConditionHandler ch = new ConditionHandler(this.where, this.table);
            Set<Row> affectedRows = ch.evaluateCondition();
            checkUpdateablility(affectedRows, this.updateQuery.getColumnValuePairs());
            for (ColumnValuePair colValPair : this.updateQuery.getColumnValuePairs()) {
                updateRows(affectedRows, colValPair);
            }
            for(int i = 0; i < affectedRows.size(); i++){
                this.count.addOne();
            }
        }
        else{
            //Need to make a set of all rows in column to check if update is allowed to be executed.
            Set<Row> affectedRows = new HashSet<>();
            for(Row row : this.table.getRows()){
                affectedRows.add(row);
            }
            for(int i = 0; i < affectedRows.size(); i++){
                this.count.addOne();
            }
            checkUpdateablility(affectedRows, this.updateQuery.getColumnValuePairs());
            for(ColumnValuePair colValPair : this.updateQuery.getColumnValuePairs()){
                updateRows(affectedRows, colValPair);
            }
        }
    }

    //JD: this comments pertains to the rest of this class:
    //Whenever you have very repetitive code, like the method below, you should ask yourself if the repeating code can be refactored into being a separate method
    //when the same code is repeated, it means a change to requirements or a bug means making the change/fix in multiple places, and that can easily result in inconsistency, bugs, etc.
    //Even if you find that you can't simply parameterize and reuse code as a method, it is still best practice to break a large method down into smaller ones - easier to understand
    //and maintain
    
    private void updateRows(Set<Row> affectedRows, ColumnValuePair colValPair){
        for(Row row : this.table.getRows()){
            if(affectedRows.contains(row)){
                int columnIndex = this.table.getColumnListIndices().get(colValPair.getColumnID().getColumnName());
                ColumnDescription.DataType dataType = this.table.getSchema().get(columnIndex).getDataType();
                String columnName = this.table.getSchema().get(columnIndex).getName();
                if(DataTypeChecker.isInt(colValPair.getValue()) && dataType.equals(ColumnDescription.DataType.INT)){
                    int i = Integer.parseInt(colValPair.getValue());
                    if(this.table.getSchema().get(columnIndex).isIndexed()){
                        updateRowAndBTree(i, row, columnName, columnIndex);
                    }
                    else{
                        row.getValue(columnIndex).setElement(i);
                    }
                }
                else if(DataTypeChecker.isDouble(colValPair.getValue()) && dataType.equals(ColumnDescription.DataType.DECIMAL)){
                    double d = Double.parseDouble(colValPair.getValue());
                    if(this.table.getSchema().get(columnIndex).isIndexed()){
                        updateRowAndBTree(d, row, columnName, columnIndex);
                    }
                    else{
                        row.getValue(columnIndex).setElement(d);
                    }
                }
                else if(DataTypeChecker.isBoolean(colValPair.getValue()) && dataType.equals(ColumnDescription.DataType.BOOLEAN)){
                    String value = colValPair.getValue().substring(1, colValPair.getValue().length() - 1);
                    if(value.equals("false")){
                        if(this.table.getSchema().get(columnIndex).isIndexed()){
                            updateRowAndBTree(false, row, columnName, columnIndex);
                        }
                        else{
                            row.getValue(columnIndex).setElement(false);
                        }
                    }
                    else{
                        if(this.table.getSchema().get(columnIndex).isIndexed()){
                            updateRowAndBTree(true, row, columnName, columnIndex);
                        }
                        else{
                            row.getValue(columnIndex).setElement(true);
                        }
                    }
                }
                else if(DataTypeChecker.isString(colValPair.getValue()) && dataType.equals(ColumnDescription.DataType.VARCHAR)){
                    String value = colValPair.getValue().substring(1, colValPair.getValue().length() - 1);
                    if(this.table.getSchema().get(columnIndex).isIndexed()){
                        updateRowAndBTree(value, row, columnName, columnIndex);
                    }
                    else{
                        row.getValue(columnIndex).setElement(value);
                    }
                }
                else{
                    throw new IllegalArgumentException("The new value must match the DataType of the column");
                }
            }
        }
    }

    private void checkUpdateablility(Set<Row> affectedRows, ColumnValuePair[] columnValuePairs){
        for(ColumnValuePair colValPair : columnValuePairs){
            int columnIndex = this.table.getColumnListIndices().get(colValPair.getColumnID().getColumnName());
            //Prevents updating if column is not null, and value is null
            if(this.table.getSchema().get(columnIndex).isNotNull() && DataTypeChecker.isNull(colValPair.getValue())){
                throw new IllegalArgumentException("Null cannot be set in column because column is not null!");
            }
            //Prevents updating if column is unique and there is more than one row in that column will be affected.
            if(this.table.getSchema().get(columnIndex).isUnique() && affectedRows.size() > 1){
                throw new IllegalArgumentException("The \"" + this.tableName + "." + this.table.getSchema().get(columnIndex).getName()
                        + "\" column is unique, and more than one row in the table would be updated with this WHERE " +
                        "condition.");
            }
            //Prevents updating if column is unique, and value to be added to row already exists.
            if(this.table.getSchema().get(columnIndex).isUnique() && affectedRows.size() == 1){
                Object value;
                //Finds data type and determines if that type is in the unique set.
                if(DataTypeChecker.isInt(colValPair.getValue())){
                    value = Integer.parseInt(colValPair.getValue());
                }
                else if(DataTypeChecker.isDouble(colValPair.getValue())){
                    value = Double.parseDouble(colValPair.getValue());
                }
                else if(DataTypeChecker.isBoolean(colValPair.getValue())){
                    String v = colValPair.getValue().substring(1, colValPair.getValue().length() - 1);
                    if(v.equals("false")){
                        value = false;
                    }
                    else{
                        value = true;
                    }
                }
                else if(DataTypeChecker.isNull(colValPair.getValue())){
                    value = null;
                }
                else if(DataTypeChecker.isString(colValPair.getValue())){
                    value = colValPair.getValue().substring(1, colValPair.getValue().length() - 1);
                }
                else{
                    throw new IllegalArgumentException("The new value must match the DataType of the column!");
                }
                if(value == null || this.table.getUniqueValues().get(columnIndex).contains(value)){
                    throw new IllegalArgumentException("The \"" + this.tableName + "." + this.table.getSchema().get(columnIndex).getName()
                            + "\" column is unique, and the column already contains this value. Values in unique columns " +
                            "may not be null!");
                }
            }
            //Determines if value is not in correct format if it is a double or a string.
            SchemaElement schemaElement = this.table.getSchema().get(columnIndex);
            if(DataTypeChecker.isDouble(colValPair.getValue())){
                String[] parts = colValPair.getValue().split("\\.");
                if(parts[0].length() > schemaElement.getWholeNumberLength() || parts[0].length() < schemaElement.getWholeNumberLength()){
                    throw new IllegalArgumentException("The length of the decimal's whole number is not the correct number of digits. " +
                            "The correct size is " + schemaElement.getWholeNumberLength() + ".");
                }
                if(parts[1].length() > schemaElement.getFractionalLength() || parts[1].length() < schemaElement.getFractionalLength()){
                    if(!(parts[1].length() + 1 == schemaElement.getFractionalLength())){
                        throw new IllegalArgumentException("The length of the decimal's fractional number is not the correct number of digits. " +
                                "The correct size is " + schemaElement.getFractionalLength() + ".");
                    }
                }
            }
            if(DataTypeChecker.isString(colValPair.getValue())){
                String value = colValPair.getValue().substring(1, colValPair.getValue().length() - 1);
                if(value.length() > schemaElement.getVarcharLength()){
                    throw new IllegalArgumentException("The length of value \"" + value + "\" exceeds the limit length " +
                            "of " + schemaElement.getVarcharLength() + " in the \"" + this.tableName + "." + schemaElement.getName() + "\" column.");
                }
            }
        }
    }

    private void updateRowAndBTree(Object newValue, Row row, String columnName, int columnIndex){
        Value value = row.getValue(columnIndex);
        BTree bTree = this.table.getIndices().get(columnName).getbTree();
        Set<Row> rows = (Set<Row>) bTree.get(value);
        rows.remove(row);
        if(rows.isEmpty()){
            bTree.delete(value);
        }

        Value<?> newVal;
        if(value.getDataType().equals(ColumnDescription.DataType.INT)){
            newVal = new Value<>((Integer) newValue);
            if(this.table.getSchema().get(columnIndex).isUnique()){
                if(this.table.getUniqueValues().get(columnIndex).contains(newVal)){
                    throw new IllegalArgumentException("That value is already in the column, and column is unique");
                }
            }
            row.getRowValues().set(columnIndex, newVal);
        }
        else if(value.getDataType().equals(ColumnDescription.DataType.DECIMAL)){
            newVal = new Value<>((Double) newValue);
            if(this.table.getSchema().get(columnIndex).isUnique()){
                if(this.table.getUniqueValues().get(columnIndex).contains(newVal)){
                    throw new IllegalArgumentException("That value is already in the column, and column is unique");
                }
            }
            row.getRowValues().set(columnIndex, newVal);
        }
        else if(value.getDataType().equals(ColumnDescription.DataType.BOOLEAN)){
            newVal = new Value<>((Boolean) newValue);
            if(this.table.getSchema().get(columnIndex).isUnique()){
                if(this.table.getUniqueValues().get(columnIndex).contains(newVal)){
                    throw new IllegalArgumentException("That value is already in the column, and column is unique");
                }
            }
            row.getRowValues().set(columnIndex, newVal);
        }
        else if(value.getDataType().equals(ColumnDescription.DataType.VARCHAR)){
            newVal = new Value<>((String) newValue);
            if(this.table.getSchema().get(columnIndex).isUnique()){
                if(this.table.getUniqueValues().get(columnIndex).contains(newVal)){
                    throw new IllegalArgumentException("That value is already in the column, and column is unique");
                }
            }
            row.getRowValues().set(columnIndex, newVal);
        }
        else{
            throw new IllegalArgumentException("New value must be an int, varchar, decimal or boolean!");
        }

        if(this.table.getSchema().get(columnIndex).isUnique()){
            this.table.getUniqueValues().get(columnIndex).remove(value);
            this.table.getUniqueValues().get(columnIndex).add(newVal);
        }

        rows = (Set<Row>) bTree.get(newVal);
        if(rows == null){
            rows = new HashSet<>();
            rows.add(row);
            bTree.put(newVal, rows);
        }
        else{
            rows.add(row);
            bTree.put(newVal, rows);
        }
    }
}
