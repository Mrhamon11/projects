package utilities;

import dataStructures.*;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Avi on 12/29/2016.
 */
public class SelectWhere {
    private SelectQuery selectQuery;
    private ColumnID[] columnNames;
    private String[] tableNames;
    private boolean distinct;
    private Condition where;
    private Map<ColumnID,SelectQuery.FunctionInstance> functionMap;
    private SelectQuery.OrderBy[] orderBys;
    private List<SchemaElement> schemaElements;
    private Table table;

    public SelectWhere(SQLQuery sqlQuery, Map<String, Table> tableMap){
        this.selectQuery = (SelectQuery) sqlQuery;
        this.columnNames = this.selectQuery.getSelectedColumnNames();
        this.tableNames = this.selectQuery.getFromTableNames();
        this.distinct = this.selectQuery.isDistinct();
        this.where = this.selectQuery.getWhereCondition();
        this.functionMap = this.selectQuery.getFunctionMap();
        this.orderBys = this.selectQuery.getOrderBys();
        List<Table> tables = getAllTables(tableMap);
        //JD: effective but very expensive to create the cartesian product in all cases. What if your DB had 5 million rows in each table?
        createSelectTable(tables);
    }

    public Table tableFromConditions(){
        if(this.where != null){
            ConditionHandler ch = new ConditionHandler(this.where, this.table);
            Set<Row> affectedRows = ch.evaluateCondition();
            removeUnaffectedRows(affectedRows);
        }
        if(!this.columnNames[0].getColumnName().equals("*")){
            cutColumns();
        }
        if(this.distinct){
            removeNotDistinctRows();
        }
        if(!this.functionMap.keySet().isEmpty()){
            applyFunction();
        }
        if(this.orderBys.length > 0){
            orderBy();
        }
        return this.table;
    }

    private List<Table> getAllTables(Map<String, Table> tableMap){
        List<Table> tables = new ArrayList<>();
        for(String tableName : this.tableNames){
            if(tableMap.containsKey(tableName)){
                tables.add(tableMap.get(tableName));
            }
            else{
                throw new IllegalArgumentException("No table with the name \"" + tableName + "\" exists!");
            }
        }
        return tables;
    }

    private void createSelectTable(List<Table> tables){
        List<List<Row>> allTableRows = getRowsFromTables(tables);
        allTableRows = cartesianProduct(allTableRows);
        this.schemaElements = makeNewTableSchema(tables);
        if(tables.size() > 1){
            allTableRows = reverseLists(allTableRows);
        }
        if(!this.columnNames[0].getColumnName().equals("*")) {
            checkColumnNames();
        }
        this.table = new Table("Select Table", this.schemaElements);
        addRowsToTable(allTableRows);
        reIndexTable();
//        this.table.printTable();
    }

    private void checkColumnNames(){
        Map<String, Integer> columnNames = new HashMap<>();
        for(SchemaElement schemaElements : this.schemaElements){
            columnNames.put(schemaElements.getName(), 1);
        }
        for(ColumnID columnID : this.columnNames){
            if(columnNames.get(columnID.getColumnName()) == null &&
                    columnNames.get(columnID.getTableName() + "." + columnID.getColumnName()) == null){
                throw new IllegalArgumentException("column doesn't exist");
            }
        }
    }

