package utilities;

import dataStructures.*;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnID;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition.Operator;

import java.util.*;


/**
 * Created by Avi on 12/26/2016.
 */
public class ConditionHandler {
    private Condition where;
    private Table table;

    public ConditionHandler(Condition where, Table table){
        this.where = where;
        this.table = table;
    }

    public Set<Row> evaluateCondition(){
        Object left = this.where.getLeftOperand();
        Object right = this.where.getRightOperand();
        Operator operator = this.where.getOperator();
        Table table = evaluateCondition(left, operator, right);
        Set<Row> effectedRows = new HashSet<>();
        for(Row row: table.getRows()){
            effectedRows.add(row);
        }
        return effectedRows;
    }

    private Table evaluateCondition(Object left, Operator operator, Object right){
        if(!operator.equals(Operator.AND) && !operator.equals(Operator.OR)) {
            //Cases for String = String
            if(left instanceof String && right instanceof String){
                return makeTableFromValues((String) left, operator, (String) right);
            }
            //Cases for Column = String
            if (left instanceof ColumnID && right instanceof String) {
                return makeTableFromColumnValue((ColumnID) left, operator, (String) right);
            }
            if (left instanceof String && right instanceof ColumnID) {
                if(operator.equals(Operator.LESS_THAN)){
                    operator = Operator.GREATER_THAN;
                }
                if(operator.equals(Operator.GREATER_THAN)){
                    operator = Operator.LESS_THAN;
                }
                if(operator.equals(Operator.GREATER_THAN_OR_EQUALS)){
                    operator = Operator.lESS_THAN_OR_EQUALS;
                }
                if(operator.equals(Operator.lESS_THAN_OR_EQUALS)){
                    operator = Operator.GREATER_THAN_OR_EQUALS;
                }
                return makeTableFromColumnValue((ColumnID) right, operator, (String) left);
            }
            //Case for Column = Column
            if(left instanceof ColumnID && right instanceof ColumnID){
                return makeTableFromColumns((ColumnID) left, operator, (ColumnID) right);
            }
        }
        if(operator.equals(Operator.AND)){
            Condition leftCondition = (Condition) left;
            Condition rightCondition = (Condition) right;
            return intersection(evaluateCondition(leftCondition.getLeftOperand(), leftCondition.getOperator(), leftCondition.getRightOperand()),
                    evaluateCondition(rightCondition.getLeftOperand(), rightCondition.getOperator(), rightCondition.getRightOperand()));
        }
        if(operator.equals(Operator.OR)){
            Condition leftCondition = (Condition) left;
            Condition rightCondition = (Condition) right;
            return union(evaluateCondition(leftCondition.getLeftOperand(), leftCondition.getOperator(), leftCondition.getRightOperand()),
                    evaluateCondition(rightCondition.getLeftOperand(), rightCondition.getOperator(), rightCondition.getRightOperand()));
        }
        throw new IllegalArgumentException("Condition tree is not possible. Bad input!");
    }

