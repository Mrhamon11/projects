package utilities;

import dataStructures.SchemaElement;
import dataStructures.Table;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.CreateTableQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLQuery;
import logging.Count;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Avi on 12/25/2016.
 */
public class CreateTable {
    private CreateTableQuery createTableQuery;
    private String tableName;
    private List<SchemaElement> schema;
    private Table createdTable;

    public CreateTable(SQLQuery sqlQuery, Map<String, Table> tableMap, Count count){
        this.createTableQuery = (CreateTableQuery) sqlQuery;
        this.tableName = this.createTableQuery.getTableName();
        if(tableMap.containsKey(this.tableName)){
            throw new IllegalArgumentException("Table already exists!");
        }
        createScheme(this.createTableQuery.getColumnDescriptions(), this.createTableQuery.getPrimaryKeyColumn());
        makePrimaryKeyFirst();
        Table table = new Table(this.tableName, this.schema);
        tableMap.put(this.tableName, table);
        this.createdTable = table;
        count.addOne();
    }

    public Table createdTable(){
        return this.createdTable;
    }

    private void createScheme(ColumnDescription[] columnDescriptions, ColumnDescription primaryKeyColumn){
        this.schema = new ArrayList<>();
        for(ColumnDescription cd : columnDescriptions){
            boolean primaryKey = false;
            if(cd.getColumnName().equals(primaryKeyColumn.getColumnName())){
                primaryKey = true;
            }
            SchemaElement schemaElement = new SchemaElement(cd.getColumnName(), cd.getColumnType(), primaryKey, cd.isUnique(), cd.isNotNull(),
                    cd.getHasDefault(), cd.getDefaultValue(), cd.getWholeNumberLength(), cd.getFractionLength(), cd.getVarCharLength(), this.tableName);
            this.schema.add(schemaElement);
        }
    }

    private void makePrimaryKeyFirst(){
        if(this.schema.get(0).isPrimaryKey()){
            return;
        }
        SchemaElement schemaElement;
        for(int i = 0; i < this.schema.size(); i++){
            if(this.schema.get(i).isPrimaryKey()){
                schemaElement = this.schema.get(i);
                this.schema.set(i, this.schema.get(0));
                this.schema.set(0, schemaElement);
            }
        }
    }
}
