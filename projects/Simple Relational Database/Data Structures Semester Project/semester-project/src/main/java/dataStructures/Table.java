package dataStructures;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a table in a relational database. Tables have a schema, and rows whose values must match the schema
 * DataType. A table must have at least one column that is a primary key. “UNIQUE”, “NOT NULL”, and “DEFAULT” can be
 * specified for any column. Any column can be indexed, and all primary keys will be indexed by default. Primary key
 * values must be "UNIQUE", "NOT NULL" and cannot specify a default value. Rows can be added or deleted at any time as well.
 * Created by Avi on 12/20/2016.
 */
public class Table implements Serializable{
    private String name;
    private List<SchemaElement> schema;
    private Map<String, Integer> columnListIndices; //Map of column names and indices in schema list.
    private List<Row> rows;
    private List<Set<Value>> uniqueValues;
    private Map<String, Index> indices;

    /**
     * Constructs a new table.
     * @param name The table name.
     * @param schema A list of SchemaElements.
     */
    public Table(String name, List<SchemaElement> schema){
        this.name = name;
        this.indices = new HashMap<>();
        createSchema(schema);
        this.rows = new LinkedList<Row>();
        initializeUnique();
    }

    /**
     * Returns the name of the table.
     * @return The name of the table.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns all of the rows in the table.
     * @return All of the rows in the table.
     */
    public List<Row> getRows() {
        return this.rows;
    }

    /**
     * Sets a table's rows to a new list of rows.
     * @param rows The new list of rows.
     */
    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    /**
     * Returns a Map of all column names and their indices in the schema list.
     * @return A Map of all column names and their indices in the schema list.
     */
    public Map<String, Integer> getColumnListIndices() {
        return this.columnListIndices;
    }

    /**
     * Sets the columnListIndices field to the supplied map.
     * @param columnListIndices The new map of column indices of a list.
     */
    public void setColumnListIndices(Map<String, Integer> columnListIndices) {
        this.columnListIndices = columnListIndices;
    }

    /**
     * Returns the list of table schema.
     * @return The list of table schema.
     */
    public List<SchemaElement> getSchema() {
        return this.schema;
    }

    /**
     * Sets the column list to the supplied list of columns.
     * @param schema The new list of columns.
     */
    public void setSchema(List<SchemaElement> schema) {
        this.schema = schema;
    }

    /**
     * Returns the list containing the set of all unique values in a column if the column is unique.
     * @return The list containing the set of all unique values in a column.
     */
    public List<Set<Value>> getUniqueValues() {
        return this.uniqueValues;
    }

    /**
     * Returns the Map of the indices of indexed columns.
     * @return The Map of the indices of indexed columns.
     */
    public Map<String, Index> getIndices() {
        return this.indices;
    }

    /**
     * Sets the Map of indices to the parameter value.
     * @param indices The map of indices.
     */
    public void setIndices(Map<String, Index> indices) {
        this.indices = indices;
    }

    /**
     * Inserts a row to table. Values in list must match the correct schema for insertion.
     * @param values A list of values to be added to the table.
     */
    public void insertRow(List<Value> values){
        Row row = addValuesToRow(values);
        this.rows.add(row);
    }

    /**
     *
     * @param row
     */
    public void insertRow(Row row){
        for(int i = 0; i < row.getRowValues().size(); i++){
            if(!(row.getValue(i).getDataType() == null || row.getValue(i).getDataType().equals(this.getSchema().get(i).getDataType()))){
                throw new IllegalArgumentException("Values entered must match the schema DataType!");
            }
        }
        this.rows.add(row);
    }

