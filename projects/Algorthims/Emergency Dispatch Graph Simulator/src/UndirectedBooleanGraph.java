import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Undirected graph representing a city, where vertices are addresses and edges are roads.
 * Created by aviam on 4/2/2017.
 */
public class UndirectedBooleanGraph<T> {
    //boolean[] is used to signify the difference between ems teams knowing that the road is broken
    //and the graph having a broken road. The first value in the array is for broken roads and the
    //second value is whether or not ems teams know about the broken road.
    private Map<T, Map<T, Boolean[]>> adjList;
    private int numberOfVertices;
    private Map<T, Boolean> visited;
    private Map<T, Integer> distanceTo;
    private Map<T, T> previousVertex;
    private Map<T, Integer> componentIds;
    private int componentCount;
    private int stepsToEMS;
    private int stepsToHospital;

    /**
     * Constructs the graph.
     */
    public UndirectedBooleanGraph(){
        this.adjList = new HashMap<T, Map<T, Boolean[]>>();
        this.numberOfVertices = 0;
    }

    /**
     * Adds the vertex to the graph.
     * @param vertex The vertex to be added.
     */
    public void add(T vertex){
        Map<T, Boolean[]> edges = new HashMap<T, Boolean[]>();
        this.adjList.put(vertex, edges);
        this.numberOfVertices++;
    }

    /**
     * Connects the two supplied vertices in both directions.
     * @param from The starting vertex.
     * @param to The ending vertex.
     */
    public void connect(T from, T to){
        if(this.adjList.get(from) == null || this.adjList.get(to) == null){
            throw new IllegalArgumentException("Both vertices must have already been added to the graph to use this method!");
        }

        //Add connection from "from" to "to"
        Map<T, Boolean[]> edges = this.adjList.get(from);
        if(!edges.containsKey(to)) {
            Boolean[] bools = {true, true};
            edges.put(to, bools);
            this.adjList.put(from, edges);
        }

        //Add connection from "to" to "from"
        edges = this.adjList.get(to);
        if(!edges.containsKey(from)) {
            Boolean[] bools = {true, true};
            edges.put(from, bools);
            this.adjList.put(to, edges);
        }
    }

    /**
     * Returns the vertex in the graph that has the given id.
     * @param id The id of the vertex.
     * @return The vertex with the given id, and no vertex has the given id.
     */
    public Vertex getNodeFromID(int id){
        for(T t : this.adjList.keySet()){
            Vertex v = (Vertex) t;
            if(v.getAddress().getId() == id){
                return v;
            }
        }
        return null;
    }

    /**
     * Returns the adjacency list of the supplied vertex.
     * @param vertex The vertex whose adjacenties are desired.
     * @return The adjacency list of the supplied vertext.
     */
    public Map<T, Boolean[]> getAdjacencies(T vertex){
        return this.adjList.get(vertex);
    }

    /**
     * Simple breadth first search of graph. If breakingRoad is true, only traverse nodes with a shared street name.
     * @param s The starting vertex.
     * @param breakingRoad True if we want to search for roads to break.
     */
    public void breadthFirst(T s, boolean breakingRoad){
        Queue<T> q = new LinkedBlockingQueue<>();
        q.add(s);
        this.visited = new HashMap<T, Boolean>();
        this.visited.put(s, true);
        this.distanceTo = new HashMap<T, Integer>();
        this.distanceTo.put(s, 0);
        this.previousVertex = new HashMap<T, T>();
        while(!q.isEmpty()) {
            T v = q.remove();
            Vertex vertex1 = (Vertex) v;
            for (T w : this.getAdjacencies(v).keySet()) {
                Vertex vertex2 = (Vertex) w;
                if(breakingRoad){
                    if (this.visited.get(w) == null && vertex1.getAddress().getStreetName().equals(vertex2.getAddress().getStreetName())) {
                        updateFields(q, v, w);
                    }
                }
                else {
                    if (this.visited.get(w) == null) {
                        updateFields(q, v, w);
                    }
                }
            }
        }
    }

