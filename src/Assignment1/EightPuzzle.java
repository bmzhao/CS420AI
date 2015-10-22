package Assignment1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Created by brianzhao on 10/17/15.
 */
public class EightPuzzle {

    private static PuzzleHeuristicFunction zeroHeuristic = new PuzzleHeuristicFunction() {
        @Override
        public int calculateHeuristic(ArrayList<ArrayList<Integer>> grid) {
            return 0;
        }
    };

    private static String goalString;

    private static Board initialState;
    private static PriorityQueue<Board> frontierList;
    private static HashSet<Board> frontierSet;

    //this actually gives us the tree of everything
    private static HashSet<Board> exploredSet;

    //put initial state in frontier
    //while priority queue is not empty
    //expand top of priority queue:
        //check if goal state, if so terminate
        //generate children
        //iterate through children, checking if already in explored set or priority queue
            //if in priority queue, check if better path cost
                //if better path cost, update priority queue (remove that node and re-add)
            //else add to priority queue





    public static void main(String[] args) {
//        ArrayList<ArrayList<Integer>> potentialBoard = Board.askForThreeByThreeInput();


    }


    //returns the goal node after it has been polled from top of priority queue
    private static Board runAStar() {
        initializeGoalString();
        resetState();
        initialState = new Board(3, zeroHeuristic);
        System.out.println(initialState);
        frontierList.add(initialState);
        frontierSet.add(initialState);
        while (!frontierList.isEmpty()) {
            Board toExpand = frontierList.poll();
            frontierSet.remove(toExpand);
            if (goalTest(toExpand)) {
                return toExpand;
            }
        }
    }

    //prints all parent nodes and actions taked to reach current board
    private static void outputPath(Board board) {

    }

    private static void initializeGoalString() {
        ArrayList<ArrayList<Integer>> goalUnderlying = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < 3; i++) {
            ArrayList<Integer> toAdd = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                toAdd.add(count++);
            }
            goalUnderlying.add(toAdd);
        }
        Board goalBoard = new Board(goalUnderlying, zeroHeuristic);
        goalString = goalBoard.getBoardState();
    }
    private static void resetState() {
        initialState = null;
        frontierList = new PriorityQueue<>();
        frontierSet = new HashSet<>();
        exploredSet = new HashSet<>();
    }

    private static boolean goalTest(Board board) {
        return (board.getBoardState().equals(goalString));
    }
}
