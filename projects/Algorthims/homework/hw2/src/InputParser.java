import java.io.*;
import java.util.*;

/**
 * Class to parse the input file and create the graph. Events will be recorded and passed to the EventHandler
 * class to actually execute events. All addresses, intersection, edges, ems units, and hospitals are kept track
 * of as the file is read, and then added to the graph by type.
 * Created by aviam on 4/2/2017.
 */
public class InputParser {
    private File input;
    private BufferedReader br;
    private UndirectedBooleanGraph<Vertex> graph;
    private List<Address> addresses;
    private List<Integer[]> edges;
    private List<Integer[]> intersections;
    private List<String[]> hospitals;
    private List<String[]> emsTeams;
    private List<String> eventLog;
    private Map<Address, Vertex> addressVertexMap;
    private Map<EMS, Vertex> emsLocations;
    private Map<Set<Vertex>, Intersection> intersectionNodes;

    /**
     * Initializes the parser.
     * @param input The input file to be read.
     */
    public InputParser(File input){
        this.input = input;
        try {
            this.br = new BufferedReader(new FileReader(this.input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.graph = new UndirectedBooleanGraph<>();
        this.addresses = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.intersections = new ArrayList<>();
        this.hospitals = new ArrayList<>();
        this.emsTeams = new ArrayList<>();
        this.eventLog = new ArrayList<>();
        this.addressVertexMap = new HashMap<>();
        this.emsLocations = new HashMap<>();
        this.intersectionNodes = new HashMap<>();
    }

    /**
     * Parses all lines of the input file. All lines pertaining to the building of the graph will read and parsed,
     * while all events will be stored and passed to the EventHandler.
     */
    public void parse(){
        String line;
        try {
            while((line = br.readLine()) != null) {
                if (line.contains(",")) {
                    String[] split = line.split(",");
                    if(split.length == 2){
                        readEdge(split);
                    }
                    else{
                        readIntersection(split);
                    }
                } else if (line.toLowerCase().startsWith("hospital")) {
                    readHospital(line);
                } else if (line.toLowerCase().startsWith("ems")) {
                    readEMS(line);
                } else {
                    String[] split = line.split(" ");
                    if (Utilities.isInt(split[0]) && Utilities.isInt(split[1]) && !Utilities.isInt(split[split.length - 1])) {
                        readAddress(split);
                    }
                    else {
                        readEvent(line);
                    }
                }
            }
            addressesToNodes();
            edgesToGraph();
            intersectionsToGraph();
            hospitalToGraph();
            EMSToGraph();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the address and adds it to the address list to be later sorted and put into the graph.
     * @param split An array of the full address split by white space so that Address objects can
     *              correctly be assigned the proper fields.
     */
    private void readAddress(String[] split){
        int id = Integer.parseInt(split[0]);
        int houseNumber = Integer.parseInt(split[1]);
        StringBuilder nameOfStreet = new StringBuilder();
        for (int i = 2; i < split.length; i++) {
            nameOfStreet.append(split[i] + " ");
        }
        String streetName = nameOfStreet.toString().trim();

        Address address = new Address(houseNumber, streetName);
        address.setId(id);
        this.addresses.add(address);
    }

    /**
     * Takes the list of addresses and sorts them by house number and street. Addresses next to each other on the same
     * street will have edges between them. Address objects are created and are mapped to the given vertex representing
     * them for later use.
     */
    private void addressesToNodes(){
        List<List<Address>> addressList = new ArrayList<>();
        this.addresses.sort(new Comparator<Address>() {
            @Override
            public int compare(Address o1, Address o2) {
                return o1.sortableString().compareTo(o2.sortableString());
            }
        });
        Map<String, List<Address>> streetNameToAddressMap = new HashMap<>();
        for(Address address : this.addresses){
            if(!streetNameToAddressMap.containsKey(address.getStreetName())){
                List<Address> block = new ArrayList<>();
                block.add(address);
                streetNameToAddressMap.put(address.getStreetName(), block);
            }
            else{
                List<Address> block = streetNameToAddressMap.get(address.getStreetName());
                block.add(address);
                streetNameToAddressMap.put(address.getStreetName(), block);
            }
        }
        for(String address : streetNameToAddressMap.keySet()){
            List<Address> block = streetNameToAddressMap.get(address);
            addressList.add(block);
        }

        for(List<Address> block : addressList){
            Vertex v1 = new Vertex(block.get(0));
            this.addressVertexMap.put(v1.getAddress(), v1);
            this.graph.add(v1);
            for(int i = 1; i < block.size(); i++){
                Vertex v2 = new Vertex(block.get(i));
                this.addressVertexMap.put(v2.getAddress(), v2);
                this.graph.add(v2);
                this.graph.connect(v1, v2);
                v1 = v2;
            }
        }
    }

    /**
     * Reads extra edges that are in the input file. Parses the id of the address and stores them in a list.
     * @param split The edges stored between the two vertices represented as strings.
     */
    private void readEdge(String[] split){
        Integer[] edge = new Integer[2];
        edge[0] = Integer.parseInt(split[0]);
        edge[1] = Integer.parseInt(split[1]);
        this.edges.add(edge);
    }

    /**
     * Adds the extra egdges from the input file into the graph.
     */
    private void edgesToGraph(){
        for(Integer[] edges : this.edges){
            Vertex v1 = this.graph.getNodeFromID(edges[0]);
            Vertex v2 = this.graph.getNodeFromID(edges[1]);
            this.graph.connect(v1, v2);
        }
    }

    /**
     * Parses the intersections outlined in the input file. Added to a list to be later processed once all nodes are
     * created.
     * @param split The intersection between the four given vertices represented as strings.
     */
    private void readIntersection(String[] split){
        if(split.length != 4){
            throw new IllegalArgumentException("Intersections can only have 4 addresses.");
        }
        Integer[] intersection = new Integer[4];
        intersection[0] = Integer.parseInt(split[0]);
        intersection[1] = Integer.parseInt(split[1]);
        intersection[2] = Integer.parseInt(split[2]);
        intersection[3] = Integer.parseInt(split[3]);
        this.intersections.add(intersection);
    }

    /**
     * Processes the intersections and adds them to the graph. Between four nodes in an intersection, six edges
     * will be created between them forming a 4 clique.
     */
    private void intersectionsToGraph(){
        for(Integer[] edges : this.intersections){
            Vertex v1 = this.graph.getNodeFromID(edges[0]);
            Vertex v2 = this.graph.getNodeFromID(edges[1]);
            Vertex v3 = this.graph.getNodeFromID(edges[2]);
            Vertex v4 = this.graph.getNodeFromID(edges[3]);
            v1.becomeIntersection();
            v2.becomeIntersection();
            v3.becomeIntersection();
            v4.becomeIntersection();
            this.graph.connect(v1, v2);
            this.graph.connect(v1, v3);
            this.graph.connect(v1, v4);
            this.graph.connect(v2, v3);
            this.graph.connect(v2, v4);
            this.graph.connect(v3, v4);
            Set<Vertex> s1 = new HashSet<>(), s2 = new HashSet<>(), s3 = new HashSet<>(), s4 = new HashSet<>(),
                    s5 = new HashSet<>(), s6 = new HashSet<>();
            s1.add(v1); s1.add(v2);
            s2.add(v1); s2.add(v3);
            s3.add(v1); s3.add(v4);
            s4.add(v2); s4.add(v3);
            s5.add(v2); s5.add(v4);
            s6.add(v3); s6.add(v4);
            Intersection intersection = new Intersection(v1, v2, v3, v4);
            this.intersectionNodes.put(s1, intersection);
            this.intersectionNodes.put(s2, intersection);
            this.intersectionNodes.put(s3, intersection);
            this.intersectionNodes.put(s4, intersection);
            this.intersectionNodes.put(s5, intersection);
            this.intersectionNodes.put(s6, intersection);
        }
    }

    /**
     * Parses the hospital line.
     * @param line The line with the hospital declaration.
     */
    private void readHospital(String line){
        String[] split = line.split(" ");
        this.hospitals.add(split);
    }

    /**
     * Processes the hospitals and adds them to the graph.
     */
    private void hospitalToGraph(){
        for(String[] str : this.hospitals){
            Vertex vertex = this.graph.getNodeFromID(Integer.parseInt(str[1]));
            vertex.becomeHospital();
        }
    }

    /**
     * Parses the EMS line.
     * @param line The line with the EMS declaration.
     */
    private void readEMS(String line){
        String[] split = line.split(" ");
        this.emsTeams.add(split);
    }

    /**
     * Processes the EMS units and adds them to the graph.
     */
    private void EMSToGraph(){
        for(String[] str : this.emsTeams){
            Vertex vertex = this.graph.getNodeFromID(Integer.parseInt(str[1]));
            EMS ems = new EMS(str[1]);
            this.emsLocations.put(ems, vertex);
            vertex.addEMS(ems);
        }
    }

    /**
     * Reads all events and adds them to a list to be processed later.
     * @param line The event represented as a string.
     */
    private void readEvent(String line){
        this.eventLog.add(line);
    }

    /**
     * Returns the undirected graph built.
     * @return The undirected graph.
     */
    public UndirectedBooleanGraph<Vertex> getGraph(){
        return this.graph;
    }

    /**
     * Returns the list of all events to be processed.
     * @return The list of events.
     */
    public List<String> getEventLog() {
        return this.eventLog;
    }

    /**
     * Returns the address to vertex map.
     * @return The address to vertex map.
     */
    public Map<Address, Vertex> getAddressVertexMap() {
        return this.addressVertexMap;
    }

    /**
     * Returns a map between a set of vertices, and the intersection they are in.
     * @return The map between a set of vertices and their intersections.
     */
    public Map<Set<Vertex>, Intersection> getIntersectionNodes() {
        return this.intersectionNodes;
    }

    /**
     * Returns the map of the location of a given EMS unit.
     * @return The map of the location of a given EMS unit.
     */
    public Map<EMS, Vertex> getEmsLocations() {
        return this.emsLocations;
    }

    public static void main(String[] args) {
        File file = new File("test.txt");
        InputParser ip = new InputParser(file);
        ip.parse();
        System.out.println("");
    }
}
