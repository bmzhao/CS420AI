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
                        int expectedRow = currentValue / 3;
                        int expectedColumn = currentValue % 3;
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
//        outputPath(runAStar());

//        ArrayList<ArrayList<Integer>> test = new ArrayList<>();
//        ArrayList<Integer> testAdd = new ArrayList<>();
//        testAdd.add(0);
//        testAdd.add(2);
//        test.add(testAdd);
//        ArrayList<Integer> testAdd1 = new ArrayList<>();
//        testAdd1.add(1);
//        testAdd1.add(3);
//        test.add(testAdd1);
//        System.out.println(misplacedTilesHeuristic.calculateHeuristic(test));

        long totalTime = 0;
//        for (int i = 0; i < 100; i++) {
        long start = System.currentTimeMillis();
        outputPath(runAStar());
        long finish = System.currentTimeMillis();
        totalTime += finish - start;
//        }

        System.out.println(totalTime / 1000.0);
    }


    //returns the goal node after it has been polled from top of priority queue
    private static Board runAStar() {
        initializeGoal();
        resetState();
        initialState = new Board(3, manhattanDistanceHeuristic);
        System.out.println(initialState);
        frontierList.add(initialState);
        frontierSet.put(initialState, initialState.getCost());
        while (!frontierList.isEmpty()) {
            Board toExpand = frontierList.poll();
            frontierSet.remove(toExpand);
            if (goalTest(toExpand)) {
                return toExpand;
            }
            ArrayList<Board> children = toExpand.generateChildren();
            for (Board child : children)
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

    private static void initializeGoal() {
        ArrayList<ArrayList<Integer>> goalUnderlying = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < 3; i++) {
            ArrayList<Integer> toAdd = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                toAdd.add(count++);
            }
            goalUnderlying.add(toAdd);
        }
        goal = new Board(goalUnderlying, zeroHeuristic);

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