    private List<SchemaElement> makeNewTableSchema(List<Table> tables){
        //First get full schema list from all tables.
        List<SchemaElement> schemaElements = new ArrayList<>();
        Map<String, Integer> nameCount = new HashMap<>();
        Map<String, List<Integer>> indexMap = new HashMap<>();
        int index = 0;
        for(Table table : tables){
            for(int i = 0; i < table.getSchema().size(); i++){
                SchemaElement schemaElement = table.getSchema().get(i);
                SchemaElement newSchema = new SchemaElement(schemaElement.getName(),schemaElement.getDataType(),schemaElement.isPrimaryKey(),
                        schemaElement.isUnique(), schemaElement.isNotNull(), schemaElement.isHasDefault(), schemaElement.getDefaultValue(),
                        schemaElement.getWholeNumberLength(), schemaElement.getFractionalLength(), schemaElement.getVarcharLength(),
                        schemaElement.getTableName());
                if(schemaElement.isIndexed()){
                    newSchema.setIndexed(true);
                }
                Integer count = nameCount.get(newSchema.getName());
                if(count == null){
                    nameCount.put(newSchema.getName(), 1);
                    List<Integer> indexList = new ArrayList<>();
                    indexList.add(index++);
                    indexMap.put(newSchema.getName(), indexList);
                }
                else{
                    nameCount.put(newSchema.getName(), ++count);
                    List<Integer> indexList = indexMap.get(newSchema.getName());
                    indexList.add(index++);
                    indexMap.put(newSchema.getName(), indexList);
                }
                schemaElements.add(newSchema);
            }
        }
        //Check if two columns have the same name, and change it to tableName.columnName if they do.
        for(SchemaElement schemaElement : schemaElements){
            Integer count = nameCount.get(schemaElement.getName());
            if(count != null && (count > 1 && count == indexMap.get(schemaElement.getName()).size())){
                String name = schemaElement.getName();
                for(int i = 0; i < count; i++){
                    List<Integer> indexList = indexMap.get(name);
                    SchemaElement s = schemaElements.get(indexList.get(i));
                    s.setName(s.getTableName() + "." + name);
                    indexList.set(i, null);
                    indexMap.put(name, indexList);
                    int newCount = nameCount.get(name) - 1;
                    nameCount.put(name, newCount);
                }
            }
        }
        return schemaElements;
    }

    public List<List<Row>> cartesianProduct(List<List<Row>> rows){
        if(rows.size() == 0){
            throw new IllegalArgumentException("All Select statements must refer to at least one table!");
        }
        return cartesianProduct(0, rows);
    }

    private List<List<Row>> cartesianProduct(int index, List<List<Row>> rows){
        List<List<Row>> allTableRows = new ArrayList<>();
        if(index == rows.size()){
            allTableRows.add(new ArrayList<Row>());
        }
        else{
            for(Row row : rows.get(index)){
                for(List<Row> r : cartesianProduct(index + 1, rows)){
                    r.add(row);
                    allTableRows.add(r);
                }
            }
        }
        return allTableRows;
    }

    private List<List<Row>> getRowsFromTables(List<Table> tables){
        List<List<Row>> allTableRows = new ArrayList<>();
        for(Table table : tables){
            List<Row> tableRows = new ArrayList<>();
            for(Row row : table.getRows()){
                tableRows.add(row);
            }
            allTableRows.add(tableRows);
        }
        return allTableRows;
    }

    private List<List<Row>> reverseLists(List<List<Row>> rows){
        List<List<Row>> allTableRows = new ArrayList<>();
        for(List<Row> row : rows) {
            List<Row> r = new ArrayList<>();
            for (int i = row.size() - 1; i >= 0; i--) {
                r.add(row.get(i));
            }
            allTableRows.add(r);
        }
        return allTableRows;
    }

    private void addRowsToTable(List<List<Row>> rows){
        for(List<Row> row : rows){
            Row newRow = new Row();
            for(Row currentRow : row){
                for(Value value : currentRow.getRowValues()){
                    newRow.addValue(value);
                }
            }
            this.table.insertRow(newRow);
        }
    }

    private void reIndexTable(){
        for(SchemaElement schemaElement : this.table.getSchema()){
            if(schemaElement.isIndexed()) {
                schemaElement.setIndexName(schemaElement.getName() + "_index");
                int rowIndex = this.table.getColumnListIndices().get(schemaElement.getName());
                Index<?> index;
                if (schemaElement.getDataType().equals(ColumnDescription.DataType.INT)) {
                    index = new Index<Integer>(schemaElement.getName() + "_index", this.table.getName());
                } else if (schemaElement.getDataType().equals(ColumnDescription.DataType.DECIMAL)) {
                    index = new Index<Double>(schemaElement.getName() + "_index", this.table.getName());
                } else if (schemaElement.getDataType().equals(ColumnDescription.DataType.BOOLEAN)) {
                    index = new Index<Boolean>(schemaElement.getName() + "_index", this.table.getName());
                } else if (schemaElement.getDataType().equals(ColumnDescription.DataType.VARCHAR)) {
                    index = new Index<String>(schemaElement.getName() + "_index", this.table.getName());
                } else {
                    throw new IllegalArgumentException("Only int, decimal, boolean and varchar allowed for column types!");
                }
                for (Row row : this.table.getRows()) {
                    if (row.getValue(rowIndex).getDataType() != null) {
                        Set<Row> rowSet = (Set<Row>) index.getbTree().get(row.getValue(rowIndex));
                        if (rowSet == null) {
                            rowSet = new HashSet<>();
                        }
                        rowSet.add(row);
                        index.getbTree().put(row.getValue(rowIndex), rowSet);
                    }
                }
                this.table.getIndices().put(schemaElement.getName(), index);
            }
        }
    }