    /**
     * Breadth first search from starting point until we find an EMS unit or hospital in a separate component from s.
     * Return vertex once found.
     * @param s The starting vertex.
     * @param vertexComponentMap The vertex component map.
     * @return The vertex of the EMS unit or hospital in a separate component if found, and s otherwise.
     */
    public Vertex bfsToEMSOrHospital(T s, Map<Vertex, Component> vertexComponentMap){
        Queue<T> q = new LinkedBlockingQueue<>();
        q.add(s);
        this.stepsToEMS = 0;
        this.stepsToHospital = 0;
        this.visited = new HashMap<T, Boolean>();
        this.visited.put(s, true);
        this.distanceTo = new HashMap<T, Integer>();
        this.distanceTo.put(s, 0);
        this.previousVertex = new HashMap<T, T>();
        while(!q.isEmpty()) {
            T v = q.remove();
            Vertex vertex1 = (Vertex) v;
            Vertex source = (Vertex) s;
            Component c1 = vertexComponentMap.get(source);
            Component c2 = vertexComponentMap.get(vertex1);
            boolean sameComponent = c1.equals(c2);
            if((vertex1.isHospital() || vertex1.hasEMSUnits()) && !sameComponent){
                return vertex1;
            }
            for (T w : this.getAdjacencies(v).keySet()) {
                if (this.visited.get(w) == null) {
                    updateFields(q, v, w);
                }
            }
        }
        return (Vertex) s;
    }

    /**
     * Breath first search to find the patient. Once found, break.
     * @param s Starting vertex of ems team.
     * @param e Vertex where patient is.
     */
    public void breadthFirstToPerson(T s, Vertex e){
        Queue<T> q = new LinkedBlockingQueue<>();
        q.add(s);
        this.visited = new HashMap<T, Boolean>();
        this.visited.put(s, true);
        this.distanceTo = new HashMap<T, Integer>();
        this.distanceTo.put(s, 0);
        this.previousVertex = new HashMap<T, T>();
        while(!q.isEmpty()) {
            T v = q.remove();
            Vertex vertex1 = (Vertex) v;
            if(vertex1.equals(e)){
                break;
            }
            for (T w : this.getAdjacencies(v).keySet()) {
                //Checks to see if the EMS team thinks the road is not broken.
                boolean roadDrivable = this.adjList.get(v).get(w)[1];
                if (this.visited.get(w) == null && roadDrivable) {
                    updateFields(q, v, w);
                }
            }
        }
    }

    /**
     * Finds all connected components through depth first search and keeps track of them.
     * @return The last hospital vertex seen which is used only if there is one component for road repair.
     */
    public Vertex connectedComponents(){
        this.visited = new HashMap<T, Boolean>();
        this.componentIds = new HashMap<T, Integer>();
        this.componentCount = 0;
        Vertex hospital = null;
        T[] vertices = (T[]) this.adjList.keySet().toArray(new Object[this.adjList.keySet().size()]);
        for(T vertex : vertices){
            if(((Vertex) vertex).isHospital()){
                hospital = (Vertex) vertex;
            }
            if(this.visited.get(vertex) == null){
                dfs(vertex);
                this.componentCount++;
            }
        }
        return hospital;
    }

    /**
     * Basic depth first search from starting point.
     * @param source The starting vertex.
     */
    private void dfs(T source){
        this.visited.put(source, true);
        this.componentIds.put(source, this.componentCount);
        for(T w : getAdjacencies(source).keySet()){
            boolean roadDrivable = this.adjList.get(source).get(w)[1];
            if(this.visited.get(w) == null && roadDrivable){
                this.dfs(w);
            }
        }
    }

    /**
     * Breadth first search from starting hospital vertex until we find closest broken road.
     * @param s The starting hospital vertex.
     * @return The vertex connected to the first broken road.
     */
    public Vertex bfsFromHospitals(T s){
        Queue<T> q = new LinkedBlockingQueue<>();
        q.add(s);
        this.visited = new HashMap<T, Boolean>();
        this.visited.put(s, true);
        this.distanceTo = new HashMap<T, Integer>();
        this.distanceTo.put(s, 0);
        this.previousVertex = new HashMap<T, T>();
        while(!q.isEmpty()) {
            T v = q.remove();
            Vertex vertex = (Vertex) v;
            if(vertex.hasBrokenRoad()){
                return vertex;
            }
            for (T w : this.getAdjacencies(v).keySet()) {
                if (this.visited.get(w) == null) {
                    updateFields(q, v, w);
                }
            }
        }
        return (Vertex) s;
    }

    /**
     * Returns the number of components in the graph.
     * @return The number of components in the graph.
     */
    public int getComponentCount() {
        return this.componentCount;
    }

    /**
     * Returns the component ids of each vertex in the graph.
     * @return The component ids of each vertex in the graph.
     */
    public Map<T, Integer> getComponentIds() {
        return this.componentIds;
    }

    /**
     * Updates fields for all breadth first search.
     * @param q The queue.
     * @param v The previous vertex.
     * @param w The current vertex.
     */
    private void updateFields(Queue<T> q, T v, T w){
        q.add(w);
        this.visited.put(w, true);
        this.previousVertex.put(w, v);
        this.distanceTo.put(w, this.distanceTo.get(v) + 1);
    }

