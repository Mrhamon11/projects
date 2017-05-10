import java.util.*;

/**
 * Generic undirected graph.
 * Created by aviam on 4/2/2017.
 */
public class UndirectedGraph<T> {
    private Map<T, List<T>> adjList;
    private int numberOfVertices;

    public UndirectedGraph(){
        this.adjList = new HashMap<T, List<T>>();
        this.numberOfVertices = 0;
    }

    public void add(T vertex){
        List edges = new LinkedList<>();
        this.adjList.put(vertex, edges);
        this.numberOfVertices++;
    }

    public void connect(T from, T to){
        if(this.adjList.get(from) == null || this.adjList.get(to) == null){
            throw new IllegalArgumentException("Both vertices must have already been added to the graph to use this method!");
        }

        //Add connection from "from" to "to"
        List edges = this.adjList.get(from);
        if(!edges.contains(to)) {
            edges.add(to);
            this.adjList.put(from, edges);
        }

        //Add connection from "to" to "from"
        edges = this.adjList.get(to);
        if(!edges.contains(from)) {
            edges.add(from);
            this.adjList.put(to, edges);
        }
    }

    public Set<T> getVertices(){
        return this.adjList.keySet();
    }

    public List<T> getAdjacencies(T vertex){
        return this.adjList.get(vertex);
    }

    public Map<T, List<T>> getAdjList() {
        return adjList;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void printGraph(){
        for(T v : this.adjList.keySet()){
            System.out.print(v.toString() + " -> ");
            List<T> edges = getAdjacencies(v);
            for(T e : edges){
                System.out.print(e + ", ");
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        UndirectedGraph<Integer> g = new UndirectedGraph<>();
        g.add(1);
        g.add(2);
        g.add(3);
        g.add(4);
        g.add(5);
        g.add(6);
        g.add(7);

        g.connect(1, 2);
        g.connect(1, 4);
        g.connect(2, 3);
        g.connect(3, 1);
        g.connect(3, 4);
        g.connect(4, 1);
        g.connect(5, 6);
        g.connect(7, 5);

        g.printGraph();
    }

}