    private void removeUnaffectedRows(Set<Row> affectedRows){
        List<Row> rows = this.table.getRows();
        Iterator<Row> it = rows.iterator();
        while(it.hasNext()){
            Row row = it.next();
            if(!affectedRows.contains(row)){
                it.remove();
            }
        }
    }

    private void cutColumns(){
        List<SchemaElement> schemaElements = new LinkedList<>();
        List<Row> rows = new LinkedList<>();
        Map<String, Integer> colListIndexMap = new HashMap<>();

        for(int j = 0; j < this.columnNames.length; j++){
            ColumnID columnID = this.columnNames[j];
            int columnIndex;
            try{
                columnIndex = this.table.getColumnListIndices().get(columnID.getColumnName());
            }catch (Exception e){
                try{
                    columnIndex = this.table.getColumnListIndices().get(columnID.getTableName() + "." + columnID.getColumnName());
                }catch(Exception e1){
                    throw new IllegalArgumentException(columnID.getColumnName() + " is either not a valid column name, or " +
                            "it is too ambiguous. If you are selecting from more than one column, and two columns have the " +
                            "same name, use the \"tableName.columnName\" notation to refer to it.");
                }
            }
            schemaElements.add(this.table.getSchema().get(columnIndex));
            colListIndexMap.put(schemaElements.get(j).getName(), j);
            if(rows.isEmpty()) {
                for (Row row : this.table.getRows()) {
                    Row newRow = new Row();
                    newRow.addValue(row.getValue(columnIndex));
                    rows.add(newRow);
                }
            }
            else{
                for(int i = 0; i < this.table.getRows().size(); i++){
                    rows.get(i).addValue(this.table.getRows().get(i).getValue(columnIndex));
                }
            }
        }
        this.table.setSchema(schemaElements);
        this.table.setRows(rows);
        this.table.setColumnListIndices(colListIndexMap);
    }

    private void removeNotDistinctRows(){
        Map<Row, Integer> map = new HashMap<>();
        Iterator<Row> it = this.table.getRows().iterator();
        while(it.hasNext()){
            Row row = it.next();
            Integer count = map.get(row);
            if(count == null){
                map.put(row, 1);
            }
            else{
                map.put(row, ++count);
                it.remove();
            }
        }
    }

