package FinalProject;


import java.io.File;
import java.util.Scanner;

class searchStops {
    TST<String> tst;

    searchStops(){
        tst = new TST<String>();
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