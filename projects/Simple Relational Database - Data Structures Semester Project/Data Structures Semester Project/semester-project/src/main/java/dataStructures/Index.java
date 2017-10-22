package dataStructures;

import java.io.Serializable;
import java.util.Set;

/**
 * An index for a column. Contains the index name, the table name that the column is in, as well as the actual BTree
 * for the column. BTree has values as keys, and a set of rows those values appear in as the values.
 * Created by Avi on 12/21/2016.
 */
public class Index<E extends Comparable<E>> implements Serializable{
    private String name;
    private String tableName;
    private BTree<Value<E>, Set<Row>> bTree;

    /**
     * Makes a new index.
     * @param name The name of the index.
     * @param tableName The name of the table the column is in.
     */
    public Index(String name, String tableName){
        this.name = name;
        this.tableName = tableName;
        this.bTree = new BTree<Value<E>, Set<Row>>();
    }

    /**
     * Returns the name of the index.
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the name of the table.
     * @return The name of the table where the index column resides.
     */
    public String getTableName(){
        return this.tableName;
    }

    /**
     * Returns the BTree index for the column.
     * @return The BTree index for the column.
     */
    public BTree<Value<E>, Set<Row>> getbTree() {
        return this.bTree;
    }
}