    private void applyFunction(){
        List<SchemaElement> columns = new ArrayList<>();
        List<Row> rows = new ArrayList<>();
        Row newRow = new Row();
        for(ColumnID columnID : this.selectQuery.getSelectedColumnNames()){
            String columnName = columnID.getColumnName();
            int index;
            try{
                index = this.table.getColumnListIndices().get(columnID.getColumnName());
            }catch (Exception e){
                try{
                    index = this.table.getColumnListIndices().get(columnID.getTableName() + "." + columnID.getColumnName());
                    columnName = columnID.getTableName() + "." + columnID.getColumnName();
                }catch(Exception e1){
                    throw new IllegalArgumentException(columnID.getColumnName() + " is either not a valid column name, or " +
                            "it is too ambiguous. If you are selecting from more than one column, and two columns have the " +
                            "same name, use the \"tableName.columnName\" notation to refer to it.");
                }
            }
            index = this.table.getColumnListIndices().get(columnName);
            boolean isDistinct = this.functionMap.get(columnID).isDistinct;

            SchemaElement schemaElement = this.table.getSchema().get(index);
            SchemaElement s = new SchemaElement(schemaElement.getName(), schemaElement.getDataType(), schemaElement.isPrimaryKey(), schemaElement.isUnique(), schemaElement.isNotNull(),
            schemaElement.isHasDefault(), schemaElement.getDefaultValue(), schemaElement.getWholeNumberLength(), schemaElement.getFractionalLength(), schemaElement.getVarcharLength(),schemaElement.getTableName());

            if(schemaElement.isIndexed()){
                switch (this.functionMap.get(columnID).function.toString()){
                    case("SUM"):
                        if(!(schemaElement.getDataType().equals(ColumnDescription.DataType.INT) || schemaElement.getDataType().equals(ColumnDescription.DataType.DECIMAL))){
                            throw new IllegalArgumentException("Only ints and decimals can be summed");
                        }
                        calculateSumIndexed(columnName, newRow, s, columns, isDistinct);
                        break;
                    case("AVG"):
                        if(!(schemaElement.getDataType().equals(ColumnDescription.DataType.INT) || schemaElement.getDataType().equals(ColumnDescription.DataType.DECIMAL))){
                            throw new IllegalArgumentException("Only ints and decimals can be summed");
                        }
                        calculateAvgIndexed(columnName, newRow, s, columns, isDistinct);
                        break;
                    case("COUNT"):
                        calculateCountIndexed(columnName, newRow, s, columns, isDistinct);
                        break;
                    case("MAX"):
                        getMaxFromColumnIndexed(columnName, newRow, s, columns, isDistinct);
                        break;
                    case("MIN"):
                        getMinFromColumnIndexed(columnName, newRow, s, columns, isDistinct);
                }
            }
            else{
                switch (this.functionMap.get(columnID).function.toString()){
                    case("SUM"):
                        if(!(schemaElement.getDataType().equals(ColumnDescription.DataType.INT) || schemaElement.getDataType().equals(ColumnDescription.DataType.DECIMAL))){
                            throw new IllegalArgumentException("Only ints and decimals can be summed");
                        }
                        calculateSumNoIndex(columnName, index, newRow, s, columns, isDistinct);
                        break;
                    case("AVG"):
                        if(!(schemaElement.getDataType().equals(ColumnDescription.DataType.INT) || schemaElement.getDataType().equals(ColumnDescription.DataType.DECIMAL))){
                            throw new IllegalArgumentException("Only ints and decimals can be summed");
                        }
                        calculateAvgNoIndex(columnName, index, newRow, s, columns, isDistinct);
                        break;
                    case("COUNT"):
                        calculateCountNoIndex(columnName, index, newRow, s, columns, isDistinct);
                        break;
                    case("MAX"):
                        getMaxFromColumnNoIndex(columnName, index, newRow, s, columns);
                        break;
                    case("MIN"):
                        getMinFromColumnNoIndex(columnName, index, newRow, s, columns);
                }
            }
        }
        this.table.setSchema(columns);
        rows.add(newRow);
        this.table.setRows(rows);
    }

    private void calculateSumNoIndex(String columnName, int index, Row row, SchemaElement schemaElement, List<SchemaElement> columns, boolean isDistinct){
        int sumInt = 0;
        double sumDouble = 0.0;
        boolean isInt = false;
        Map<Value, Integer> map = new HashMap<>();
        for(Row r : this.table.getRows()){
            if(isDistinct) {
                Integer occurrences = map.get(r.getValue(index));
                if (occurrences == null && r.getValue(index).getElement() != null) {
                    map.put(r.getValue(index), 1);
                    if (r.getValue(index).getDataType().equals(ColumnDescription.DataType.INT)) {
                        sumInt += (Integer) r.getValue(index).getElement();
                        isInt = true;
                    } else {
                        sumDouble += (Double) r.getValue(index).getElement();
                    }
                }
                else{
                    if(r.getValue(index).getElement() != null){
                        map.put(r.getValue(index), occurrences++);
                    }
                }
            }
            else{
                if (r.getValue(index).getElement() != null) {
                    map.put(r.getValue(index), 1);
                    if (r.getValue(index).getDataType().equals(ColumnDescription.DataType.INT)) {
                        sumInt += (Integer) r.getValue(index).getElement();
                        isInt = true;
                    } else {
                        sumDouble += (Double) r.getValue(index).getElement();
                    }
                }
            }
        }
        if(isInt){
            Value value = new Value<Integer>(sumInt);
            row.addValue(value);
        }
        else{
            sumDouble = round(sumDouble, schemaElement.getFractionalLength());
            Value value = new Value<Double>(sumDouble);
            row.addValue(value);
        }
        if(isDistinct){
            schemaElement.setName("SUM(DISTINCT " + columnName + ")");
        }
        else {
            schemaElement.setName("SUM(" + columnName + ")");
        }
        columns.add(schemaElement);
    }

