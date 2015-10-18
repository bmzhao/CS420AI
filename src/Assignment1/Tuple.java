package Assignment1;

/**
 * Created by brianzhao on 10/17/15.
 */


public class Tuple {
    int rowNumber;
    int columnNumber;

    public Tuple(int rowNumber, int columnNumber) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    //works with dimension 3
    public boolean isValid() {
        return rowNumber >= 0 && rowNumber <= 2 && columnNumber >= 0 && columnNumber <= 2;
    }

    public Tuple add(Tuple tuple) {
        return new Tuple(tuple.rowNumber + rowNumber, tuple.columnNumber + columnNumber);
    }

    public Tuple subtract(Tuple tuple) {
        return new Tuple(rowNumber - tuple.rowNumber, columnNumber - tuple.columnNumber);
    }

}
