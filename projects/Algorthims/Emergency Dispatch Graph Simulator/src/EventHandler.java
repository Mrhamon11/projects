import java.io.File;
import java.util.*;

/**
 * Parses all events and carries out each event line by line in the order that appears in the input file.
 * Created by Avi on 4/22/2017.
 */
public class EventHandler {
    private UndirectedBooleanGraph<Vertex> graph;
    private PriorityQueueHelper queue;
    private List<String> eventLog;
    private Map<Address, Vertex> addressVertexMap;
    private Map<EMS, Vertex> emsLocations;
    private Map<Set<Vertex>, Intersection> intersectionNodes;
    private Map<Vertex, Component> vertexComponentMap;
    private int repairTeams;
    private int repairTeamCounter;
    private int deploymentCounter;
    private int reachableCounter;

    /**
     * Constructs the Event handler. Calls the InputParser on the supplied file to build the graph and parse events.
     * All other data structures are pulled from the InputParser as well.
     * @param file The file to be parsed.
     */
    public EventHandler(File file){
        InputParser inputParser = new InputParser(file);
        inputParser.parse();
        this.graph = inputParser.getGraph();
        this.eventLog = inputParser.getEventLog();
        this.addressVertexMap = inputParser.getAddressVertexMap();
        this.emsLocations = inputParser.getEmsLocations();
        this.intersectionNodes = inputParser.getIntersectionNodes();
        this.vertexComponentMap = new HashMap<>();
        this.queue = new PriorityQueueHelper(this.emsLocations.keySet().size());
        this.repairTeams = 0;
        this.repairTeamCounter = 0;
        this.deploymentCounter = 0;
        this.reachableCounter = 0;
    }

    /**
     * Executes each event on the graph. Pulls from the event log in order and determines which kind of event
     * it is.
     */
    public void run(){
        for(int i = 0; i < this.eventLog.size(); i++){
            String str = this.eventLog.get(i);
            if(isAddress(str)) {
                String[] split = str.split(" ");
                Address address = parseAddress(split);
                Person person = new Person(address, Integer.parseInt(split[split.length - 1]));
                sendEMSToPerson(person);
            }
            else if(isCallingGroup(str)){
                str = this.eventLog.get(++i);
                while(!str.toLowerCase().startsWith("end")){
                    String[] split = str.split(" ");
                    Address address = parseAddress(split);
                    Person person = new Person(address, Integer.parseInt(split[split.length - 1]));
                    this.queue.add(person);
                    str = this.eventLog.get(++i);
                }
                System.out.println("Call block starting:");
                sendEMSToPeople();
                System.out.println("Call block end.\n");
            }
            else if(isBrokenRoad(str)){
                String[] split = str.split(" ");
                parseBrokenRoad(split);
            }
            else{ //repair team
                this.repairTeams++;
                checkForRepairs();
            }
        }
    }

    /**
     * Returns true if the event is a single address. If so, it is a single 911 call and informs the program
     * that an EMS team should be dispatched.
     * @param line The string to be parsed.
     * @return True if the line is an address, false otherwise.
     */
    private boolean isAddress(String line){
        String[] split = line.split(" ");
        if(Utilities.isInt(split[0]) && Utilities.isInt(split[split.length - 1])){
            return true;
        }
        return false;
    }

    /**
     * Returns true if we are about to start parsing a series of addresses in a calling group.
     * @param line The string to be parsed.
     * @return True if the line starts the calling group, false otherwise.
     */
    private boolean isCallingGroup(String line){
        if(line.toLowerCase().startsWith("begin")){
            return true;
        }
        return false;
    }

    /**
     * Returns true if the event is a broken road event.
     * @param line The string to be parsed.
     * @return True if the event is a broken road event, false otherwise.
     */
    private boolean isBrokenRoad(String line){
        if(line.toLowerCase().startsWith("broken")){
            return true;
        }
        return false;
    }

    /**
     * Parses the address, and creates an Address object, splitting up the street name and number.
     * @param split The address split by spaces.
     * @return An Address object representing that address.
     */
    private Address parseAddress(String[] split){
        String streetName = getStreetName(split, 1, true);
        Address address = new Address(Integer.parseInt(split[0]), streetName);
        return address;
    }

    /**
     * Parses the broken road. Determines the roads to break between the house numbers given in the event line.
     * @param split The event, split by spaces to easily parse the start and end house numbers on the street.
     */
    private void parseBrokenRoad(String[] split){
        int startAddress = Integer.parseInt(split[1]);
        int endAddress = Integer.parseInt(split[2]);
        String streetName = getStreetName(split, 3, false);
        Address start = new Address(startAddress, streetName);
        Address end = new Address(endAddress, streetName);
        Vertex startVertex = this.addressVertexMap.get(start);
        Vertex endVertex = this.addressVertexMap.get(end);
        this.graph.breakRoadBetweenVertices(startVertex, endVertex, this.intersectionNodes);
    }

