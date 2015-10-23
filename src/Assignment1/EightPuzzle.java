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


    private static final int DIMENSION = 3;
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
        displayMenu();

    }


    private static void displayMenu() {
        String input = "";
        while (true) {
            System.out.println("Select an option to perform:");
            System.out.println("1. Enter your own puzzle");
            System.out.println("2. See the solution to a randomly generated puzzle");
            System.out.println("3. Do 1000 random puzzles, and see data ");
            System.out.println("4. Exit");
            System.out.print("Enter your option: ");
            Scanner scanner = new Scanner(System.in);
            //read this in as a string to be more robust
            input = scanner.nextLine();
            if (!input.matches("[1234]")) {
                System.out.println("You entered a bad input...retrying");
                input = "";
                continue;
            }
            int value = Integer.parseInt(input);
            if (value == 4) {
                return;
            }
            System.out.println();
            PuzzleHeuristicFunction puzzleHeuristicFunction = chooseHeuristic();
            if (puzzleHeuristicFunction == null) {
                continue;
            }

            Board board = null;
            switch (value) {
                case 1:
                    board = Board.askForThreeByThreeInput(puzzleHeuristicFunction);
                    break;
                case 2:
                    board = new Board(DIMENSION, puzzleHeuristicFunction);
                    break;
                case 3: {
                    InfoRecorder info = new InfoRecorder();
                    for (int i = 0; i < 1000; i++) {
                        Board currentBoard = new Board(DIMENSION, puzzleHeuristicFunction);
                        long startTime = System.currentTimeMillis();
                        Board resultBoard =runAStar(currentBoard, DIMENSION, puzzleHeuristicFunction);
                        long endTime = System.currentTimeMillis();
                        info.addRecord(new Record(resultBoard, exploredSet.size(), endTime - startTime));
                        System.out.println("Finished puzzle " + i + "...");
                    }
                    info.displayTable();
                    continue;
                }
                case 4:
                    return;
                default:
                    break;
            }
            InfoRecorder info = new InfoRecorder();
            long startTime = System.currentTimeMillis();
            Board result = runAStar(board, DIMENSION, puzzleHeuristicFunction);
            long endTime = System.currentTimeMillis();
            info.addRecord(new Record(result, exploredSet.size(), endTime - startTime));
            System.out.println("Here is the output path: ");
            outputPath(result);
            System.out.println("\nHere is the data obtained from this solution: ");
            info.displayTable();
        }
    }

    private static PuzzleHeuristicFunction chooseHeuristic() {
        String input = "";
        Scanner scanner = new Scanner(System.in);

        while (!input.equalsIgnoreCase("n")) {
            System.out.println("Which heuristic would you like to choose?");
            System.out.println("1. h(n) = 0");
            System.out.println("2. h(n) = number of misplaced tiles");
            System.out.println("3. h(n) = manhattan distance");
            System.out.println("4. To return back to another choice");

            input = scanner.nextLine();
            if (!input.matches("[1234]")) {
                System.out.println("You entered a bad input...retrying");
                input = "";
                continue;
            }
            int value = Integer.parseInt(input);
            switch (value) {
                case 1:
                    return zeroHeuristic;
                case 2:
                    return misplacedTilesHeuristic;
                case 3:
                    return manhattanDistanceHeuristic;
                case 4:
                    return null;
                default:
                    throw new RuntimeException("error in input to chosen heuristic");
            }
        }
        return null;
    }


    //returns the goal node after it has been polled from top of priority queue
    private static Board runAStar(Board board, int dimension, PuzzleHeuristicFunction heuristicFunction) {
        initializeGoal(dimension, heuristicFunction);
        resetState();
        initialState = board;
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
            System.out.println(finalPathCurrent.getParentAction() == null? "initial state": finalPathCurrent.getParentAction());
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