    /**
     * Creates a schema for the table. SchemaElements must be in a list, and each one must have a unique name. At least
     * one SchemaElement must be a primary key.
     * @param schemaElementsList A list of SchemaElements for the table.
     * @throws IllegalArgumentException If two SchemaElements have the same name.
     * @throws IllegalArgumentException If no SchemaElement is a primary key.
     */
    private void createSchema(List<SchemaElement> schemaElementsList){
        this.schema = new LinkedList<>();
        this.columnListIndices = new HashMap<>();
        Set<String> schemaSet = new HashSet<>();
        boolean hasPrimaryKey = false;
        for(int i = 0; i < schemaElementsList.size(); i++) {
            if(schemaSet.contains(schemaElementsList.get(i).getName())){
                throw new IllegalArgumentException("You have at least two \"" + schemaElementsList.toString() + "\" " +
                        "columns. All columns must have different names!");
            }
            if(schemaElementsList.get(i).isPrimaryKey()){
                hasPrimaryKey = true;
                indexColumn(schemaElementsList.get(i), null);
            }
            String columnName = schemaElementsList.get(i).getName();
            schemaSet.add(columnName);
            this.columnListIndices.put(columnName, i);
            this.schema.add(schemaElementsList.get(i));
        }
        if(!hasPrimaryKey){
            throw new IllegalArgumentException("Every table must have a primary key!");
        }
    }

    private void indexColumn(SchemaElement schemaElement, String name){
        if(name == null){
            schemaElement.setIndexName(this.getName() + "_" + schemaElement.getName() + "_index");
        }
        else{
            schemaElement.setIndexName(name);
        }
        Index index = new Index(schemaElement.getIndexName(), this.getName());
        this.indices.put(schemaElement.getName(), index);
    }

    /**
     * Initializes the List of Sets to store unique elements for every column.
     */
    private void initializeUnique(){
        this.uniqueValues = new LinkedList<>();
        for (int i = 0; i < this.schema.size(); i++) {
            uniqueValues.add(new HashSet<Value>());
        }
    }

    /**
     * Creates and returns a row out of the supplied list of values. If the values corresponding to any column DataType
     * don't match, or if primary key, unique, or not null rules are broken, throw exception.
     * @param values The list of values to be added to the row.
     * @return The newly created row.
     * @throws IllegalArgumentException If the number of values exceeds the number of columns in the table.
     * @throws IllegalArgumentException If the DataType of a value at a given index in the list doesn't match the
     *                                  DataType of the column.
     *
     */
    private Row addValuesToRow(List<Value> values){
        int numColumns = values.size();
        if(numColumns > this.schema.size()){
            throw new IllegalArgumentException("Number of values exceeds number of columns in table!");
        }
        Row row = new Row();
        for (int i = 0; i < numColumns; i++) {
            Value<?> value = values.get(i);
            //If value is null because user never specified a value for it, make a new Value with element == null.
            if(value == null){
                value = new Value(null);
            }
            SchemaElement schemaElement = this.schema.get(i);
            Set uniqueSet = this.uniqueValues.get(i);
            //Checks that values added in each column are of the correct DataType.
            if(value.getElement() == null || value.getDataType().equals(schemaElement.getDataType())){

                //Checks to see that every element in row is legal. If any are not, an exception will be thrown in one
                //of the helper methods bellow.
                checkPrimaryKeyNull(value, schemaElement, uniqueSet);
                checkUnique(value, schemaElement, uniqueSet);
                checkNull(value, schemaElement);
                checkLength(value, schemaElement);
                changeDefaultValue(value, schemaElement);

                //If we've gotten here, the value is legal, so add value to row.
                row.addValue(value);
                //Only add element to set corresponding to column if a primary key or unique.
                if(schemaElement.isPrimaryKey() || schemaElement.isUnique()){
                    uniqueSet.add(values.get(i));
                }
                //Add value to index if column is indexed.
                if(schemaElement.isIndexed()){
                    updateSchemaIndex(schemaElement, value, row);
                }
            }
            else{
                throw new IllegalArgumentException("Values entered must match the table schema!");
            }
        }
        return row;
    }

