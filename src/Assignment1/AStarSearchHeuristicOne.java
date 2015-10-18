package Assignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Created by brianzhao on 10/17/15.
 */
public class AStarSearchHeuristicOne {
    private Board initialState;
    private PriorityQueue<Board> frontierList;
    private HashSet<String> frontierSet;
    //this actually gives us the tree of everything
    private HashSet<BoardNode> exploredSet;

    //put initial state in frontier
    //generate children of top of priority queue,
    //put children in

    public AStarSearchHeuristicOne(Board board) {
        initialState = board;
    }




}
