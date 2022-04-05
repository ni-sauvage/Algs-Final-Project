package edu.princeton.cs.algs4;

import java.io.File;
import java.sql.Time;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

class searchTimes {
    PriorityQueue<stop> stopQueue;

    private class stop {
        int trip_id;
        Time arrivalTime;
        Time departureTime;
        String details;

        stop(int trip_id, Time arrivalTime, Time departureTime, String details) {
            this.trip_id = trip_id;
            this.arrivalTime = arrivalTime;
            this.departureTime = departureTime;
            this.details = details;
        }
    }

    private class stopComparator implements Comparator<stop> {
        @Override
        public int compare(stop s1, stop s2) {
            if (s1.trip_id < s2.trip_id) return -1;
            else if (s1.trip_id > s2.trip_id) return 1;
            else return 0;
        }
    }

    searchTimes() {
        try {
            stopQueue = new PriorityQueue<>(new stopComparator());
            File inp = new File("inp/stop_times.txt");
            Scanner readIn = new Scanner(inp);
            while (readIn.hasNextLine()) {
                String stop = readIn.nextLine();
                try {
                    String arrivalTimeString = stop.split(",")[1].trim();
                    int trip_id = Integer.valueOf(stop.split(",")[0]);
                    String[] arrivalTimeArray = arrivalTimeString.split(":");
                    String departureTimeString = stop.split(",")[1];
                    departureTimeString.trim();
                    String[] departureTimeArray = arrivalTimeString.split(":");
                    Time arrivalTime = new Time(Integer.valueOf(arrivalTimeArray[0]), Integer.valueOf(arrivalTimeArray[1]), Integer.valueOf(arrivalTimeArray[2]));
                    Time departureTime = new Time(Integer.valueOf(departureTimeArray[0]), Integer.valueOf(departureTimeArray[1]), Integer.valueOf(departureTimeArray[2]));
                    stopQueue.add(new stop(trip_id, arrivalTime, departureTime, stop));
                } catch (IllegalArgumentException badTime) {
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred when reading in file" + e);
        }
    }

    LinkedList<stop> getTimes(String specifiedArrivalTime) {
        try {
            checkInputtedArrivalTime(specifiedArrivalTime);
        } catch (IllegalArgumentException specifiedBadTime) {
            throw new IllegalArgumentException();
        }
        LinkedList<stop> stopsWithTime = new LinkedList<>();
        String hourMinsSec[] = specifiedArrivalTime.split(":");
        Time parsedArrivalTime = new Time(Integer.valueOf(hourMinsSec[0]), Integer.valueOf(hourMinsSec[1]), Integer.valueOf(hourMinsSec[2]));
        PriorityQueue<stop> stopQueueLocalCopy = new PriorityQueue<>(stopQueue);
        while (!stopQueueLocalCopy.isEmpty()) {
            stop currentStop = stopQueueLocalCopy.poll();
            if (currentStop.arrivalTime.equals(parsedArrivalTime)) {
                stopsWithTime.add(currentStop);
            }
        }
        return stopsWithTime;
    }

    void printSearchTimes(LinkedList<stop> stopsWithTime) {
        for (int i = 0; i < stopsWithTime.size(); i++) {
            System.out.println(stopsWithTime.get(i).details);
        }
    }

    void checkInputtedArrivalTime(String time) {
        int hour = Integer.valueOf(time.split(":")[0]);
        int minutes = Integer.valueOf(time.split(":")[1]);
        int seconds = Integer.valueOf(time.split(":")[2]);
        if (hour > 23) throw new IllegalArgumentException();
        if (minutes > 59) throw new IllegalArgumentException();
        if (seconds > 59) throw new IllegalArgumentException();
    }
}