    public void indexExistingColumn(SchemaElement schemaElement, String indexName){
        if(schemaElement.isIndexed()){
            throw new IllegalArgumentException("Column is already indexed!");
        }
        schemaElement.setIndexed(true);
        schemaElement.setIndexName(indexName);
        int rowIndex = this.columnListIndices.get(schemaElement.getName());
        Index<?> index;
        if(schemaElement.getDataType().equals(DataType.INT)){
            index = new Index<Integer>(indexName, this.getName());
        }
        else if(schemaElement.getDataType().equals(DataType.DECIMAL)){
            index = new Index<Double>(indexName, this.getName());
        }
        else if(schemaElement.getDataType().equals(DataType.BOOLEAN)){
            index = new Index<Boolean>(indexName, this.getName());
        }
        else if(schemaElement.getDataType().equals(DataType.VARCHAR)){
            index = new Index<String>(indexName, this.getName());
        }
        else{
            throw new IllegalArgumentException("Only int, decimal, boolean and varchar allowed for column types!");
        }
        for(Row row : this.rows){
            if(row.getValue(rowIndex).getDataType() != null) {
                Set<Row> rowSet = index.getbTree().get(row.getValue(rowIndex));
                if (rowSet == null) {
                    rowSet = new HashSet<>();
                }
                rowSet.add(row);
                index.getbTree().put(row.getValue(rowIndex), rowSet);
            }
        }
        this.indices.put(schemaElement.getName(), index);
    }

    private void updateSchemaIndex(SchemaElement schemaElement, Value value, Row row){
    	//JD: repetitive but effective. Might want to think about how to have the logic only one time.    	
        if(schemaElement.getDataType().equals(DataType.INT)) {
            BTree<Value<Integer>, Set<Row>> bTree = this.indices.get(schemaElement.getName()).getbTree();
            Set<Row> rowSet = bTree.get(value);
            if (rowSet == null) {
                rowSet = new HashSet<>();
            }
            rowSet.add(row);
            bTree.put(value, rowSet);
        }
        else if(schemaElement.getDataType().equals(DataType.DECIMAL)){
            BTree<Value<Double>, Set<Row>> bTree = this.indices.get(schemaElement.getName()).getbTree();
            Set<Row> rowSet = bTree.get(value);
            if (rowSet == null) {
                rowSet = new HashSet<>();
            }
            rowSet.add(row);
            bTree.put(value, rowSet);
        }
        else if(schemaElement.getDataType().equals(DataType.BOOLEAN)){
            BTree<Value<Boolean>, Set<Row>> bTree = this.indices.get(schemaElement.getName()).getbTree();
            Set<Row> rowSet = bTree.get(value);
            if (rowSet == null) {
                rowSet = new HashSet<>();
            }
            rowSet.add(row);
            bTree.put(value, rowSet);
        }
        else if(schemaElement.getDataType().equals(DataType.VARCHAR)){
            BTree<Value<String>, Set<Row>> bTree = this.indices.get(schemaElement.getName()).getbTree();
            Set<Row> rowSet = bTree.get(value);
            if (rowSet == null) {
                rowSet = new HashSet<>();
            }
            rowSet.add(row);
            bTree.put(value, rowSet);
        }
    }

    public void deleteRows(List<Row> rows){
        for(int i = 0; i < rows.size(); i++){
            Row row = rows.get(i);
            for(int j = 0; j < row.getRowValues().size(); j++){
                this.uniqueValues.get(j).remove(row.getValue(j).getElement());
            }
            this.rows.remove(row);
        }
    }

    /**
     * If the value should go in a primary key column, checks are made to ensure that it is a unique value, and not
     * null.
     * @param value The value to be checked if it is unique.
     * @param schemaElement The current schema where the value will be added.
     * @param uniqueSet The columns set of unique values to ensure that the value is unique.
     */
    private void checkPrimaryKeyNull(Value<?> value, SchemaElement schemaElement, Set<?> uniqueSet){
        if(schemaElement.isPrimaryKey()) {
            checkUnique(value, schemaElement, uniqueSet);
            checkNull(value, schemaElement);
        }
    }