    private void calculateAvgNoIndex(String columnName, int index, Row row, SchemaElement schemaElement, List<SchemaElement> columns, boolean isDistinct){
        int avgInt = 0;
        double avgDouble = 0.0;
        boolean isInt = false;
        int count = 0;
        Map<Value, Integer> map = new HashMap<>();
        for(Row r : this.table.getRows()){
            if(isDistinct) {
                Integer occurrences = map.get(r.getValue(index));
                if (occurrences == null && r.getValue(index).getElement() != null) {
                    map.put(r.getValue(index), 1);
                    if (r.getValue(index).getDataType().equals(ColumnDescription.DataType.INT)) {
                        avgInt += (Integer) r.getValue(index).getElement();
                        isInt = true;
                        count++;
                    } else {
                        avgDouble += (Double) r.getValue(index).getElement();
                        count++;
                    }
                }
                else{
                    if(r.getValue(index).getElement() != null){
                        map.put(r.getValue(index), occurrences++);
                    }
                }
            }
            else{
                if (r.getValue(index).getElement() != null) {
                    map.put(r.getValue(index), 1);
                    if (r.getValue(index).getDataType().equals(ColumnDescription.DataType.INT)) {
                        avgInt += (Integer) r.getValue(index).getElement();
                        isInt = true;
                        count++;
                    } else {
                        avgDouble += (Double) r.getValue(index).getElement();
                        count++;
                    }
                }
            }
        }
        double avg;
        if(isInt){
            avg = avgInt / count;
        }
        else{
            avg = avgDouble / count;
        }
        if(schemaElement.getFractionalLength() == null){
            avg = round(avg, 2);
        }
        avg = round(avg, schemaElement.getFractionalLength());
        Value value = new Value<Double>(avg);
        row.addValue(value);
        if(isDistinct){
            schemaElement.setName("AVG(DISTINCT " + columnName + ")");
        }
        else {
            schemaElement.setName("AVG(" + columnName + ")");
        }
        columns.add(schemaElement);
    }

    private void calculateCountNoIndex(String columnName, int index, Row row, SchemaElement schemaElement, List<SchemaElement> columns, boolean isDistinct){
        int count = 0;
        Map<Value, Integer> map = new HashMap<>();
        for(Row r : this.table.getRows()){
            if(isDistinct){
                Integer occurrences = map.get(r.getValue(index));
                if(occurrences == null && r.getValue(index).getElement() != null){
                    map.put(r.getValue(index), 1);
                    count++;
                }
                else{
                    if(r.getValue(index).getElement() != null){
                        map.put(r.getValue(index), occurrences++);
                    }
                }
            }
            else{
                if(r.getValue(index).getElement() != null){
                    count++;
                }
            }
        }
        Value value = new Value<Integer>(count);
        row.addValue(value);
        if(isDistinct){
            schemaElement.setName("COUNT(DISTINCT " + columnName + ")");
        }
        else{
            schemaElement.setName("COUNT(" + columnName + ")");
        }
        columns.add(schemaElement);
    }

    private void getMaxFromColumnNoIndex(String columnName, int index, Row row, SchemaElement schemaElement, List<SchemaElement> columns){
        Value max = this.table.getRows().get(0).getValue(index);
        for(Row r : this.table.getRows()){
            if(r.getValue(index).getElement() != null && max.compareTo(r.getValue(index)) == -1){
                max = r.getValue(index);
            }
        }
        Value value = new Value(max.getElement());
        row.addValue(value);
        schemaElement.setName("MAX(" + columnName +")");
        columns.add(schemaElement);
    }

    private void getMinFromColumnNoIndex(String columnName, int index, Row row, SchemaElement schemaElement, List<SchemaElement> columns){
        Value min = this.table.getRows().get(0).getValue(index);
        for(Row r : this.table.getRows()){
            if(r.getValue(index).getElement() != null && min.compareTo(r.getValue(index)) == 1){
                min = r.getValue(index);
            }
        }
        Value value = new Value(min.getElement());
        row.addValue(value);
        schemaElement.setName("MIN(" + columnName +")");
        columns.add(schemaElement);
    }

