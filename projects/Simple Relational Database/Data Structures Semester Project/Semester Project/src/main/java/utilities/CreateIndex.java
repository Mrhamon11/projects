package utilities;

import dataStructures.SchemaElement;
import dataStructures.Table;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.CreateIndexQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLQuery;
import logging.Count;

import java.util.Map;

/**
 * Created by Avi on 12/25/2016.
 */
public class CreateIndex {
    private CreateIndexQuery createIndexQuery;
    private String tableName;
    private String columnName;
    private String indexName;
    private Table table;

    public CreateIndex(SQLQuery sqlQuery, Map<String, Table> tableMap, Count count){
        this.createIndexQuery = (CreateIndexQuery) sqlQuery;
        this.tableName = this.createIndexQuery.getTableName();
        this.columnName = this.createIndexQuery.getColumnName();
        this.indexName = this.createIndexQuery.getIndexName();
        this.table = tableMap.get(this.tableName);
        SchemaElement schemaElement = this.table.getSchema().get(this.table.getColumnListIndices().get(this.columnName));
        this.table.indexExistingColumn(schemaElement, this.indexName);
        count.addOne();
    }
}