    /**
     * Sends an available EMS unit to the person. Returns true if the patient was treated, false otherwise.
     * @param person The person to be treated.
     * @return True if the person was treated, false otherwise.
     */
    private boolean sendEMSToPerson(Person person){
        EMS[] emsKeys = this.emsLocations.keySet().toArray(new EMS[this.emsLocations.keySet().size()]);
        for(int i = 0; i < emsKeys.length; i++){
            EMS ems = emsKeys[i];
            if(checkEMSDeployment()){
                i = 0;
                ems = emsKeys[i];
            }
            if(!ems.isDeployed()) {
                ems.deploy();
                System.out.println("Dispatching EMS unit " + ems.toString() + " to patient to address " + person.getAddress());
                treatPerson(ems, person);
                if (person.isTreated()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sends EMS units to the people in the priority queue. Method will print if the patient could not be reached, and
     * whether or not the patient could ever be reached.
     */
    private void sendEMSToPeople(){
        List<Person> highestPriority = this.queue.peek();
        while(!highestPriority.isEmpty()){
            Person person = highestPriority.get(0);
            boolean treated = sendEMSToPerson(person);
            if(!treated){
                this.reachableCounter++;
                //If we've tried to reach patient at least twice, and don't have any repair units, patient dies.
                if(this.reachableCounter >= 2 && this.repairTeams == 0){
                    System.out.println("Patient at " + person.getAddress() + " cannot be reached, and no repair teams are available. Patient dies.\n");
                    this.reachableCounter = 0;
                    this.queue.poll();
                }
                else{
                    System.out.println("Patient at " + person.getAddress() + " cannot be treated by any EMS unit on current run through. EMS units will be deployed again.\n");
                }
            }
            else {
                this.queue.poll();
                this.reachableCounter = 0;
            }
        }
    }

    /**
     * Sends the supplied EMS unit to the person after doing a breadth first search on the graph to find optimal path.
     * @param ems The EMS team that will be dispatched.
     * @param person The patient to be treated.
     */
    private void treatPerson(EMS ems, Person person){
        Vertex start = this.emsLocations.get(ems);
        Vertex personVertex = this.addressVertexMap.get(person.getAddress());
        this.graph.breadthFirstToPerson(start, personVertex);
        driveToPatient(ems, person, start, personVertex);
    }

    /**
     * EMS team will drive down the newly discovered path. If at any point the EMS unit discovers a broken road, it
     * calls the treatPerson method again with the same parameters to find a new route. If one can't be found, the EMS
     * unit is freed and a new one is dispatched.
     * @param ems The EMS team to be dispatched.
     * @param person The patient to be treated.
     * @param start The starting vertex of the EMS
     * @param personVertex The ending vertex where the patient resides.
     */
    private void driveToPatient(EMS ems, Person person, Vertex start, Vertex personVertex){
        List<Vertex> path = (List<Vertex>) this.graph.pathTo(start, personVertex);
        if(path != null) {
            for (int i = 0; i < path.size() - 1; i++) {
                boolean roadDrivable = this.graph.getAdjacencies(path.get(i)).get(path.get(i + 1))[0];
                if (roadDrivable) {
                    path.get(i).getEMSUnits().remove(ems);
                    this.emsLocations.put(ems, path.get(i + 1));
                    path.get(i + 1).getEMSUnits().add(ems);
                } else {
                    this.graph.getAdjacencies(path.get(i)).get(path.get(i + 1))[1] = false;
                    this.graph.getAdjacencies(path.get(i + 1)).get(path.get(i))[1] = false;
                    System.out.println("Broken road found between " + path.get(i).getAddress() + " and "
                                        + path.get(i + 1) + ". Finding new route.\n");
                    treatPerson(ems, person);
                    return;
                }
            }
            person.patientTreated();
            System.out.println("Patient at " + person.getAddress() + " has been treated by EMS unit " + ems.toString() + ".\n");
            this.deploymentCounter++;
            this.repairTeamCounter++;
            checkForRepairs();
        }
        else{
            System.out.println("Patient at " + person.getAddress() + " could not be reached by EMS unit " + ems.toString() + ".\n");
            this.deploymentCounter++;
            this.repairTeamCounter++;
            checkForRepairs();
            ems.free();
        }
    }

    /**
     * Repairs roads based on components that exist if we have a repair team. If there is more than one component, find
     * the component with smallest number of combined EMS units and hospitals, and fix all roads between the component
     * and the nearest EMS unit or hospital in any other component. If there is only one component, fix roads from hospitals.
     */
    private void repairRoads(){
        if(this.repairTeams == 0){
            return;
        }
        Vertex hospital = this.graph.connectedComponents();
        int componentCount = this.graph.getComponentCount();
        if(componentCount > 1){
            List<Component> components = createComponents();
            components.sort(new Comparator<Component>() {
                @Override
                public int compare(Component o1, Component o2) {
                    return o1.compareTo(o2);
                }
            });
            Vertex start = components.get(0).getRandomVertex();
            Vertex end = this.graph.bfsToEMSOrHospital(start, this.vertexComponentMap);
            fixRoadsBetween(start, end);
        }
        else{
            Vertex firstBrokenRoadNode = this.graph.bfsFromHospitals(hospital);
            fixRoadFromFirstBroken(firstBrokenRoadNode);
        }
    }

    /**
     * Fixes roads from the starting vertex until it hits an road that isn't broken.
     * @param start The starting vertex.
     */
    private void fixRoadFromFirstBroken(Vertex start){
        Set<Vertex> set = this.graph.getAdjacencies(start).keySet();
        Vertex[] vertices = set.toArray(new Vertex[set.size()]);
        for(Vertex vertex : vertices){
            if(!this.graph.getAdjacencies(start).get(vertex)[1]){
                vertex.roadFixed();
                this.graph.getAdjacencies(start).get(vertex)[0] = true;
                this.graph.getAdjacencies(start).get(vertex)[1] = true;
                this.graph.getAdjacencies(vertex).get(start)[0] = true;
                this.graph.getAdjacencies(vertex).get(start)[1] = true;

                System.out.println("Road between " + start.getAddress() + " and " + vertex.getAddress() + " has been fixed.\n");

                fixRoadFromFirstBroken(vertex);
            }
        }
    }

    /**
     * Fixes all roads between the start and end vertex.
     * @param start The starting vertex.
     * @param end The ending vertex.
     */
    private void fixRoadsBetween(Vertex start, Vertex end){
        List<Vertex> path = (List<Vertex>) this.graph.pathTo(start, end);
        if(path != null){
            for (int i = 0; i < path.size() - 1; i++) {
                this.graph.getAdjacencies(path.get(i)).get(path.get(i + 1))[0] = true;
                this.graph.getAdjacencies(path.get(i)).get(path.get(i + 1))[1] = true;
                Vertex vertex = (Vertex) path.get(i);
                vertex.roadFixed();
            }
            this.graph.getAdjacencies(path.get(path.size() - 1)).get(path.get(path.size() - 2))[0] = true;
            this.graph.getAdjacencies(path.get(path.size() - 1)).get(path.get(path.size() - 2))[1] = true;
            Vertex vertex = (Vertex) path.get(path.size() - 1);
            vertex.roadFixed();
            System.out.println("Roads between " + start.getAddress() + " and " + end.getAddress() + " have been fixed.\n");
        }
    }

    /**
     * Creates a list of components that are used for sorting based on smallest number of combined EMS units and
     * hospitals. Organizes graph based on component.
     * @return Returns the list of components in the graph.
     */
    private List<Component> createComponents(){
        List<Component> components = new ArrayList<>();
        Map<Vertex, Integer> componentIds = this.graph.getComponentIds();
        Map<Integer, List<Vertex>> idsToVertexComponents = new HashMap<>();
        for(Vertex v : componentIds.keySet()){
            int compNum = componentIds.get(v);
            List<Vertex> vertices = idsToVertexComponents.get(compNum);
            if(vertices == null){
                vertices = new ArrayList<>();
            }
            vertices.add(v);
            idsToVertexComponents.put(compNum, vertices);
        }
        for(int i : idsToVertexComponents.keySet()){
            Component component = new Component();
            for(Vertex v : idsToVertexComponents.get(i)){
                this.vertexComponentMap.put(v, component);
                component.add(v);
            }
            components.add(component);
        }
        return components;
    }

    /**
     * Checks if repairs can be done.
     */
    private void checkForRepairs(){
        if(this.repairTeamCounter >= this.emsLocations.keySet().size() * 2){
            repairRoads();
            decrementRepairTeam();
            this.reachableCounter = 0;
        }
    }

    /**
     * Reduces the number of repair teams we have by one.
     */
    private void decrementRepairTeam(){
        if(repairTeams > 0){
            repairTeams--;
        }
    }

    /**
     * Checks to see that all EMS units have been deployed. If so, free all units and return true.
     * @return Return true if all EMS units have been deployed, false otherwise.
     */
    private boolean checkEMSDeployment(){
        if(this.deploymentCounter == this.emsLocations.keySet().size()){
            freeAllUnits();
            this.deploymentCounter = 0;
            return true;
        }
        return false;
    }

    /**
     * Frees all EMS units for use.
     */
    private void freeAllUnits(){
        for(EMS ems : this.emsLocations.keySet()){
            ems.free();
        }
    }

    /**
     * Returns the street name of the address.
     * @param split The array that contains the split string of the address.
     * @param start The starting index.
     * @param call Whether or not we are parsing a 911 call.
     * @return The street name as as string.
     */
    private String getStreetName(String[] split, int start, boolean call){
        int length;
        //If it's a 911 call, the last digit is severity and isn't part of the street name, so don't include last digit.
        if(call){
            length = split.length - 1;
        }
        else {
            length = split.length;
        }
        String streetName = "";
        for(int i = start; i < length; i++){
            streetName += " " + split[i];
        }
        return streetName.trim();
    }

    public static void main(String[] args) {
        File file = new File("example.txt");
        EventHandler eh = new EventHandler(file);
        eh.run();
    }
}