    private Table makeTableFromValues(String value1, Operator operator, String value2){
        List<SchemaElement> schemaElements = new LinkedList<>();
        //Copy over all schema elements to new list so we don't mess with pointers to main table.
        for(SchemaElement schemaElement : this.table.getSchema()){
            SchemaElement s = new SchemaElement(schemaElement.getName(), schemaElement.getDataType(), schemaElement.isPrimaryKey(), schemaElement.isUnique(), schemaElement.isNotNull(),
                    schemaElement.isHasDefault(), schemaElement.getDefaultValue(), schemaElement.getWholeNumberLength(), schemaElement.getFractionalLength(),
                    schemaElement.getVarcharLength(), this.table.getName());
            s.setIndexed(false);
            schemaElements.add(s);
        }

        Table table = new Table("temp", schemaElements); //change the schema elements so none are indexed.

        //JD: regarding your use of compareTo, the API only says its return result is negative, 0, or positive - it doesn't guarantee it being -1 or 1 for less than and greater than
        //you should test for 0, <0, and >0
        for (Row row : this.table.getRows()) {
            //figure out operator
            switch (operator.toString()) {
                case "=":
                    if (value1.equals(value2)) {
                        table.insertRow(row);
                    }
                    break;
                case "<>":
                    if (!value1.equals(value2)) {
                        table.insertRow(row);
                    }
                    break;
                case "<":
                    if (value1.compareTo(value2) == -1) {
                        table.insertRow(row);
                    }
                    break;
                case ">":
                    if (value1.compareTo(value2) == 1) {
                        table.insertRow(row);
                    }
                    break;
                case "<=":
                    if (value1.compareTo(value2) == -1 || value1.equals(value2)) {
                        table.insertRow(row);
                    }
                    break;
                case ">=":
                    if (value1.compareTo(value2) == 1 || value1.equals(value2)) {
                        table.insertRow(row);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("This is not a valid operator!");
            }
        }
        return table;
    }

    private Table makeTableFromColumnValue(ColumnID columnID, Operator operator, String value){
        List<SchemaElement> schemaElements = new LinkedList<>();
        //Copy over all schema elements to new list so we don't mess with pointers to main table.
        for(SchemaElement schemaElement : this.table.getSchema()){
            SchemaElement s = new SchemaElement(schemaElement.getName(), schemaElement.getDataType(), schemaElement.isPrimaryKey(), schemaElement.isUnique(), schemaElement.isNotNull(),
                    schemaElement.isHasDefault(), schemaElement.getDefaultValue(), schemaElement.getWholeNumberLength(), schemaElement.getFractionalLength(),
                    schemaElement.getVarcharLength(), this.table.getName());
            s.setIndexed(false);
            schemaElements.add(s);
        }

        Table table = new Table("temp", schemaElements); //change the schema elements so none are indexed.
        int index;
        try{
            index = this.table.getColumnListIndices().get(columnID.getColumnName());
        }catch (Exception e){
            try{
                index = this.table.getColumnListIndices().get(columnID.getTableName() + "." + columnID.getColumnName());
            }catch(Exception e1){
                throw new IllegalArgumentException(columnID.getColumnName() + " is either not a valid column name, or " +
                        "it is too ambiguous. If you are selecting from more than one column, and two columns have the " +
                        "same name, use the \"tableName.columnName\" notation to refer to it.");
            }
        }
        DataType columnDataType = this.table.getSchema().get(index).getDataType();
        //Check if value is a boolean value, and strip single quotes.
        if(columnDataType.equals(DataType.BOOLEAN) && (value.equals("'true'") || value.equals("'false'"))){
            value = value.substring(1, value.length() - 1);
        }
        //if this is a string
        if(value.startsWith("'") && value.endsWith("'")){
            value = value.substring(1, value.length() - 1); //removes single quotes
            if(!columnDataType.equals(DataType.VARCHAR)){
                throw new IllegalArgumentException("Cannot compare column \"" + this.table.getName() + "."
                        + this.table.getSchema().get(index).getName() + "\" with " + value + " as they" +
                        "are not the same type!");
            }
            if(this.table.getSchema().get(index).isIndexed()){
                columnIsIndexedComparator(table, operator, value, index);
            }
            else{
                tableColumnValueComparator(table, operator, value, index);
            }

            return table;
        }
        try{
            Integer i = Integer.parseInt(value);
            if(!columnDataType.equals(DataType.INT)){
                throw new IllegalArgumentException("Cannot compare column \"" + this.table.getName() + "."
                        + this.table.getSchema().get(index).getName() + "\" with " + value + " as they" +
                        "are not the same type!");
            }
            if(this.table.getSchema().get(index).isIndexed()){
                columnIsIndexedComparator(table, operator, i, index);
            }
            else{
                tableColumnValueComparator(table, operator, i, index);
            }
            return table;
        }catch (Exception e){

        }
        try{
            Double d = Double.parseDouble(value);
            if(!columnDataType.equals(DataType.DECIMAL)){
                throw new IllegalArgumentException("Cannot compare column \"" + this.table.getName() + "."
                        + this.table.getSchema().get(index).getName() + "\" with " + value + " as they" +
                        "are not the same type!");
            }
            if(this.table.getSchema().get(index).isIndexed()){
                columnIsIndexedComparator(table, operator, d, index);
            }
            else{
                tableColumnValueComparator(table, operator, d, index);
            }
            return table;
        }catch (Exception e) {

        }
        //Last possibility is that it's a boolean.
        Boolean b = null;
        if(value.toLowerCase().equals("true")){
            b = true;
        }
        if(value.toLowerCase().equals("false")){
            b = false;
        }
        if(b != null) {
            if (!columnDataType.equals(DataType.BOOLEAN)) {
                throw new IllegalArgumentException("Cannot compare column \"" + this.table.getName() + "."
                        + this.table.getSchema().get(index).getName() + "\" with " + value + " as they " +
                        "are not the same type!");
            }
            if(this.table.getSchema().get(index).isIndexed()){
                columnIsIndexedComparator(table, operator, b, index);
            }
            else{
                tableColumnValueComparator(table, operator, b, index);
            }
            return table;
        }

        //If we got here, then somehow the value wasn't an int, string, boolean, or double, so throw error
        throw new IllegalArgumentException("Only int, decimal, boolean, and varchar value types allowed.");
    }

    private Table makeTableFromColumns(ColumnID columnID1, Operator operator, ColumnID columnID2){
        List<SchemaElement> schemaElements = new LinkedList<>();
        //Copy over all schema elements to new list so we don't mess with pointers to main table.
        for(SchemaElement schemaElement : this.table.getSchema()){
            SchemaElement s = new SchemaElement(schemaElement.getName(), schemaElement.getDataType(), schemaElement.isPrimaryKey(), schemaElement.isUnique(), schemaElement.isNotNull(),
                    schemaElement.isHasDefault(), schemaElement.getDefaultValue(), schemaElement.getWholeNumberLength(), schemaElement.getFractionalLength(),
                    schemaElement.getVarcharLength(), this.table.getName());
            s.setIndexed(false);
            schemaElements.add(s);
        }

        Table table = new Table("temp", schemaElements); //change the schema elements so none are indexed.
        int col1Index;
        int col2Index;
        try{
            col1Index = this.table.getColumnListIndices().get(columnID1.getColumnName());
        }catch (Exception e){
            try{
                col1Index = this.table.getColumnListIndices().get(columnID1.getTableName() + "." + columnID1.getColumnName());
            }catch(Exception e1){
                throw new IllegalArgumentException(columnID1.getColumnName() + " is either not a valid column name, or " +
                        "it is too ambiguous. If you are selecting from more than one column, and two columns have the " +
                        "same name, use the \"tableName.columnName\" notation to refer to it.");
            }
        }
        try{
            col2Index = this.table.getColumnListIndices().get(columnID2.getColumnName());
        }catch (Exception e){
            try{
                col2Index = this.table.getColumnListIndices().get(columnID2.getTableName() + "." + columnID2.getColumnName());
            }catch(Exception e1){
                throw new IllegalArgumentException(columnID2.getColumnName() + " is either not a valid column name, or " +
                        "it is too ambiguous. If you are selecting from more than one column, and two columns have the " +
                        "same name, use the \"tableName.columnName\" notation to refer to it.");
            }
        }

        DataType col1DataType = this.table.getSchema().get(col1Index).getDataType();
        DataType col2DataType = this.table.getSchema().get(col2Index).getDataType();
        if(!col1DataType.equals(col2DataType)){
            throw new IllegalArgumentException("Columns must be of the same type to be compared!");
        }
        if(this.table.getSchema().get(col1Index).isIndexed() && !this.table.getSchema().get(col2Index).isIndexed()){
            oneColumnIsIndexedComparator(table, operator, col1Index, col2Index);
        }
        else if(this.table.getSchema().get(col2Index).isIndexed() && !this.table.getSchema().get(col1Index).isIndexed()){
            oneColumnIsIndexedComparator(table, operator, col2Index, col1Index);
        }
        else if(this.table.getSchema().get(col1Index).isIndexed() && this.table.getSchema().get(col2Index).isIndexed()){
            bothColumnsIndexedComparator(table, operator, col1Index, col2Index);
        }
        else{
            tableColumnsComparator(table, operator, col1Index, col2Index);
        }
        return table;
    }

    private void tableColumnValueComparator(Table table, Operator operator, Object value, int index){
        for (Row row : this.table.getRows()) {
            //figure out operator
            int i = "harry".compareTo("avi");
            switch (operator.toString()) {
                case "=":
                    if ((row.getValue(index).getElement() != null && value != null) &&
                            row.getValue(index).getElement().equals(value)) {
                        table.insertRow(row);
                    }
                    break;
                case "<>":
                    if ((row.getValue(index).getElement() != null && value != null) &&
                            !row.getValue(index).getElement().equals(value)) {
                        table.insertRow(row);
                    }
                    break;
                case "<":
                    if ((row.getValue(index).getElement() != null && value != null) &&
                            row.getValue(index).getElement().compareTo(value) < 0) {
                        table.insertRow(row);
                    }
                    break;
                case ">":
                    if ((row.getValue(index).getElement() != null && value != null) &&
                            row.getValue(index).getElement().compareTo(value) > 0) {
                        table.insertRow(row);
                    }
                    break;
                case "<=":
                    if ((row.getValue(index).getElement() != null && value != null) &&
                            row.getValue(index).getElement().compareTo(value) < 0 || row.getValue(index).getElement().equals(value)) {
                        table.insertRow(row);
                    }
                    break;
                case ">=":
                    if ((row.getValue(index).getElement() != null && value != null) &&
                            row.getValue(index).getElement().compareTo(value) > 0 || row.getValue(index).getElement().equals(value)) {
                        table.insertRow(row);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("This is not a valid operator!");
            }
        }
    }

    private void columnIsIndexedComparator(Table table, Operator operator, Object value, int index){
        //Get the list of all entries in the bTree in order.
        String columnName = this.table.getSchema().get(index).getName();
        BTree bTree;
        try{
            bTree = this.table.getIndices().get(columnName).getbTree();
        }catch(Exception e){
            String[] parts = columnName.split("\\.");
            bTree = this.table.getIndices().get(parts[1]).getbTree();
        }
        List<BTree.Entry> orderedEntries = bTree.getOrderedEntries();
        for(BTree.Entry entry : orderedEntries){
            Value rowValue = (Value) entry.getKey();
            //figure out operator
            switch (operator.toString()) {
                case "=":
                    if ((rowValue.getElement() != null && value != null) && rowValue.getElement().equals(value)) {
                        Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                        for (Row row : rows) {
                            table.insertRow(row);
                        }
                    }
                    break;
                case "<>":
                    if ((rowValue.getElement() != null && value != null) && !rowValue.getElement().equals(value)) {
                        Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                        for (Row row : rows) {
                            table.insertRow(row);
                        }
                    }
                    break;
                case "<":
                    if ((rowValue.getElement() != null && value != null) && rowValue.getElement().compareTo(value) < 0) {
                        Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                        for (Row row : rows) {
                            table.insertRow(row);
                        }
                    }
                    break;
                case ">":
                    if ((rowValue.getElement() != null && value != null) && rowValue.getElement().compareTo(value) > 0) {
                        Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                        for (Row row : rows) {
                            table.insertRow(row);
                        }
                    }
                    break;
                case "<=":
                    if ((rowValue.getElement() != null && value != null) &&
                            rowValue.getElement().compareTo(value) < 0 || rowValue.getElement().equals(value)) {
                        Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                        for (Row row : rows) {
                            table.insertRow(row);
                        }
                    }
                    break;
                case ">=":
                    if ((rowValue.getElement() != null && value != null) &&
                            rowValue.getElement().compareTo(value) > 0 || rowValue.getElement().equals(value)) {
                        Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                        for (Row row : rows) {
                            table.insertRow(row);
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("This is not a valid operator!");
            }
        }
    }

    private void tableColumnsComparator(Table table, Operator operator, int col1Index, int col2Index){
        for(Row row : this.table.getRows()) {
            if (row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) {
                //figure out operator
                switch (operator.toString()) {
                    case "=":
                        if ((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                                row.getValue(col1Index).getElement().equals(row.getValue(col2Index).getElement())) {
                            table.insertRow(row);
                        }
                        break;
                    case "<>":
                        if ((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                                !row.getValue(col1Index).getElement().equals(row.getValue(col2Index).getElement())) {
                            table.insertRow(row);
                        }
                        break;
                    case "<":
                        if ((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                                row.getValue(col1Index).getElement().compareTo(row.getValue(col2Index).getElement()) < 0) {
                            table.insertRow(row);
                        }
                        break;
                    case ">":
                        if ((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                                row.getValue(col1Index).getElement().compareTo(row.getValue(col2Index).getElement()) > 0) {
                            table.insertRow(row);
                        }
                        break;
                    case "<=":
                        if ((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                                row.getValue(col1Index).getElement().compareTo(row.getValue(col2Index).getElement()) < 0 ||
                                row.getValue(col1Index).getElement().equals(row.getValue(col2Index).getElement())) {
                            table.insertRow(row);
                        }
                        break;
                    case ">=":
                        if ((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                                row.getValue(col1Index).getElement().compareTo(row.getValue(col2Index).getElement()) > 0 ||
                                row.getValue(col1Index).getElement().equals(row.getValue(col2Index).getElement())) {
                            table.insertRow(row);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("This is not a valid operator!");
                }
            }
        }
    }

    private void oneColumnIsIndexedComparator(Table table, Operator operator, int indexedColIndex, int notIndexedColIndex){
        //Get the list of all entries in the bTree in order.
        String columnName = this.table.getSchema().get(indexedColIndex).getName();
        BTree bTree;
        try{
            bTree = this.table.getIndices().get(columnName).getbTree();
        }catch(Exception e){
            String[] parts = columnName.split("\\.");
            bTree = this.table.getIndices().get(parts[1]).getbTree();
        }
        List<BTree.Entry> orderedEntries = bTree.getOrderedEntries();

        for(BTree.Entry entry : orderedEntries){
            Value rowValue = (Value) entry.getKey();
            //figure out operator
            switch (operator.toString()) {
                case "=": {
                    Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                    Set<Row> rowsToBeAdded = new HashSet<>();
                    for (Row row : rows) {
                        if ((rowValue.getElement() != null && row.getValue(notIndexedColIndex).getElement() != null) &&
                                rowValue.getElement().equals(row.getValue(notIndexedColIndex).getElement())) {
                            rowsToBeAdded.add(row);
                        }
                    }
                    for (Row row : rowsToBeAdded) {
                        table.insertRow(row);
                    }
                    break;
                }
                case "<>": {
                    Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                    Set<Row> rowsToBeAdded = new HashSet<>();
                    for (Row row : rows) {
                        if ((rowValue.getElement() != null && row.getValue(notIndexedColIndex).getElement() != null) &&
                                !rowValue.getElement().equals(row.getValue(notIndexedColIndex).getElement())) {
                            rowsToBeAdded.add(row);
                        }
                    }
                    for (Row row : rowsToBeAdded) {
                        table.insertRow(row);
                    }
                    break;
                }
                case "<": {
                    Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                    Set<Row> rowsToBeAdded = new HashSet<>();
                    for (Row row : rows) {
                        if ((rowValue.getElement() != null && row.getValue(notIndexedColIndex).getElement() != null) &&
                                rowValue.getElement().compareTo(row.getValue(notIndexedColIndex).getElement()) < 0) {
                            rowsToBeAdded.add(row);
                        }
                    }
                    for (Row row : rowsToBeAdded) {
                        table.insertRow(row);
                    }
                    break;
                }
                case ">": {
                    Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                    Set<Row> rowsToBeAdded = new HashSet<>();
                    for (Row row : rows) {
                        if ((rowValue.getElement() != null && row.getValue(notIndexedColIndex).getElement() != null) &&
                                rowValue.getElement().compareTo(row.getValue(notIndexedColIndex).getElement()) > 0) {
                            rowsToBeAdded.add(row);
                        }
                    }
                    for (Row row : rowsToBeAdded) {
                        table.insertRow(row);
                    }
                    break;
                }
                case "<=": {
                    Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                    Set<Row> rowsToBeAdded = new HashSet<>();
                    for (Row row : rows) {
                        if ((rowValue.getElement() != null && row.getValue(notIndexedColIndex).getElement() != null) &&
                                rowValue.getElement().compareTo(row.getValue(notIndexedColIndex).getElement()) < 0 ||
                                rowValue.getElement().equals(row.getValue(notIndexedColIndex).getElement()) ) {
                            rowsToBeAdded.add(row);
                        }
                    }
                    for (Row row : rowsToBeAdded) {
                        table.insertRow(row);
                    }
                    break;
                }
                case ">=": {
                    Set<Row> rows = (Set<Row>) bTree.get(rowValue);
                    Set<Row> rowsToBeAdded = new HashSet<>();
                    for (Row row : rows) {
                        if ((rowValue.getElement() != null && row.getValue(notIndexedColIndex).getElement() != null) &&
                                rowValue.getElement().compareTo(row.getValue(notIndexedColIndex).getElement()) > 0 ||
                                rowValue.getElement().equals(row.getValue(notIndexedColIndex).getElement()) ) {
                            rowsToBeAdded.add(row);
                        }
                    }
                    for (Row row : rowsToBeAdded) {
                        table.insertRow(row);
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("This is not a valid operator!");
            }
        }
    }

    private void bothColumnsIndexedComparator(Table table, Operator operator, int col1Index, int col2Index){
        //Get the list of all entries in the bTree in order.
        String column1Name = this.table.getSchema().get(col1Index).getName();

        BTree bTree1;
        try{
            bTree1 = this.table.getIndices().get(column1Name).getbTree();
        }catch(Exception e){
            String[] parts = column1Name.split("\\.");
            bTree1 = this.table.getIndices().get(parts[1]).getbTree();
        }
        List<BTree.Entry> orderedEntries1 = bTree1.getOrderedEntries();
        String column2Name = this.table.getSchema().get(col2Index).getName();

        BTree bTree2;
        try{
            bTree2 = this.table.getIndices().get(column2Name).getbTree();
        }catch(Exception e){
            String[] parts = column2Name.split("\\.");
            bTree2 = this.table.getIndices().get(parts[1]).getbTree();
        }
        List<BTree.Entry> orderedEntries2 = bTree2.getOrderedEntries();

        //Make a set to hold all rows in both btrees and iterate over them.
        Set<Row> rows = new HashSet<>();
        for(int i = 0; i < Math.max(orderedEntries1.size(), orderedEntries2.size()); i++) {
            if (i < orderedEntries1.size() && i < orderedEntries2.size()) {
                Set<Row> r = (Set<Row>) orderedEntries1.get(i).getValue();
                for (Row row : r) {
                    if (!rows.contains(row)) {
                        rows.add(row);
                    }
                }
                r = (Set<Row>) orderedEntries2.get(i).getValue();
                for (Row row : r) {
                    if (!rows.contains(row)) {
                        rows.add(row);
                    }
                }
            } else if (i < orderedEntries2.size()) {
                Set<Row> r = (Set<Row>) orderedEntries2.get(i).getValue();
                for (Row row : r) {
                    if (!rows.contains(row)) {
                        rows.add(row);
                    }
                }
            } else {
                Set<Row> r = (Set<Row>) orderedEntries1.get(i).getValue();
                for (Row row : r) {
                    if (!rows.contains(row)) {
                        rows.add(row);
                    }
                }
            }
        }
        for(Row row : rows){
            //figure out operator
            switch (operator.toString()) {
                case "=": {
                    if((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                            row.getValue(col1Index).getElement().equals(row.getValue(col2Index).getElement())){
                        table.insertRow(row);
                    }
                    break;
                }
                case "<>": {
                    if((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                            !row.getValue(col1Index).getElement().equals(row.getValue(col2Index).getElement())){
                        table.insertRow(row);
                    }
                    break;
                }
                case "<": {
                    if((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                            row.getValue(col1Index).getElement().compareTo(row.getValue(col2Index).getElement()) < 0){
                        table.insertRow(row);
                    }
                    break;
                }
                case ">": {
                    if((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                            row.getValue(col1Index).getElement().compareTo(row.getValue(col2Index).getElement()) > 0){
                        table.insertRow(row);
                    }
                    break;
                }
                case "<=": {
                    if((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                            row.getValue(col1Index).getElement().compareTo(row.getValue(col2Index).getElement()) < 0 ||
                            row.getValue(col1Index).getElement().equals(row.getValue(col2Index).getElement())){
                        table.insertRow(row);
                    }
                    break;
                }
                case ">=": {
                    if((row.getValue(col1Index).getElement() != null && row.getValue(col2Index).getElement() != null) &&
                            row.getValue(col1Index).getElement().compareTo(row.getValue(col2Index).getElement()) > 0 ||
                            row.getValue(col1Index).getElement().equals(row.getValue(col2Index).getElement())){
                        table.insertRow(row);
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("This is not a valid operator!");
            }
        }
    }

    private Table union(Table table1, Table table2){
        Set<Row> rowSet = new HashSet<>();
        for(Row row : table2.getRows()){
            rowSet.add(row);
        }
        for(Row row : table1.getRows()){
            if(!rowSet.contains(row)){
                rowSet.add(row);
            }
        }
        List<Row> rows = new LinkedList<>();
        for(Row row : rowSet){
            rows.add(row);
        }
        Table table =  new Table("Union Table", table1.getSchema());
        table.setRows(rows);
        return table;
    }

    private Table intersection(Table table1, Table table2){
        Set<Row> rowSet = new HashSet<>();
        LinkedList<Row> rows = new LinkedList<>();
        for(Row row : table1.getRows()){
            rowSet.add(row);
        }
        for(Row row : table2.getRows()){
            if(rowSet.contains(row)){
                rows.add(row);
            }
        }
        Table table = new Table("Intersection Table", table1.getSchema());
        table.setRows(rows);
        return table;
    }
}
