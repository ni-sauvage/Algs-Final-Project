package edu.princeton.cs.algs4;

import java.io.File;
import java.sql.Time;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class FinalProject {
    private searchStops stops;
    private searchTimes times;
}

class searchStops {
    TST<String> tst;

    searchStops(){
        tst = new TST<>();
        try{
            File inp = new File("inp/stops.txt");
            Scanner readIn = new Scanner(inp);
            while (readIn.hasNextLine()){
                String stop = readIn.nextLine();
                String stopName = stop.split(",")[2];
                StringBuilder build = new StringBuilder(stopName);
                String prefix = stopName.split(" ")[0];
                build.delete(0, prefix.length() - 1);
                build.append(" "+prefix);
                tst.put(stopName, stopName);
            }
        } catch (Exception e) { System.out.println("Error occurred when reading in file" + e); }
    }
    public Queue<String> stopsStartingWith(String prefix){
        Queue<String> returnQueue = (Queue<String>) tst.keysWithPrefix(prefix);
        return returnQueue;
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
            else if(s1.trip_id < s2.trip_id) return 1;
            else return 0;
        }
    }
    searchTimes(String specifiedArrivalTime){
        try{
            checkInputtedArrivalTime(specifiedArrivalTime);
        } catch (IllegalArgumentException specifiedBadTime) {
            System.out.println("Time specified is not possible" + specifiedBadTime);
            throw new IllegalArgumentException();
        }
        try{
            stopQueue = new PriorityQueue<>(0, new stopComparator());
            File inp = new File("inp/stop_times.txt");
            Scanner readIn = new Scanner(inp);
            while (readIn.hasNextLine()) {
                String stop = readIn.nextLine();
                stop readInStop;
                try{
                    int trip_id = Integer.valueOf(stop.split(",")[0]);
                    String arrivalTimeString = stop.split(",")[1];
                    arrivalTimeString.trim();
                    String[] arrivalTimeArray = arrivalTimeString.split(":");
                    String departureTimeString = stop.split(",")[1];
                    departureTimeString.trim();
                    String[] departureTimeArray = arrivalTimeString.split(":");
                    Time arrivalTime = new Time(Integer.valueOf(arrivalTimeArray[0]), Integer.valueOf(arrivalTimeArray[1]), Integer.valueOf(arrivalTimeArray[2]));
                    Time departureTime = new Time(Integer.valueOf(departureTimeArray[0]), Integer.valueOf(departureTimeArray[1]), Integer.valueOf(departureTimeArray[2]));
                    if(specifiedArrivalTime.equals(arrivalTimeString)){
                        stopQueue.add(new stop(trip_id, arrivalTime, departureTime, stop));
                    }
                } catch (IllegalArgumentException badTime) {}
            }
        } catch (Exception e) { System.out.println("Error occurred when reading in file" + e); }
    }
    void printSearchTimes(){
        for(stop s : stopQueue){
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
}