    private void calculateSumIndexed(String columnName, Row row, SchemaElement schemaElement, List<SchemaElement> columns, boolean isDistinct){
        int sumInt = 0;
        double sumDouble = 0.0;
        boolean isInt = false;

        List<BTree.Entry> orderedEntries = this.table.getIndices().get(columnName).getbTree().getOrderedEntries();
        for(BTree.Entry entry : orderedEntries){
            Value value = (Value) entry.getKey();
            if(isDistinct) {
                if (value.getDataType().equals(ColumnDescription.DataType.INT)) {
                    sumInt += (Integer) value.getElement();
                    isInt = true;
                } else {
                    sumDouble += (Double) value.getElement();
                }
            }
            else{
                Set<Row> rows = (Set<Row>) entry.getValue();
                if(rows.size() == 1){
                    if (value.getElement() != null && value.getDataType().equals(ColumnDescription.DataType.INT)) {
                        sumInt += (Integer) value.getElement();
                        isInt = true;
                    } else {
                        sumDouble += (Double) value.getElement();
                    }
                }
                else{
                    if (value.getElement() != null && value.getDataType().equals(ColumnDescription.DataType.INT)) {
                        sumInt += ((Integer) value.getElement() * rows.size());
                        isInt = true;
                    } else {
                        sumDouble += ((Double) value.getElement() * rows.size());
                    }
                }
            }
        }
        if(isInt){
            Value value = new Value<Integer>(sumInt);
            row.addValue(value);
        }
        else{
            sumDouble = round(sumDouble, schemaElement.getFractionalLength());
            Value value = new Value<Double>(sumDouble);
            row.addValue(value);
        }
        if(isDistinct){
            schemaElement.setName("SUM(DISTINCT " + columnName + ")");
        }
        else {
            schemaElement.setName("SUM(" + columnName + ")");
        }
        columns.add(schemaElement);
    }

    private void calculateAvgIndexed(String columnName, Row row, SchemaElement schemaElement, List<SchemaElement> columns, boolean isDistinct){
        int avgInt = 0;
        double avgDouble = 0.0;
        int count = 0;
        boolean isInt = false;

        List<BTree.Entry> orderedEntries = this.table.getIndices().get(columnName).getbTree().getOrderedEntries();
        for(BTree.Entry entry : orderedEntries){
            Value value = (Value) entry.getKey();
            if(isDistinct){
                if (value.getDataType().equals(ColumnDescription.DataType.INT)) {
                    avgInt += (Integer) value.getElement();
                    count++;
                    isInt = true;
                }
                else{
                    avgDouble += (Double) value.getElement();
                    count++;
                }
            }
            else{
                Set<Row> rows = (Set<Row>) entry.getValue();
                if(rows.size() == 1){
                    if(value.getElement() != null && value.getDataType().equals(ColumnDescription.DataType.INT)){
                        avgInt += (Integer) value.getElement();
                        count++;
                        isInt = true;
                    }
                    else{
                        avgDouble += (Double) value.getElement();
                        count++;
                    }
                }
                else{
                    if(value.getElement() != null && value.getDataType().equals(ColumnDescription.DataType.INT)) {
                        avgInt += ((Integer) value.getElement() * rows.size());
                        count += rows.size();
                        isInt = true;
                    }
                    else{
                        avgDouble += ((Double) value.getElement() * rows.size());
                        count += rows.size();
                    }
                }
            }
        }
        double avg;
        if(isInt) {
            avg = avgInt / count;
        }
        else{
            avg = avgDouble / count;
        }
        if(schemaElement.getFractionalLength() == null){
            avg =  round(avg, 2);
        }
        else{
            avg = round(avg, schemaElement.getFractionalLength());
        }
        Value value = new Value<Double>(avg);
        row.addValue(value);

        if(isDistinct){
            schemaElement.setName("AVG(DISTINCT " + columnName + ")");
        }
        else {
            schemaElement.setName("AVG(" + columnName + ")");
        }
        columns.add(schemaElement);
    }

