package Assignment1;

/**
 * Created by brianzhao on 10/17/15.
 */


public class Tuple {
    int rowNumber;
    int columnNumber;
    int dimension;

    public Tuple(int rowNumber, int columnNumber,int dimension) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.dimension = dimension;
    }

    public boolean isValid() {
        return rowNumber >= 0 && rowNumber <= dimension-1 && columnNumber >= 0 && columnNumber <= dimension-1;
    }

    public Tuple add(Tuple tuple) {
        return new Tuple(tuple.rowNumber + rowNumber, tuple.columnNumber + columnNumber, dimension);
    }

}
