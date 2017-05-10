<<<<<<< HEAD
package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

/**
 * @author diament@yu.edu
 *
 */
public abstract class SQLQuery
{
    private String queryString;

    SQLQuery(String query)
    {
	this.queryString = query;
    }
    
    public String getQueryString()
    {
	return this.queryString;
    }
    public String toString()
    {
	return this.getQueryString();
    }
    public abstract ColumnValuePair[] getColumnValuePairs();
    abstract void addColumnValuePair(ColumnID col, String value);
=======
package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

/**
 * @author diament@yu.edu
 *
 */
public abstract class SQLQuery
{
    private String queryString;

    SQLQuery(String query)
    {
	this.queryString = query;
    }
    
    public String getQueryString()
    {
	return this.queryString;
    }
    public String toString()
    {
	return this.getQueryString();
    }
    public abstract ColumnValuePair[] getColumnValuePairs();
    abstract void addColumnValuePair(ColumnID col, String value);
>>>>>>> 49564e4bbc8b08c930e5d939b47fef965051152d
}