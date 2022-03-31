package edu.princeton.cs.algs4;

import java.io.File;
import java.sql.Time;
import java.util.LinkedList;
import java.util.Scanner;

public class FinalProject {
    private searchStops stops;

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
    private class stops{
        String trip_id;
        Time arrivalTime;
        Time departureTime;
    }
}
