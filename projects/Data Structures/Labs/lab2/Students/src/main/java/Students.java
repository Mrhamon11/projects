import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Students {
	
	private Person[] students;
	
	public Students() {
		
	}

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		
		//read json file data to String
		byte[] jsonData = Files.readAllBytes(Paths.get("src/main/resources/students.text"));
				
		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
				
		//convert json string to object
		Students stu = objectMapper.readValue(jsonData, Students.class);
				
		//System.out.println(stu);
		System.out.println("Sorting by Age:\n");
		stu.sortByAge();
		System.out.println("");
		System.out.println("Sorting by Name:\n");
		stu.sortByName();
	}
	
	public Person[] getStudents() {
		return students;
	}
	
	public void setStudents(Person[] studs) {
		students = studs;
	}
	
	public String toString() {
		String result = "";
		for(Person student : students) {
			result += student.toString() + "\n";
		}
		return result;
	}
	
	
	public void sortByAge() {
		Arrays.sort(this.students,
				new Comparator<Person>() {
		    		public int compare(Person person1, Person person2) {
		    			return person1.getAge() - person2.getAge();
		    		} 
		    	}
		);
		for(Person student : this.students) {
			System.out.println(student.toString());
		}
	}
	
	public void sortByName() {
		Arrays.sort(this.students,
                new Comparator<Person>() {
                    public int compare(Person person1, Person person2) {
                        return person1.getName().compareTo(person2.getName());
                    }        
                }
		);
		for(Person student : students) {
			System.out.println(student.toString());
		}
	}
	
}
