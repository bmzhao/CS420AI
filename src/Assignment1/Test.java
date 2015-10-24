package Assignment1;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by brianzhao on 10/20/15.
 */
public class Test {
    public static final int DIMENSION = 3;
    public static void main(String[] args) throws Exception{
        callGenerateBoards(DIMENSION);
        PrintWriter pw = new PrintWriter(new File("AllBoards.txt"));
        long count = 0;
        for (ArrayList<Integer> currentArray : allBoards) {
            if (Board.isSolvable(currentArray)) {
                ArrayList<ArrayList<Integer>> twoD = convertToTwoDimensions(currentArray, DIMENSION);
                Board board = new Board(twoD, EightPuzzle.zeroHeuristic);
                pw.println(board);
                System.out.println("printed board " + (++count));
            }
        }
        pw.close();

    }

    public static HashSet<ArrayList<Integer>> allBoards = new HashSet<>();
    public static void callGenerateBoards(int dimension) {
        ArrayList<Integer> initial = new ArrayList<>();
        generateAllBoards(initial, dimension);
    }

//    public static ArrayList<ArrayList<Integer>> convertTo2d(ArrayList<Integer> input, int dimension) {
//        ArrayList<ArrayList<Integer>> toReturn = new ArrayList<>();
//        ArrayList<Integer> toAdd = new ArrayList<>();
//        for (int i = 0; i < input.size(); i++) {
//            if (i % dimension == dimension - 1) {
//                toAdd.add(input.get(i));
//                toReturn.add(toAdd);
//                toAdd = new ArrayList<>();
//            } else {
//                toAdd.add(input.get(i));
//            }
//        }
//        return toReturn;
//    }

    private static ArrayList<ArrayList<Integer>> convertToTwoDimensions(ArrayList<Integer> input, int dimension) {
        ArrayList<ArrayList<Integer>> toReturn = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            if (i % dimension == 0) {
                toReturn.add(new ArrayList<Integer>());
            }
            toReturn.get(toReturn.size() - 1).add(input.get(i));
        }
        return toReturn;
    }



    public static void generateAllBoards(ArrayList<Integer> given, int dimension) {
        int dimensionSquared = dimension * dimension;
        if (given.size() == dimensionSquared) {
            if (allBoards.contains(given)) {
                System.out.println("weird!");
            }
            allBoards.add(given);
        } else {
            for (int i = 0; i < dimensionSquared; i++) {
                if (!given.contains(i)) {
                    ArrayList<Integer> nextStep = (ArrayList<Integer>) given.clone();
                    nextStep.add(i);
                    generateAllBoards(nextStep, dimension);
                }
            }
        }
    }
}
