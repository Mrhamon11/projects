/**
 * Represents an intersection between four vertices.
 * Created by aviam on 5/7/2017.
 */
public class Intersection {
    private Vertex v1;
    private Vertex v2;
    private Vertex v3;
    private Vertex v4;

    /**
     * Creates and intersection between the four vertices supplied.
     * @param v1 Vertex 1.
     * @param v2 Vertex 2.
     * @param v3 Vertex 3.
     * @param v4 Vertex 4.
     */
    public Intersection(Vertex v1, Vertex v2, Vertex v3, Vertex v4){
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    /**
     * Returns an array pairing of the first and second vertices.
     * @return an array pairing of the first and second vertices.
     */
    public Vertex[] oneTwo(){
        Vertex[] pair = {this.v1, this.v2};
        return pair;
    }

    /**
     * Returns an array pairing of the first and third vertices.
     * @return an array pairing of the first and third vertices.
     */
    public Vertex[] oneThree(){
        Vertex[] pair = {this.v1, this.v3};
        return pair;
    }

    /**
     * Returns an array pairing of the first and fourth vertices.
     * @return an array pairing of the first and fourth vertices.
     */
    public Vertex[] oneFour(){
        Vertex[] pair = {this.v1, this.v4};
        return pair;
    }

    /**
     * Returns an array pairing of the second and third vertices.
     * @return an array pairing of the second and third vertices.
     */
    public Vertex[] twoThree(){
        Vertex[] pair = {this.v2, this.v3};
        return pair;
    }

    /**
     * Returns an array pairing of the second and fourth vertices.
     * @return an array pairing of the second and fourth vertices.
     */
    public Vertex[] twoFour(){
        Vertex[] pair = {this.v2, this.v4};
        return pair;
    }

    /**
     * Returns an array pairing of the third and fourth vertices.
     * @return an array pairing of the third and fourth vertices.
     */
    public Vertex[] threeFour(){
        Vertex[] pair = {this.v3, this.v4};
        return pair;
    }
}

