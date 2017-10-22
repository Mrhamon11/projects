Currently, the JSON file that I'm using is in the src/main/resources folder, and the file is called courses.json.

I am giving you the original untouched data, and when you make a legal add or delete call, the data is saved and a 
new file is created with the data saved onto the new file of the same name as the original. 

From within the folder, mvn test -Dfile=src/main/resources/courses.json will work. Absolute paths work too, it just
needs to be surrounded with quotes if there are spaces in the path.  

Entering mvn exec:java -Dexec.mainClass=main.Main -Dexec.args="src/main/resources/courses.json" where the args can also be a 
different path to the JSON file. This will launch the main method of the project.

The main class and method that should be run is in the package main.Main.java