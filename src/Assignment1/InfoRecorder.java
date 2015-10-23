package Assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by brianzhao on 10/23/15.
 */
public class InfoRecorder {
    //depth mapped to list of number of searched nodes for each trial
    private ArrayList<Record> records = new ArrayList<>();
    private HashMap<Integer, ColumnsOfTable> map = new HashMap<>();

    public InfoRecorder() {

    }

    public void addRecord(Record record) {
        records.add(record);
    }


    public void displayTable() {
        System.out.println("Currently analyzing all recorded data...");
        if (records.size() == 0) {
            return;
        }
        Collections.sort(records);
        ArrayList<Integer> depths = new ArrayList<>();
//        depth: number of cases, avg seach cost (number of explored nodes), avg time taken in seconds,
        int currentDepth = records.get(0).getDepth();
        depths.add(currentDepth);
        map.put(currentDepth, new ColumnsOfTable());

        for (int i = 0; i < records.size(); i++) {
            Record currentRecord = records.get(i);
            if (currentRecord.getDepth() == currentDepth) {
                ColumnsOfTable currentColumn = map.get(currentDepth);
                currentColumn.numberOfCases++;
                currentColumn.totalSearchCost += currentRecord.getNumExpandedNodes();
                currentColumn.totalTimeTakenMilliseconds += currentRecord.getRunTimeMillis();
            } else {
                currentDepth = currentRecord.getDepth();
                depths.add(currentDepth);
                ColumnsOfTable currentColumn = new ColumnsOfTable();
                currentColumn.numberOfCases++;
                currentColumn.totalSearchCost += currentRecord.getNumExpandedNodes();
                currentColumn.totalTimeTakenMilliseconds += currentRecord.getRunTimeMillis();
                map.put(currentDepth, currentColumn);
            }
        }

        //print the head of the table:
        System.out.format("%-20s %-20s %-20s %-20s\n", "depth", "number of cases", "avg search cost", "avg time in millis");

        for (int i = 0; i <depths.size(); i++) {
            ColumnsOfTable currentColumn = map.get(depths.get(i));
            long numCases = currentColumn.numberOfCases;
            double avgSearchCost = currentColumn.totalSearchCost * 1.0 / numCases;
            double avgTimeInSeconds = currentColumn.totalTimeTakenMilliseconds * 1.0 / numCases / 1000;
            System.out.format("%-20d %-20d %-20f %-20f\n", depths.get(i), currentColumn.numberOfCases, avgSearchCost, avgTimeInSeconds);
        }
        System.out.println();

    }

    public void reset() {
        records.clear();
        map = new HashMap<>();
    }

    private class ColumnsOfTable {
        public long totalSearchCost = 0;
        public long totalTimeTakenMilliseconds = 0;
        public long numberOfCases =0;
    }

}
