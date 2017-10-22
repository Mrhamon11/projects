import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implementation of a priority queue containing a sorted list of all Person objects by severity. Keeps a reference to
 * the top k priority people in the list where k is the number of EMS units. Top k list is updated as people are popped
 * off the queue.
 * Created by aviam on 5/7/2017.
 */
public class PriorityQueueHelper {
    private List<Person> highestPriority;
    private List<Person> people;
    private int numEMSUnits;

    /**
     * Constructs the queue.
     * @param numEMSUnits The number of EMS units available to limit the highestPriority list size.
     */
    public PriorityQueueHelper(int numEMSUnits){
        this.highestPriority = new ArrayList<>();
        this.people = new ArrayList<Person>();
        this.numEMSUnits = numEMSUnits;
    }

    /**
     * Adds the element to the array by using binary search to find the correct index to insert, and then inserts.
     * If there are more than numEMSUnits people in the array after insertion, only the top numEMSUnits people will
     * remain in the list.
     * This inserts into the array based on priority, with higher severity at the beginning of the array.
     * @param person The person to insert.
     */
    public void add(Person person){
        int index = search(person, 0, this.people.size() - 1);
        this.people.add(index, person);
        if(index <= highestPriority.size()) {
            this.highestPriority.add(index, person);
        }
        chop();
    }

    /**
     * Binary search through the list to find the index that the person should be inserted into.
     * @param person The Person to be inserted.
     * @param low The low index.
     * @param high The high index.
     * @return The index where the person should be inserted.
     */
    private int search(Person person, int low, int high){
        int mid = (low + high) / 2;
        if(low > high){
            return low;
        }
        else if(this.people.get(mid).compareTo(person) == 0){
            return mid;
        }
        else if(this.people.get(mid).compareTo(person) > 0){
            return search(person, mid + 1, high);
        }
        else{
            return search(person, low, high - 1);
        }
    }

    /**
     * Returns the list of people with the highest priority. Number of elements in the list will always be less or equal
     * to the number of EMS units.
     * @return The list of people with the highest priority.
     */
    public List<Person> peek(){
        return this.highestPriority;
    }

    /**
     * Removes the first element on both the regular and highestPriority lists. highestPriority list is updated with
     * next person in main list.
     * @return The Person removed.
     */
    public Person poll(){
        Person person = this.people.get(0);
        if(numEMSUnits < size()){
            this.highestPriority.add(this.people.get(numEMSUnits));
        }
        this.people.remove(0);
        this.highestPriority.remove(0);
        return person;
    }

    /**
     * Chops off the last element in the highestPriority list if the number of elements in the list exceeds the number
     * of EMS units available.
     */
    private void chop(){
        if(this.highestPriority.size() > numEMSUnits){
            this.highestPriority.remove(this.highestPriority.size() - 1);
        }
    }

    /**
     * The total number of people in the queue.
     * @return
     */
    public int size(){
        return this.people.size();
    }

    public static void main(String[] args) {
        Person p1 = new Person(null, 30);
        Person p2 = new Person(null, 5);
        Person p3 = new Person(null, 50);
        Person p4 = new Person(null, 1);
        Person p5 = new Person(null, 7);
        Person p6 = new Person(null, 3);
        Person p7 = new Person(null, 4);
        Person p8 = new Person(null, 20);
        Person p9 = new Person(null, 5);
        Person p10 = new Person(null, 35);
        Person p11 = new Person(null, 40);
        PriorityQueue<Person> people = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueueHelper queueHelper = new PriorityQueueHelper(5);
        people.add(p1);
        people.add(p2);
        people.add(p3);
        people.add(p4);
        people.add(p5);
        people.add(p6);
        people.add(p7);
        people.add(p8);
        people.add(p9);
        people.add(p10);
        people.add(p11);

        queueHelper.add(p1);
        queueHelper.add(p2);
        queueHelper.add(p3);
        queueHelper.add(p4);
        queueHelper.add(p5);
        queueHelper.add(p6);
        queueHelper.add(p7);
        queueHelper.add(p8);
        queueHelper.add(p9);
        queueHelper.add(p10);
        queueHelper.add(p11);

        System.out.println("");

        queueHelper.poll();
        queueHelper.poll();
        queueHelper.poll();
        queueHelper.poll();
        queueHelper.poll();
        queueHelper.poll();
        queueHelper.poll();
        queueHelper.poll();
        queueHelper.poll();
        queueHelper.poll();
        queueHelper.poll();

        System.out.println("");
    }
}
