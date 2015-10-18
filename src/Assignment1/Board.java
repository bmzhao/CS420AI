package Assignment1;

import java.util.*;

/**
 * Created by brianzhao on 10/17/15.
 */
public class Board {
    private int dimension;
    private ArrayList<ArrayList<Integer>> underlying;

    public Board(int dimension) {
        this.dimension = dimension;
        generateBoard();
    }

    public Board(ArrayList<ArrayList<Integer>> input) {
        dimension = input.size();
        ArrayList<Integer> oneDimension =convertToSingleDimension(input);
        if (!isSolvable(oneDimension)) {
            throw new RuntimeException();
        }
        underlying = (ArrayList<ArrayList<Integer>>)input.clone();
    }

    private void generateBoard() {
        int size = dimension* dimension;
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i <size; i++) {
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
        for (int i = 0; i < input.size()-1; i++) {
            for (int j = i+1; j < input.size(); j++) {
                if (input.get(i) > input.get(j)) {
                    numInversions++;
                }
            }
        }
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



}
