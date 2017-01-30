package testing;

import dataStructures.SchemaElement;
import dataStructures.Table;
import dataStructures.Value;
import database.Database;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import logging.Count;
import utilities.*;

import java.util.*;

/**
 * Test class to show that the database works. Database will be initially empty. However, Mordy's GUI does work on this,
 * just change to it in the run configuration. That will make it easier for your to test if other queries work.
 * Created by Avi on 12/24/2016.
 */
public class TestClass {

	/** JD: my general comment - GREAT JOB!
	* I have only one significant piece of non-positive feedback: the point of having a BTree is to NOT search through all the elements every time, and instead get a very fast lookup
	* via a balanced tree. By always using BTree.getOrderedEntries on all your selects, etc. you defeated the performance goal of having an index.
	* That method was only intended for operations that specifically required ordering, max, or min, not for all searches.
	* More generally - you clearly have the basics of writing code down. The next step is to start thinking about efficiency/algorithms.
	* 	
	* If databases is a topic that interests you, check out the Stanford Database book: https://www.amazon.com/Database-Systems-Complete-Book-2nd/dp/0131873253
	*/
	
	
    //Below are some of my tests to show to you that the queries work. The execute method returns a the table, and I print it for you to show you
    //what the output is.
    //Note that because of saving, you'll have to go into
    //the logging folder in the src folder to delete all of the files there if you want to run this test multiple times.
    //For example, the first test are for creating table, and those will always fail if you don't delete the save files in the
    //logging folder.
    //Please note that when inserting a row into a table, if a value in the row is to be put into a decimal column,
    //it must be labeled as a decimal for it to work. For example, 7.0 instead of just 7.
    //Also, when selecting, for some reason the parser requires that even boolean values be surrounded in single
    //quotes. My code does determines if the boolean value is parsed as a boolean or a string depending on the column
    //it's being inserted too.
    //Also note, everything is case sensitive.
    //Distinct on functions should be working on sum and avg as well, I just didn't test them here.
    public static void main(String[] args) {
        Database db = new Database();
        Table table;
        //Test create table. 2 tables will be made and stored.
        try{
            table = db.execute("create table table1 (id int, number int, word varchar (5) unique, bool boolean, dec decimal (1,2), primary key(id));");
            //table will contain an empty table with the column names.
            table.printTable();
            System.out.println("");
            //running the same query again will return a table with false as the table already exists in the DB.
            table = db.execute("create table table1 (id int, number int, word varchar (5) not null, bool boolean, dec decimal (1,2), primary key(id));");
            table.printTable();
            System.out.println("");
            table = db.execute("create table table2 (id int, year varchar (255), gpa decimal (1,2), lastyear varchar (255), passing boolean NOT NULL, primary key (id))");
            table.printTable();
            System.out.println("");
        }catch (Exception e){

        }

        //Test insertions into both tables.
        try{
            //table1 insertions, all queries will execute successfully.
            table = db.execute("insert into table1 (id, number, word, bool, dec) values (1, 10, 'avi', true, 2.1)");
            table.printTable();
            System.out.println("");
            table = db.execute("insert into table1 (id, number, word, bool, dec) values (2, 12, 'binny', false, 3.2)");
            table.printTable();
            System.out.println("");
            //serialization should happen after this query, new log files should be there.
            table = db.execute("insert into table1 (id, number, word, bool, dec) values (3, 8, 'josh', true, 4.5)");
            table.printTable();
            System.out.println("");
            table = db.execute("insert into table1 (id, number, word, dec) values (4, 10, 'zak', 2.1)"); //null boolean value
            table.printTable();
            System.out.println("");
            table = db.execute("insert into table1 (id, number, word,  dec) values (5, 18, 'sam', 7.0)");
            table.printTable();
            System.out.println("");
            //full table will be printed.
            table = db.execute("select * from table1");
            table.printTable();
            System.out.println("");

            //table2 insertions
            table = db.execute("insert into table2 (id, year, gpa, lastyear, passing) values (1, 'senior', 3.42, 'junior', true)");
            table.printTable();
            System.out.println("");
            table = db.execute("insert into table2 (id, year, gpa, lastyear, passing) values (2, 'soph', 1.2, 'freshman', true)");
            table.printTable();
            System.out.println("");
            table = db.execute("insert into table2 (id, year, gpa, lastyear, passing) values (3, 'senior', .5, 'junior', true)");
            table.printTable();
            System.out.println("");
            table = db.execute("insert into table2 (id, year, gpa, lastyear, passing) values (4, 'junior', 4.0, 'sophomore', true)");
            table.printTable();
            System.out.println("");
            //full table will be printed.
            table = db.execute("select * from table2");
            table.printTable();
            System.out.println("");

            //Now try insertions that will fail on table1.
            //Will fail because id isn't unique
            table = db.execute("insert into table1 (id, number, word, bool, dec) values (1, 10, 'new guy', true, 2.1)");
            table.printTable();
            System.out.println("");
            //Will fail because word is unique and can't be null.
            table = db.execute("insert into table1 (id, number, bool, dec) values (7, 12, false, 3.2)");
            table.printTable();
            System.out.println("");
            //Will fail because word is limited to 5 characters.
            table = db.execute("insert into table1 (id, number, word, bool, dec) values (7, 8, 'Mr Long Name', true, 4.5)");
            table.printTable();
            System.out.println("");
            //Will fail because dec is limited to (1,2) format.
            table = db.execute("insert into table1 (id, number, word, dec) values (4, 10, 'zak', 2.134)"); //null boolean value
            table.printTable();
            System.out.println("");
            //full table will be printed to show that nothing has changed.
            table = db.execute("select * from table1");
            table.printTable();
            System.out.println("");

            //Now try insertions that will fail on table2.
            //Will fail because id isn't unique.
            table = db.execute("insert into table2 (id, year, gpa, lastyear, passing) values (1, 'senior', 3.42, 'junior', true)");
            table.printTable();
            System.out.println("");
            //Will fail because boolean value is null when it isn't allowed to be.
            table = db.execute("insert into table2 (id, year, gpa, lastyear) values (2, 'soph', 1.2, 'freshman')");
            table.printTable();
            System.out.println("");
            //full table will be printed to show that nothing has changed.
            table = db.execute("select * from table2");
            table.printTable();
            System.out.println("");
        }catch (Exception e){

        }

        //Update tests on table2
        try{
            //Try updating primary key in last row to one that already exists. Will fail because of unique.
            table = db.execute("update table2 set id = 4 where id = 3");
            table.printTable();
            System.out.println("");
            //Update last row in table2 by changing primary key. Will succeed because 5 isn't in the table.
            table = db.execute("update table2 set id = 5 where id = 4");
            table.printTable();
            System.out.println("");
            //Insert new row into table2 with primary key = 4. Will succeed because the row that had primary key = 4 has been changed to 5.
            table = db.execute("insert into table2 (id, passing) values (4, false)");
            table.printTable();
            System.out.println("");
            //full table will be printed to show the changes.
            table = db.execute("select * from table2");
            table.printTable();
            System.out.println("");

            //Update all GPAs to 3.5 if current year is 'senior'
            table = db.execute("update table2 set gpa = 3.5 where year = 'senior'");
            table = db.execute("select * from table2");
            table.printTable();
            System.out.println("");
        }catch (Exception e){

        }

        //Create index on a couple of columns.
        try{
            //create index on dec column in table1
            table = db.execute("create index dec_index on table1(dec)");
            table.printTable();
            System.out.println("");

            //create index on lastyear column in table2
            table = db.execute("create index last_year on table2(lastyear)");
            table.printTable();
            System.out.println("");
        }catch (Exception e){

        }

        //A bunch of select queries
        try{
            table = db.execute("select * from table1, table2");
            table.printTable();
            System.out.println("");
            table = db.execute("select table1.id, table2.id from table1, table2");
            table.printTable();
            System.out.println("");
            table = db.execute("select bool, gpa, word from table1, table2 where table1.id > 2");
            table.printTable();
            System.out.println("");
            table = db.execute("select distinct bool from table1 where table1.id > 2");
            table.printTable();
            System.out.println("");
            table = db.execute("select * from table1, table2 order by table1.id desc, passing asc, dec asc");
            table.printTable();
            System.out.println("");
            table = db.execute("select AVG(id) from table1");
            table.printTable();
            System.out.println("");
            table = db.execute("select SUM(dec) from table1");
            table.printTable();
            System.out.println("");
            table = db.execute("select COUNT(DISTINCT gpa) from table2");
            table.printTable();
            System.out.println("");
            table = db.execute("select MAX(word) from table1 where id < 3");
            table.printTable();
            System.out.println("");
            table = db.execute("select SUM(number), MIN(bool), MIN(dec) from table1 where table1.id <> 4");
            table.printTable();
            System.out.println("");
        }catch (Exception e){

        }

        //Test Delete with condition, and full table.
        try{
            table = db.execute("delete from table1 where id > 3");
            table.printTable();
            System.out.println("");
            table = db.execute("select * from table1");
            table.printTable();
            System.out.println("");
            table = db.execute("delete from table1");
            table.printTable();
            System.out.println("");
            table = db.execute("Select * from table1");
            table.printTable();
            System.out.println("");
        }catch (Exception e){

        }
    }
}
