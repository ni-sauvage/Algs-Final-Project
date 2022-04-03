package edu.princeton.cs.algs4;

import java.io.File;
import java.sql.Time;
import java.util.*;

public class FinalProject {
    private searchStops stops;
    private searchTimes times;
    private EdgeWeightedDigraph graph;
    FinalProject(){
        this.stops = new searchStops();
        this.graph = new EdgeWeightedDigraph();
    }
    void returnSearchStops(String prefix){
        this.stops.printStopsStartingWith(prefix);
    }

    void searchForTimes(String time){
        this.times = new searchTimes(time);
        System.out.println("The following list was returned based on arrival time: ");
        this.times.printSearchTimes();
    }

    void printShortestPath(int fromStopID, int toStopId){
         EdgeWeightedDigraph.path shortestPath = graph.getShortestPath(fromStopID, toStopId);
         String pathString = "";
         for(EdgeWeightedDigraph.stop s : shortestPath.pathStops){
            pathString += s.stop_id + "->";
         }
         pathString = pathString.substring(0, pathString.length() - 2);
         System.out.println("The shortest path by bus stops is:\n" + pathString +"\nwhich has a cost of " + shortestPath.cost);
    }
}

class EdgeWeightedDigraph{
    private LinkedList<edge>[] adjList;
    private LinkedList<stop> stopList = new LinkedList<>();
    private LinkedList<edge> edgeList = new LinkedList<>();
    private HashMap<Integer, stop> stop_id_hash = new HashMap<Integer, stop>();
    double distTo[];
    stop prevTo[];
    private int V;
    public class stop{
        int map_id;
        int stop_id;
        String stop_code;
        String stop_name;
        String stop_desc;
        String stop_lat;
        String stop_lon;
        String zone_id;
        String stop_url;
        String location_type;
        String parent_station;
        stop(int stop_id, String stop_code, String stop_name, String stop_desc, String stop_lat, String stop_lon, String zone_id, String stop_url, String location_type, String parent_station, int map_id){
            this.stop_id = stop_id; this.stop_code = stop_code; this.location_type = location_type; this.map_id = map_id;
            this.stop_lat = stop_lat; this.stop_lon = stop_lon;
            this.stop_name = stop_name; this.stop_desc = stop_desc; this.stop_url = stop_url; this.parent_station = parent_station; this.zone_id = zone_id;
        }
    }
    private class edge{
        double weight;
        stop stopFrom;
        stop stopTo;
        edge(double weight, stop stopFrom, stop stopTo){
            this.weight = weight; this.stopFrom = stopFrom; this.stopTo = stopTo;
        }
    }
    public EdgeWeightedDigraph(){
        try {
            File inp = new File("inp/stops.txt");
            Scanner readIn = new Scanner(inp);
            int i = 0;
            readIn.nextLine();
            while (readIn.hasNextLine()){
                String line = readIn.nextLine();
                String[] parameters = line.split(",");
                for(String s : parameters) s.trim();
                stop currentStop = new stop(
                        Integer.valueOf(parameters[0]), parameters[1], parameters[2], parameters[3],
                        parameters[4], parameters[5], parameters[6], parameters[7],
                        parameters[8], parameters.length != 10 ? null : parameters[9], i);
                stopList.add(currentStop);
                stop_id_hash.put(Integer.valueOf(parameters[0]), currentStop);
                i++;
            }
            adjList = new LinkedList[i];
            V = i;
            for(int j = 0; j < adjList.length; j++) adjList[j] = new LinkedList<>();
            inp = new File("inp/transfers.txt");
            readIn = new Scanner(inp);
            readIn.nextLine();
            while (readIn.hasNextLine()){
                String line = readIn.nextLine();
                String[] parameters = line.split(",");
                for(String s : parameters){
                    s.trim();
                }
                edge currentEdge;
                if(parameters[2].equals("0")){
                    currentEdge = new edge(2, getStop(Integer.valueOf(parameters[0])), getStop(Integer.valueOf(parameters[1])));
                }
                else {
                    currentEdge = new edge(Double.valueOf(parameters[3])/100, getStop(Integer.valueOf(parameters[0])), getStop(Integer.valueOf(parameters[1])));
                }
                adjList[currentEdge.stopFrom.map_id].add(currentEdge);
                edgeList.add(currentEdge);
            }
            inp = new File("inp/stop_times.txt");
            readIn = new Scanner(inp);
            readIn.nextLine();
            String previousLine = readIn.nextLine();
            while (readIn.hasNextLine()){
                String currentLine = readIn.nextLine();
                String[] prevParameters = previousLine.split(",");
                for(String s : prevParameters) s.trim();
                String[] currParameters = currentLine.split(",");
                for(String s : currParameters) s.trim();
                if(prevParameters[0].equals(currParameters[0])){
                    edge currentEdge = new edge(1, getStop(Integer.valueOf(prevParameters[3])), getStop(Integer.valueOf(currParameters[3])));
                    adjList[currentEdge.stopFrom.map_id].add(currentEdge);
                    edgeList.add(currentEdge);
                }
                previousLine = currentLine;
            }
        } catch (Exception e) { System.out.println("Error occurred when reading in file " + e); }
    }
    public class path{
        LinkedList<stop> pathStops;
        double cost;
        path(LinkedList<stop> pathStops, double cost){
            this.pathStops = pathStops;
            this.cost = cost;
        }
    }
    path getShortestPath(int fromStopID, int toStopID){
        stop fromStop = getStop(fromStopID);
        stop toStop = getStop(toStopID);
        distTo = new double[V];
        prevTo = new stop[V];
        LinkedList<stop> unsettledNodes = new LinkedList<>();
        for(stop s : stopList){
            distTo[s.map_id] = Double.POSITIVE_INFINITY;
            prevTo[s.map_id] = null;
            unsettledNodes.add(s);
        }
        distTo[fromStop.map_id] = 0;
        while(!unsettledNodes.isEmpty()){
            Collections.sort(unsettledNodes, new stopCompare());
            stop currentStop = unsettledNodes.poll();
            if(distTo[currentStop.map_id] != Double.POSITIVE_INFINITY) {
                for (edge e : adjList[currentStop.map_id]) {
                    double alternativePath = distTo[e.stopFrom.map_id] + e.weight;
                    if(alternativePath < distTo[e.stopTo.map_id]){
                        distTo[e.stopTo.map_id] = alternativePath;
                        prevTo[e.stopTo.map_id] = e.stopFrom;
                    }
                }
            }
            else {
                break;
            }
        }
        LinkedList<stop> stopPath = new LinkedList<stop>();
        stopPath.add(toStop);
        stop prev = prevTo[toStop.map_id];
        while(prev != fromStop){
            stopPath.add(prev);
            prev = prevTo[prev.map_id];
        }
        stopPath.add(fromStop);
        LinkedList reversedStopPath = new LinkedList();{
            for(int k = stopPath.size() - 1; k >= 0; k--){
                reversedStopPath.add(stopPath.get(k));
            }
        }
        return new path(reversedStopPath, distTo[toStop.map_id]);
    }
    private class stopCompare implements Comparator<stop> {
        @Override
        public int compare(stop o1, stop o2) {
            if(distTo[o1.map_id] < distTo[o2.map_id])
                return -1;
            else if(distTo[o1.map_id] > distTo[o2.map_id])
                return 1;
            else return 0;
        }
    }
    stop getStop(int stop_id){
        try {
            return stop_id_hash.get(stop_id);
        } catch (Exception e) { throw new IllegalArgumentException(); }
    }
}
class searchStops {
    TST<String> tst;

