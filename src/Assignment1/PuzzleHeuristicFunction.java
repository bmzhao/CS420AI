package Assignment1;

import java.util.ArrayList;

/**
 * Created by brianzhao on 10/21/15.
 */
public interface PuzzleHeuristicFunction {
    //do not attempt to modify grid!!
    int calculateHeuristic(ArrayList<ArrayList<Integer>> grid);
}
