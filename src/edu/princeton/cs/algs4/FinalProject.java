package edu.princeton.cs.algs4;

import java.io.File;
import java.sql.Time;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class FinalProject {
    private searchStops stops;
    private searchTimes times;

    FinalProject(){
        this.stops = new searchStops();
    }
    void returnSearchStops(String prefix){
        this.stops.printStopsStartingWith(prefix);
    }
    void searchForTimes(String time){
        this.times = new searchTimes(time);
        System.out.println("The following list was returned based on arrival time: ");
        this.times.printSearchTimes();
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
        } catch (Exception e) { System.out.println("Error occurred when reading in file" + e); }
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
                            System.out.print("Please enter a STRING to search stops for: ");
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

