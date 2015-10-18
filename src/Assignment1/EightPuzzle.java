package Assignment1;

import java.util.ArrayList;

/**
 * Created by brianzhao on 10/17/15.
 */
public class EightPuzzle {
    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> potentialBoard = Board.askForThreeByThreeInput();
        Board board = new Board(potentialBoard);

    }
}
