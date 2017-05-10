package database;

import dataStructures.Table;

/**
 * Interface that the database implements for the GUI.
 * Created by Avi on 1/1/2017.
 */
public interface CanExecute {
    public Table execute(String sql) throws Exception;
}