    /**
     * Checks that the value at the given column is unique.
     * @param value The value to be checked if it is unique.
     * @param schemaElement The current schema where the value will be added.
     * @param uniqueSet The column set of unique values.
     * @throws IllegalArgumentException If the value to be added isn't unique.
     */
    private void checkUnique(Value<?> value, SchemaElement schemaElement, Set<?> uniqueSet){
        if(value.getElement() == null && schemaElement.isUnique()){
            throw new IllegalArgumentException("\"" + value.toString() + "\" cannot be added to the \"" +
                    getName() + "." + schemaElement.toString() + "\" column because the column is unique, null values " +
                    "are not allowed.");
        }
        if(schemaElement.isUnique() && uniqueSet.contains(value)){
            throw new IllegalArgumentException("\"" + value.toString() + "\" cannot be added to the \"" +
                    getName() + "." + schemaElement.toString() + "\" column because the column is unique, and the value already exists.");
        }
    }

    /**
     * Checks that the value is not null if the column is a not null column.
     * @param value The value to be added to the row.
     * @param schemaElement The SchemaElement of the table at that value's column.
     * @throws IllegalArgumentException If the column is a not null column and the value is null.
     */
    private void checkNull(Value<?> value, SchemaElement schemaElement){
        if(schemaElement.isNotNull() && value.getElement() == null){
            throw new IllegalArgumentException("Values in the  \"" + getName() + "." + schemaElement.toString() + "\" " +
                    "column cannot be null. Please specify a value for this column.");
        }
    }

