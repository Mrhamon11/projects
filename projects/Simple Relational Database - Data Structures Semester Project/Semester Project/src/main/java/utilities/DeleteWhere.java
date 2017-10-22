package utilities;

import dataStructures.*;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.DeleteQuery;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SQLQuery;
import logging.Count;

import java.util.*;

/**
 * Created by Avi on 12/25/2016.
 */
public class DeleteWhere {
    private DeleteQuery deleteQuery;
    private Condition where;
    private String tableName;
    private Table table;
    private Count count;

    public DeleteWhere(SQLQuery sqlQuery, Map<String, Table> tableMap, Count count){
        this.deleteQuery = (DeleteQuery) sqlQuery;
        this.where = this.deleteQuery.getWhereCondition();
        this.tableName = this.deleteQuery.getTableName();
        this.table = tableMap.get(this.tableName);
        this.count = new Count();
        deleteRows();
    }

    private void deleteRows(){
        if(where != null) {
            ConditionHandler ch = new ConditionHandler(this.where, this.table);
            Set<Row> affectedRows = ch.evaluateCondition();
            deleteRows(affectedRows);
            for(int i = 0; i < affectedRows.size(); i++){
                count.addOne();
            }
        }
        else{
            //Need to make a set of all rows in column to check if update is allowed to be executed.
            List<Row> rows = this.table.getRows();
            for(int i = 0; i < this.table.getRows().size(); i++){
                this.count.addOne();
            }
            //Remove all values from unique sets if they exist.
            Iterator<Row> it = rows.iterator();
            while(it.hasNext()){
                it.next();
                it.remove();
            }
            for(Set<Value> values : this.table.getUniqueValues()){
                values.clear();
            }
            deleteAllBTreeElements();
        }
    }

    private void deleteRows(Set<Row> affectedRows){
        List<Row> rows = this.table.getRows();
        Iterator<Row> it = rows.iterator();
        while(it.hasNext()){
            Row row = it.next();
            if(affectedRows.contains(row)){
                for(int i = 0; i < row.getRowValues().size(); i++){
                    if(this.table.getUniqueValues().get(i).contains(row.getValue(i))){
                        this.table.getUniqueValues().get(i).remove(row.getValue(i));
                    }
                }
                it.remove();
                deleteRowFromBTree(row);
            }
        }
    }

    private void deleteAllBTreeElements(){
        Set<String> indices = this.table.getIndices().keySet();
        for(String indexName : indices){
            BTree bTree = this.table.getIndices().get(indexName).getbTree();
            List<BTree.Entry> orderedEntries = bTree.getOrderedEntries();
            for(BTree.Entry entry : orderedEntries){
                Value key = (Value) entry.getKey();
                bTree.delete(key);
            }
        }
    }

    private void deleteRowFromBTree(Row row){
        Set<String> indices = this.table.getIndices().keySet();
        for(String indexName : indices){
            BTree bTree = this.table.getIndices().get(indexName).getbTree();
            List<BTree.Entry> orderedEntries = bTree.getOrderedEntries();
            for(BTree.Entry entry : orderedEntries){
                Set<Row> value = (Set<Row>) entry.getValue();
                if(value.contains(row)){
                    value.remove(row);
                }
                if(value.isEmpty()){
                    bTree.delete(entry.getKey());
                }
            }
        }
    }
}
