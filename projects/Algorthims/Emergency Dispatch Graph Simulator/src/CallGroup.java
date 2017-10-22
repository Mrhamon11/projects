import java.util.List;

/**
 * Created by Avi on 4/23/2017.
 */
public class CallGroup implements Event{
    private List<Person> people;

    public CallGroup(List<Person> people){
        this.people = people;
    }

    public List<Person> getPeople() {
        return this.people;
    }

    public void addPerson(Person person){
        this.people.add(person);
    }
}
