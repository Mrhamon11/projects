package dataStructures;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType;

import java.io.Serializable;

/**
 * Represents a column in the Table. Contains information about the column regarding what default values it has, if it's
 * unique, not null, indexed as well as other important information.
 * Created by Avi on 12/21/2016.
 */
public class SchemaElement implements Serializable{
    private String name;
    private DataType dataType;
    private boolean primaryKey;
    private boolean unique;
    private boolean notNull;
    private boolean hasDefault;
    private String defaultValue;
    private boolean indexed;
    private Integer wholeNumberLength;
    private Integer fractionalLength;
    private Integer varcharLength;
    private String indexName;
    private String tableName;

    /**
     * Creates a new Column with the supplied information.
     * @param name The name of the column.
     * @param dataType The data type of the column.
     * @param primaryKey If the column is a primary key.
     * @param unique If the column is unique.
     * @param notNull If the column is not null.
     * @param hasDefault If the column has a default value.
     * @param defaultValue The default value of the column if hasDefault is true.
     * @param wholeNumberLength The whole number length of a decimal number if the column is of type decimal.
     * @param fractionalLength The fractional length of the decimal number if the column is of type decimal.
     * @param varcharLength The string max string length permitted for a string if column is of type varchar.
     * @param tableName The table name of that the column is in.
     */
    public SchemaElement(String name, DataType dataType, boolean primaryKey, boolean unique, boolean notNull, boolean hasDefault, String defaultValue, Integer wholeNumberLength,
                         Integer fractionalLength, Integer varcharLength, String tableName){
        this.name = name;
        this.dataType = dataType;
        this.tableName = tableName;
        setSchemaCharacteristics(primaryKey, unique, notNull, hasDefault, defaultValue);
        setLengths(wholeNumberLength, fractionalLength, varcharLength);
    }

    /**
     * Assigns column characteristics based on constructor parameters.
     * @param primaryKey If the column is a primary key.
     * @param unique If the column is unique.
     * @param notNull If the column is not null.
     * @param hasDefault If the column has a default value.
     * @param defaultValue The default value for the column if it is declared as a default value column.
     */
    private void setSchemaCharacteristics(boolean primaryKey, boolean unique, boolean notNull, boolean hasDefault, String defaultValue){
        //If this column is a primary key, set these four fields to true, hasDefault to false, and defaultValue to null;
        if(primaryKey){
            this.primaryKey = true;
            this.unique = true;
            this.notNull = true;
            this.indexed = true;
            this.hasDefault = false;
            this.defaultValue = null;
        }
        //Otherwise, set primary key and indexed to false, and unique and notNull to parameter values.
        else{
            this.primaryKey = false;
            this.unique = unique;
            this.notNull = notNull;
            this.indexed = false;
            //If it's unique, we don't want to have a default value.
            if(this.unique){
                this.hasDefault = false;
            }
            else if(this.notNull && defaultValue == null) {
                this.hasDefault = false;
            }
            else{
                this.hasDefault = hasDefault;
            }
            setDefaultValue(defaultValue);
        }
    }

    /**
     * Sets the default value field to either the default value given if the hasDefault field is true,
     * and null if it is false.
     * @param defaultValue The supplied default value;
     */
    private void setDefaultValue(String defaultValue){
        if(defaultValue != null && this.hasDefault){
            this.defaultValue = defaultValue;
        }
        else{
            this.defaultValue = null;
        }
    }

    /**
     * Assigns the lengths for the column if it's data type is either varchar or decimal. Otherwise, it sets
     * the fields to null.
     * @param wholeNumberLength The whole number length if the column is of type decimal.
     * @param fractionalLength The fractional number length if the column is of type decimal.
     * @param varcharLength The varchar
     */
    private void setLengths(Integer wholeNumberLength, Integer fractionalLength, Integer varcharLength){
        if(this.dataType.equals(DataType.DECIMAL)){
            this.wholeNumberLength = wholeNumberLength;
            this.fractionalLength = fractionalLength;
            this.varcharLength = null;
        }
        else if(this.dataType.equals(DataType.VARCHAR)){
            this.wholeNumberLength = null;
            this.fractionalLength = null;
            this.varcharLength = varcharLength;
        }
        else{
            this.wholeNumberLength = null;
            this.fractionalLength = null;
            this.varcharLength = null;
        }
    }

    /**
     * Returns the name of the index on this column if it exists, null otherwise.
     * @return The name of the index on this column if it exists, null otherwise.
     */
    public String getIndexName() {
        return this.indexName;
    }

    /**
     * Sets the name of the index on this column to the supplied indexName.
     * @param indexName The new name of the column index.
     */
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    /**
     * Returns the name of the column.
     * @return The name of the column.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the column to the supplied name.
     * @param name The new name of the column.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the DataType of the column.
     * @return The DataType of the column.
     */
    public DataType getDataType() {
        return this.dataType;
    }

    /**
     * Returns true if this column is a primary key, false otherwise.
     * @return true if this column is a primary key, false otherwise.
     */
    public boolean isPrimaryKey() {
        return this.primaryKey;
    }

    /**
     * Returns true if this column is unique, false otherwise.
     * @return true if this column is unique, false otherwise.
     */
    public boolean isUnique() {
        return this.unique;
    }

    /**
     * Returns true if this column is not null, false otherwise.
     * @return true if this column is not null, false otherwise.
     */
    public boolean isNotNull() {
        return this.notNull;
    }

    /**
     * Returns true if this column has a default, false otherwise.
     * @return true if this column has a default, false otherwise.
     */
    public boolean isHasDefault() {
        return this.hasDefault;
    }

    /**
     * Returns the default value of the column as a string.
     * @return The default value of the column as a string.
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Returns the whole number length of the column. If the data type is not decimal, it
     * will return null.
     * @return The whole number length for the decimal, null if the column is any other type.
     */
    public Integer getWholeNumberLength() {
        return this.wholeNumberLength;
    }

    /**
     * Returns the fractional number length of the column. If the data type is not decimal, it
     * will return null.
     * @return The fractional number length for the decimal, null if the column is any other type.
     */
    public Integer getFractionalLength() {
        return this.fractionalLength;
    }

    /**
     * Returns the varchar length of the column. If the data type is not varchar, it
     * will return null.
     * @return The varchar length for the varchar, null if the column is any other type.
     */
    public Integer getVarcharLength() {
        return this.varcharLength;
    }

    /**
     * Returns true if the column has been indexed, false otherwise.
     * @return true if the column has been indexed, false otherwise.
     */
    public boolean isIndexed() {
        return this.indexed;
    }

    /**
     * Returns the table name where the column resides.
     * @return The table name where the column resides.
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Sets the table name field to the supplied value.
     * @param tableName The name of the table.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Sets the indexed field to the value supplied.
     * @param indexed The new boolean value for the indexed field.
     */
    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    /**
     * Returns the name of the column.
     * @return The name of the column.
     */
    @Override
    public String toString() {
        return getName();
    }
}


