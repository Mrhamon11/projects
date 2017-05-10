
public class Person {
	
	private String name;
	private int age;
	private String[] interests;
	
	public Person() {
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public String[] getInterests() {
		return this.interests;
	}
	
	public void setInterests(String[] interests) {
		this.interests = interests;
	}
	
	public String toString() {
		String personInformation = this.name + "\n" + Integer.toString(this.age) + "\n";
		for(String interest : this.interests) {
			personInformation += interest + ", ";
		}
		return personInformation ;
	}
	
}
