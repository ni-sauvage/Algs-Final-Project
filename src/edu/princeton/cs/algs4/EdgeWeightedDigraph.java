package edu.princeton.cs.algs4;

import java.io.File;
import java.util.*;

class EdgeWeightedDigraph {
    private LinkedList<edge>[] adjList;
    private final LinkedList<stop> stopList = new LinkedList<>();
    private final HashMap<Integer, stop> stop_id_hash = new HashMap<Integer, stop>();
    double distTo[];
    stop prevTo[];
    private int V;

    public class stop {
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

        stop(int stop_id, String stop_code, String stop_name, String stop_desc, String stop_lat, String stop_lon, String zone_id, String stop_url, String location_type, String parent_station, int map_id) {
            this.stop_id = stop_id;
            this.stop_code = stop_code;
            this.location_type = location_type;
            this.map_id = map_id;
            this.stop_lat = stop_lat;
            this.stop_lon = stop_lon;
            this.stop_name = stop_name;
            this.stop_desc = stop_desc;
            this.stop_url = stop_url;
            this.parent_station = parent_station;
            this.zone_id = zone_id;
        }
    }

    private class edge {
        double weight;
        stop stopFrom;
        stop stopTo;

        edge(double weight, stop stopFrom, stop stopTo) {
            this.weight = weight;
            this.stopFrom = stopFrom;
            this.stopTo = stopTo;
        }
    }

    public EdgeWeightedDigraph() {
        try {
            File inp = new File("inp/stops.txt");
            Scanner readIn = new Scanner(inp);
            int i = 0;
            readIn.nextLine();
            while (readIn.hasNextLine()) {
                String line = readIn.nextLine();
                String[] parameters = line.split(",");
                for (String s : parameters) s.trim();
                stop currentStop = new stop(
                        Integer.parseInt(parameters[0]), parameters[1], parameters[2], parameters[3],
                        parameters[4], parameters[5], parameters[6], parameters[7],
                        parameters[8], parameters.length != 10 ? null : parameters[9], i);
                stopList.add(currentStop);
                stop_id_hash.put(Integer.valueOf(parameters[0]), currentStop);
                i++;
            }
            adjList = new LinkedList[i];
            V = i;
            for (int j = 0; j < adjList.length; j++) adjList[j] = new LinkedList<>();
            inp = new File("inp/transfers.txt");
            readIn = new Scanner(inp);
            readIn.nextLine();
            while (readIn.hasNextLine()) {
                String line = readIn.nextLine();
                String[] parameters = line.split(",");
                for (String s : parameters) {
                    s.trim();
                }
                edge currentEdge;
                if (parameters[2].equals("0")) {
                    currentEdge = new edge(2, getStop(Integer.parseInt(parameters[0])), getStop(Integer.parseInt(parameters[1])));
                } else {
                    currentEdge = new edge(Double.parseDouble(parameters[3]) / 100, getStop(Integer.parseInt(parameters[0])), getStop(Integer.parseInt(parameters[1])));
                }
                adjList[currentEdge.stopFrom.map_id].add(currentEdge);
            }
            inp = new File("inp/stop_times.txt");
            readIn = new Scanner(inp);
            readIn.nextLine();
            String previousLine = readIn.nextLine();
            while (readIn.hasNextLine()) {
                String currentLine = readIn.nextLine();
                String[] prevParameters = previousLine.split(",");
                for (String s : prevParameters) s.trim();
                String[] currParameters = currentLine.split(",");
                for (String s : currParameters) s.trim();
                if (prevParameters[0].equals(currParameters[0])) {
                    edge currentEdge = new edge(1, getStop(Integer.parseInt(prevParameters[3])), getStop(Integer.parseInt(currParameters[3])));
                    adjList[currentEdge.stopFrom.map_id].add(currentEdge);
                }
                previousLine = currentLine;
            }
        } catch (Exception e) {
            System.out.println("Error occurred when reading in file " + e);
        }
    }

    public class path {
        ArrayList<stop> pathStops;
        double cost;

        path(ArrayList<stop> pathStops, double cost) {
            this.pathStops = pathStops;
            this.cost = cost;
        }
    }

    path getShortestPath(int fromStopID, int toStopID) {
        stop fromStop = getStop(fromStopID);
        stop toStop = getStop(toStopID);
        distTo = new double[V];
        prevTo = new stop[V];
        ArrayList<stop> unsettledNodes = new ArrayList<>();
        for (stop s : stopList) {
            distTo[s.map_id] = Double.POSITIVE_INFINITY;
            prevTo[s.map_id] = null;
            unsettledNodes.add(s);
        }
        distTo[fromStop.map_id] = 0;
        while (!unsettledNodes.isEmpty()) {
            stop currentStop = getMinimum(unsettledNodes);
            if(currentStop == null) break; //If no stop had a distTo less than infinity, it will return null, so we should not execute the for loop
            for (edge e : adjList[currentStop.map_id]) {
                double alternativePath = distTo[e.stopFrom.map_id] + e.weight;
                if (alternativePath < distTo[e.stopTo.map_id]) {
                    distTo[e.stopTo.map_id] = alternativePath;
                    prevTo[e.stopTo.map_id] = e.stopFrom;
                }
            }
        }
        ArrayList<stop> stopPath = new ArrayList<>();
        stopPath.add(toStop);
        stop prev = prevTo[toStop.map_id];
        while (prev != fromStop) {
            stopPath.add(prev);
            prev = prevTo[prev.map_id];
        }
        stopPath.add(fromStop);
        Collections.reverse(stopPath);
        return new path(stopPath, distTo[toStop.map_id]);
    }

    //Runs in 0(V) time
    private stop getMinimum(ArrayList<stop> listOfStops){
        double minimum = Double.POSITIVE_INFINITY;
        stop minStop = null;
        for(stop s : listOfStops){
            if(distTo[s.map_id] < minimum){
                minimum = distTo[s.map_id];
                minStop = s;
            }
        }
        listOfStops.remove(minStop);
        return minStop;
    }

    stop getStop(int stop_id) {
        try {
            return stop_id_hash.get(stop_id);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}