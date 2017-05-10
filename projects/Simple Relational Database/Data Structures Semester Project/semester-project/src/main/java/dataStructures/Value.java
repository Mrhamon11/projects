package dataStructures;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription.DataType;

import java.io.Serializable;

/**
 * Created by Avi on 12/20/2016.
 */
//JD: given what you do in initialSetDataType, what does the type parameter add?
public class Value<E extends Comparable<E>> implements Comparable<Value<E>>, Serializable{
    private E element;
    private DataType dataType;

    public Value(E element){
        this.element = element;
        initialSetDataType(element);
    }

    private void initialSetDataType(E element){
        if(element instanceof Integer){
            this.dataType = DataType.INT;
        }
        else if(element instanceof Double){
            this.dataType = DataType.DECIMAL;
        }
        else if(element instanceof String){
            this.dataType = DataType.VARCHAR;
        }
        else if(element instanceof Boolean){
            this.dataType = DataType.BOOLEAN;
        }
        else if(element == null){
            this.dataType = null;
        }
        else{
            throw new IllegalArgumentException("Only int, decimal, varchar, and boolean types allowed");
        }
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public E getElement() {
        return this.element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    @Override
    public String toString() {
        if(this.element == null){
            return null;
        }
        return this.element.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;

        Value<?> value = (Value<?>) o;

        if (getElement() != null ? !getElement().equals(value.getElement()) : value.getElement() != null) return false;
        return getDataType() == value.getDataType();
    }

    @Override
    public int hashCode() {
        int result = getElement() != null ? getElement().hashCode() : 0;
        result = 31 * result + (getDataType() != null ? getDataType().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Value<E> v) {
        if(this.getElement() == null && v.getElement() == null){
            return 0;
        }
        if(this.getElement() == null && v.getElement() != null){
            return -1;
        }
        if(v.getElement() == null){
            return 1;
        }
        if(this.getDataType().equals(v.getDataType())){
            return this.getElement().compareTo(v.getElement());
        }
        throw new IllegalArgumentException("Two values must have the same DataType to be comparable!");
    }

}
