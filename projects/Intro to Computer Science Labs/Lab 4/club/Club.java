import java.util.ArrayList;

/**
 * Store details of club memberships.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Club
{
    // Define any necessary fields here ...
    public ArrayList<Membership> members;
    /**
     * Constructor for objects of class Club
     */
    public Club()
    {
        // Initialise any fields here ...
        members = new ArrayList<Membership>();
    }

    /**
     * Add a new member to the club's list of members.
     * @param member The member object to be added.
     */
    public void join(Membership member)
    {
        members.add(member);
    }

    /**
     * @return The number of members (Membership objects) in
     *         the club.
     */
    public int numberOfMembers()
    {
        return members.size();
    }

    /**
     * Determine the number of members who joined in the
     * given month.
     * @param month The month we are interested in.
     * @return The number of members who joined in the month.
     */
    public int joinedInMonth(int month)
    {
        if(month >= 1 && month <= 12) {
            int numPeopleMonth = 0;
            for (int i = 0; i < members.size(); i++) {
                if(month == members.get(i).getMonth()) {
                    numPeopleMonth++;
                }
            }
            return numPeopleMonth;
        }
        else {
            System.out.println("Error!");
            return 0;
        }
    }
}
