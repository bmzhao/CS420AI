package Assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by brianzhao on 10/17/15.
 */
public class Board implements Comparable<Board> {
    private final int dimension;
    private ArrayList<ArrayList<Integer>> underlying;
    private Board parentBoard;
    private int cost;
    private int heuristicCost;
    private final String boardState;

    //still need to set heuristiccost
    public Board(int dimension) {
        this.dimension = dimension;
        generateBoard();
        boardState = this.toString();
        parentBoard = null;
        cost = 0;
    }

    //still need to set heuristiccost
    public Board(ArrayList<ArrayList<Integer>> input) {
        dimension = input.size();
        ArrayList<Integer> oneDimension = convertToSingleDimension(input);
        if (!isSolvable(oneDimension)) {
            throw new RuntimeException();
        }
        parentBoard = null;
        underlying = (ArrayList<ArrayList<Integer>>) input.clone();
        boardState = this.toString();
        cost = 0;
    }

    private void generateBoard() {
        int size = dimension * dimension;
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums);
        while (!isSolvable(nums)) {
            Collections.shuffle(nums);
        }
        underlying = convertToTwoDimensions(nums);
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
        System.out.println(numInversions%2 == 0);
        System.out.println(numInversions);

        return numInversions % 2 == 0;
    }

    public static ArrayList<ArrayList<Integer>> askForThreeByThreeInput() {
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
        return toReturn;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
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

//    public ArrayList<Board> generateChildren() {
//        Tuple zeroPosition = getZeroPosition();
//        ArrayList<Board> children = new ArrayList<>();
//
//        Tuple leftSwap = zeroPosition.subtract(new Tuple(0, -1));
//
//        //left swap
//
//
//        Tuple rightSwap = zeroPosition.subtract(new Tuple(0, -1));
//        //right swap
//
//
//        //down swap
//
//
//
//        //top swap
//    }


    public Tuple getZeroPosition() {
        boolean done = false;
        Tuple toReturn = null;
        for (int i = 0; i < underlying.size(); i++) {
            for (int j = 0; j < underlying.get(i).size(); j++) {
                if (underlying.get(i).get(j) == 0) {
                    toReturn =new Tuple(i, j);
                    done = true;
                    break;
                }
            }
            if (done) {
                break;
            }
        }
        if (toReturn == null) {
            throw new RuntimeException();
        }
        return toReturn;
    }


}
