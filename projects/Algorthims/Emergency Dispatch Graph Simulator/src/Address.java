/**
 * Represents an address on a street
 * Created by aviam on 4/2/2017.
 */
public class Address implements Comparable<Address> {
    private Integer id;
    private int houseNumber;
    private String streetName;

    /**
     * Creates new address with the supplied id, house number and street name.
     * @param houseNumber The house number of the address.
     * @param streetName The street name of the address.
     */
    public Address(int houseNumber, String streetName) {
        this.id = null;
        this.houseNumber = houseNumber;
        this.streetName = streetName;
    }

    /**
     * Returns the id of the address.
     * @return The id of the address.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id to the newly supplied value.
     * @param id The new id for the address.
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Returns the house number of the address.
     * @return The house number of the address.
     */
    public int getHouseNumber() {
        return houseNumber;
    }

    /**
     * Returns the street name of the address.
     * @return The street name of the address.
     */
    public String getStreetName() {
         return streetName;
    }

    @Override
    /**
     * Returns a string representation of the address: id, house number, and street.
     * @return A string representation of the address: id, house number, and street.
     */
    public String toString() {
        if(this.id == null){
            return sortableString();
        }
        return this.id + " " + sortableString();
    }

    /**
     * Returns a string representation of the address for sorting by house number, then street name.
     * @return A string representation of the address for sorting by house number, then street name.
     */
    public String sortableString(){
        return this.houseNumber + " " + this.streetName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (houseNumber != address.houseNumber) return false;
        return streetName.equals(address.streetName);
    }

    @Override
    public int hashCode() {
        int result = houseNumber;
        result = 31 * result + streetName.hashCode();
        return result;
    }

    public int compareTo(Address a){
        return this.getId() - a.getId();
    }
}
