package Assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by brianzhao on 10/17/15.
 */
public class Board implements Comparable<Board> {

    public enum BoardAction {
        LeftSwap, RightSwap, TopSwap, DownSwap
    }

    private final int dimension;    //this is the size of rows/columns
    private ArrayList<ArrayList<Integer>> underlying; //this is the underlying arraylist
    private Board parentBoard;  //this is the parent pointer
    private int cost; //this is the pathCost g(n)
    private final int heuristicCost; //this is the heuristic cost h(n)
    private BoardAction parentAction; //action that the parent took to get to this node
    private final String boardState; //string representation, must always be immediately generated
    private final PuzzleHeuristicFunction heuristicDelegate;

    //entry point for creating a random puzzle
    public Board(int dimension, PuzzleHeuristicFunction heuristicDelegate) {
        this.dimension = dimension;
        underlying = generateBoard(dimension);
        parentBoard = null;
        cost = 0;
        parentAction = null;
        boardState = this.toString();
        this.heuristicDelegate = heuristicDelegate;
        heuristicCost = this.heuristicDelegate.calculateHeuristic(underlying);
    }

    //entry point for asking user for a puzzle
    public static Board askForThreeByThreeInput(PuzzleHeuristicFunction heuristicDelegate) {
        System.out.println("Please input a 3 x 3 board");
        Scanner scanner = new Scanner(System.in);
        ArrayList<ArrayList<Integer>> toReturn = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String[] integers = scanner.nextLine().split("\\s+");
            ArrayList<Integer> toAdd = new ArrayList<>();
            if (integers.length != 3) {
                throw new RuntimeException();
            }
            for (String x : integers) {
                toAdd.add(Integer.parseInt(x));
            }
            toReturn.add(toAdd);
        }
        return new Board(toReturn,heuristicDelegate);
    }

    //should have no parentaction
    public Board(ArrayList<ArrayList<Integer>> input, PuzzleHeuristicFunction heuristicDelegate) {
        dimension = input.size();
        ArrayList<Integer> oneDimension = convertToSingleDimension(input);
        if (!isSolvable(oneDimension)) {
            throw new RuntimeException("Attempted to initialize with unsolvable board");
        }
        underlying = gridCloner(input);
        parentBoard = null;
        parentAction = null;
        cost = 0;
        boardState = this.toString();
        this.heuristicDelegate = heuristicDelegate;
        heuristicCost = this.heuristicDelegate.calculateHeuristic(underlying);
    }

    private Board(int dimension, ArrayList<ArrayList<Integer>> underlying, Board parentBoard, int cost, int heuristicCost, BoardAction parentAction, PuzzleHeuristicFunction heuristicDelegate) {
        this.dimension = dimension;
        this.underlying = underlying;
        this.parentBoard = parentBoard;
        this.cost = cost;
        this.heuristicCost = heuristicCost;
        this.parentAction = parentAction;
        this.boardState = this.toString();
        this.heuristicDelegate = heuristicDelegate;
    }


    private ArrayList<ArrayList<Integer>> generateBoard(int dimension) {
        int size = dimension * dimension;
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums);
        while (!isSolvable(nums)) {
            Collections.shuffle(nums);
        }
        return convertToTwoDimensions(nums);
    }

    private ArrayList<ArrayList<Integer>> convertToTwoDimensions(ArrayList<Integer> input) {
        ArrayList<ArrayList<Integer>> toReturn = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            if (i % dimension == 0) {
                toReturn.add(new ArrayList<>());
            }
            toReturn.get(toReturn.size() - 1).add(input.get(i));
        }
        return toReturn;
    }



    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        for (int i = 0; i < underlying.size(); i++) {
            for (int j = 0; j < underlying.get(i).size(); j++) {
                toReturn.append(underlying.get(i).get(j) + " ");
            }
            toReturn.append("\n");
        }
        return toReturn.toString();
    }

    public void draw() {
        StringBuilder toReturn = new StringBuilder();

        for (int i = 0; i < underlying.size(); i++) {
            toReturn.append("|\t");
            for (int j = 0; j < underlying.get(i).size(); j++) {
                toReturn.append(underlying.get(i).get(j) + "\t|\t");
            }
            toReturn.append("\n\n\n");
        }
        System.out.println(toReturn.toString());
    }

    public static ArrayList<Integer> convertToSingleDimension(ArrayList<ArrayList<Integer>> input) {
        ArrayList<Integer> toReturn = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).size(); j++) {
                toReturn.add(input.get(i).get(j));
            }
        }
        return toReturn;
    }

    public static boolean isSolvable(ArrayList<Integer> input) {
        int numInversions = 0;
        for (int i = 0; i < input.size() - 1; i++) {
            for (int j = i + 1; j < input.size(); j++) {
                if ((input.get(i) > input.get(j)) && (input.get(i) != 0 && input.get(j) != 0)) {
                    numInversions++;
                }
            }
        }
        return numInversions % 2 == 0;
    }



    public String getBoardState() {
        return boardState;
    }

    public Board getParentNode() {
        return parentBoard;
    }

    public void setParentNode(Board parentBoard) {
        this.parentBoard = parentBoard;
    }

    public BoardAction getParentAction() {
        return parentAction;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getHeuristicCost() {
        return heuristicCost;
    }

    @Override
    public int compareTo(Board o) {
        return (this.cost + this.heuristicCost) - (o.cost + o.heuristicCost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;
        if (boardState == null || board.boardState == null) {
            return false;
        } else {
            return boardState.equals(board.boardState);
        }

    }

    @Override
    public int hashCode() {
        return boardState.hashCode();
    }

    public ArrayList<Board> generateChildren() {
        Tuple zeroPosition = getZeroPosition();
        ArrayList<Board> children = new ArrayList<>();

        Tuple leftSwap = zeroPosition.add(new Tuple(0, -1));
        if (leftSwap.isValid()) {
            children.add(generateChildBoard(performSwap(zeroPosition, leftSwap), BoardAction.LeftSwap));
        }

        Tuple rightSwap = zeroPosition.add(new Tuple(0, 1));
        if (rightSwap.isValid()) {
            children.add(generateChildBoard(performSwap(zeroPosition, rightSwap), BoardAction.RightSwap));
        }

        Tuple downSwap = zeroPosition.add(new Tuple(1, 0));
        if (downSwap.isValid()) {
            children.add(generateChildBoard(performSwap(zeroPosition, downSwap), BoardAction.DownSwap));
        }

        Tuple topSwap = zeroPosition.add(new Tuple(-1,0));
        if (topSwap.isValid()) {
            children.add(generateChildBoard(performSwap(zeroPosition, topSwap), BoardAction.TopSwap));
        }
        return children;
    }
    
    
    //deep copies underyling, performs the enumerated action on the copy and returns it
    private ArrayList<ArrayList<Integer>> performSwap(Tuple startCoord, Tuple endCoord) {
        ArrayList<ArrayList<Integer>> toReturn = gridCloner(underlying);
        int start = toReturn.get(startCoord.rowNumber).get(startCoord.columnNumber);
        int end = toReturn.get(endCoord.rowNumber).get(endCoord.columnNumber);

        //set start to end
        toReturn.get(startCoord.rowNumber).set(startCoord.columnNumber, end);
        //set end to start
        toReturn.get(endCoord.rowNumber).set(endCoord.columnNumber, start);
        return toReturn;
    }


    //uses shallow copy
    private Board generateChildBoard(ArrayList<ArrayList<Integer>> input, BoardAction parentAction) {
        return new Board(dimension, input, this, cost + 1, heuristicDelegate.calculateHeuristic(input), parentAction, heuristicDelegate);
    }


    public Tuple getZeroPosition() {
        boolean done = false;
        Tuple toReturn = null;
        for (int i = 0; i < underlying.size(); i++) {
            for (int j = 0; j < underlying.get(i).size(); j++) {
                if (underlying.get(i).get(j) == 0) {
                    return new Tuple(i, j);
                }
            }
        }
        throw new RuntimeException("There was no zero position");
    }

    private static ArrayList<ArrayList<Integer>> gridCloner(ArrayList<ArrayList<Integer>> input) {
        ArrayList<ArrayList<Integer>> toReturn = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            toReturn.add((ArrayList<Integer>) input.get(i).clone());
        }
        return toReturn;
    }

}