    private void calculateCountIndexed(String columnName, Row row, SchemaElement schemaElement, List<SchemaElement> columns, boolean isDistinct){
        int count = 0;
        List<BTree.Entry> orderedEntries = this.table.getIndices().get(columnName).getbTree().getOrderedEntries();
        if(isDistinct){
            count = orderedEntries.size();
        }
        else{
            for(BTree.Entry entry : orderedEntries){
                Set<Row> rows = (Set<Row>) entry.getValue();
                count += rows.size();
            }
        }

        Value value = new Value<Integer>(count);
        row.addValue(value);
        if(isDistinct){
            schemaElement.setName("COUNT(DISTINCT " + columnName + ")");
        }
        else{
            schemaElement.setName("COUNT(" + columnName + ")");
        }
        columns.add(schemaElement);
    }

    private void getMaxFromColumnIndexed(String columnName, Row row, SchemaElement schemaElement, List<SchemaElement> columns, boolean isDistinct){
        BTree.Entry maxEntry = this.table.getIndices().get(columnName).getbTree().getMaxEntry();
        Value value = (Value) maxEntry.getKey();

        Value newValue = new Value(value.getElement());
        row.addValue(newValue);
        schemaElement.setName("MAX(" + columnName +")");
        columns.add(schemaElement);
    }

    private void getMinFromColumnIndexed(String columnName, Row row, SchemaElement schemaElement, List<SchemaElement> columns, boolean isDistinct){
        BTree.Entry minEntry = this.table.getIndices().get(columnName).getbTree().getMinEntry();
        Value value = (Value) minEntry.getKey();

        Value newValue = new Value(value.getElement());
        row.addValue(newValue);
        schemaElement.setName("MIN(" + columnName +")");
        columns.add(schemaElement);
    }

    private void orderBy(){
        for(int i = orderBys.length - 1; i >= 0; i--){
            boolean ascending = orderBys[i].isAscending();
            String columnName = orderBys[i].getColumnID().getColumnName();
            int index;
            try{
                index = this.table.getColumnListIndices().get(orderBys[i].getColumnID().getColumnName());
            }catch (Exception e){
                try{
                    index = this.table.getColumnListIndices().get(orderBys[i].getColumnID().getTableName() + "." + orderBys[i].getColumnID().getColumnName());
                    columnName = orderBys[i].getColumnID().getTableName() + "." + orderBys[i].getColumnID().getColumnName();
                }catch(Exception e1){
                    throw new IllegalArgumentException(orderBys[i].getColumnID().getColumnName() + " is either not a valid column name, or " +
                            "it is too ambiguous. If you are selecting from more than one column, and two columns have the " +
                            "same name, use the \"tableName.columnName\" notation to refer to it.");
                }
            }
            mergeSort(this.table.getRows(), ascending, index);
        }
    }

    private void mergeSort(List<Row> rows, boolean ascending, int index){
        if(rows.size() < 2){
            return;
        }

        int mid = rows.size() / 2;
        List<Row> firstHalf = new ArrayList<>();
        List<Row> secondHalf = new ArrayList<>();

        for(int i = 0; i < mid; i++){
            firstHalf.add(rows.get(i));
        }
        for(int i = mid; i < rows.size(); i++){
            secondHalf.add(rows.get(i));
        }

        mergeSort(firstHalf, ascending, index);
        mergeSort(secondHalf, ascending, index);
        merge(firstHalf, secondHalf, rows, ascending, index);
    }

    private void merge(List<Row> firstHalf, List<Row> secondHalf, List<Row> rows, boolean ascending, int index){
        int i = 0, j = 0;
        while(i + j < rows.size()){
            if(ascending){
                if(j == secondHalf.size() || (i < firstHalf.size() &&
                        firstHalf.get(i).getValue(index).compareTo(secondHalf.get(j).getValue(index)) <= 0)){
                    rows.set(i + j, firstHalf.get(i++));
                }
                else{
                    rows.set(i + j, secondHalf.get(j++));
                }
            }
            else{
                if(j == secondHalf.size() || (i < firstHalf.size() &&
                        firstHalf.get(i).getValue(index).compareTo(secondHalf.get(j).getValue(index)) >= 0)){
                    rows.set(i + j, firstHalf.get(i++));
                }
                else{
                    rows.set(i + j, secondHalf.get(j++));
                }
            }
        }
    }

    /**
     * Function to round a double to any number of places. Taken from https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
     * @param value The value needed to be rounded.
     * @param places The number of places to be rounded to.
     * @return The rounded double.
     */
    private double round(double value, int places){
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