    /**
     * Determines if the element of the value is of the correct size. Only applies to varchar and decimal.
     * @param value The value being checked.
     * @param schemaElement The schema element where the value is to be added to.
     */
    private void checkLength(Value<?> value, SchemaElement schemaElement){
        if(value.getElement() != null){
            if(schemaElement.getDataType().equals(DataType.VARCHAR) && schemaElement.getVarcharLength() != null){
                String valueString = value.toString();
                if(valueString.length() > schemaElement.getVarcharLength()) {
                    throw new IllegalArgumentException("The length of value \"" + valueString + "\" exceeds the limit length " +
                            "of " + schemaElement.getVarcharLength() + " in the \"" + this.name + "." + schemaElement.getName() + "\" column.");
                }
            }
            if(schemaElement.getDataType().equals(DataType.DECIMAL) &&
                    (schemaElement.getWholeNumberLength() != null && schemaElement.getFractionalLength() != null)){
                String[] parts = value.toString().split("\\.");
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
        }
    }

    /**
     * Doesn't look like I need, but I'll keep around for a bit.
     * @param schemaElement
     */
    private void checkDefaultValue(SchemaElement schemaElement){
        //This is only called if the column is a primary key as default values cannot be null.
        if(schemaElement.getDefaultValue() != null){
            throw new IllegalArgumentException("Values in the  \"" + getName() + "." + schemaElement.toString() + "\" " +
                    "column cannot have a default value as it is a primary key. ");
        }
    }

    /**
     * If the value is null, and the column has a default value, the value's element will become the default value.
     * @param value The value whose element is potentially null.
     * @param schemaElement The SchemaElement that might have defined a default value.
     */
    private void changeDefaultValue(Value value, SchemaElement schemaElement){
        if(value.getElement() == null && schemaElement.isHasDefault()){
            String defaultValue = schemaElement.getDefaultValue();
            if(schemaElement.getDataType().equals(DataType.INT)){
                try{
                    int i = Integer.parseInt(defaultValue);
                    value.setElement(i);
                    value.setDataType(DataType.INT);
                }catch(Exception e){
                    throw new IllegalArgumentException("The schema default data type did not match the schema data type.");
                }
            }
            else if(schemaElement.getDataType().equals(DataType.DECIMAL)){
                try{
                    double d = Double.parseDouble(defaultValue);
                    value.setElement(d);
                    value.setDataType(DataType.DECIMAL);
                }catch(Exception e){
                    throw new IllegalArgumentException("The schema default data type did not match the schema data type.");
                }
            }
            else if(schemaElement.getDataType().equals(DataType.BOOLEAN)){
                try{
                    if(defaultValue.toLowerCase().equals("true") || defaultValue.toLowerCase().equals("false")){
                        Boolean b = Boolean.parseBoolean(defaultValue);
                        value.setElement(b);
                        value.setDataType(DataType.BOOLEAN);
                    }
                }catch(Exception e){
                    throw new IllegalArgumentException("The schema default data type did not match the schema data type.");
                }
            }
            else {
                //If we get here, we know it's a string, so use it.
                value.setElement(defaultValue);
                value.setDataType(DataType.VARCHAR);
            }
        }
    }

    /**
     * For testing. Prints the table to see that it operations worked.
     */
    public void printTable(){
        for(SchemaElement s : this.schema){
            System.out.print(s + " ");
        }
        System.out.println("");
        for(Row r : this.rows){
            for(Value v : r.getRowValues()){
                System.out.print(v.toString() + " ");
            }
            System.out.println("");
        }
    }

    public String returnTable(){
        StringBuilder sb = new StringBuilder();
        for(SchemaElement s : this.schema){
            sb.append(s + " ");
        }
        sb.append("\n");
        for(Row r : this.rows){
            for(Value v : r.getRowValues()){
                sb.append(v.toString() + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    //Test for table. Better testing can now be done in the Test class.
    public static void main(String[] args) {
        SchemaElement s1 = new SchemaElement("int", DataType.INT, true, false, true, false, null, null, null, null, "table1");
        SchemaElement s2 = new SchemaElement("double", DataType.DECIMAL, false, false, false, true, "2.36", 1, 2, null, "table1");
        SchemaElement s3 = new SchemaElement("string", DataType.VARCHAR, false, true, false, false, null,null, null, null, "table1");
        SchemaElement s4 = new SchemaElement("bool", DataType.BOOLEAN, false, false, false, false, null,null, null, null, "table1");
//        SchemaElement s5 = new SchemaElement("bool", DataType.BOOLEAN, false, false, false, null);
        List<SchemaElement> schema = new LinkedList<SchemaElement>();
        schema.add(s1);
        schema.add(s2);
        schema.add(s3);
        schema.add(s4);
//        schema.add(s5);

        Value<Integer> v1 = new Value<Integer>(5);
        Value<Double> v2 = new Value<Double>(null);
        Value<String> v3 = new Value<String>("test");
        Value<Boolean> v4 = new Value<Boolean>(true);
        List<Value> l = new LinkedList<>();
        l.add(v1);
        l.add(v2);
        l.add(v3);
        l.add(v4);

        Table table = new Table("table1", schema);
        table.insertRow(l);

        Value<Integer> v5 = new Value<Integer>(6);
        Value<Double> v6 = new Value<Double>(2.30);
        Value<String> v7 = new Value<String>("next");
        Value<Boolean> v8 = new Value<Boolean>(false);
//        Value<Boolean> v9 = new Value<Boolean>(false);
        List<Value> l2 = new LinkedList<>();
        l2.add(v5);
        l2.add(v6);
        l2.add(v7);
        l2.add(v8);
//        l2.add(v9);

        table.insertRow(l2);
        table.printTable();
        System.out.println("");
        List<Row> r = new LinkedList<>();
        r.add(table.getRows().get(0));
        table.deleteRows(r);
        table.printTable();
        System.out.println("");

        Value<Integer> v9 = new Value<Integer>(5);
        Value<Double> v10 = new Value<Double>(null);
        Value<String> v11 = new Value<String>("test");
        Value<Boolean> v12 = new Value<Boolean>(false);
        List<Value> l3 = new LinkedList<>();
        l3.add(v9);
        l3.add(v10);
        l3.add(v11);
        l3.add(v12);
        table.insertRow(l3);
        table.printTable();
    }
}