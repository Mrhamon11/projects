import java.util.ArrayList;
import java.util.List;

/**
 * Represents a vertex in the graph.
 * Created by aviam on 4/2/2017.
 */
public class Vertex {
    private Address address;
    private boolean hospital;
    private List<EMS> emsUnits;
    private boolean intersection;
    private boolean hasBrokenRoad;

    /**
     * Constructs an vertex from the supplied address.
     * @param address
     */
    public Vertex(Address address){
        this.address = address;
        this.hospital = false;
        this.emsUnits = new ArrayList<>();
        this.intersection = false;
        this.hasBrokenRoad = false;
    }

    /**
     * Returns true if the vertex has any EMS units.
     * @return True if the vertex has any EMS units.
     */
    public boolean hasEMSUnits(){
        return !this.emsUnits.isEmpty();
    }

    /**
     * Returns true if the vertex has any broken roads.
     * @return True if the vertex has any broken roads.
     */
    public boolean hasBrokenRoad() {
        return this.hasBrokenRoad;
    }

    /**
     * Sets the hasBrokenRoad field to true;
     */
    public void breakRoad(){
        this.hasBrokenRoad = true;
    }

    /**
     * Sets the hasBrokenRoad field to false.
     */
    public void roadFixed(){
        this.hasBrokenRoad = false;
    }

    /**
     * Returns true if the vertex is a hospital.
     * @return true if the vertex is a hospital.
     */
    public boolean isHospital() {
        return this.hospital;
    }

    /**
     * Sets the intersection field to true;
     */
    public void becomeIntersection(){
        this.intersection = true;
    }

    /**
     * Sets the hospital field to true;
     */
    public void becomeHospital(){
        this.hospital = true;
    }

    /**
     * Returns the list of EMS units in the vertex.
     * @return
     */
    public List<EMS> getEMSUnits() {
        return emsUnits;
    }

    /**
     * Adds EMS unit to the vertex.
     * @param ems The EMS to be added.
     */
    public void addEMS(EMS ems){
        this.emsUnits.add(ems);
    }

    /**
     * Returns the address of the vertex.
     * @return The address of the vertex.
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * Returns The string representation of the vertex.
     * @return the string representation of the vertex.
     */
    @Override
    public String toString() {
        return this.address.toString();
    }
}
