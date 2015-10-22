package Assignment1;

import java.util.*;

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

    private static PuzzleHeuristicFunction misplacedTilesHeuristic = new PuzzleHeuristicFunction() {
        @Override
        public int calculateHeuristic(ArrayList<ArrayList<Integer>> grid) {
            int dimension = grid.size();
            int result = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int currentValue = grid.get(i).get(j);
                    if (currentValue != 0 && (dimension * i + j) != currentValue) {
                        result++;
                    }
                }
            }
            return result;
        }
    };

    private static PuzzleHeuristicFunction manhattanDistanceHeuristic = new PuzzleHeuristicFunction() {
        @Override
        public int calculateHeuristic(ArrayList<ArrayList<Integer>> grid) {
            int dimension = grid.size();
            int result = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int currentValue = grid.get(i).get(j);
                    if (currentValue != 0) {
                        int expectedRow = currentValue / dimension;
                        int expectedColumn = currentValue % dimension;
                        result += Math.abs(i - expectedRow) + Math.abs(j - expectedColumn);
                    }
                }
            }
            return result;
        }
    };


    private static Board goal;

    private static Board initialState;
    private static PriorityQueue<Board> frontierList;

    //maps from board to cost of the board state
    private static HashMap<Board, Integer> frontierSet;

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

        long totalTime = 0;
        for (int i = 0; i < 1000; i++) {
            long start = System.currentTimeMillis();
            runAStar(3, manhattanDistanceHeuristic);
            long finish = System.currentTimeMillis();
            totalTime += finish - start;
            System.out.println(finish-start);
        }


        System.out.println("Average time: " + (totalTime / 1000.0 / 1000.0));
    }


    //returns the goal node after it has been polled from top of priority queue
    private static Board runAStar(int dimension,PuzzleHeuristicFunction heuristicFunction) {
        initializeGoal(dimension,heuristicFunction);
        resetState();
        initialState = new Board(dimension, heuristicFunction);
//        initialState = Board.askForTwoByTwoInput(manhattanDistanceHeuristic);
//        System.out.println(initialState);
        frontierList.add(initialState);
        frontierSet.put(initialState, initialState.getCost());
        while (!frontierList.isEmpty()) {
            Board toExpand = frontierList.poll();
            frontierSet.remove(toExpand);
            exploredSet.add(toExpand);
            if (goalTest(toExpand)) {
                return toExpand;
            }
            ArrayList<Board> children = toExpand.generateChildren();
            for (Board child : children) {
                if (!exploredSet.contains(child)) { //if the explored set doesn't already have this
                    if (frontierSet.containsKey(child)) {
                        if (child.getCost() < frontierSet.get(child)) {
                            frontierSet.put(child, child.getCost());
                            frontierList.remove(child); //this will remove the node already in frontierlist
                            frontierList.add(child); //this will add the new child to frontierlist
                        } //else don't consider this node or any of its children
                    } else {
                        frontierSet.put(child, child.getCost());
                        frontierList.add(child);
                    }
                }//otherwise don't do anything
            }

        }
        return null;
    }


    //prints all parent nodes and actions taked to reach current board
    private static void outputPath(Board board) {
        if (board == null) {
            throw new RuntimeException("Null Board!!!");
        }
        Board currentBoard = board;
        Stack<Board> path = new Stack<>();
        while (currentBoard.getParentAction() != null) {
            path.push(currentBoard);
            currentBoard = currentBoard.getParentNode();
        }
        path.push(currentBoard);
        while (!path.empty()) {
            Board finalPathCurrent = path.pop();
            System.out.println(finalPathCurrent.getParentAction());
            System.out.println(finalPathCurrent);
        }
    }

    private static void initializeGoal(int dimension, PuzzleHeuristicFunction heuristicFunction) {
        ArrayList<ArrayList<Integer>> goalUnderlying = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < dimension; i++) {
            ArrayList<Integer> toAdd = new ArrayList<>();
            for (int j = 0; j < dimension; j++) {
                toAdd.add(count++);
            }
            goalUnderlying.add(toAdd);
        }
        goal = new Board(goalUnderlying, heuristicFunction);

    }

    private static void resetState() {
        initialState = null;
        frontierList = new PriorityQueue<>();
        frontierSet = new HashMap<>();
        exploredSet = new HashSet<>();
    }

    private static boolean goalTest(Board board) {
        return (board.equals(goal));
    }
}
