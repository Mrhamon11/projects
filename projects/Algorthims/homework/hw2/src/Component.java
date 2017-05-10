import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * /**
 * Represents a component in the graph.
 * Created by aviam on 5/7/2017.
 */
public class Component implements Comparable<Component> {
    private List<Vertex> vertices;
    private int numEMSUnits;
    private int numHospitals;

    /**
     * Constructs a component.
     */
    public Component(){
        this.vertices = new ArrayList<>();
        this.numEMSUnits = 0;
        this.numHospitals = 0;
    }

    /**
     * Adds a vertex to the list of vertices.
     * @param v The vertex to be added.
     */
    public void add(Vertex v){
        this.vertices.add(v);
        if(v.isHospital()){
            this.numHospitals++;
        }
        this.numEMSUnits += v.getEMSUnits().size();
    }

    /**
     * The sum of the EMS units and hospitals in the component.
     * @return The sum of EMS units and hospitals in the component.
     */
    private int emsPlusHospitals(){
        return this.numEMSUnits + this.numHospitals;
    }

    /**
     * Returns a random vertex in the component.
     * @return A random vertex in the component.
     */
    public Vertex getRandomVertex(){
        Random random = new Random();
        int index = random.nextInt(this.vertices.size());
        return this.vertices.get(index);
    }

    @Override
    public int compareTo(Component c) {
        return this.emsPlusHospitals() - c.emsPlusHospitals();
    }
}
