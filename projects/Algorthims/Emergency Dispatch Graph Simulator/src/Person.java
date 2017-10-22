/**
 * Represents a person.
 * Created by Avi on 4/22/2017.
 */
public class Person implements Comparable<Person>{
    private Address address;
    private int severity;
    private boolean treated;

    /**
     * Constructs a person at the given address with a severity level.
     * @param address The address of the person.
     * @param severity The person's severity level.
     */
    public Person(Address address, int severity){
        this.address = address;
        this.severity = severity;
        treated = false;
    }

    /**
     * Returns the address of the person.
     * @return The address of the person.
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * Returns true of the person has been treated, false otherwise.
     * @return True of the person has been treated, false otherwise.
     */
    public boolean isTreated(){
        return treated;
    }

    /**
     * Sets the person as being treated by and EMS unit.
     */
    public void patientTreated(){
        this.treated = true;
    }

    /**
     * Returns the severity level of the patient.
     * @return The severity level of the patient.
     */
    public int getSeverity() {
        return this.severity;
    }

    /**
     * Compares this to the supplied person by severity level.
     * @param p The person to be compared to.
     * @return 0 if they have the same severity, less than 0 if this is less than p, and greater than 0 if this is
     *          greater than p.
     */
    @Override
    public int compareTo(Person p){
        return this.getSeverity() - p.getSeverity();
    }
}
