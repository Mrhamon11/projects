/**
 * Represents an EMS unit.
 * Created by aviam on 5/6/2017.
 */
public class EMS {
    private String id; //id of initial starting address of the ems unit
    private boolean deployed;

    /**
     * Constructs an EMS unit with the given ID. ID represents the initial vertex that the unit started at.
     * @param id The id of the EMS unit.
     */
    public EMS(String id){
        this.id = id;
        this.deployed = false;
    }

    /**
     * Returns true if the unit is deployed, false otherwise.
     * @return True if the unit is deployed, false otherwise.
     */
    public boolean isDeployed(){
        return deployed;
    }

    /**
     * Deployes the unit.
     */
    public void deploy(){
        this.deployed = true;
    }

    /**
     * Frees the unit.
     */
    public void free(){
        this.deployed = false;
    }

    @Override
    public String toString() {
        return id;
    }
}