    searchStops(){
        tst = new TST<>();
        try{
            File inp = new File("inp/stops.txt");
            Scanner readIn = new Scanner(inp);
            readIn.nextLine(); //skips header
            while (readIn.hasNextLine()){
                String stop = readIn.nextLine();
                String stopName = stop.split(",")[2];
                stopName.trim();
                if(stopName.startsWith("WB") || stopName.startsWith("NB") || stopName.startsWith("SB") || stopName.startsWith("EB") || stopName.startsWith("FLAGSTOP")) {
                    StringBuilder build = new StringBuilder(stopName);
                    String prefix = stopName.split(" ")[0];
                    build.delete(0, prefix.length());
                    build.append(" " + prefix);
                    stopName = build.toString().trim();
                }
                tst.put(stopName, stop);
            }
        } catch (Exception e) {
            System.out.println("Error occurred when reading in file" + e);
        }
    }
    void printStopsStartingWith(String prefix){
        Iterable<String> prefixStrings = tst.keysWithPrefix(prefix);
        for(String s : prefixStrings){
            System.out.println(s);
        }
    }
}

class searchTimes{
    PriorityQueue<stop> stopQueue;
    private class stop{
        int trip_id;
        Time arrivalTime;
        Time departureTime;
        String details;

        stop(int trip_id, Time arrivalTime, Time departureTime, String details){
            this.trip_id = trip_id;
            this.arrivalTime = arrivalTime;
            this.departureTime = departureTime;
            this.details = details;
        }
    }
    private class stopComparator implements Comparator<stop>{
        @Override
        public int compare(stop s1, stop s2){
            if(s1.trip_id < s2.trip_id) return -1;
            else if(s1.trip_id > s2.trip_id) return 1;
            else return 0;
        }
    }
    searchTimes(String specifiedArrivalTime){
        try{
            checkInputtedArrivalTime(specifiedArrivalTime);
        } catch (IllegalArgumentException specifiedBadTime) {
            throw new IllegalArgumentException();
        }
        try{
            stopQueue = new PriorityQueue<>(new stopComparator());
            File inp = new File("inp/stop_times.txt");
            Scanner readIn = new Scanner(inp);
            while (readIn.hasNextLine()) {
                String stop = readIn.nextLine();
                try{
                    String arrivalTimeString = stop.split(",")[1].trim();
                    if(specifiedArrivalTime.equals(arrivalTimeString)){
                        int trip_id = Integer.valueOf(stop.split(",")[0]);
                        String[] arrivalTimeArray = arrivalTimeString.split(":");
                        String departureTimeString = stop.split(",")[1];
                        departureTimeString.trim();
                        String[] departureTimeArray = arrivalTimeString.split(":");
                        Time arrivalTime = new Time(Integer.valueOf(arrivalTimeArray[0]), Integer.valueOf(arrivalTimeArray[1]), Integer.valueOf(arrivalTimeArray[2]));
                        Time departureTime = new Time(Integer.valueOf(departureTimeArray[0]), Integer.valueOf(departureTimeArray[1]), Integer.valueOf(departureTimeArray[2]));
                        stopQueue.add(new stop(trip_id, arrivalTime, departureTime, stop));
                    }
                } catch (IllegalArgumentException badTime) {}
            }
        } catch (Exception e) { System.out.println("Error occurred when reading in file" + e); }
    }
    void printSearchTimes(){
        while (!stopQueue.isEmpty()){
            System.out.println(stopQueue.poll().details);
        }
    }
    void checkInputtedArrivalTime(String time){
        int hour = Integer.valueOf(time.split(":")[0]);
        int minutes = Integer.valueOf(time.split(":")[1]);
        int seconds = Integer.valueOf(time.split(":")[2]);
        if(hour > 23) throw new IllegalArgumentException();
        if(minutes > 59) throw new IllegalArgumentException();
        if(seconds > 59) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        FinalProject project = new FinalProject();
        Scanner input = new Scanner(System.in);
        boolean programmeTerminated = false;
        while (!programmeTerminated) {
            System.out.println(
                    "\nIn order to find the shortest path between two bus stops, enter \"1\"\n" +
                            "In order to search for a bus stop by either it's full name or the first few characters, enter \"2\"\n" +
                            "In order to search for a bus stop by given arrival time, enter \"3\"\n" +
                            "In order to end the programme, please enter \"4\""
            );
            int selection = 0;
            boolean selectedSearch = false;
            System.out.print("Please enter an integer representing your choice of task: ");
            while (!selectedSearch) {
                if (input.hasNextInt()) {
                    selection = input.nextInt();
                    if (selection > 4 || selection < 1) {
                        System.out.print("ERROR: Please enter an integer BETWEEN 1 and 4 representing your choice: ");
                    } else {
                        selectedSearch = true;
                    }
                } else {
                    input.next();
                    System.out.print("ERROR: Please enter an INTEGER between 1 and 4 representing your choice: ");
                }
            }
            switch (selection) {
                case 1: {
                    boolean validStops = false;
                    try {
                        System.out.print("Please enter the ID of the stop you would like to go from: ");
                        while (!validStops) {
                            int fromID = 0;
                            int toID = 0;
                            boolean fromIDEntered = false;
                            while (!fromIDEntered) {
                                if (input.hasNextInt()) {
                                    fromID = input.nextInt();
                                    fromIDEntered = true;
                                } else {
                                    System.out.print("ERROR: Please enter the ID of the stop you would like to go from: ");
                                }
                            }
                            boolean toIDEntered = false;
                            System.out.print("Please enter the ID of the stop you would like to go to: ");
                            while (!toIDEntered) {
                                if (input.hasNextInt()) {
                                    toID = input.nextInt();
                                    toIDEntered = true;
                                } else {
                                    System.out.print("ERROR: Please enter the ID of the stop you would like to go to: ");
                                }
                            }
                            project.printShortestPath(fromID, toID);
                            validStops = true;
                        }
                    } catch (Exception e) {
                        System.out.println("ERROR: not a valid stopID");
                    }
                    break;
                }
                case 2: {
                    String searchString = "";
                    boolean searchStringEntered = false;
                    System.out.print("Please enter a string to search stops for: ");
                    while (!searchStringEntered) {
                        if (input.hasNext()) {
                            searchString = input.next().toUpperCase();
                            searchStringEntered = true;
                        } else {
                            System.out.print("ERROR: Please enter a STRING to search stops for: ");
                        }
                    }
                    project.returnSearchStops(searchString);
                    break;
                }
                case 3: {
                    System.out.print("Please enter a time hh:mm:ss to search arrival times for: ");
                    boolean legalTimeEntered = true;
                    do {
                        legalTimeEntered = true;
                        try {
                            if (input.hasNext()) {
                                project.searchForTimes(input.next());
                            }
                        } catch (Exception e) {
                            legalTimeEntered = false;
                            System.out.print("ERROR: Illegal time entered, please enter a time hh:mm:ss to search arrival times for: ");
                        }
                    } while (!legalTimeEntered);
                    break;
                }
                case 4:
                    System.out.println("Thank you for using this programme, programme is now terminating");
                    programmeTerminated = true;
                    break;
            }
        }
    }
}

