package Assignment1;

import java.io.File;
import java.util.Scanner;

/**
 * Created by brianzhao on 10/23/15.
 */
public class CSVMaker {
    public static void main(String[] args) throws Exception {
        File file = new File("DataH2.txt");
        Scanner scanner = new Scanner(file);
        int j = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().replaceAll("\\s+", ",");
            System.out.println(line.substring(0, line.length() - 1));
        }
    }
}