    /**
     * Returns true if a path to the supplied vertex exists from the start vertex used in the last breadth first search.
     * @param vertex The vertex we want to know if there is a path to.
     * @return True if a path exists, false otherwise.
     */
    private boolean hasPathTo(T vertex) {
        if(this.visited.get(vertex) == null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Returns the path from the source vertex to the end vertex.
     * @param source The start vertex.
     * @param vertex The end vertex.
     * @return The path between the vertices.
     */
    public Iterable<T> pathTo(T source, T vertex) {
        //if not marked, there is no path
        if(!hasPathTo(vertex)) {
            return null;
        }
        List<T> path = new ArrayList<>();
        //trace back through it's history
        for(T x = vertex; x!=source; x=this.previousVertex.get(x)) {
            path.add(x);
        }
        path.add(source);
        return reverse(path);
    }

    /**
     * Breaks all roads between the supplied vertices in both directions.
     * @param t1 The start vertex.
     * @param t2 The end vertex.
     * @param interSectionNodes The map between sets of vertices and intersections. Used so intersections are unpassable
     *                          if any roads connected to vertices in the intersections break.
     */
    public void breakRoadBetweenVertices(T t1, T t2, Map<Set<Vertex>, Intersection> interSectionNodes){
        this.breadthFirst(t1, true);
        List<T> path = (List<T>) pathTo(t1, t2);
        if(path != null && !path.isEmpty()) {
            this.adjList.get(path.get(0)).get(path.get(1))[0] = false;
            Vertex v = (Vertex) path.get(0);
            v.breakRoad();
            for (int i = 1; i < path.size() - 1; i++) {
                Set<Vertex> set = new HashSet<>();
                set.add((Vertex) path.get(i - 1));
                set.add((Vertex) path.get(i));
                if(interSectionNodes.get(set) != null){
                    Intersection intersection = interSectionNodes.get(set);
                    breakIntersectionRoads(intersection);
                }
                this.adjList.get(path.get(i)).get(path.get(i - 1))[0] = false;
                this.adjList.get(path.get(i)).get(path.get(i + 1))[0] = false;
                Vertex vertex = (Vertex) path.get(i);
                vertex.breakRoad();
            }
            Vertex vertex = (Vertex) path.get(path.size() - 1);
            vertex.breakRoad();
            this.adjList.get(path.get(path.size() - 1)).get(path.get(path.size() - 2))[0] = false;
        }
    }

    /**
     * Reverses order of the list.
     * @param path The list to be reversed.
     * @return The newly reversed list.
     */
    private List<T> reverse(List<T> path){
        List<T> reverse = new ArrayList<T>();
        for(int i = path.size() - 1; i >= 0; i--){
            reverse.add(path.get(i));
        }
        return reverse;
    }

    /**
     * Breaks all roads between vertices in the supplied intersection.
     * @param intersection The intersection whose roads should be broken.
     */
    private void breakIntersectionRoads(Intersection intersection){
        Vertex[] pair = intersection.oneTwo();
        breakIntersectionPair(pair);
        pair = intersection.oneThree();
        breakIntersectionPair(pair);
        pair = intersection.oneFour();
        breakIntersectionPair(pair);
        pair = intersection.twoThree();
        breakIntersectionPair(pair);
        pair = intersection.twoFour();
        breakIntersectionPair(pair);
        pair = intersection.threeFour();
        breakIntersectionPair(pair);
    }

    /**
     * Breaks roads between two vertices in both direction.
     * @param pair The pair of vertices.
     */
    private void breakIntersectionPair(Vertex[] pair){
        this.adjList.get(pair[0]).get(pair[1])[0] = false;
        this.adjList.get(pair[1]).get(pair[0])[0] = false;
    }

    /**
     * Prints a string representation of the graph.
     */
    public void printGraph(){
        for(T v : this.adjList.keySet()){
            System.out.print(v.toString() + " -> ");
            Map<T, Boolean[]> edges = getAdjacencies(v);
            for(T e : edges.keySet()){
                System.out.print(e + ", ");
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        UndirectedBooleanGraph<Integer> g = new UndirectedBooleanGraph<>();
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
        g.connect(2, 5);

//        g.getAdjList().get(2).get(3)[1] = false;
//        g.getAdjList().get(2).get(1)[1] = false;
//        g.getAdjList().get(1).get(2)[1] = false;
//        g.getAdjList().get(3).get(2)[1] = false;

//        g.breakRoadBetweenVertices(4,2);
        g.connectedComponents();
        g.printGraph();
    }
}
