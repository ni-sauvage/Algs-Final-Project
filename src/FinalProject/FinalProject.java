package FinalProject;

import java.util.*;

public class FinalProject {
    private searchStops stops;
    private searchTimes times;
    private EdgeWeightedDigraph graph;
    FinalProject(){
        this.stops = new searchStops();
        this.graph = new EdgeWeightedDigraph();
        this.times = new searchTimes();
    }
    void returnSearchStops(String prefix){
        this.stops.printStopsStartingWith(prefix);
    }

    void searchForTimes(String time){
        System.out.println("The following list was returned based on arrival time: ");
        this.times.printSearchTimes(this.times.getTimes(time));
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
