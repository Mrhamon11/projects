package dataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A row in a table. Rows contain a list of Values.
 * Created by Avi on 12/20/2016.
 */
public class Row implements Serializable{
    private List<Value> rowValues;

    /**
     * Makes a new row with no Values in it.
     */
    public Row(){
        this.rowValues = new ArrayList<>();
    }

    /**
     * Returns the full row.
     * @return The list of Values representing the row.
     */
    public List<Value> getRowValues() {
        return this.rowValues;
    }

    /**
     * Add the value to the row.
     * @param value The value to be added.
     */
    public void addValue(Value value){
        this.rowValues.add(value);
    }

    /**
     * Returns the Value in the row at the supplied index.
     * @param index The index of the Value desired.
     * @return The Value at the supplied index.
     */
    public Value getValue(int index){
        return this.rowValues.get(index);
    }

    /**
     * Returns a string representation of the Row.
     * @return A string representation of the Row.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Value value : rowValues){
            sb.append(value.toString() + " ");
        }
        return sb.toString();
    }

    /**
     * Returns true if two rows have the same Values in them, in the same order, false otherwise.
     * @param o The row that is being compared with this.
     * @return True if they are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Row)) return false;

        Row row = (Row) o;

        return getRowValues().equals(row.getRowValues());
    }

    /**
     * Returns the hashcode for the Row object.
     * @return The hashcode for the Row object.
     */
    @Override
    public int hashCode() {
        return getRowValues().hashCode();
    }

    //Testing only!
    public static void main(String[] args) {
        Row row = new Row();
        Value<Integer> v1 = new Value<Integer>(5);
        Value<Double> v2 = new Value<Double>(2.3);
        Value<String> v3 = new Value<String>("test");
        Value<Boolean> v4 = new Value<Boolean>(true);
        row.addValue(v1);
        row.addValue(v2);
        row.addValue(v3);
        row.addValue(v4);

        for(Value v : row.getRowValues()){
            System.out.println(v.toString());
        }
    }
}
