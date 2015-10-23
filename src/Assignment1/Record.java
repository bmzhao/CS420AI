package Assignment1;

import java.util.Comparator;

/**
 * Created by brianzhao on 10/23/15.
 */
public class Record implements Comparable<Record>{
    private int depth;
    private int numExpandedNodes;
    private long runTimeMillis;

    public Record(int depth, int numExpandedNodes, long runTimeMillis) {
        this.depth = depth;
        this.numExpandedNodes = numExpandedNodes;
        this.runTimeMillis = runTimeMillis;
    }

    public Record(Board board, int numExpandedNodes, long runTimeMillis) {
        int currentDepth = 0;
        while (board.getParentNode()!= null) {
            currentDepth++;
            board = board.getParentNode();
        }
        this.depth = currentDepth;
        this.numExpandedNodes = numExpandedNodes;
        this.runTimeMillis = runTimeMillis;
    }

    public int getDepth() {
        return depth;
    }

    public int getNumExpandedNodes() {
        return numExpandedNodes;
    }

    public long getRunTimeMillis() {
        return runTimeMillis;
    }

    @Override
    public int compareTo(Record o) {
        return this.depth-o.depth;
    }
}
